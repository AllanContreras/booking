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
    void testFindOneUserSuccess() throws Exception {
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
    
        Mockito.when(bookingService.getOne(1)).thenReturn(booking);
    
        mockMvc.perform(get("/Booking/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10000087))
                .andExpect(jsonPath("$.laboratoryName").value("FUNDAMENTOS"))
                .andExpect(jsonPath("$.day").value("LUNES"))
                .andExpect(jsonPath("$.startHour").value("ONCE_TREINTA"))
                .andExpect(jsonPath("$.endHour").value("UNA"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                ;
    }
    @Test
    void testFindOneBookingFailure() throws Exception {
        Integer invalidBookingId = 99;
    
        Mockito.when(bookingService.getOne(invalidBookingId))
                .thenThrow(new RuntimeException("Booking not found"));
    
        mockMvc.perform(get("/Booking/" + invalidBookingId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al obtener la reserva con ID " + invalidBookingId))
                .andExpect(jsonPath("$.details").value("Booking not found"));
    }

    @Test
    void testFindAllBookingsSuccess() throws Exception {
        Booking booking1 = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking2 = new Booking(2, 10000097, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.getAll()).thenReturn(Arrays.asList(booking1, booking2));

        mockMvc.perform(get("/Booking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].userId").value(10000087))
                .andExpect(jsonPath("$[1].userId").value(10000097));
    }

    @Test
    void testFindAllBookingsFailure() throws Exception {
        Mockito.when(bookingService.getAll()).thenThrow(new RuntimeException("Database connection failure"));

        mockMvc.perform(get("/Booking"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("[]"));  
    }

    

    @Test
    void testUpdateBookingSuccess() throws Exception {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.update(Mockito.eq(1), Mockito.any(BookingDto.class))).thenReturn(booking);

        mockMvc.perform(put("/Booking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(10000087));
    }
    @Test
    void testUpdateBookingFailure() throws Exception {
        BookingDto bookingDto = new BookingDto(10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);

        Mockito.when(bookingService.update(Mockito.eq(1), Mockito.any(BookingDto.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/Booking/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al actualizar la reserva"));
    }


    @Test
    void testDeleteBookingSuccess() throws Exception {
        Integer bookingId = 1;
        Booking booking = new Booking(1, 10000087, LaboratoryName.FUNDAMENTOS, Day.LUNES, Hour.ONCE_TREINTA, Hour.UNA, BookingStatus.AVAILABLE);
        Mockito.when(bookingService.delete(bookingId)).thenReturn(booking);

        mockMvc.perform(delete("/Booking/" + bookingId))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.message").value("Reserva eliminada correctamente")); 

        Mockito.verify(bookingService, Mockito.times(1)).delete(bookingId);
    }

    @Test
    void testDeleteFailBookingfailure() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete error")).when(bookingService).delete(1);

        mockMvc.perform(delete("/Booking/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar la reserva"));
    }

}
