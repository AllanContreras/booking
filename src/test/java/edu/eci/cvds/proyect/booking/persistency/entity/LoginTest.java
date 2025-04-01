package edu.eci.cvds.proyect.booking.persistency.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void testSetName() {
        Login login = new Login();
        login.setName("UsuarioTest");
        assertEquals("UsuarioTest", login.getName());
    }

    @Test
    void testSetPassword() {
        Login login = new Login();
        login.setPassword("password123");
        assertEquals("password123", login.getPassword());
    }
    @Test
    void testGetName() {
        Login login = new Login();
        login.setName("UsuarioTest");
        assertEquals("UsuarioTest", login.getName());
    }

    @Test
    void testGetPassword() {
        Login login = new Login();
        login.setPassword("password123");
        assertEquals("password123", login.getPassword());
    }

}
