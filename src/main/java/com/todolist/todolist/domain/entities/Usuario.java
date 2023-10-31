package com.todolist.todolist.domain.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@NoArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_codigo")
    private Long codigo;

    @NonNull
    @Column(name = "usuario_apelido", unique = true)
    private String apelido;

    @NonNull
    @Column(name = "usuario_senha")
    private String senha;

    @OneToMany(mappedBy = "usuario")
    @OrderBy("codigo")
    private List<Lista> listas = new ArrayList<>();

    public Usuario(String apelido, String senha, List<Lista> listas) {
        this.apelido = apelido;
        this.senha = senha;
        if (listas != null) {
            this.listas = listas;
        }
    }

}
