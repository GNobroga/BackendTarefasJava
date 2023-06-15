package com.todolist.todolist.domain.filters;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.domain.entities.dtos.UsuarioDTO;
import com.todolist.todolist.domain.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UsuarioAutenticacaoFiltro extends UsernamePasswordAuthenticationFilter {
    
    private AuthenticationManager authenticationManager;
	private JwtUtils jwtUtils;

	public UsuarioAutenticacaoFiltro(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			UsuarioDTO dto = new ObjectMapper().readValue(request.getInputStream(), UsuarioDTO.class);
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(dto.apelido(), dto.senha());
			return authenticationManager.authenticate(authenticationToken);
		} catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
		}
	}
	
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
                                            
		Usuario user = ((Usuario) auth.getPrincipal());
		String token = jwtUtils.generateToken(user);

		res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, enctype, Location");
        res.setHeader("Authorization", "Bearer " + token);
        res.setStatus(HttpStatus.OK.value());
        res.getWriter().append(jsonToken(token));

	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(401);
		response.setContentType("application/json");
		System.out.println("Entrouu");
		response.getWriter().append(json());
	}

    private CharSequence jsonToken(String token) {
        return "{ \"token\": \"%s\" }".formatted(token);
    }

	private CharSequence json() {
		long date = new Date().getTime();
		return "{"
				+ "\"timestamp\": " + date + ", " 
				+ "\"status\": 401, "
				+ "\"error\": \"Não autorizado\", "
				+ "\"message\": \"Email ou senha inválidos\", "
				+ "\"path\": \"/login\"}";
	}
}
