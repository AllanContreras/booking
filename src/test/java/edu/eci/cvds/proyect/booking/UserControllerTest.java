package edu.eci.cvds.proyect.booking;

import edu.eci.cvds.proyect.booking.controller.UserController;
import edu.eci.cvds.proyect.booking.dto.UserDto;
import edu.eci.cvds.proyect.booking.entity.User;
import edu.eci.cvds.proyect.booking.entity.UserRole;
import edu.eci.cvds.proyect.booking.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;




    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSaveUser_Success() throws Exception {
        UserDto userDto = new UserDto("Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");

        Mockito.when(userService.save(Mockito.any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Andres Silva"));
    }

    @Test
    public void testSaveUser_Failure() throws Exception {
        UserDto userDto = new UserDto("Error User", "error@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userService.save(Mockito.any(UserDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/User")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al guardar el usuario"));
    }

    @Test
    public void testFindAllUsers_Success() throws Exception {
        User user1 = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        User user2 = new User(2, "Juan Lopez", "JuanLopez@gmail.com", UserRole.TEACHER, "1234567");

        Mockito.when(userService.getAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/User"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Andres Silva"))
                .andExpect(jsonPath("$[1].name").value("Juan Lopez"));
    }

    @Test
    public void testFindAllUsers_Failure() throws Exception {
        Mockito.when(userService.getAll()).thenThrow(new RuntimeException("Database connection failure"));

        mockMvc.perform(get("/User"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("[]"));  // 💡 Verifica que la respuesta es una lista vacía
    }


    @Test
    public void testUpdateUser_Success() throws Exception {
        UserDto userDto = new UserDto("Updated Name", "updated@example.com", UserRole.TEACHER, "123456");
        User updatedUser = new User(1, "Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userService.update(Mockito.eq(1), Mockito.any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/User/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        Integer userId = 1;

        // Simular que el servicio devuelve un usuario eliminado
        User deletedUser = new User(userId, "Test User", "test@example.com", UserRole.TEACHER, "123456");
        Mockito.when(userService.delete(userId)).thenReturn(deletedUser);

        // Ejecutar la solicitud DELETE a través de MockMvc
        mockMvc.perform(delete("/User/" + userId))
                .andExpect(status().isOk()) // Esperamos que retorne HTTP 200 OK
                .andExpect(jsonPath("$.Message").value("Usuario eliminado correctamente")); // Verificar el mensaje de éxito

        // Verificar que el servicio fue llamado exactamente una vez
        Mockito.verify(userService, Mockito.times(1)).delete(userId);
    }










    @Test
    public void testDeleteUser_Failure_Exception() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete error")).when(userService).delete(1);

        mockMvc.perform(delete("/User/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar el usuario"));
    }
}
