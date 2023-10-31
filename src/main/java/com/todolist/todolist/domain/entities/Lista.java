package com.todolist.todolist.domain.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cod_usuario")
    private Usuario usuario;

    @Transient
    @NonNull
    private Long usuarioId;

    @OneToMany(mappedBy = "lista", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("codigo")
    private List<Tarefa> tarefas = new ArrayList<>();

    public Lista(String titulo, Usuario usuario) {
        this.titulo = titulo;
        this.usuario = usuario;
    }

    public long getFeitas() {
        return tarefas.stream()
            .filter(t -> t.isFeito())
            .count();
    }

    public long getNaoFeitas() {
        return tarefas.stream()
            .filter(t -> !t.isFeito())
            .count();
    }

     public long getVencidas() {
        return tarefas.stream()
            .filter(t -> t.getExpirou())
            .count();
    }

}
