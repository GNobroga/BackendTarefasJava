package com.todolist.todolist.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.boot.internal.IdGeneratorInterpreterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todolist.todolist.domain.entities.Lista;
import com.todolist.todolist.domain.entities.Tarefa;
import com.todolist.todolist.domain.entities.Usuario;
import com.todolist.todolist.domain.entities.dtos.UsuarioDTO;
import com.todolist.todolist.exceptions.ObjectNotFoundException;
import com.todolist.todolist.repositories.ApiRepository;
import com.todolist.todolist.repositories.TaskRepository;
import com.todolist.todolist.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApiService {

    @Autowired
    private ApiRepository repository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<Lista> findAll() {
        return repository.findAll();
    }

    public Lista findById(Long id) {
        return repository
            .findById(id)
            .orElseThrow(() ->
                 new ObjectNotFoundException("Nao foi possivel encontrador a lista com id: " + id)
            );
    }

    public Lista create(Lista obj) {
        obj.setCodigo(null);

        List<Tarefa> tarefas = obj.getTarefas();
        obj.setTarefas(null);

        Lista lista = repository.save(obj);

        tarefas.forEach( tarefa -> {
            tarefa.setCodigo(null);
            tarefa.setLista(lista);
            taskRepository.save(tarefa);
        });

        lista.setTarefas(tarefas);

        return lista;
    }

    @Transactional
    public Lista update(Long id, Lista obj) {
        Optional<Lista> optional = repository.findById(id);
        List<Tarefa> oldTasks = obj.getTarefas();

        if (optional.isPresent()) {
            Lista lista = optional.get();
            List<Tarefa> tarefas = lista.getTarefas();

            oldTasks.sort(this::sort);
            tarefas.sort(this::sort);

            for (int i = 0 ; i < oldTasks.size() ; i++) {
                Tarefa source = oldTasks.get(i);

                if (i < tarefas.size() && source.getCodigo() != null) {
                    Tarefa dest = tarefas.get(i);
                    swapTask(source, dest);
                    taskRepository.save(dest);
                } else {
                    source.setLista(lista);
                    taskRepository.save(source);
                }
            }
        
            lista.setTitulo(obj.getTitulo());
            return repository.save(lista);
        }

        return create(obj);
        
    }

    private int sort(Tarefa x, Tarefa y) {
        Long codigo1 = x.getCodigo();
        Long codigo2 = y.getCodigo();
        if (codigo1 == null && codigo2 == null) {
            return 0;
        } else if (codigo1 == null) {
            return 1;  
        } else if (codigo2 == null) {
            return -1; 
        } else {
            return codigo1.compareTo(codigo2);
        }
    }

    private void swapTask(Tarefa source, Tarefa dest) {
        dest.setAtivo(source.isAtivo());
        dest.setDataCriacao(source.getDataCriacao());
        dest.setDataPrevisao(source.getDataPrevisao());
        dest.setTitulo(source.getTitulo());
        dest.setDescricao(source.getDescricao());
    }

    @Transactional
    public void delete(Lista lista) {
        repository.delete(lista);
    }
    
    @Transactional
    public void deleteTask(Long id) {
        Lista lista = repository.findByTarefa(id);
        if (lista != null) {
            List<Tarefa> tarefas = lista.getTarefas();
            Tarefa tarefa = tarefas.stream()
                    .filter(t -> t.getCodigo().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada: " + id));
            tarefas.remove(tarefa);
            repository.save(lista);
        }
    }

    public List<Tarefa> findAllTasks() {
        return taskRepository.findAll();
    }

    public Usuario createUsuario(UsuarioDTO dto) {
        if (existsUsuario(dto.apelido())) {
            throw new IllegalArgumentException("Usuário já cadastrado");
        }
        Usuario usuario = new Usuario(null, dto.apelido(), encoder.encode(dto.senha()));
        return usuarioRepository.save(usuario);
    }

    private boolean existsUsuario(String usuario) {
        return (usuarioRepository.findByUsuario(usuario) != null);
    } 

}
