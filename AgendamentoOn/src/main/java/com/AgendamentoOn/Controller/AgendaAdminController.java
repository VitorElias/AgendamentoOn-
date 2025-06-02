package com.AgendamentoOn.Controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AgendamentoOn.Enum.Especialidade;
import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Security.CustomUserDetails;
import com.AgendamentoOn.Service.AgendamentoService;
import com.AgendamentoOn.Service.UsuarioService;

@Controller
public class AgendaAdminController {

    private final UsuarioService usuarioService;
    private final AgendamentoService agendamentoService;

    public AgendaAdminController(UsuarioService usuarioService, AgendamentoService agendamentoService) {
        this.usuarioService = usuarioService;
        this.agendamentoService = agendamentoService;
    }

    // Método que retorna página HTML
    @GetMapping("/agenda-admin")
    public String carregarTelaAgendaAdmin() {
        return "agendaAdmin"; // Nome da view HTML (Thymeleaf, JSP etc)
    }

    // Método que retorna JSON - usuario logado
    @GetMapping("/api/usuario-logado")
@ResponseBody
public ResponseEntity<Usuario> getUsuarioLogado(@AuthenticationPrincipal CustomUserDetails userDetails) {
    if (userDetails == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    Usuario usuario = userDetails.getUsuario();
    return ResponseEntity.ok(usuario);
}

@GetMapping("/api/admin/agendamentos/todos")
@ResponseBody
public List<Agendamento> listarTodosAgendamentos() {
    List<Agendamento> agendamentos = agendamentoService.listarTodos();

    // Ordena da data mais recente para a mais antiga
    agendamentos.sort(Comparator.comparing(Agendamento::getData).reversed());

    return agendamentos;
}

 // Atualizar perfil do usuário autenticado (senha tratada na service)
    @PutMapping("/usuario/perfil")
    @ResponseBody
    public ResponseEntity<?> atualizarPerfil(@RequestBody Usuario usuarioAtualizado) {
        try {
            Usuario usuario = getUsuarioAutenticado();
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
            }

            usuarioAtualizado.setId(usuario.getId()); // garante que atualizará o usuário correto
            Usuario usuarioAtualizadoSalvo = usuarioService.atualizar(usuarioAtualizado);

            if (usuarioAtualizadoSalvo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado para atualização");
            }

            return ResponseEntity.ok(usuarioAtualizadoSalvo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar perfil");
        }
    }

     // Método utilitário para obter o usuário autenticado
    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsuario();
        }
        return null;
    }


@GetMapping("/api/admin/agendamentos")
@ResponseBody
public List<Agendamento> listarAgendamentosUltimos30Dias() {
    List<Agendamento> agendamentos = agendamentoService.listarAgendamentosUltimos30Dias();

    // Ordena da data mais recente para a mais antiga
    agendamentos.sort(Comparator.comparing(Agendamento::getData).reversed());

    System.out.println("Agendamentos encontrados nos últimos 30 dias:");
    for (Agendamento ag : agendamentos) {
        System.out.println(ag);
    }

    return agendamentos;
}


    // Listar clientes - JSON
    @GetMapping("/api/admin/clientes")
    @ResponseBody
    public List<Usuario> listarTodosClientes() {
        return usuarioService.buscarTodosClientes();
    }

    // Outro endpoint JSON para usuario via Authentication
    @GetMapping("/api/admin/me")
    @ResponseBody
    public ResponseEntity<?> getUsuarioLogadoViaAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSenha(null);
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

  @PatchMapping("/api/admin/agendamentos/{id}/status")
@ResponseBody
public ResponseEntity<?> atualizarStatusAgendamento(@PathVariable Long id, @RequestBody Map<String, String> body) {
    String novoStatus = body.get("status");
    boolean atualizado = agendamentoService.atualizarStatus(id, novoStatus);
    
    if (atualizado) {
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado.");
    }
}

@PostMapping("/api/admin/agendamentos/bloquear")
@ResponseBody
public ResponseEntity<?> bloquearData(@RequestBody Map<String, String> payload) {
    try {
        String dataStr = payload.get("data");

        if (dataStr == null) {
            return ResponseEntity.badRequest().body("Data não informada");
        }

        // Corrige o parsing: de ISO8601 com 'Z' → Instant → LocalDateTime
        Instant instant = Instant.parse(dataStr);
        LocalDateTime data = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Sempre BLOQUEADO
        Especialidade especialidade = Especialidade.BLOQUEADO;

        // Chama o service para bloquear (assumindo que o status também é tratado lá)
        Agendamento bloqueio = agendamentoService.bloquearData(data, especialidade);

        return ResponseEntity.ok(bloqueio);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao bloquear a data: " + e.getMessage());
    }
}

@GetMapping("/api/admin/agendamentos/verificar-disponibilidade")
@ResponseBody
public ResponseEntity<?> verificarDisponibilidade(
        @RequestParam("data") String dataStr) {
    try {
        LocalDate data = LocalDate.parse(dataStr);

        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.plusDays(1).atStartOfDay().minusNanos(1);

        List<Agendamento> agendamentosNoDia = agendamentoService
            .buscarAgendamentosPorData(inicioDoDia, fimDoDia);

        return ResponseEntity.ok(agendamentosNoDia);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao verificar disponibilidade: " + e.getMessage());
    }
}

@GetMapping("/api/blocked-dates")
public ResponseEntity<List<String>> getDatasBloqueadas(
        @RequestParam int year,
        @RequestParam int month) {
    try {
        List<LocalDate> datas = agendamentoService.buscarDatasBloqueadasNoMes(year, month);
        List<String> datasFormatadas = datas.stream()
                .map(LocalDate::toString) // yyyy-MM-dd
                .collect(Collectors.toList());

        // Log para validar as datas bloqueadas
        System.out.println("Datas bloqueadas enviadas para o front-end:");
        datasFormatadas.forEach(data -> System.out.println(data));

        return ResponseEntity.ok(datasFormatadas);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
    }
}

 

}
