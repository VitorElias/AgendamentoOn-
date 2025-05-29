package com.AgendamentoOn.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Model.Usuario;
import com.AgendamentoOn.Security.CustomUserDetails;
import com.AgendamentoOn.Service.AgendamentoService;
import com.AgendamentoOn.Service.UsuarioService;

@Controller
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendamentoService agendamentoService;
    private final UsuarioService usuarioService;

    @Autowired
    public AgendaController(AgendamentoService agendamentoService,
                            UsuarioService usuarioService) {
        this.agendamentoService = agendamentoService;
        this.usuarioService = usuarioService;
    }

    // Serve a página HTML ao acessar /api/agenda
    @GetMapping
    public String mostrarTela() {
        System.out.println("Requisição GET para mostrar a tela da agenda");
        return "agenda"; // agenda.html em src/main/resources/templates
    }

    // Listar somente os agendamentos do cliente autenticado
    @GetMapping("/listar")
    @ResponseBody
    public List<Agendamento> listar() {
        Usuario usuario = getUsuarioAutenticado();
        List<Agendamento> agendamentos = agendamentoService.getAgendamentosByClienteId(usuario.getId());
        System.out.println("Listando agendamentos do cliente: " + usuario.getId() + ", total = " + agendamentos.size());
        return agendamentos;
    }

    // Criar novo agendamento associando automaticamente o cliente autenticado
    @PostMapping("/novo")
    @ResponseBody
    public ResponseEntity<?> novoAgendamento(@RequestBody Agendamento agendamento) {
        Usuario usuario = getUsuarioAutenticado();
        agendamento.setCliente(usuario); // associar o cliente autenticado
        Agendamento salvo = agendamentoService.saveAgendamento(agendamento);
        System.out.println("Novo agendamento criado pelo cliente ID: " + usuario.getId());
        System.out.println(salvo);
        return ResponseEntity.ok(salvo);
    }

    // Excluir agendamento, mas apenas se pertencer ao cliente autenticado
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable Long id) {
        try {
            agendamentoService.cancelarAgendamento(id);
            return ResponseEntity.ok().body("Agendamento cancelado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cancelar agendamento");
        }
    }

    // Buscar agendamento por ID apenas se for do cliente autenticado
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getAgendamento(@PathVariable Long id) {
        Usuario usuario = getUsuarioAutenticado();
        Optional<Agendamento> agendamentoOpt = agendamentoService.getAgendamentoById(id);

        if (agendamentoOpt.isPresent()) {
            Agendamento agendamento = agendamentoOpt.get();
            if (!agendamento.getCliente().getId().equals(usuario.getId())) {
                return ResponseEntity.status(403).body("Você não tem permissão para visualizar este agendamento.");
            }
            return ResponseEntity.ok(agendamento);
        }
        return ResponseEntity.notFound().build();
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

    @GetMapping("/usuario/perfil")
@ResponseBody
public ResponseEntity<?> getPerfil() {
    Usuario usuario = getUsuarioAutenticado();
    if (usuario == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
    }
    return ResponseEntity.ok(usuario);
}

 @GetMapping("/admin")
    public String mostrarPaginaAgenda() {
        System.out.println("Requisição GET para página de administração da agenda");
        return "agendaAdmin";  // agendaAdmin.html em src/main/resources/templates
    }
    

}
