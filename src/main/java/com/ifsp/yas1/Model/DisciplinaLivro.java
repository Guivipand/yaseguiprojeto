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
@Table(name = "Disciplina_Livro")
public class DisciplinaLivro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "disciplina")
    private Disciplina disciplina;

    @ManyToOne
    @JoinColumn(name = "livro")
    private Livro livro;

    public DisciplinaLivro() {
    }

    public DisciplinaLivro(Disciplina disciplina, Livro livro) {
        this.disciplina = disciplina;
        this.livro = livro;
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

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public boolean validarDisciplina() {
        return disciplina != null;
    }

    public boolean validarLivro() {
        return livro != null;
    }

    public boolean validar() {
        return validarDisciplina() && validarLivro();
    }
}