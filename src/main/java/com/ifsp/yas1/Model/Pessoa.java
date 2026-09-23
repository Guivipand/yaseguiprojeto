package com.ifsp.yas1.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table (name = "Pessoa")
public class Pessoa {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id")
    private int id;

    @Column (name = "nome")
    private String nome;

    @Column (name = "cpf")
    private String cpf;

    @Column (name = "telefone")
    private String telefone;

    @Column (name = "email")
    private String email;

    @Column (name = "dataNasc")
    private String dataNasc;

    @Column (name = "endereco")
    private String endereco;

    public Pessoa() {
    }

    public Pessoa(String nome, String cpf, String telefone, String email, String dataNasc, String endereco, int id) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.dataNasc = dataNasc;
        this.endereco = endereco;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean validarNome() {
        return nome != null && !nome.trim().isEmpty();
    }

    public boolean validarCpf() {
        if (cpf == null) {
            return false;
        }

        String numero = cpf.replaceAll("[^0-9]", "");

        if (numero.length() != 11) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += Integer.parseInt(numero.substring(i, i + 1)) * (10 - i);
        }

        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;

        if (digito1 != Integer.parseInt(numero.substring(9, 10))) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += Integer.parseInt(numero.substring(i, i + 1)) * (11 - i);
        }

        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;

        return digito2 == Integer.parseInt(numero.substring(10, 11));
    }

    public boolean validarTelefone() {
        return telefone != null && !telefone.trim().isEmpty();
    }

    public boolean validarEmail() {
        return email != null
            && email.contains("@")
            && email.contains(".")
            && !email.contains(" ");
    }

    public boolean validarDataNasc() {
        return dataNasc != null && !dataNasc.trim().isEmpty();
    }

    public boolean validarEndereco() {
        return endereco != null && !endereco.trim().isEmpty();
    }

    public boolean validar() {
        return validarNome()
            && validarCpf()
            && validarTelefone()
            && validarEmail()
            && validarDataNasc()
            && validarEndereco();
    }
}
