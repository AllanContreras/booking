package edu.eci.cvds.proyect.booking.persistency.entity;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BookingTest {

    @Test
    void testSetAndGetId() {
        Booking booking = new Booking();
        booking.setId("123");
        assertEquals("123", booking.getId());
    }

    @Test
    void testSetAndGetLaboratoryName() {
        Booking booking = new Booking();
        LaboratoryName labName = LaboratoryName.AULA_EDFI;
        booking.setLaboratoryName(labName);
        assertEquals(labName, booking.getLaboratoryName());
    }

    @Test
    void testSetAndGetDay() {
        Booking booking = new Booking();
        Day day = Day.LUNES;
        booking.setDay(day);
        assertEquals(day, booking.getDay());
    }

    @Test
    void testSetAndGetHours() {
        Booking booking = new Booking();
        Hour startHour = Hour.OCHO_TREINTA;
        Hour endHour = Hour.DIEZ;
        booking.setStartHour(startHour);
        booking.setEndHour(endHour);
        assertEquals(startHour, booking.getStartHour());
        assertEquals(endHour, booking.getEndHour());
    }

    @Test
    void testSetAndGetAvailable() {
        Booking booking = new Booking();
        booking.setAvailable(true);
        assertTrue(booking.getAvailable());
    }

    @Test
    void testSetAndGetOwnerIds() {
        Booking booking = new Booking();
        List<String> owners = new ArrayList<>();
        owners.add("user1");
        owners.add("user2");
        booking.setOwnerIds(owners);
        assertEquals(owners, booking.getOwnerIds());
    }

    @Test
    void testAddAndRemoveOwnerId() {
        Booking booking = new Booking();
        booking.setOwnerIds(new ArrayList<>());
        booking.addOwnerId("user1");
        assertTrue(booking.getOwnerIds().contains("user1"));

        booking.removeOwnerId("user1");
        assertFalse(booking.getOwnerIds().contains("user1"));
    }

    @Test
    void testSetAndGetPriority() {
        Booking booking = new Booking();
        booking.setPriority(5);
        assertEquals(5, booking.getPriority());
    }

    @Test
    void testToString() {
        Booking booking = new Booking();
        booking.setId("456");
        booking.setAvailable(true);
        String expected = "Booking{id='456', laboratoryName='null', day='null', startHour='null', endHour='null', available='true'}";
        assertEquals(expected, booking.toString());
    }

    @Test
    void testEquals() {
        Booking booking1 = new Booking("789");
        Booking booking2 = new Booking("789");
        assertEquals(booking1, booking2);
    }
}
