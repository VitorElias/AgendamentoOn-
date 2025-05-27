package com.AgendamentoOn.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Service.AgendamentoService;

@Controller
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendamentoService agendamentoService;

    @Autowired
    public AgendaController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    // Serve a página HTML ao acessar /api/agenda
    @GetMapping
    public String mostrarTela() {
        System.out.println("Requisição GET para mostrar a tela da agenda");
        return "agenda"; // agenda.html em src/main/resources/templates
    }

    // API REST que retorna JSON com os agendamentos para o JS consumir
    @GetMapping("/listar")
    @ResponseBody
    public List<Agendamento> listar() {
        List<Agendamento> agendamentos = agendamentoService.getAllAgendamentos();
        System.out.println("Listando agendamentos: total = " + agendamentos.size());
        agendamentos.forEach(a -> System.out.println(a));
        return agendamentos;
    }

    // API REST para criar novo agendamento via AJAX
    @PostMapping("/novo")
    @ResponseBody
    public ResponseEntity<?> novoAgendamento(@RequestBody Agendamento agendamento) {
        Agendamento salvo = agendamentoService.saveAgendamento(agendamento);
        System.out.println("Novo agendamento criado:");
        System.out.println(salvo);
        return ResponseEntity.ok(salvo);
    }

    // API REST para excluir agendamento pelo ID
    @DeleteMapping("/excluir/{id}")
    @ResponseBody
    public ResponseEntity<?> excluirAgendamento(@PathVariable Long id) {
        boolean removido = agendamentoService.deleteAgendamento(id);
        if (removido) {
            System.out.println("Agendamento removido com id: " + id);
            return ResponseEntity.ok().build();
        }
        System.out.println("Tentativa de remover agendamento com id inválido: " + id);
        return ResponseEntity.badRequest().body("ID inválido");
    }

    // API REST para buscar agendamento pelo ID (opcional)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getAgendamento(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoService.getAgendamentoById(id);
        return agendamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
