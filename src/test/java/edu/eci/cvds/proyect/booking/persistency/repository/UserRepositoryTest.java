package edu.eci.cvds.proyect.booking.persistency.repository;

import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.users.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByName() {
        User user = new User("1", "Carlos Pérez", "carlos@example.com", UserRole.ADMIN, "password123");
        userRepository.save(user);

        User result = userRepository.findByName("Carlos Pérez");
        assertNotNull(result);
        assertEquals("carlos@example.com", result.getEmail());
    }

    @Test
    void testFindByEmail() {
        User user = new User("2", "María Gómez", "maria@example.com", UserRole.TEACHER, "securePass");
        userRepository.save(user);

        User result = userRepository.findByEmail("maria@example.com");
        assertNotNull(result);
        assertEquals("María Gómez", result.getName());
    }
}
