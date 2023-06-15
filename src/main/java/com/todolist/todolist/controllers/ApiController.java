package com.todolist.todolist.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.domain.entities.Lista;
import com.todolist.todolist.domain.entities.Tarefa;
import com.todolist.todolist.services.ApiService;

import jakarta.validation.Valid;


@RestController
@RequestMapping({ "/listas", "/listas/"})
@Validated
public class ApiController {

    @Autowired
    private ApiService service;
    
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Lista> findAll() {
        return service.findAll().stream().sorted((x, y) -> x.getCodigo().compareTo(y.getCodigo())).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Lista findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Lista create(@RequestBody @Valid Lista lista) {
        return service.create(lista);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Lista update(@PathVariable Long id, @RequestBody @Valid Lista lista) {
        return service.update(id, lista);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        service.delete(service.findById(id));
    }

    @GetMapping("/tarefas")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Tarefa> findAlltasks() {
        return service.findAllTasks();
    }

    @DeleteMapping("/tarefas/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }

}
