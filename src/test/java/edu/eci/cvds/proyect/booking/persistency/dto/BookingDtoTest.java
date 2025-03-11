package edu.eci.cvds.proyect.booking.persistency.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;

 class BookingDtoTest {
    

    @Test
    void getBookingDtoUserIdTest(){
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        assertEquals(10000087,bookingDto.getUserId());
    }

    @Test
    void getBookingDtoLaboratoryNameTest(){
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        assertEquals(LaboratoryName.FUNDAMENTOS,bookingDto.getLaboratoryName());
    }

    @Test
    void getBookingDtoDayTest(){
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        assertEquals(Day.MARTES,bookingDto.getDay());
    }

    @Test
    void getBookingDtoStartHourTest(){
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.OCHO_TREINTA, Hour.DIEZ, BookingStatus.AVAILABLE);
        assertEquals(Hour.OCHO_TREINTA,bookingDto.getStartHour());
    }

    @Test
    void getBookingDtoEndHourTest(){
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.OCHO_TREINTA, Hour.DIEZ, BookingStatus.AVAILABLE);
        assertEquals(Hour.DIEZ,bookingDto.getEndHour());
    }

    @Test
    void getBookingDtoStatusTest(){
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.DESARROLLO_VJ, Day.MARTES, Hour.ONCE_TREINTA, Hour.DIEZ, BookingStatus.UNAVAILABLE);
        assertEquals(BookingStatus.UNAVAILABLE,bookingDto.getStatus());
    } 
    
    @Test
    void setBookingDtoUserIdTest() {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setUserId(20000099);
        assertEquals(20000099, bookingDto.getUserId());
    }

    @Test
    void setBookingDtoLaboratoryNameTest() {
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setLaboratoryName(LaboratoryName.DESARROLLO_VJ);
        assertEquals(LaboratoryName.DESARROLLO_VJ, bookingDto.getLaboratoryName());
    }

    @Test
    void setBookingDtoDayTest() {
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setDay(Day.MIERCOLES);
        assertEquals(Day.MIERCOLES, bookingDto.getDay());
    }

    @Test
    void setBookingDtoStartHourTest() {
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setStartHour(Hour.OCHO_TREINTA);
        assertEquals(Hour.OCHO_TREINTA, bookingDto.getStartHour());
    }

    @Test
    void setBookingEndHourTest() {
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setEndHour(Hour.CUATRO);
        assertEquals(Hour.CUATRO, bookingDto.getEndHour());
    }

    @Test
    void setBookingDtoStatusTest() {
        BookingDto bookingDto = new BookingDto( 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        bookingDto.setStatus(BookingStatus.UNAVAILABLE);
        assertEquals(BookingStatus.UNAVAILABLE, bookingDto.getStatus());
    }
}