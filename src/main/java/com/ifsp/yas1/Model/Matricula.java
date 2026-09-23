package com.ifsp.yas1.Model;

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

    public boolean validarAluno() {
        return aluno != null;
    }

    public boolean validarCurso() {
        return curso != null;
    }

    public boolean validarOfertaDisciplina() {
        return ofertaDisciplina != null;
    }

    public boolean validar() {
        return validarAluno()
            && validarCurso()
            && validarOfertaDisciplina();
    }
}
