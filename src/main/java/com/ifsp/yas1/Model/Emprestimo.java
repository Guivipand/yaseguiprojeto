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

    public Emprestimo() {
    }
    
    public Emprestimo(Exemplar exemplar, Aluno aluno, LocalDate data_retirada, LocalDate data_prevista,
            LocalDate data_devolucao, String status, int renovacoes) {
        this.exemplar = exemplar;
        this.aluno = aluno;
        this.data_retirada = data_retirada;
        this.data_prevista = data_prevista;
        this.data_devolucao = data_devolucao;
        this.status = status;
        this.renovacoes = renovacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public LocalDate getData_retirada() {
        return data_retirada;
    }

    public void setData_retirada(LocalDate data_retirada) {
        this.data_retirada = data_retirada;
    }

    public LocalDate getData_prevista() {
        return data_prevista;
    }

    public void setData_prevista(LocalDate data_prevista) {
        this.data_prevista = data_prevista;
    }

    public LocalDate getData_devolucao() {
        return data_devolucao;
    }

    public void setData_devolucao(LocalDate data_devolucao) {
        this.data_devolucao = data_devolucao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRenovacoes() {
        return renovacoes;
    }

    public void setRenovacoes(int renovacoes) {
        this.renovacoes = renovacoes;
    }

    public boolean validar() {
        return validarCampos() && validarDataPrevista();
    }

    public boolean validarCampos() {
        return exemplar != null
                && exemplar.validar()
                && aluno != null
                && renovacoes >= 0
                && renovacoes <= 3;
    }

    public boolean validarDataPrevista() {
        return data_retirada != null
                && data_prevista != null
                && !data_prevista.isBefore(data_retirada)
                && !data_prevista.isAfter(data_retirada.plusDays(7L * (renovacoes + 1)));
    }

    public boolean validarDataRetirada() {
        return data_retirada != null && !data_retirada.isBefore(LocalDate.now());
    }

    public boolean validarDisponibilidadeExemplar() {
        return exemplar != null && exemplar.validarDisponibilidade();
    }

    public boolean podeRenovar() {
        return data_devolucao == null
                && "Emprestado".equals(status)
                && data_prevista != null
                && renovacoes < 3;
    }

    public boolean renovar() {
        if (!podeRenovar()) {
            return false;
        }
        data_prevista = data_prevista.plusWeeks(1);
        renovacoes++;
        return true;
    }

}