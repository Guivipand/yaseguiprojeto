package com.ifsp.yas1.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Prova")
public class Prova {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @ManyToOne
    @JoinColumn (name = "ofertaDisciplina")
    private OfertaDisciplina ofertaDisciplina;

    @DateTimeFormat (iso = DateTimeFormat.ISO.DATE)
    @Column (name = "data")
    private LocalDate data;

    @Column (name = "conteudo")
    private String conteudo;

    @Column (name = "peso")
    private double peso = 1.0;

    @ManyToMany
    @JoinTable (name = "prova_aluno_estudou",
            joinColumns = @JoinColumn (name = "prova"),
            inverseJoinColumns = @JoinColumn (name = "aluno"))
    private List<Aluno> alunosQueEstudaram = new ArrayList<>();

    public Prova() {
    }

    public Prova(OfertaDisciplina ofertaDisciplina, LocalDate data, String conteudo, double peso) {
        this.ofertaDisciplina = ofertaDisciplina;
        this.data = data;
        this.conteudo = conteudo;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(OfertaDisciplina ofertaDisciplina) {
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public List<Aluno> getAlunosQueEstudaram() {
        return alunosQueEstudaram;
    }

    public void setAlunosQueEstudaram(List<Aluno> alunosQueEstudaram) {
        this.alunosQueEstudaram = alunosQueEstudaram;
    }

    public boolean foiEstudadaPor(Aluno aluno) {
        return aluno != null && alunosQueEstudaram.stream().anyMatch(item -> item.getId() == aluno.getId());
    }

    public boolean validarOfertaDisciplina() {
        return ofertaDisciplina != null;
    }

    public boolean validarData() {
        return data != null;
    }

    public boolean validarConteudo() {
        return conteudo != null
            && !conteudo.trim().isEmpty()
            && conteudo.length() <= 255;
    }

    public boolean validarPeso() {
        return peso > 0 && peso <= 10;
    }

    public boolean validar() {
        return validarOfertaDisciplina()
            && validarData()
            && validarConteudo()
            && validarPeso();
    }
}
