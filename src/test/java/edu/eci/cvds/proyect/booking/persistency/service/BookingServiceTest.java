package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.exceptions.BookingException;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.BookingRepository;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import edu.eci.cvds.proyect.booking.users.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    private User user;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User("1", "John", "john@example.com", UserRole.ADMIN, "pass");
        booking = new Booking("B1");
        booking.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        booking.setDay(Day.LUNES);
        booking.setStartHour(Hour.OCHO_TREINTA);
        booking.setEndHour(Hour.DIEZ);
        booking.setPriority(3);
        booking.setAvailable(true);
        booking.setOwnerIds(List.of(user.getId()));
    }

    @Test
    void testGetAllBookings() throws AppException {
        when(bookingRepository.findByOwnerIdsContaining("1")).thenReturn(List.of(booking));

        List<Booking> result = bookingService.getAllBookings(user);

        assertFalse(result.isEmpty());
        assertEquals("B1", result.get(0).getId());
    }

    @Test
    void testGetAllBookingsEmpty() throws AppException {
        when(bookingRepository.findByOwnerIdsContaining("1")).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.getAllBookings(user);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBookingById() throws AppException {
        when(bookingRepository.findFirstByOwnerIdsContainingAndId("1", "B1")).thenReturn(booking);

        Booking result = bookingService.getBookingById("B1", user);

        assertEquals("B1", result.getId());
    }

    @Test
    void testGetBookingByIdThrowsException() {
        when(bookingRepository.findFirstByOwnerIdsContainingAndId("1", "B2")).thenReturn(null);

        assertThrows(BookingException.BookingNotFoundException.class,
                () -> bookingService.getBookingById("B2", user));
    }

    @Test
    void testCreateBooking() throws AppException {
        Booking testBooking = new Booking(null);
        testBooking.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        testBooking.setDay(Day.LUNES);
        testBooking.setStartHour(Hour.OCHO_TREINTA);
        testBooking.setEndHour(Hour.DIEZ);
        testBooking.setPriority(3);
        testBooking.setAvailable(false); // Intenta establecer disponible en false

        when(bookingRepository.insert(any(Booking.class))).thenAnswer(invocation -> {
            Booking saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID().toString());
            return saved;
        });

        Booking result = bookingService.createBooking(testBooking, user);

        assertNotNull(result.getId());
        assertTrue(UUID.fromString(result.getId()).toString().equals(result.getId())); // Verifica UUID válido
        assertEquals(LaboratoryName.FUNDAMENTOS, result.getLaboratoryName());
        assertEquals(Day.LUNES, result.getDay());
        assertEquals(Hour.OCHO_TREINTA, result.getStartHour());
        assertEquals(Hour.DIEZ, result.getEndHour());
        assertEquals(3, result.getPriority());
        assertTrue(result.getAvailable()); // Debe sobrescribir a true
        assertEquals(List.of(user.getId()), result.getOwnerIds());

        verify(bookingRepository, times(1)).insert(any(Booking.class));
    }

    @Test
    void testCreateBookingWithNullPriority() throws AppException {
        Booking testBooking = new Booking(null);
        testBooking.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        testBooking.setDay(Day.LUNES);
        testBooking.setStartHour(Hour.OCHO_TREINTA);
        testBooking.setEndHour(Hour.DIEZ);
        testBooking.setPriority(null); // Forzar prioridad nula

        when(bookingRepository.insert(any(Booking.class))).thenAnswer(invocation -> {
            Booking saved = invocation.getArgument(0);
            return saved;
        });

        Booking result = bookingService.createBooking(testBooking, user);

        assertEquals(3, result.getPriority()); // Prioridad por defecto asignada
        verify(bookingRepository, times(1)).insert(any(Booking.class));
    }
    @Test
    void testUpdateBooking() throws AppException {
        Booking existing = new Booking("B1");
        existing.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        existing.setDay(Day.LUNES);
        existing.setStartHour(Hour.OCHO_TREINTA);
        existing.setEndHour(Hour.DIEZ);
        existing.setPriority(3);
        existing.setAvailable(true);
        existing.setOwnerIds(List.of(user.getId()));

        when(bookingRepository.findById("B1")).thenReturn(Optional.of(existing));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking updateData = new Booking(null);
        updateData.setDay(Day.MARTES);
        updateData.setPriority(4);

        Booking result = bookingService.updateBooking("B1", updateData, user);

        assertEquals(Day.MARTES, result.getDay());
        assertEquals(4, result.getPriority());
        assertEquals(LaboratoryName.FUNDAMENTOS, result.getLaboratoryName()); // Campo no modificado
        assertEquals(Hour.OCHO_TREINTA, result.getStartHour()); // Campo no modificado
    }

    @Test
    void testUpdateBookingThrowsException() {
        when(bookingRepository.findById("B2")).thenReturn(Optional.empty());

        assertThrows(BookingException.BookingNotFoundException.class,
                () -> bookingService.updateBooking("B2", booking, user));
    }

    @Test
    void testDeleteBooking() throws AppException {
        when(bookingRepository.findFirstByOwnerIdsContainingAndId("1", "B1")).thenReturn(booking);

        Booking result = bookingService.deleteBooking("B1", user);

        assertEquals("B1", result.getId());
        verify(bookingRepository, times(1)).delete(booking);
    }

    @Test
    void testDeleteAllBookings() throws AppException {
        when(bookingRepository.findByOwnerIdsContaining("1")).thenReturn(List.of(booking));

        List<Booking> result = bookingService.deleteAllBookings(user);

        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).deleteById("B1");
    }

    @Test
    void testGenerateExamples() throws AppException {
        when(bookingRepository.insert(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Booking> examples = bookingService.generateExamples(user);

        assertFalse(examples.isEmpty());
        examples.forEach(example -> {
            assertNotNull(example.getId());
            assertTrue(
                    example.getStartHour().ordinal() < example.getEndHour().ordinal(),
                    "Hora inválida: " + example.getStartHour() + " - " + example.getEndHour()
            );
            assertTrue(example.getPriority() >= 1 && example.getPriority() <= 5);
        });

        verify(bookingRepository, times(1)).insert(anyList());
    }

    @Test
    void testIsValidBooking() {
        Booking valid = new Booking("B1");
        valid.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        valid.setDay(Day.LUNES);
        valid.setStartHour(Hour.OCHO_TREINTA);
        valid.setEndHour(Hour.DIEZ);
        valid.setPriority(3);

        assertDoesNotThrow(() -> bookingService.isValidBooking(valid));

        // Caso con hora de inicio igual a fin
        Booking invalidHours = new Booking("B2");
        invalidHours.setLaboratoryName(LaboratoryName.FUNDAMENTOS);
        invalidHours.setStartHour(Hour.DIEZ);
        invalidHours.setEndHour(Hour.DIEZ);

        assertThrows(BookingException.BookingInvalidValueException.class,
                () -> bookingService.isValidBooking(invalidHours));
    }
    @Test
    void getRandomDateTime_GeneratesDateWithinRange() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        int daysOfRange = 5;

        IntStream.range(0, 1000).forEach(i -> {
            LocalDateTime randomDateTime = bookingService.getRandomDateTime(startDate, daysOfRange);
            LocalDate randomDate = randomDateTime.toLocalDate();

            // Límite máximo ajustado (startDate + daysOfRange inclusive)
            LocalDate fechaMaxima = startDate.plusDays(daysOfRange);

            assertTrue(
                    !randomDate.isBefore(startDate) &&
                            !randomDate.isAfter(fechaMaxima),
                    "Fecha fuera de rango: " + randomDate
            );
        });
    }

    @Test
    void getRandomDateTime_GeneratesValidTime() {
        LocalDate startDate = LocalDate.now();
        int daysOfRange = 7;

        IntStream.range(0, 1000).forEach(i -> {
            LocalDateTime randomDateTime = bookingService.getRandomDateTime(startDate, daysOfRange);
            LocalTime randomTime = randomDateTime.toLocalTime();

            // Convertir cada hora del enum a LocalTime para comparar
            boolean isValid = Arrays.stream(Hour.values())
                    .anyMatch(hour -> {
                        // Formatear la hora del enum (ej: "8:30" -> "08:30")
                        String[] partes = hour.getHour().split(":");
                        String horaFormateada = String.format("%02d:%02d", Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
                        return LocalTime.parse(horaFormateada).equals(randomTime);
                    });

            assertTrue(isValid, "Hora inválida: " + randomTime);
        });
    }

    @Test
    void getRandomDateTime_WhenDaysOfRangeIsZero() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        int daysOfRange = 0;
        LocalDateTime randomDateTime = bookingService.getRandomDateTime(startDate, daysOfRange);
        assertEquals(startDate, randomDateTime.toLocalDate(), "Debe ser la misma fecha inicial");
    }

    // --- Tests para isValidDay ---
    @Test
    void isValidDay_CuandoDiaEsValido() {
        LocalDate fecha = LocalDate.of(2023, 10, 2); // Lunes
        Day[] validos = { Day.LUNES, Day.MIERCOLES };
        assertTrue(bookingService.isValidDay(fecha, validos), "Lunes debería ser válido");
    }

    @Test
    void isValidDay_CuandoDiaEsInvalido() {
        LocalDate fecha = LocalDate.of(2023, 10, 3); // Martes
        Day[] validos = { Day.LUNES, Day.MIERCOLES };
        assertFalse(bookingService.isValidDay(fecha, validos), "Martes no es válido");
    }

    @Test
    void isValidDay_CuandoNoHayDiasValidos() {
        LocalDate fecha = LocalDate.now();
        Day[] validos = {};
        assertFalse(bookingService.isValidDay(fecha, validos), "Sin días válidos debe ser falso");
    }
}


