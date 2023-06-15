package com.todolist.todolist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.domain.entities.dtos.UsuarioDTO;
import com.todolist.todolist.services.ApiService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/cadastro"})
public class CadastroController {

    @Autowired
    private ApiService service;

    @PostMapping
    public Usuario cadastrar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return service.createUsuario(usuarioDTO);
    } 

}
