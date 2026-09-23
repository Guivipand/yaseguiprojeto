package com.ifsp.yas1.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Professor")
public class Professor extends Pessoa {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "prontuario")
    private String prontuario;

    @Column (name = "formacao")
    private String formacao;

    public Professor() {
    }

    public Professor(String nome, String cpf, String telefone, String email, String dataNasc, String endereco, String prontuario, String formacao, int id) {
        super(nome, cpf, telefone, email, dataNasc, endereco, id);
        this.prontuario = prontuario;
        this.formacao = formacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public boolean validarProntuario() {
        return prontuario != null && !prontuario.trim().isEmpty();
    }

    public boolean validarFormacao() {
        return formacao != null && !formacao.trim().isEmpty();
    }

    @Override
    public boolean validar() {
        return super.validar()
            && validarProntuario()
            && validarFormacao();
    }
}
