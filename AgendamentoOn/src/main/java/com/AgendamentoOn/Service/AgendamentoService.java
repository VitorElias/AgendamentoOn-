package com.AgendamentoOn.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AgendamentoOn.Enum.Especialidade;
import com.AgendamentoOn.Enum.Status;
import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Repository.AgendamentoRepository;
import java.util.Arrays;


import jakarta.transaction.Transactional;

@Service
public class AgendamentoService {

     private final AgendamentoRepository agendamentoRepository;

    @Autowired
    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    private boolean isBloqueado(Agendamento ag) {
    return ag.getStatus() == Status.BLOQUEADO || ag.getEspecialidade() == Especialidade.BLOQUEADO;
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
    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }

    // Método para deletar um agendamento
    public boolean deleteAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);  // Hibernate remove o agendamento do banco
            return true;
        }
        return false;
    }

     public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }


    // Método para buscar agendamentos de um cliente
    public List<Agendamento> getAgendamentosByClienteId(Long clienteId) {
    return agendamentoRepository.findByClienteId(clienteId).stream()
            .filter(ag -> !isBloqueado(ag))
            .collect(Collectors.toList());
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

 
public List<Agendamento> listarAgendamentosUltimos30Dias() {
    LocalDateTime trintaDiasAtras = LocalDateTime.now().minusDays(30);
    return agendamentoRepository.findByDataAfter(trintaDiasAtras).stream()
            .filter(ag -> !isBloqueado(ag))
            .collect(Collectors.toList());
}

public List<Agendamento> listarTodos() {
    return agendamentoRepository.findAll().stream()
            .filter(ag -> !isBloqueado(ag))
            .collect(Collectors.toList());
}

  @Transactional
    public boolean atualizarStatus(Long id, String novoStatus) {
        return agendamentoRepository.findById(id).map(agendamento -> {
            try {
                Status statusEnum = Status.valueOf(novoStatus.toUpperCase());
                agendamento.setStatus(statusEnum);
                agendamentoRepository.save(agendamento);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }).orElse(false);
    }

    public Agendamento bloquearData(LocalDateTime data, Especialidade especialidade) {
    Agendamento bloqueio = new Agendamento();
    bloqueio.setData(data);
    bloqueio.setEspecialidade(especialidade);
    bloqueio.setStatus(Status.BLOQUEADO);
    bloqueio.setCliente(null);
    return agendamentoRepository.save(bloqueio);
}

public List<Agendamento> buscarAgendamentosPorData(LocalDateTime inicio, LocalDateTime fim) {
    return agendamentoRepository.findByDataBetween(inicio, fim);
}

 public List<LocalDate> buscarDatasBloqueadasNoMes(int year, int month) {
        // Obtém o primeiro e último dia do mês
        LocalDate primeiroDia = LocalDate.of(year, month, 1);
        LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        // Busca no repo todos agendamentos que estão entre essas datas e que estão bloqueados ou agendados
        List<Agendamento> agendamentos = agendamentoRepository
            .findByDataBetweenAndStatusInOrEspecialidade(
                primeiroDia.atStartOfDay(), 
                ultimoDia.atTime(23,59,59, 999_999_999),
                Arrays.asList(Status.AGENDADO, Status.BLOQUEADO),
                Especialidade.BLOQUEADO);

        // Retorna apenas as datas únicas
        return agendamentos.stream()
                .map(ag -> ag.getData().toLocalDate())
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean isHorarioDisponivel(LocalDateTime dataHora) {
    LocalDate dia = dataHora.toLocalDate();
    
    LocalDateTime inicioDoDia = dia.atStartOfDay();            // 00:00 do dia
    LocalDateTime fimDoDia = dia.atTime(LocalTime.MAX);        // 23:59:59.999999999 do dia

    // Verifica se o dia está bloqueado
    List<Agendamento> bloqueiosDoDia = agendamentoRepository.findByStatusAndDataBetween(Status.BLOQUEADO, inicioDoDia, fimDoDia);
    if (!bloqueiosDoDia.isEmpty()) {
        return false; // Dia inteiro bloqueado
    }

    // Se o dia não está bloqueado, verifica se o horário está ocupado
    return agendamentoRepository.findByData(dataHora).isEmpty();
}

    
}
