package com.example.springboottesting.user;

import com.example.springboottesting.errorhandling.MyControllerAdvice;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.springboottesting.TestUtils.resourceAsString;
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
        // given
        String content = resourceAsString("testdata/user/validCreateUserDto.json");

        long id = 2L;
        String email = "a@a.pl";
        String username = "adam";
        UserDto userDto = new UserDto(id, email, username, "secure password");

        when(userService.addUser(any())).thenReturn(userDto);

        // when + then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value(username))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService, times(1)).addUser(any());
    }

    @Test
    public void shouldNotAddUser() throws Exception {
        // given
        String content = resourceAsString("testdata/user/invalidCreateUserDto.json");

        // when + then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.validationErrors[*].field", Matchers.containsInAnyOrder("firstName", "email")));

        verify(userService, never()).addUser(any());
    }


}