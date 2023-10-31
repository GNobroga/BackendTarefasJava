package com.todolist.todolist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.todolist.todolist.exceptions.ObjectNotFoundException;
import com.todolist.todolist.repositories.ListaRepository;
import com.todolist.todolist.repositories.TarefaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ListaRepository listaRepository;
    
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Tarefa> todos() {
        return tarefaRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Tarefa criar(@RequestBody @Valid Tarefa tarefa) {
        tarefa.setCodigo(null);
        Lista lista = listaRepository.findById(tarefa.getListaId()).orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
        tarefa.setLista(lista);
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return tarefaRepository.findById(tarefaSalva.getCodigo()) 
            .orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK) 
    public Tarefa atualizar(@PathVariable Long id, @RequestBody @Valid Tarefa tarefa) {
        Tarefa tarefaSalva = tarefaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
        tarefaSalva.setFeito(tarefa.isFeito());
        tarefaSalva.setDataPrevisao(tarefa.getDataPrevisao());
        tarefaSalva.setDescricao(tarefa.getDescricao());
        tarefaSalva.setTitulo(tarefa.getTitulo());
        tarefaSalva.setListaId(tarefaSalva.getLista().getCodigo());
        tarefaSalva = tarefaRepository.save(tarefaSalva);
        tarefaSalva.setListaId(tarefaSalva.getLista().getCodigo());
        return tarefaSalva;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deletar(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
    }
}
