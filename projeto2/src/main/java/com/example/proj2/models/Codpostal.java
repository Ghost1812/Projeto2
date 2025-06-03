package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"codpostal\"")
public class Codpostal {
    @Id
    @Column(name = "codpostal", nullable = false, length = 10)
    private String codpostal;

    @Column(name = "localidade", nullable = false, length = 255)
    private String localidade;

    public String getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(String codpostal) {
        this.codpostal = codpostal;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }
}