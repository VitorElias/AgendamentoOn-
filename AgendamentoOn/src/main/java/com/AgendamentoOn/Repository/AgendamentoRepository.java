package com.AgendamentoOn.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.AgendamentoOn.Enum.Especialidade;
import com.AgendamentoOn.Enum.Status;
import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Model.Usuario;  

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findById(Long id);

    List<Agendamento> findByCliente(Usuario cliente);
    
    List<Agendamento> findByClienteId(Long id);
    
    // Modifique para utilizar o enum Status em vez de String
    List<Agendamento> findByClienteAndStatus(Usuario cliente, Status status);

    List<Agendamento> findByDataAfter(LocalDateTime data);

    List<Agendamento> findByDataBetween(LocalDateTime start, LocalDateTime end);

    List<Agendamento> findByDataBetweenAndStatusInOrEspecialidade(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("statusList") List<Status> statusList,
        @Param("especialidadeBloqueado") Especialidade especialidadeBloqueado
    );

Optional<Agendamento> findByData(LocalDateTime data);

    List<Agendamento> findByStatusAndDataBetween(Status status, LocalDateTime start, LocalDateTime end);

    

 
    
}
