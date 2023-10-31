package com.todolist.todolist.domain.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.todolist.todolist.domain.entities.auth.UsuarioAuth;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expires}")
    private Long minutes;
    
    public String generateToken(UsuarioAuth usuario) {
        return JWT
            .create()
            .withClaim("id", usuario.getCodigo())
            .withSubject(usuario.getApelido())
            .withExpiresAt(LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00")))
            .sign(Algorithm.HMAC256(secret));
    }

    public String getSubject(String token) {
        return JWT
            .require(Algorithm.HMAC256(secret))
            .build()
            .verify(token)
            .getSubject();
    }
}
