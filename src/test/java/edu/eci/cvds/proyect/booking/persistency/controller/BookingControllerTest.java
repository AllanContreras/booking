package edu.eci.cvds.proyect.booking.persistency.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.persistency.dto.BookingDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.service.BookingService;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSaveBookingSuccess() throws Exception {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.save(Mockito.any(BookingDto.class))).thenReturn(booking);

        mockMvc.perform(post("/Booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(10000087));
    }

    @Test
    void testSaveBookingFailure() throws Exception {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.save(Mockito.any(BookingDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/Booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al guardar la reserva"));
    }

    @Test
    void testFindOneBookingSuccess() throws Exception {
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.getOne(1)).thenReturn(booking);

        mockMvc.perform(get("/Booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10000087))
                .andExpect(jsonPath("$.laboratoryName").value("FUNDAMENTOS"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    void testFindAllBookingsSuccess() throws Exception {
        Booking booking1 = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking2 = new Booking(2, 10000097, LaboratoryName.REDES, Day.MARTES, Hour.DOS_TREINTA, Hour.CUATRO, BookingStatus.UNAVAILABLE);

        Mockito.when(bookingService.getAll()).thenReturn(Arrays.asList(booking1, booking2));

        mockMvc.perform(get("/Booking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testUpdateBookingSuccess() throws Exception {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.REDES, Day.MIERCOLES, Hour.CUATRO, Hour.CINCO_TREINTA, BookingStatus.UNAVAILABLE);
        Booking updatedBooking = new Booking(1, 10000087, LaboratoryName.REDES, Day.MIERCOLES, Hour.CUATRO, Hour.CINCO_TREINTA, BookingStatus.UNAVAILABLE);

        Mockito.when(bookingService.update(Mockito.eq(1), Mockito.any(BookingDto.class))).thenReturn(updatedBooking);

        mockMvc.perform(put("/Booking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNAVAILABLE"));

    }

    @Test
    void testDeleteBookingSuccess() throws Exception {
        Integer bookingId = 1;

        Mockito.when(bookingService.delete(Mockito.eq(bookingId))).thenReturn(null); // O devolver un objeto si necesario

        mockMvc.perform(delete("/Booking/" + bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reserva eliminada correctamente"));

        Mockito.verify(bookingService, Mockito.times(1)).delete(bookingId);
    }




    @Test
    void testDeleteBookingFailure() throws Exception {
        Integer bookingId = 99;

        Mockito.doThrow(new RuntimeException("Booking not found")).when(bookingService).delete(bookingId);

        mockMvc.perform(delete("/Booking/" + bookingId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar la reserva"));
    }
}
