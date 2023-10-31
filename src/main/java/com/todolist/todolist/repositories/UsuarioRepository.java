package com.todolist.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.todolist.todolist.domain.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.apelido = :apelido")
    Usuario findByApelido(@Param("apelido") String apelido);
}
