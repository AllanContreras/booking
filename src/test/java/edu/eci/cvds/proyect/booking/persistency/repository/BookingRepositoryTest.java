package edu.eci.cvds.proyect.booking.persistency.repository;

import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void testFindByOwnerIdsContaining() {
        Booking booking = new Booking();
        booking.setId("1");
        booking.setOwnerIds(List.of("user123"));
        bookingRepository.save(booking);

        List<Booking> result = bookingRepository.findByOwnerIdsContaining("user123");
        assertFalse(result.isEmpty());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    void testFindByOwnerIdsContainingAndAvailableTrue() {
        Booking booking = new Booking();
        booking.setId("2");
        booking.setOwnerIds(List.of("user456"));
        booking.setAvailable(true);
        bookingRepository.save(booking);

        List<Booking> result = bookingRepository.findByOwnerIdsContainingAndAvailableTrue("user456");
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getAvailable());
    }

    @Test
    void testDeleteById() {
        Booking booking = new Booking();
        booking.setId("3");
        booking.setOwnerIds(List.of("user789"));
        bookingRepository.save(booking);

        assertTrue(bookingRepository.existsById("3"));
        bookingRepository.deleteById("3");

        assertFalse(bookingRepository.existsById("3"));
    }

}
