package edu.eci.cvds.proyect.booking.persistency.entity;

import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PublicUserTest {

    @Test
    void testSetAndGetId() {
        PublicUser user = new PublicUser(new User("123", "Andres Romero", "andresR@gmail.com", UserRole.ADMIN, "password123"));
        user.setId("456");
        assertEquals("456", user.getId());
    }

    @Test
    void testSetAndGetName() {
        PublicUser user = new PublicUser(new User("123", "Andres Romero", "andresR@gmail.com", UserRole.ADMIN, "password123"));
        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    void testSetAndGetEmail() {
        PublicUser user = new PublicUser(new User("123", "Andres Romero", "andresR@gmail.com", UserRole.ADMIN, "password123"));
        user.setEmail("jane@example.com");
        assertEquals("jane@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetRole() {
        PublicUser user = new PublicUser(new User("123", "Andres Romero", "andresR@gmail.com", UserRole.ADMIN, "password123"));
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void testConstructorWithUser() {
        User mockUser = new User("789", "Andrea", "andrea22@hotmail.com", UserRole.TEACHER, "securePass");
        PublicUser publicUser = new PublicUser(mockUser);

        assertEquals("789", publicUser.getId());
        assertEquals("Andrea", publicUser.getName());
        assertEquals("andrea22@hotmail.com", publicUser.getEmail());
        assertEquals(UserRole.TEACHER, publicUser.getRole());
    }
}
