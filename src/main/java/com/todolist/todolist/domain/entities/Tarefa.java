package com.todolist.todolist.domain.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tarefa")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_tarefa")
    private Long codigo;

    @NotBlank(message = "Preencha o titulo da tarefa")
    @NotNull
    @Column(name = "titulo_tarefa", nullable = false)
    private String titulo;

    @Column(name = "descricao_tarefa", length = 255)
    private String descricao = "";

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @NotNull(message = "Preencha a data de previsao")
    @Column(name = "data_previsao", nullable = false)
    private LocalDate dataPrevisao;
  
    @Column(name = "feito_tarefa", nullable = false)
    private boolean feito = false;
    
    @ToString.Exclude 
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cod_lista", nullable = false)
    private Lista lista;

    @Transient
    @NotNull
    private Long listaId;

    public Tarefa(String titulo, String descricao, LocalDate dataPrevisao, Lista lista) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataPrevisao = dataPrevisao;
        this.lista = lista;
    }

    public boolean getExpirou() {
        return LocalDate.now().isAfter(dataPrevisao);
    }

    public LocalDate getConclusao() {
        if (this.feito) {
            return LocalDate.now();
        } 
        return null;
    }

    public LocalDate getVencimento() {
        if (this.getExpirou()) {
            return LocalDate.now();
        }
        return null;
    }
    
}
