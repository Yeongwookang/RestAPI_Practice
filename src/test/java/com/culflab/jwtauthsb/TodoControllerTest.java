package com.culflab.jwtauthsb;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

import com.culflab.jwtauthsb.todo.controller.TodoController;
import com.culflab.jwtauthsb.todo.service.TodoService;
import com.culflab.jwtauthsb.authentication.service.JwtService;
import com.culflab.jwtauthsb.todo.service.handler.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@WebMvcTest(TodoController.class)
@WithMockCustomUser
public class TodoControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private JwtService jwtService;

    private static final int CREATED_TODO_ID = 4;
    private static final int UPDATED_TODO_ID = 4;

    @Test
    @DisplayName("TodoList 읽어오기")
    public void retrieveTodos() throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        //Given
        List<Todo> mockList = Arrays.asList(
                new Todo(1, "Jack", "Learn Spring MVC", new Date(), false),
                new Todo(2, "Jack", "Learn Struts", new Date(), false)
        );

        when(todoService.retrieveTodos(anyString())).thenReturn(mockList);

        //When
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/Jack/todos")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //Then
        stringBuilder.append("[");
        stringBuilder.append("{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false},");
        stringBuilder.append("{id:2,user:Jack,desc:\"Learn Struts\",done:false}");
        stringBuilder.append("]");
        JSONAssert.assertEquals(stringBuilder.toString(), result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Todo 한개 읽어오기")
    public void retrieveTodo() throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        //Given
        Todo mockTodo = new Todo(1,"Jack", "Learn Spring MVC", new Date(), false);
        //When
        when(todoService.retrieveTodo(anyInt())).thenReturn(mockTodo);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/v1/users/Jack/todos/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //Then
        stringBuilder.append("{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}");

        JSONAssert.assertEquals(stringBuilder.toString(), result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Todo 추가")
    public void createdTodo() throws Exception {
        //Given
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack", "Learn Spring MVC", new Date(), false);

        //When
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn Spring MVC\",\"done\":\"false\"}";
        when(todoService.addTodo(anyString(), anyString(), isNull(), anyBoolean())).thenReturn(mockTodo);

        //Then
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/users/Jack/todos")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .content(todo)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location",
                        containsString("/users/Jack/todos/" + CREATED_TODO_ID)))
                .andReturn();
    }

    @Test
    @DisplayName("Todo 추가 유효성검사 실패")
    public void created4xxTodo() throws Exception {
        //Given
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack", "Learn Spring MVC", new Date(), false);

        //When
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn\",\"done\":\"false\"}";
        when(todoService.addTodo(anyString(), anyString(), isNull(), anyBoolean())).thenReturn(mockTodo);
        //Then
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/v1/users/Jack/todos")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(todo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @DisplayName("Todo 수정")
    public void updateTodo() throws Exception{
        //Given
        Todo mockTodo = new Todo(UPDATED_TODO_ID,"Jack","Learn Spring MVC and Database", new Date(), false);

        //When
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn Spring MVC and Database\",\"done\":false}";
        when(todoService.update(mockTodo)).thenReturn(mockTodo);

        //Then
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/api/v1/users/Jack/todos/"+UPDATED_TODO_ID)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(todo)
                            .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andReturn();
        JSONAssert.assertEquals(todo, result.getResponse().getContentAsString(), false);
    }

    @Test
    @DisplayName("Todo 삭제")
    public void deleteTodo()throws Exception{
        //Given
        Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", new Date(), false);

        //When
        when(todoService.deleteById(anyInt())).thenReturn(mockTodo);

        //Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/Jack/Todos/"+mockTodo.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }
}
