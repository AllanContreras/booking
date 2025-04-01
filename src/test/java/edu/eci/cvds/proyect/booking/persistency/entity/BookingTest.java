package edu.eci.cvds.proyect.booking.persistency.entity;

import org.junit.jupiter.api.Test;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class BookingTest {
    @Test
    void getBookingIdTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        assertEquals(123, booking.getId());
    }

    @Test
    void getBookingUserIdTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        assertEquals(10000087,booking.getUserId());
    }

    @Test
    void getBookingLaboratoryNameTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        assertEquals(LaboratoryName.FUNDAMENTOS,booking.getLaboratoryName());
    }

    @Test
    void getBookingDayTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        assertEquals(Day.MARTES,booking.getDay());
    }

    @Test
    void getBookingStartHourTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.OCHO_TREINTA, Hour.DIEZ, BookingStatus.AVAILABLE,5);
        assertEquals(Hour.OCHO_TREINTA,booking.getStartHour());
    }

    @Test
    void getBookingEndHourTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.OCHO_TREINTA, Hour.DIEZ, BookingStatus.AVAILABLE,5);
        assertEquals(Hour.DIEZ,booking.getEndHour());
    }

    @Test
    void getBookingStatusTest(){
        Booking booking = new Booking(123, 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.ONCE_TREINTA, Hour.DIEZ, BookingStatus.UNAVAILABLE,5);
        assertEquals(BookingStatus.UNAVAILABLE,booking.getStatus());
    } 
    
    @Test
    void setBookingIdTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setId(456);
        assertEquals(456, booking.getId());
    }

    @Test
    void setBookingUserIdTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setUserId(20000099);
        assertEquals(20000099, booking.getUserId());
    }

    @Test
    void setBookingLaboratoryNameTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setLaboratoryName(LaboratoryName.DESARROLLO_VJ);
        assertEquals(LaboratoryName.DESARROLLO_VJ, booking.getLaboratoryName());
    }

    @Test
    void setBookingDayTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setDay(Day.MIERCOLES);
        assertEquals(Day.MIERCOLES, booking.getDay());
    }

    @Test
    void setBookingStartHourTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setStartHour(Hour.OCHO_TREINTA);
        assertEquals(Hour.OCHO_TREINTA, booking.getStartHour());
    }

    @Test
    void setBookingEndHourTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setEndHour(Hour.CUATRO);
        assertEquals(Hour.CUATRO, booking.getEndHour());
    }

    @Test
    void setBookingStatusTest() {
        Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
        booking.setStatus(BookingStatus.UNAVAILABLE);
        assertEquals(BookingStatus.UNAVAILABLE, booking.getStatus());

    }
     @Test
     void setBookingPriorityTest() {
         Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
         booking.setPriority(5);
         assertEquals(5, booking.getPriority());

     }

     @Test
     void getBookingPriorityTest() {
         Booking booking = new Booking(123, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE,5);
         assertEquals(5, booking.getPriority());

     }

}