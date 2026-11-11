package com.ifsp.yas1.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table (name = "Bibliotecario")
public class Bibliotecario extends Pessoa {
    public static final int IDADE_MINIMA = 18;

    @Column (name = "prontuario")
    private String prontuario;

    @Column (name = "crb")
    private String crb;

    public Bibliotecario() {
    }

    public String getProntuario() {
        return prontuario;
    }

    public void setProntuario(String prontuario) {
        this.prontuario = prontuario;
    }

    public String getCrb() {
        return crb;
    }

    public void setCrb(String crb) {
        this.crb = crb;
    }

    public boolean validarProntuario() {
        return prontuario != null && !prontuario.trim().isEmpty();
    }

    public boolean validarCrb() {
        return crb != null && !crb.trim().isEmpty();
    }

    public boolean validarIdade() {
        return validarIdadeMinima(IDADE_MINIMA);
    }

    public boolean validarDados() {
        return super.validar()
            && validarProntuario()
            && validarCrb();
    }

    @Override
    public boolean validar() {
        return validarDados()
            && validarIdade();
    }
}
