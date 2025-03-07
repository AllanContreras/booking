package edu.eci.cvds.proyect.booking;

import edu.eci.cvds.proyect.booking.controller.UserController;
import edu.eci.cvds.proyect.booking.documents.UserRole;
import edu.eci.cvds.proyect.booking.documents.Users;
import edu.eci.cvds.proyect.booking.repository.UserRepository;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSaveUser_Success() throws Exception {
        Users user = new Users(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

        mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Andres Silva"));
    }

    @Test
    public void testSaveUser_Failure() throws Exception {
        Users user = new Users(1, "Error User", "error@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.save(Mockito.any(Users.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al guardar el usuario: Database error"));
    }

    @Test
    public void testFindAllUsers_Success() throws Exception {
        Users user1 = new Users(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        Users user2 = new Users(2, "Juan Lopez", "JuanLopez@gmail.com", UserRole.TEACHER, "1234567");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/Users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Andres Silva"))
                .andExpect(jsonPath("$[1].name").value("Juan Lopez"));
    }

    @Test
    public void testFindAllUsers_Failure() throws Exception {
        Mockito.when(userRepository.findAll()).thenThrow(new RuntimeException("Database connection failure"));

        mockMvc.perform(get("/Users"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al obtener los usuarios: Database connection failure"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        Users user = new Users(1, "Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

        mockMvc.perform(put("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testUpdateUser_Failure_UserNotFound() throws Exception {
        Users user = new Users(1, "Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(false);

        mockMvc.perform(put("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    public void testUpdateUser_Failure_Exception() throws Exception {
        Users user = new Users(1, "Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userRepository.existsById(user.getId())).thenReturn(true);
        Mockito.when(userRepository.save(Mockito.any(Users.class)))
                .thenThrow(new RuntimeException("Update failed"));

        mockMvc.perform(put("/Users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al actualizar el usuario: Update failed"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(1);

        mockMvc.perform(delete("/Users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado"));
    }

    @Test
    public void testDeleteUser_Failure_UserNotFound() throws Exception {
        Mockito.when(userRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(delete("/Users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    public void testDeleteUser_Failure_Exception() throws Exception {
        Mockito.when(userRepository.existsById(1)).thenReturn(true);
        Mockito.doThrow(new RuntimeException("Delete error")).when(userRepository).deleteById(1);

        mockMvc.perform(delete("/Users/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al eliminar el usuario: Delete error"));
    }
}
