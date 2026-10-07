package com.ifsp.yas1.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Aluno")
public class Aluno extends Pessoa {
    @Column (name = "idMatricula")
    private String idMatricula;

    @Column (name = "semestre")
    private String semestre;

    @ManyToOne
    @JoinColumn (name = "curso")
    private Curso curso;

    public Aluno() {
    }

    public Aluno(String nome, String cpf, String telefone, String email, LocalDate dataNasc, String endereco, int id, String idMatricula, String semestre, Curso curso) {
        super(nome, cpf, telefone, email, dataNasc, endereco, id);
        this.idMatricula = idMatricula;
        this.semestre = semestre;
        this.curso = curso;
    }

    public String getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(String idMatricula) {
        this.idMatricula = idMatricula;
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

    public boolean validarIdMatricula() {
        return idMatricula != null && !idMatricula.trim().isEmpty();
    }

    public boolean validarSemestre() {
        if (semestre == null || semestre.trim().isEmpty()) {
            return false;
        }
        try {
            int valor = Integer.parseInt(semestre.trim());
            return valor >= 1 && valor <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validarCurso() {
        return curso != null;
    }

    @Override
    public boolean validar() {
        return super.validar()
            && validarIdMatricula()
            && validarSemestre()
            && validarCurso();
    }
}
