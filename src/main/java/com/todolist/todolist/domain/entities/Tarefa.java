package com.todolist.todolist.domain.entities;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @NonNull
    @Column(name = "titulo_tarefa", nullable = false)
    private String titulo;

    @Column(name = "descricao_tarefa", length = 255)
    private String descricao = "";

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @NotNull(message = "Preencha a data de previsao")
    @Column(name = "data_previsao", nullable = false)
    private LocalDate dataPrevisao;
  
    @Column(name = "ativo_tarefa", nullable = false)
    private boolean ativo = true;
    
    @ToString.Exclude 
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cod_lista", nullable = false)
    private Lista lista;

    public Tarefa(String titulo, String descricao, LocalDate dataPrevisao, Lista lista) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataPrevisao = dataPrevisao;
        this.lista = lista;
    }
    
}
