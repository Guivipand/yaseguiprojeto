package com.ifsp.yas1.Model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Matricula")
public class Matricula {
    public static final List<String> STATUS = List.of("Ativa", "Trancada", "Concluída", "Cancelada");

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @ManyToOne
    @JoinColumn (name = "aluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn (name = "curso")
    private Curso curso;

    @ManyToOne
    @JoinColumn (name = "ofertaDisciplina")
    private OfertaDisciplina ofertaDisciplina;

    @DateTimeFormat (iso = DateTimeFormat.ISO.DATE)
    @Column (name = "data_matricula")
    private LocalDate data_matricula;

    @Column (name = "status", columnDefinition = "varchar(20) default 'Ativa'")
    private String status;

    public Matricula() {
    }

    public Matricula(Aluno aluno, Curso curso, OfertaDisciplina ofertaDisciplina) {
        this.aluno = aluno;
        this.curso = curso;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(OfertaDisciplina ofertaDisciplina) {
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public LocalDate getData_matricula() {
        return data_matricula;
    }

    public void setData_matricula(LocalDate data_matricula) {
        this.data_matricula = data_matricula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean validarAluno() {
        return aluno != null;
    }

    public boolean validarCurso() {
        return curso != null;
    }

    public boolean validarOfertaDisciplina() {
        return ofertaDisciplina != null;
    }

    public boolean validarStatus() {
        return STATUS.contains(status);
    }

    public boolean validar() {
        return validarAluno()
            && validarCurso()
            && validarOfertaDisciplina()
            && validarStatus();
    }
}
