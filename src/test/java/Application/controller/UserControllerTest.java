package Application.controller;

import Application.dto.UserCreateDto;
import Application.dto.UserResponseDto;
import Application.dto.UserUpdateDto;
import Application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testGetUserSuccess() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setName("Joao");

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Joao"));

        verify(userService).getUser(1L);
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setName("Joao");
        response.setEmail("joao@test.com");
        response.setAge(25);

        when(userService.addUser(any(UserCreateDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Joao\", \"email\": \"joao@test.com\", \"age\": 25 }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Joao"))
                .andExpect(jsonPath("$.email").value("joao@test.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userService).addUser(any(UserCreateDto.class));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setName("Joao");
        response.setEmail("joao@test.com");
        response.setAge(25);

        when(userService.updateUserData(eq(1L), any(UserUpdateDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Joao\", \"email\": \"joao@test.com\", \"age\": 25 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Joao"));

        verify(userService).updateUserData(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).removeUser(1L);
    }
}
