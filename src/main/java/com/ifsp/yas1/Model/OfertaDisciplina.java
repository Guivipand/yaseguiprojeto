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
@Table (name = "OfertaDisciplina")
public class OfertaDisciplina {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @ManyToOne
    @JoinColumn (name = "disciplina")
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn (name = "professor")
    private Professor professor;

    public OfertaDisciplina() {
    }

    public OfertaDisciplina(Disciplina disciplina, Professor professor) {
        this.disciplina = disciplina;
        this.professor = professor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public boolean validarDisciplina() {
        return disciplina != null;
    }

    public boolean validarProfessor() {
        return professor != null;
    }

    public boolean validar() {
        return validarDisciplina()
            && validarProfessor();
    }
}
