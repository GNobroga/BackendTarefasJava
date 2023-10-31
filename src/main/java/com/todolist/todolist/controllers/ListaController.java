package com.todolist.todolist.controllers;

import java.util.List;

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
import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.exceptions.ObjectNotFoundException;
import com.todolist.todolist.repositories.ListaRepository;
import com.todolist.todolist.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/listas")
@Validated
public class ListaController {

    @Autowired
    private ListaRepository listaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Lista> todos() {
        return listaRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Lista criar(@RequestBody @Valid Lista lista) {
        lista.setCodigo(null);
        Usuario usuario = usuarioRepository.findById(lista.getUsuarioId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuario nao encontrado"));

        lista.setUsuario(usuario);
        Lista listaSalva = listaRepository.save(lista);
        return listaRepository.findById(listaSalva.getCodigo())
                .orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Lista alterar(@PathVariable Long id, @RequestBody @Valid Lista lista) {
        Lista listaSalva = listaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado"));
        listaSalva.setTitulo(lista.getTitulo());
        listaSalva = listaRepository.save(listaSalva);
        listaSalva.setUsuarioId(listaSalva.getUsuario().getCodigo());
        return listaSalva;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deletar(@PathVariable Long id) {
        listaRepository.deleteById(id);
    }

}
