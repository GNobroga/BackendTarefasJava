package com.todolist.todolist.domain.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLSelect;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lista")
@NoArgsConstructor
@Getter
@Setter
public class Lista {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_lista")
    private Long codigo;

    @NotNull
    @NotBlank
    @Column(name = "titulo_lista", nullable = false)
    private String titulo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @OneToMany(mappedBy = "lista", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Tarefa> tarefas = new ArrayList<>();

    public Lista(String titulo) {
        this.titulo = titulo;
    }

    public void adicionarTarefas(Tarefa ...tarefas) {
        for (Tarefa tarefa: tarefas) {
            if (tarefa != null && !this.tarefas.contains(tarefa)) {
                this.tarefas.add(tarefa);
                tarefa.setLista(this);
            }
        }
    }

    public void setTarefas(List<Tarefa> tarefas) {
        if (tarefas == null) {
            this.tarefas = new ArrayList<>();
        } else {
            this.tarefas = tarefas;
        }
    }


}
