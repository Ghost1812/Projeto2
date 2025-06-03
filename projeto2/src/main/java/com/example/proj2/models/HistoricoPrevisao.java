package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"historico_previsao\"")
public class HistoricoPrevisao {
    @Id
    @Column(name = "id_encomenda", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_encomenda", nullable = false)
    private Encomenda encomenda;

    @Column(name = "entrega", length = Integer.MAX_VALUE)
    private String entrega;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

}