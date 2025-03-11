package edu.eci.cvds.proyect.booking.persistency.controller; 

import edu.eci.cvds.proyect.booking.persistency.dto.UserDto;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import edu.eci.cvds.proyect.booking.persistency.service.UserService;

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


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSaveUserSuccess() throws Exception {
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
    void testSaveUserFailure() throws Exception {
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
    void testFindOneUserSuccess() throws Exception {
        User user = new User(1, "Andres Silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
    
        Mockito.when(userService.getOne(1)).thenReturn(user);
    
        mockMvc.perform(get("/User/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Andres Silva"))
                .andExpect(jsonPath("$.email").value("AndresSilva@gmail.com"))
                .andExpect(jsonPath("$.role").value("TEACHER"));
    }
    @Test
    void testFindOneUserFailure() throws Exception {
        Integer invalidUserId = 99;
    
        Mockito.when(userService.getOne(invalidUserId))
                .thenThrow(new RuntimeException("User not found"));
    
        mockMvc.perform(get("/User/" + invalidUserId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al obtener el usuario con ID " + invalidUserId))
                .andExpect(jsonPath("$.details").value("User not found"));
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
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
    void testFindAllUsersFailure() throws Exception {
        Mockito.when(userService.getAll()).thenThrow(new RuntimeException("Database connection failure"));

        mockMvc.perform(get("/User"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("[]"));  
    }

    

    @Test
    void testUpdateUserSuccess() throws Exception {
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
    void testUpdateUserFailure() throws Exception {
        UserDto userDto = new UserDto("Updated Name", "updated@example.com", UserRole.TEACHER, "123456");

        Mockito.when(userService.update(Mockito.eq(1), Mockito.any(UserDto.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/User/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al actualizar el usuario"));
    }


    @Test
    void testDeleteUserSuccess() throws Exception {
        Integer userId = 1;
        User deletedUser = new User(userId, "Test User", "test@example.com", UserRole.TEACHER, "123456");
        Mockito.when(userService.delete(userId)).thenReturn(deletedUser);

        mockMvc.perform(delete("/User/" + userId))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.Message").value("Usuario eliminado correctamente")); 

        Mockito.verify(userService, Mockito.times(1)).delete(userId);
    }

    @Test
    void testDeleteUserFailure() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete error")).when(userService).delete(1);

        mockMvc.perform(delete("/User/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar el usuario"));
    }

    

}