package edu.eci.cvds.proyect.booking.persistency.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.cvds.proyect.booking.exceptions.*;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.List;

class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private SessionService sessionService;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String VALID_TOKEN = "valid-token";
    private static final String INVALID_TOKEN = "invalid-token";
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        testUser = new User("1", "Test User", "test@example.com", null, "password");
    }

    // ------------------------- getAllBookings Tests -------------------------
    @Test
    void getAllBookings_SuccessWithResults() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.getAllBookings(testUser)).thenReturn(List.of(booking));

        mockMvc.perform(get("/Booking").header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].ownerIds").doesNotExist());
    }

    @Test
    void getAllBookings_InvalidSession() throws Exception {
        when(sessionService.isSessionActive(INVALID_TOKEN)).thenReturn(false);

        mockMvc.perform(get("/Booking").header("Authorization", INVALID_TOKEN))
                .andExpect(status().isUnauthorized()) // 401
                .andExpect(jsonPath("$.error").value("Session with ID :" + INVALID_TOKEN + " is invalid or unauthorized."));
    }


    @Test
    void getBookingById_Success() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.getBookingById("123", testUser)).thenReturn(booking);

        mockMvc.perform(get("/Booking/123").header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void getBookingById_NotFound() throws Exception {
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.getBookingById("123", testUser))
                .thenThrow(new BookingException.BookingNotFoundException("123"));

        mockMvc.perform(get("/Booking/123").header("Authorization", VALID_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Booking: 123, not found in the database."));
    }

    // ------------------------- createBooking Tests -------------------------
    @Test
    void createBooking_Success() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.createBooking(any(Booking.class), eq(testUser))).thenReturn(booking);

        mockMvc.perform(post("/Booking")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void createBooking_InvalidInput() throws Exception {
        Booking invalidBooking = new Booking();
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.createBooking(any(Booking.class), eq(testUser)))
                .thenThrow(new BookingException.BookingInvalidValueException("startDate"));

        mockMvc.perform(post("/Booking")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBooking)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid value for: startDate"));
    }

    @Test
    void createBooking_Conflict() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.createBooking(any(Booking.class), eq(testUser)))
                .thenThrow(new BookingException.BookingConflictException("123"));

        mockMvc.perform(post("/Booking")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Booking: 123, already exists in the database."));
    }


    @Test
    void updateBooking_Success() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.updateBooking(eq("123"), any(Booking.class), eq(testUser))).thenReturn(booking);

        mockMvc.perform(patch("/Booking/123")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void updateBooking_Conflict() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.updateBooking(eq("123"), any(Booking.class), eq(testUser)))
                .thenThrow(new BookingException.BookingConflictException("123"));

        mockMvc.perform(patch("/Booking/123")
                        .header("Authorization", VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Booking: 123, already exists in the database."));
    }


    @Test
    void deleteBooking_Success() throws Exception {
        Booking booking = new Booking();
        booking.setId("123");
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.deleteBooking("123", testUser)).thenReturn(booking);

        mockMvc.perform(delete("/Booking/123").header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void deleteBooking_Conflict() throws Exception {
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.deleteBooking("123", testUser))
                .thenThrow(new BookingException.BookingConflictException("123"));

        mockMvc.perform(delete("/Booking/123").header("Authorization", VALID_TOKEN))
                .andExpect(status().isConflict());
    }


    @Test
    void deleteAllBookings_Success() throws Exception {
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.deleteAllBookings(testUser)).thenReturn(List.of(new Booking(), new Booking()));

        mockMvc.perform(delete("/Booking/all").header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().string("2 bookings were deleted successfully"));
    }

    @Test
    void deleteAllBookings_Conflict() throws Exception {
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        when(bookingService.deleteAllBookings(testUser))
                .thenThrow(new BookingException.BookingConflictException("user-1"));

        mockMvc.perform(delete("/Booking/all").header("Authorization", VALID_TOKEN))
                .andExpect(status().isConflict());
    }


    @Test
    void generateBookings_Success() throws Exception {
        when(sessionService.isSessionActive(VALID_TOKEN)).thenReturn(true);
        when(sessionService.getUserFromSession(VALID_TOKEN)).thenReturn(testUser);
        doNothing().when(authorizationService).adminResource(VALID_TOKEN);
        when(bookingService.generateExamples(testUser)).thenReturn(List.of(new Booking(), new Booking()));

        mockMvc.perform(post("/Booking/gen").header("Authorization", VALID_TOKEN))
                .andExpect(status().isCreated())
                .andExpect(content().string("2 bookings were generated"));
    }


    @Test
    void checkHealth_Success() throws Exception {
        mockMvc.perform(get("/Booking/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.message").value("The server is up"));
    }
}