package com.ifsp.yas1.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Curso")
public class Curso {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "titulo")
    private String titulo;

    @Column (name = "qntSemestre")
    private String qntSemestre;

    public Curso() {
    }

    public Curso(String titulo, String qntSemestre) {
        this.titulo = titulo;
        this.qntSemestre = qntSemestre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getQntSemestre() {
        return qntSemestre;
    }

    public void setQntSemestre(String qntSemestre) {
        this.qntSemestre = qntSemestre;
    }

    public boolean validarTitulo() {
        return titulo != null && !titulo.trim().isEmpty();
    }

    public boolean validarQntSemestre() {
        if (qntSemestre == null || qntSemestre.trim().isEmpty()) {
            return false;
        }
        try {
            int valor = Integer.parseInt(qntSemestre.trim());
            return valor >= 1 && valor <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validar() {
        return validarTitulo()
            && validarQntSemestre();
    }
}
