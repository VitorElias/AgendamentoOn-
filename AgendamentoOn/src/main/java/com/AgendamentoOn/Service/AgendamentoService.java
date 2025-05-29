package com.AgendamentoOn.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AgendamentoOn.Enum.Status;
import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Repository.AgendamentoRepository;

@Service
public class AgendamentoService {

     private final AgendamentoRepository agendamentoRepository;

    @Autowired
    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    // Método para salvar um agendamento
    public Agendamento saveAgendamento(Agendamento agendamento) {
        return agendamentoRepository.save(agendamento); // Hibernate cuida da persistência
    }

    // Método para buscar um agendamento pelo ID
    public Optional<Agendamento> getAgendamentoById(Long id) {
        return agendamentoRepository.findById(id);  // Hibernate busca o agendamento pelo ID
    }

    // Método para buscar todos os agendamentos
    public List<Agendamento> getAllAgendamentos() {
        return agendamentoRepository.findAll();  // Hibernate busca todos os registros
    }

    // Método para atualizar um agendamento
    public Agendamento updateAgendamento(Long id, Agendamento agendamento) {
        if (agendamentoRepository.existsById(id)) {
            agendamento.setId(id);  // Garantir que o ID correto está sendo atualizado
            return agendamentoRepository.save(agendamento); // Hibernate cuida da atualização
        }
        return null;  // Ou lance uma exceção caso não encontre o agendamento
    }

    // Método para deletar um agendamento
    public boolean deleteAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);  // Hibernate remove o agendamento do banco
            return true;
        }
        return false;
    }

    // Método para buscar agendamentos de um cliente
    public List<Agendamento> getAgendamentosByClienteId(Long clienteId) {
        return agendamentoRepository.findByClienteId(clienteId);  // Consulta personalizada
    }

    public void cancelarAgendamento(Long id) {
    Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);
    if (agendamentoOpt.isPresent()) {
        Agendamento agendamento = agendamentoOpt.get();
        agendamento.setStatus(Status.CANCELADO);
        agendamentoRepository.save(agendamento);
    } else {
        throw new RuntimeException("Agendamento não encontrado");
    }
}

    
}
