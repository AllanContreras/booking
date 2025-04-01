package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.exceptions.SessionException;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.service.AuthorizationService;
import edu.eci.cvds.proyect.booking.persistency.service.BookingService;
import edu.eci.cvds.proyect.booking.persistency.service.SessionService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Obtener todas las reservas.
     *
     * @return Lista de todas las reservas.
     */
    @GetMapping
    public ResponseEntity<?> getAllbookings(@RequestHeader("Authorization") String sessionToken) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            List<Booking> bookings = bookingService.getAllBookings(userLogged);
            bookings.forEach(elem -> elem.setOwnerIds(null));
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            } else {
                return ResponseEntity.status(500).body(Collections.singletonMap("error ", "Server error "));
            }
        }
    }

    /**
     * Obtener una reserva por su ID.
     *
     * @param id Identificador de la reserva.
     * @return La reserva correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getbookingById(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            Booking booking = bookingService.getBookingById(id, userLogged);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error ", "Server error "));
        }
    }

    /**
     * Crear una nueva reserva.
     *
     * @param booking Modelo de reserva enviado en el cuerpo de la solicitud.
     * @return El UUID de la nueva reserva creada.
     */
    @PostMapping
    public ResponseEntity<?> createbooking(@RequestHeader("Authorization") String sessionToken, @RequestBody Booking booking) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            Booking Booking = bookingService.createBooking(booking, userLogged);
            Booking.setOwnerIds(null);
            return ResponseEntity.status(201).body(Booking);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error  ", "Server error  "));
        }
    }

    /**
     * Actualizar una reserva existente por su ID.
     *
     * @param id   Identificador de la reserva a actualizar.
     * @param booking Modelo de reserva actualizado.
     * @return La reserva actualizada.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatebooking(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id, @RequestBody Booking booking) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            Booking updatedbooking = bookingService.updateBooking(id, booking, userLogged);
            updatedbooking.setOwnerIds(null);
            return ResponseEntity.status(200).body(updatedbooking);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Eliminar una reserva por su ID.
     *
     * @param id Identificador de la reserva a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletebooking(@RequestHeader("Authorization") String sessionToken, @PathVariable("id") String id) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            Booking deletedbooking = bookingService.deleteBooking(id, userLogged);
            deletedbooking.setOwnerIds(null);
            return ResponseEntity.ok(deletedbooking);
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Eliminar todas las reservas.
     *
     * @return Un mensaje indicando cuántas reservas fueron eliminadas exitosamente.
     */
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllbookings(@RequestHeader("Authorization") String sessionToken) {

        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            List<Booking> bookingDeleted = this.bookingService.deleteAllBookings(userLogged);
            return ResponseEntity.status(200).body(bookingDeleted.size() + " bookings were deleted successfully");
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Generar reservas de ejemplo.
     *
     * @return Un mensaje indicando cuántas reservas fueron generadas.
     */
    @PostMapping("/gen")
    public ResponseEntity<?> generatebookings(@RequestHeader("Authorization") String sessionToken) {
        try {
            User userLogged = this.getUserFromSessions(sessionToken);
            authorizationService.adminResource(sessionToken);
            List<Booking> newbookings = bookingService.generateExamples(userLogged);
            return ResponseEntity.status(201).body(newbookings.size() + " bookings were generated");
        } catch (Exception e) {
            if (e instanceof AppException) {
                return ((AppException) e).getResponse();
            }
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Server error"));
        }
    }

    /**
     * Endpoint for verify service state
     *
     * @return message
     */
    @GetMapping("/health")
    public ResponseEntity<?> checkHealth() {
        HashMap<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "The server is up");
        return ResponseEntity.ok(response);
    }

    private User getUserFromSessions(String sessionToken) throws SessionException {
        if (sessionToken == null) {
            throw new SessionException.SessionNotFoundException(null);
        }
        if (!this.sessionService.isSessionActive(sessionToken)) {
            throw new SessionException.InvalidSessionException(sessionToken);
        }
        return this.sessionService.getUserFromSession(sessionToken);
    }
}
