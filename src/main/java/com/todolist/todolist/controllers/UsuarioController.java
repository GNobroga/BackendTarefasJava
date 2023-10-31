package com.todolist.todolist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.domain.entities.dtos.UsuarioDTO;
import com.todolist.todolist.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Usuario cadastrar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario(usuarioDTO.apelido(), encoder.encode(usuarioDTO.senha()), null);
        Usuario encontrado = usuarioRepository.findByApelido(usuarioDTO.apelido());
        if (encontrado == null) {
            return usuarioRepository.save(usuario);
        }
        throw new IllegalArgumentException("Apelido já cadastrado");
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Usuario> todos() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Usuario findById(@PathVariable Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("O codigo do usuário não existe."));
    }

}
