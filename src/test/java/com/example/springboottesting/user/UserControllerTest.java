package com.example.springboottesting.user;

import com.example.springboottesting.errorhandling.MyControllerAdvice;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    @Mock UserService userService;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new MyControllerAdvice())
                .build();
    }

    @Test
    public void shouldAddUser() throws Exception {
        String content = loadResourceAsString("testdata/user/validCreateUserDto.json");

        UserDto userDto = new UserDto();
        long id = 2L;
        userDto.setId(id);
        String email = "a@a.pl";
        userDto.setEmail(email);
        String adam = "adam";
        userDto.setFirstName(adam);
        userDto.setPassword("secure password");

        when(userService.addUser(any())).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(adam))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).addUser(any());
    }

    @Test
    public void shouldNotAddUser() throws Exception {
        String content = loadResourceAsString("testdata/user/invalidCreateUserDto.json");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.validationErrors[*].field", Matchers.containsInAnyOrder("firstName", "email")));

        verify(userService, never()).addUser(any());
    }

    private String loadResourceAsString(String name) {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(name);
        if (resourceAsStream == null) {
            throw new RuntimeException("resource " + name + " not found");
        }
        try (Reader reader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}