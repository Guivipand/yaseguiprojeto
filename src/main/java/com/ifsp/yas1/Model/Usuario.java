package com.ifsp.yas1.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "login")
    private String login;

    @Column (name = "senha_hash")
    private String senha_hash;

    @Column (name = "papel")
    private String papel;

    @Column (name = "foto_perfil")
    private String foto_perfil;

    @Column (name = "ativo")
    private boolean ativo;

    @Column (name = "tipo")
    private String tipo;

    @OneToOne
    @JoinColumn (name = "aluno")
    private Aluno aluno;

    public Usuario() {
    }

    public Usuario(String login, String senha_hash, String papel, String foto_perfil, boolean ativo, Aluno aluno) {
        this.login = login;
        this.senha_hash = senha_hash;
        this.papel = papel;
        this.foto_perfil = foto_perfil;
        this.ativo = ativo;
        this.aluno = aluno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha_hash() {
        return senha_hash;
    }

    public void setSenha_hash(String senha_hash) {
        this.senha_hash = senha_hash;
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

}
