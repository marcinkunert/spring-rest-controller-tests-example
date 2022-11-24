package com.example.springboottesting.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.springboottesting.TestUtils.resourceAsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Value("classpath:testdata/user/validCreateUserDto.json")
    private Resource validCreateUserDto;

    @Value("classpath:testdata/user/invalidCreateUserDto.json")
    private Resource invalidCreateUserDto;

    @Test
    public void shouldAddUser() throws Exception {
        //given
        String content = resourceAsString(validCreateUserDto);

        // when + then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));

        // potentially check the repository for changes
    }

    @Test
    public void shouldNotAddUser() throws Exception {
        //given
        String content = resourceAsString(invalidCreateUserDto);

        // when + then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(400))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.validationErrors[*].field", Matchers.containsInAnyOrder("firstName", "email")));
    }

}