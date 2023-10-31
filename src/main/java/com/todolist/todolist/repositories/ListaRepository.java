package com.todolist.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.todolist.todolist.domain.entities.Lista;
import java.util.List;



@Repository
public interface ListaRepository extends JpaRepository<Lista, Long> {

    @Query("SELECT l FROM Lista l WHERE l.usuario.codigo = :codigo")
    List<Lista> findByUsuarioId(Long codigo);
}
 