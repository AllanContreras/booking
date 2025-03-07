package edu.eci.cvds.proyect.booking;
import edu.eci.cvds.proyect.booking.documents.UserRole;
import edu.eci.cvds.proyect.booking.documents.Users;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UsersTest {
    @Test
    public void getIdTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        assertEquals(1, user.getId());
    }

    @Test
    public void getNameTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        assertEquals("Andres silva", user.getName());
    }

    @Test
    public void getEmailTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        assertEquals("AndresSilva@gmail.com", user.getEmail());
    }

    @Test
    public void getRoleTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        assertEquals(UserRole.TEACHER, user.getRole());
    }

    @Test
    public void getPasswordTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        assertEquals("123456", user.getPassword());
    }

    @Test
    public void setIdTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        user.setId(2);
        assertEquals(2, user.getId());
    }
    @Test
    public void setNameTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        user.setName("Updated Name");
        assertEquals("Updated Name", user.getName());
    }
    @Test
    public void setEmailTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        user.setEmail("Updated Email");
        assertEquals("Updated Email", user.getEmail());
    }
    @Test
    public void setRoleTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
    }
    @Test
    public void setPasswordTest() {
        Users user = new Users(1, "Andres silva", "AndresSilva@gmail.com", UserRole.TEACHER, "123456");
        user.setPassword("Updated Password");
        assertEquals("Updated Password", user.getPassword());
    }

}
