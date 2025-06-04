package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"feedback\"")
public class Feedback {
    @Id
    @Column(name = "id_feedback", nullable = false)
    private Integer id;

    @Column(name = "reclamacao", nullable = false, length = Integer.MAX_VALUE)
    private String reclamacao;

    @Column(name = "notadeservico", nullable = false, length = Integer.MAX_VALUE)
    private String notadeservico;

    @Column(name = "opinioes", nullable = false, length = Integer.MAX_VALUE)
    private String opinioes;

    @Column(name = "questionario", nullable = false, length = Integer.MAX_VALUE)
    private String questionario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente idCliente;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(String reclamacao) {
        this.reclamacao = reclamacao;
    }

    public String getNotadeservico() {
        return notadeservico;
    }

    public void setNotadeservico(String notadeservico) {
        this.notadeservico = notadeservico;
    }

    public String getOpinioes() {
        return opinioes;
    }

    public void setOpinioes(String opinioes) {
        this.opinioes = opinioes;
    }

    public String getQuestionario() {
        return questionario;
    }

    public void setQuestionario(String questionario) {
        this.questionario = questionario;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }
}