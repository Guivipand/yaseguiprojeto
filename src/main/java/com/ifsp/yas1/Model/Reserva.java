package com.ifsp.yas1.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "Reserva")
public class Reserva {
    public static final String AGUARDANDO = "Aguardando";
    public static final String NOTIFICADA = "Notificada";
    public static final String ATENDIDA = "Atendida";
    public static final String EXPIRADA = "Expirada";
    public static final String CANCELADA = "Cancelada";

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @ManyToOne
    @JoinColumn (name = "livro")
    private Livro livro;

    @ManyToOne
    @JoinColumn (name = "aluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn (name = "exemplar")
    private Exemplar exemplar;

    @Column (name = "data_reserva")
    private LocalDateTime data_reserva;

    @Column (name = "prazo_retirada")
    private LocalDateTime prazo_retirada;

    @Column (name = "status")
    private String status;

    @Column (name = "posicao_fila")
    private Integer posicao_fila;

    public Reserva() {
    }

    public Reserva(Livro livro, Aluno aluno) {
        this.livro = livro;
        this.aluno = aluno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public LocalDateTime getData_reserva() {
        return data_reserva;
    }

    public void setData_reserva(LocalDateTime data_reserva) {
        this.data_reserva = data_reserva;
    }

    public LocalDateTime getPrazo_retirada() {
        return prazo_retirada;
    }

    public void setPrazo_retirada(LocalDateTime prazo_retirada) {
        this.prazo_retirada = prazo_retirada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPosicao_fila() {
        return posicao_fila;
    }

    public void setPosicao_fila(Integer posicao_fila) {
        this.posicao_fila = posicao_fila;
    }

    public boolean isPendente() {
        return AGUARDANDO.equals(status) || NOTIFICADA.equals(status);
    }

    public boolean validar() {
        return livro != null && aluno != null;
    }
}
