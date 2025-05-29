package com.AgendamentoOn.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Service.AgendamentoService;

@RestController
@RequestMapping("/api/admin")
public class AgendaAdminController {

    private final AgendamentoService agendamentoService;

    @Autowired
    public AgendaAdminController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    // ✅ Serve a página HTML de administração
    @GetMapping("/agenda")
    public String mostrarPaginaAgenda() {
        System.out.println("Requisição GET para página de administração da agenda");
        return "agendaAdmin";  // agendaAdmin.html em src/main/resources/templates
    }

    // ✅ Listar todos os agendamentos
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        List<Agendamento> agendamentos = agendamentoService.getAllAgendamentos();
        System.out.println("Listando todos os agendamentos, total = " + agendamentos.size());
        return ResponseEntity.ok(agendamentos);
    }


    // ✅ Criar um novo agendamento
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Agendamento agendamento) {
        try {
            Agendamento salvo = agendamentoService.saveAgendamento(agendamento);
            System.out.println("Novo agendamento criado com ID: " + salvo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar agendamento");
        }
    }

    // ✅ Atualizar um agendamento existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Agendamento agendamento) {
        try {
            Agendamento atualizado = agendamentoService.updateAgendamento(id, agendamento);
            if (atualizado != null) {
                System.out.println("Agendamento atualizado ID: " + id);
                return ResponseEntity.ok(atualizado);
            } else {
                System.out.println("Agendamento não encontrado para atualização ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar agendamento");
        }
    }

    // ✅ Deletar um agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (agendamentoService.deleteAgendamento(id)) {
                System.out.println("Agendamento deletado ID: " + id);
                return ResponseEntity.noContent().build();
            } else {
                System.out.println("Agendamento não encontrado para exclusão ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar agendamento");
        }
    }
}
