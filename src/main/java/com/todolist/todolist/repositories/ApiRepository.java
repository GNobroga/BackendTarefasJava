package com.todolist.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.todolist.todolist.domain.entities.Lista;


@Repository
public interface ApiRepository extends JpaRepository<Lista, Long> {

    @Query("SELECT l FROM Lista l JOIN FETCH l.tarefas t WHERE t.codigo = :codigo")
    Lista findByTarefa(@Param("codigo") Long codigo);
}
