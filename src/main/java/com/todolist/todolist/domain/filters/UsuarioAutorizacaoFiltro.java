package com.todolist.todolist.domain.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.domain.utils.JwtUtils;
import com.todolist.todolist.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UsuarioAutorizacaoFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    
        var authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            String apelido = jwtUtils.getSubject(token);
            Usuario usuario = repository.findByUsuario(apelido);
            var encapsulado = new UsernamePasswordAuthenticationToken(apelido, usuario, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(encapsulado);

        }

        filterChain.doFilter(request, response);
    }
    

}
