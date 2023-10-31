package com.todolist.todolist.domain.entities.dtos;

import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(@NotNull String apelido, @NotNull String senha) {

}
