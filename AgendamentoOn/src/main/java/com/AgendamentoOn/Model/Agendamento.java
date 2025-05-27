package com.AgendamentoOn.Model;

import java.time.LocalDateTime;

import com.AgendamentoOn.Enum.Especialidade;
import com.AgendamentoOn.Enum.Status;

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
    private LocalDateTime Data;

    @NotNull
    private double valor;

    @NotNull
    private Especialidade especialidade;

    @NotNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "usuario_id_cliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id_profissional")
    private Usuario profissional;

    public Agendamento(@NotNull LocalDateTime data, @NotNull double valor, @NotNull Especialidade especialidade,
            Usuario cliente, Usuario profissional, Status status) {
        Data = data;
        this.status = status;
        this.valor = valor;
        this.especialidade = especialidade;
        this.cliente = cliente;
        this.profissional = profissional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return Data;
    }

    public void setData(LocalDateTime horario) {
        Data = horario;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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

    public Usuario getProfissional() {
        return profissional;
    }

    public void setProfissional(Usuario profissional) {
        this.profissional = profissional;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
