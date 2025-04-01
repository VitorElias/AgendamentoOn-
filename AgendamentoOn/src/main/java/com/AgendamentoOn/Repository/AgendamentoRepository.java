package com.AgendamentoOn.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AgendamentoOn.Enum.Status;
import com.AgendamentoOn.Model.Agendamento;
import com.AgendamentoOn.Model.Usuario;  

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findById(Long id);

    List<Agendamento> findByCliente(Usuario cliente);
    
    List<Agendamento> findByClienteId(Long id);
    
    List<Agendamento> findByProfissional(Usuario profissional);

    // Modifique para utilizar o enum Status em vez de String
    List<Agendamento> findByClienteAndStatus(Usuario cliente, Status status);
    
    List<Agendamento> findByProfissionalAndStatus(Usuario profissional, Status status);
}
