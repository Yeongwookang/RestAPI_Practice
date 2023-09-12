package com.culflab.jwtauthsb.todo.service;

import com.culflab.jwtauthsb.todo.service.handler.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();
    private static int todoCount =3;

    static{
        todos.add(new Todo(1, "Jack", "Learn Spring MVC", new Date(), false));
        todos.add(new Todo(2, "Jack", "Learn Struts", new Date(), false));
        todos.add(new Todo(3, "Jill", "Learn Hibernate", new Date(), false));
    }

    public List<Todo> retrieveTodos(String user){
        return todos.stream()
                .filter(todo -> todo.getUser().equals(user))
                .toList();
    }

    public Todo retrieveTodo(int id){
        if(todos.stream().noneMatch(todo-> todo.getId() == id)) return null;
        return todos.stream()
                .filter(todo -> todo.getId()== id)
                .findAny().get();
    }

    public Todo addTodo(String user, String desc, Date targetDate, boolean isDone){
        Todo addedTodo = new Todo(++todoCount, user, desc, targetDate, isDone);
        todos.add(addedTodo);
        return addedTodo;
    }

    public Todo update(Todo updatedTodo){
        if(todos.stream().noneMatch(todo -> todo.getId() == updatedTodo.getId())) return null;

        Todo existingTodo = todos.stream()
                    .filter(todo -> todo.getId() == updatedTodo.getId())
                    .findAny().get();

        todos.set(todos.indexOf(existingTodo), updatedTodo);
        return updatedTodo;
    }

    public Todo deleteById (int id){
        Todo todo = retrieveTodo(id);
        if (todo == null) throw new RuntimeException("Todo not found");

        if (todos.remove(todo)) return todo;

        throw new RuntimeException("Delete Unsuccessful");
    }
}
