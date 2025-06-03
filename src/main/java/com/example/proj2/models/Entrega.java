package com.example.proj2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"entrega\"")
public class Entrega {
    @Id
    @Column(name = "id_entrega", nullable = false)
    private Integer id;

    @Column(name = "destino", nullable = false, length = Integer.MAX_VALUE)
    private String destino;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}