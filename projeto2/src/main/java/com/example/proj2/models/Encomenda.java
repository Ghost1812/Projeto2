package com.example.proj2.models;

import jakarta.persistence.*;

@Entity
@Table(name = "\"encomenda\"")
public class Encomenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encomenda", nullable = false)
    private Integer id;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "id_operador", nullable = true) // Permitir null
    private OperadorTriagem idOperador;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rececionista", nullable = false)
    private Rececionista idRececionista;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente idCliente;


    @Column(name = "estado_integridade", nullable = false)
    private String estadoIntegridade;

    @Column(name = "estado_entrega", nullable = false)
    private String estadoEntrega; // <-- AQUI

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public OperadorTriagem getIdOperador() { return idOperador; }
    public void setIdOperador(OperadorTriagem idOperador) { this.idOperador = idOperador; }

    public Rececionista getIdRececionista() { return idRececionista; }
    public void setIdRececionista(Rececionista idRececionista) { this.idRececionista = idRececionista; }

    public Cliente getIdCliente() { return idCliente; }
    public void setIdCliente(Cliente idCliente) { this.idCliente = idCliente; }

    public String getEstadoIntegridade() { return estadoIntegridade; }
    public void setEstadoIntegridade(String estadoIntegridade) { this.estadoIntegridade = estadoIntegridade; }

    public String getEstadoEntrega() { return estadoEntrega; }
    public void setEstadoEntrega(String estadoEntrega) { this.estadoEntrega = estadoEntrega; }
}
