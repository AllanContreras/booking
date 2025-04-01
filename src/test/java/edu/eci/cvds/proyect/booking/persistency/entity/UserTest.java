package edu.eci.cvds.proyect.booking.persistency.entity;

import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // Pruebas para SETTERS
    @Test
    void testSetId() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        user.setId("456");
        assertEquals("456", user.getId());
    }

    @Test
    void testSetName() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void testSetEmail() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        user.setEmail("jane@example.com");
        assertEquals("jane@example.com", user.getEmail());
    }

    @Test
    void testSetRole() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        user.setRole(UserRole.TEACHER);
        assertEquals(UserRole.TEACHER, user.getRole());
    }

    @Test
    void testSetPassword() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        user.setPassword("newSecurePassword");
        assertEquals("newSecurePassword", user.getPassword());
    }

    // Pruebas para GETTERS
    @Test
    void testGetId() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        assertEquals("123", user.getId());
    }

    @Test
    void testGetName() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        assertEquals("Ricardo Rios", user.getName());
    }

    @Test
    void testGetEmail() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        assertEquals("ricardoRiver@hotmail.com", user.getEmail());
    }

    @Test
    void testGetRole() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testGetPassword() {
        User user = new User("123", "Ricardo Rios", "ricardoRiver@hotmail.com", UserRole.ADMIN, "password123");
        assertEquals("password123", user.getPassword());
    }

}
