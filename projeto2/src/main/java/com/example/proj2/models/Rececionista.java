package com.example.proj2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "\"rececionista\"")
public class Rececionista {
    @Id
    @Column(name = "id_rececionista", nullable = false)
    private Integer id;

    @Column(name = "nome", nullable = false, length = Integer.MAX_VALUE)
    private String nome;

    @Column(name = "contacto", length = Integer.MAX_VALUE)
    private String contacto;

    @Column(name = "email", length = Integer.MAX_VALUE)
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}