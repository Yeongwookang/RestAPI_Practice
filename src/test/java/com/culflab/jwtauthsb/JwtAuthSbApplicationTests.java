package com.culflab.jwtauthsb;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import com.culflab.jwtauthsb.todo.service.handler.Todo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@SpringBootTest(classes = JwtAuthSbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtAuthSbApplicationTests {
    @Autowired
    private TestRestTemplate template;

    HttpHeaders headers = createHeaders("user-name", "user-password");

    HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders(){
            {   StringBuilder sb = new StringBuilder();

                String auth = sb.append(username).append(":").append(password).toString();
                byte[] encodedAuth = Base64.getEncoder()
                        .encode(auth.getBytes(StandardCharsets.US_ASCII));

                StringBuilder sb1 = new StringBuilder();

                String authHeader = sb1.append("Basic").append(encodedAuth).toString();
                set("Authorization", authHeader);
            }
        };
    }

    @Test
    public void retrieveTodos() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false},");
        sb.append("{id:2,user:Jack,desc:\"Learn Struts\",done:false}");
        sb.append("]");
        String expected = sb.toString();

        ResponseEntity<String> response = template.exchange(
                "/users/Jack/todos",
                HttpMethod.GET,
                new HttpEntity<String>(null, headers), String.class);

        JSONAssert.assertEquals(expected,
                response.getBody(), false);
    }

    @Test
    public void retrieveTodo() throws Exception {
        String expected = "{id:1,user:\"Jack\",desc:\"Learn Spring MVC\",done:false}";

        ResponseEntity<String> response = template.getForEntity("/users/Jack/todos/1", String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void addTodo() throws Exception {
        Todo todo = new Todo(-1, "Jill", "Learn Hibernate", new Date(), false);
        URI location = template.postForLocation("/users/Jill/todos", todo);
        assertThat(location.getPath()).contains("/users/Jill/todos/4");
    }

    @Test
    public void updatedTodo() throws Exception {

        String expected = "{id:4,user:\"Jill\",desc:\"Learn Spring MVC 5\",done:false}";

        Todo todo = new Todo(4, "Jill", "Learn Spring MVC 5", new Date(), false);

        ResponseEntity<String> response = template.exchange("/users/Jill/todos/" + todo.getId(), HttpMethod.PUT,
                new HttpEntity<>(todo, headers), String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void deleteTodo() throws Exception {

        ResponseEntity<String> response = template.exchange("/users/Jill/todos/3", HttpMethod.DELETE,
                new HttpEntity<>(null, headers), String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
