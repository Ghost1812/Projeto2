package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"veiculo\"")
public class Veiculo {
    @Id
    @Column(name = "id_veiculo", nullable = false)
    private Integer id;

    @Column(name = "tipoveiculo", length = Integer.MAX_VALUE)
    private String tipoveiculo;

    @Column(name = "matricula", length = Integer.MAX_VALUE)
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estafeta", nullable = false)
    private Estafeta idEstafeta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoveiculo() {
        return tipoveiculo;
    }

    public void setTipoveiculo(String tipoveiculo) {
        this.tipoveiculo = tipoveiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Estafeta getIdEstafeta() {
        return idEstafeta;
    }

    public void setIdEstafeta(Estafeta idEstafeta) {
        this.idEstafeta = idEstafeta;
    }
}