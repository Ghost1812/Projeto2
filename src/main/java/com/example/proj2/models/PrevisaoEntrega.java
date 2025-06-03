package com.example.proj2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "\"previsao_entrega\"")
public class PrevisaoEntrega {
    @Id
    @Column(name = "id_previsao", nullable = false)
    private Integer id;

    @Column(name = "dia", nullable = false)
    private LocalDate dia;

    @Column(name = "hora", nullable = false, length = Integer.MAX_VALUE)
    private String hora;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}