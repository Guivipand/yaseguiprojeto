package com.ifsp.yas1.Model;

import java.time.Year;
import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "Livro")
public class Livro {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "titulo")
    private String titulo;

    @Column (name = "autor")
    private String autor;

    @Column (name = "editora")
    private String editora;

    @Column (name = "isbn")
    private String isbn;

    @Column (name = "edicao")
    private String edicao;

    @Column (name = "ano")
    private int ano;

    @Column (name = "sinopse")
    private String sinopse;

    @Column (name = "capa_imagem")
    private String capa_imagem;

    public Livro() {
    }

    public Livro(String titulo, String autor, String editora, String isbn, String edicao, int ano, String sinopse,
            String capa_imagem) {
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
        this.isbn = isbn;
        this.edicao = edicao;
        this.ano = ano;
        this.sinopse = sinopse;
        this.capa_imagem = capa_imagem;
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getCapa_imagem() {
        return capa_imagem;
    }

    public void setCapa_imagem(String capa_imagem) {
        this.capa_imagem = capa_imagem;
    }

    public boolean validarIsbn() {
        if (isbn == null) {
            return false;
        }

        String isbnNormalizado = isbn.replaceAll("[-\\s]", "").toUpperCase(Locale.ROOT);
        if (isbnNormalizado.matches("\\d{13}")) {
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                int peso = i % 2 == 0 ? 1 : 3;
                soma += Character.getNumericValue(isbnNormalizado.charAt(i)) * peso;
            }
            int digitoVerificador = (10 - soma % 10) % 10;
            return digitoVerificador == Character.getNumericValue(isbnNormalizado.charAt(12));
        }

        if (isbnNormalizado.matches("\\d{9}[\\dX]")) {
            int soma = 0;
            for (int i = 0; i < 10; i++) {
                int digito = isbnNormalizado.charAt(i) == 'X'
                        ? 10
                        : Character.getNumericValue(isbnNormalizado.charAt(i));
                soma += digito * (10 - i);
            }
            return soma % 11 == 0;
        }

        return false;
    }

    public boolean validarAno() {
        return ano > 0 && ano <= Year.now().getValue();
    }
}
