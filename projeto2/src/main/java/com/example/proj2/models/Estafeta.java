package com.example.proj2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"estafeta\"")
public class Estafeta {
    @Id
    @Column(name = "id_estafeta", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = Integer.MAX_VALUE)
    private String nome;

    @Column(name = "contacto", nullable = false, length = Integer.MAX_VALUE)
    private String contacto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }
}