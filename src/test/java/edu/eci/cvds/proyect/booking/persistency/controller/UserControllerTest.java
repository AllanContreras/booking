package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.persistency.dto.UserDto;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;
import edu.eci.cvds.proyect.booking.persistency.security.JwtUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSaveUserSuccess() throws Exception {
        UserDto userDto = new UserDto("Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "password");
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "hashedPassword");

        Mockito.when(userService.save(Mockito.any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Andres Silva"));
    }

    @Test
    void testSaveUserFailure() throws Exception {
        UserDto userDto = new UserDto("Error User", "error@example.com", UserRole.TEACHER, "password");

        Mockito.when(userService.save(Mockito.any(UserDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al guardar el usuario"));
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        User user1 = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "hashedPassword");
        User user2 = new User(2, "Juan Lopez", "JuanLopez@gmail.com", UserRole.TEACHER, "hashedPassword");

        Mockito.when(userService.getAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Andres Silva"))
                .andExpect(jsonPath("$[1].name").value("Juan Lopez"));
    }

    @Test
    void testFindOneUserNotFound() throws Exception {
        Integer userId = 99;

        Mockito.when(userService.getOne(userId)).thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/User/" + userId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al obtener el usuario con ID " + userId));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        Integer userId = 1;
        User deletedUser = new User(userId, "Test User", "test@example.com", UserRole.TEACHER, "hashedPassword");

        Mockito.when(userService.delete(userId)).thenReturn(deletedUser);

        mockMvc.perform(delete("/User/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message").value("Usuario eliminado correctamente"));
    }

    @Test
    void testDeleteUserFailure() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete error")).when(userService).delete(1);

        mockMvc.perform(delete("/User/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar el usuario"));
    }
}
