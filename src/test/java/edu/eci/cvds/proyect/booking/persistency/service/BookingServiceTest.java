package edu.eci.cvds.proyect.booking.persistency.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.persistency.dto.BookingDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.repository.BookingRepository;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBookingsSuccess() {
        Booking booking1 = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking2 = new Booking(2, 10000097, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        List<Booking> bookings = bookingService.getAll();

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(10000087, bookings.get(0).getUserId());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBookingsFailure() {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList());
    
        List<Booking> bookings = bookingService.getAll();
    
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty(),"lista de reservas vacia");
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetOneBookingSuccess() {
        Booking booking1 = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));

        Booking foundBooking = bookingService.getOne(1);

        assertNotNull(foundBooking);
        assertEquals(10000087, foundBooking.getUserId());
        verify(bookingRepository, times(1)).findById(1);
    }

    @Test
    void testGetOneBookingFailure() {
        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.getOne(99);
        });

        assertEquals("Reserva no encontrada con ID: 99", exception.getMessage());
        verify(bookingRepository, times(1)).findById(99);
    }

    @Test
    void testSaveBookingSuccess() {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        Booking savedBooking = bookingService.save(bookingDto);

        assertNotNull(savedBooking);
        assertEquals(10000087, savedBooking.getUserId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    @Test
    void testSaveBookingFailure() {
        BookingDto bookingDto =  null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.save(bookingDto);
        });

        assertEquals("La reserva no puede ser nula", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }


    @Test
    void testUpdateBookingSuccess() {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking updatedBooking = bookingService.update(1, bookingDto);

        assertNotNull(updatedBooking);
        assertEquals(10000087, updatedBooking.getUserId());
        verify(bookingRepository, times(1)).save(booking);
    }
    @Test
    void testUpdateBookingFailure() {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.update(99, bookingDto);
        });

        assertEquals("Reserva no encontrada con ID: 99", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void testDeleteBookingSuccess() {
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        Booking deletedBooking = bookingService.delete(1);

        assertNotNull(deletedBooking);
        assertEquals(1, deletedBooking.getId());
        verify(bookingRepository, times(1)).delete(booking);
    }
    @Test
    void testDeleteBookingFailure() {
        when(bookingRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.delete(99);
        });

        assertEquals("Reserva no encontrada con ID: 99", exception.getMessage());
        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void testAutoIncrementSuccess() {
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));
    
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking newBooking = new Booking(2, 10000097, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
    
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);
    
        Booking savedBooking = bookingService.save(bookingDto);
    
        assertNotNull(savedBooking);
        assertEquals(2, savedBooking.getId()); 
    
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    

    @Test
    void testAutoIncrementFailure() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList()); 
    
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking); 
    
        Integer newId = bookingService.save(new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE)).getId();
    
        assertEquals(1, newId, "El primer ID debería ser 1 cuando la lista está vacía.");
        verify(bookingRepository, times(1)).findAll();
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }
    
}
