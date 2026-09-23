package com.ifsp.yas1.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Emprestimo")
public class Emprestimo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @ManyToOne
    @JoinColumn (name = "exemplar")
    private Exemplar exemplar;

    @ManyToOne
    @JoinColumn (name = "aluno")
    private Aluno aluno;

    @Column (name = "data_retirada")
    private LocalDate data_retirada;

    @Column (name = "data_prevista")
    private LocalDate data_prevista;

    @Column (name = "devolucao")
    private LocalDate data_devolucao;

    @Column (name = "status")
    private String status;

    @Column (name = "renovacoes")
    private int renovacoes;

}