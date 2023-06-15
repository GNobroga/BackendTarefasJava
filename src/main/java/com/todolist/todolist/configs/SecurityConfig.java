package com.todolist.todolist.configs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.todolist.todolist.domain.entities.Lista;
import com.todolist.todolist.domain.entities.Tarefa;
import com.todolist.todolist.domain.filters.UsuarioAutenticacaoFiltro;
import com.todolist.todolist.domain.filters.UsuarioAutorizacaoFiltro;
import com.todolist.todolist.domain.utils.JwtUtils;
import com.todolist.todolist.repositories.ApiRepository;
import com.todolist.todolist.repositories.TaskRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer, CommandLineRunner {

    @Autowired
    private ApiRepository listaRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AuthenticationConfiguration config;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioAutorizacaoFiltro filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http 
            .cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/login", "/login/")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/login", "/login/", "/login/**", "/cadastro")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new UsuarioAutenticacaoFiltro(authenticationManager(), jwtUtils))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return config.getAuthenticationManager();
    } 

    @Bean
    public PasswordEncoder passwordEnconder() {
        return new BCryptPasswordEncoder();
    }

    @Override 
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
            .allowedOrigins("*");
	}

    @Override 
    public void run(String ...args) {
        final int QUANTIDADE = 100;
        List<Lista> listas = new ArrayList<>();
        List<Tarefa> tarefas = new ArrayList<>();

        for (int i = 1 ; i <= QUANTIDADE ; i++) {
            listas.add(new Lista("Lista " + i));
        }

        // for (int i = 1 ; i <= QUANTIDADE ; i++) {

        //     for (int j = 1 ; j <= QUANTIDADE ; j++) {
        //         tarefas.add(new Tarefa("Tarefa " + j , "Fazer " + j,  LocalDate.now().plusDays(3), listas.get(i - 1)));    
        //     }
        // }

        listaRepository.saveAll(listas);
        // taskRepository.saveAll(tarefas);
    }

}
