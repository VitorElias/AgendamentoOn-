package com.AgendamentoOn.Model;

import java.time.LocalDateTime;

import com.AgendamentoOn.Enum.Especialidade;
import com.AgendamentoOn.Enum.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime data;
    @NotNull
    private Especialidade especialidade;

    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "usuario_id_cliente")
    private Usuario cliente;

    public Agendamento() {
    }
    
    public Agendamento(@NotNull LocalDateTime data, @NotNull Especialidade especialidade,
            Usuario cliente, Status status) {
        this.data = data; // Corrigido
        this.status = status;
        this.especialidade = especialidade;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime horario) {
        data = horario;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
