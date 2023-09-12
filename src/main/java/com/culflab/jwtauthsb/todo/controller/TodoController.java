package com.culflab.jwtauthsb.todo.controller;

import com.culflab.jwtauthsb.todo.service.TodoService;
import com.culflab.jwtauthsb.todo.service.handler.Todo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Validated
public class TodoController {
    private final TodoService todoService;

    @Cacheable("todos")
    @GetMapping("{name}/todos")
    public List<Todo> retrieveTodos(@PathVariable String name){
        return todoService.retrieveTodos(name);
    }

    @GetMapping("{name}/todos/{id}")
    public Todo retrieveTodo(@PathVariable String name, @PathVariable int id){
        Todo todo = todoService.retrieveTodo(id);

        return todo;
    }

    @PostMapping("{name}/todos")
    public ResponseEntity<?> add(@PathVariable String name, @Valid @RequestBody  Todo todo){
        Todo createdTodo = todoService.addTodo(
                name, todo.getDesc(), todo.getTargetDate(), todo.isDone()
        );

        if (createdTodo == null) return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdTodo.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("{name}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String name, @PathVariable int id, @RequestBody Todo todo){

        todoService.update(todo);
        return ResponseEntity.ok().body(todo);
    }

    @DeleteMapping("{name}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String name, @PathVariable int id){
        Todo todo = todoService.deleteById(id);
        if(todo!=null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
