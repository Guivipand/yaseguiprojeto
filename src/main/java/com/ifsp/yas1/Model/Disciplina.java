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
@Table (name = "Disciplina")
public class Disciplina {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "materia")
    private String materia;

    @Column (name = "semestre")
    private String semestre;

    @ManyToOne
    @JoinColumn (name = "curso")
    private Curso curso;

    public Disciplina() {
    }

    public Disciplina(String materia, String semestre, Curso curso) {
        this.materia = materia;
        this.semestre = semestre;
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public boolean validarMateria() {
        return materia != null && !materia.trim().isEmpty();
    }

    public boolean validarSemestre() {
        return semestre != null && !semestre.trim().isEmpty();
    }

    public boolean validarCurso() {
        return curso != null;
    }

    public boolean validar() {
        return validarMateria()
            && validarSemestre()
            && validarCurso();
    }
}
