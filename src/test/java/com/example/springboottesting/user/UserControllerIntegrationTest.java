package com.example.springboottesting.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
//    @Autowired UserRepository userRepository;

    @Value("classpath:testdata/user/validCreateUserDto.json")
    private Resource validCreateUserDto;

    @Value("classpath:testdata/user/invalidCreateUserDto.json")
    private Resource invalidCreateUserDto;

    @Test
    public void shouldAddUser() throws Exception {
        String content = asString(validCreateUserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));

        // verify
//        userRepository.i

    }

    @Test
    public void shouldNotAddUser() throws Exception {
        String content = """
                {
                    "email": "invalid",
                    "firstName": ""
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asString(invalidCreateUserDto)))
                .andExpect(status().is(400))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.validationErrors[*].field", Matchers.containsInAnyOrder("firstName", "email")));

    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
//
//    public static String readFileToString(String path) throws IOException {
//        return FileUtils.readFileToString(ResourceUtils.getFile(path), StandardCharsets.UTF_8);
//    }

}