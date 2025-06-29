package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback", nullable = false)
    private Integer id;

    @Column(name = "notadeservico", nullable = false, columnDefinition = "text")
    private String notadeservico;

    @Column(name = "opinioes", nullable = false, columnDefinition = "text")
    private String opinioes;

    @Column(name = "questionario", nullable = false, columnDefinition = "text")
    private String questionario;

    @Column(name = "reclamacao", nullable = false, columnDefinition = "text")
    private String reclamacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda")
    private Encomenda idEncomenda;

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReclamacao() {
        return reclamacao;
    }

    public void setReclamacao(String reclamacao) {
        this.reclamacao = reclamacao;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Encomenda getIdEncomenda() {
        return idEncomenda;
    }

    public void setIdEncomenda(Encomenda idEncomenda) {
        this.idEncomenda = idEncomenda;
    }
}
