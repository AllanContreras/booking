package edu.eci.cvds.proyect.booking.controller;

import edu.eci.cvds.proyect.booking.documents.Bookings;
import edu.eci.cvds.proyect.booking.repository.BookingRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<?> saveBooking(@RequestBody Bookings booking) {
        try {
            Bookings bookingSave = bookingRepository.save(booking);
            return new ResponseEntity<>(bookingSave, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
            return new ResponseEntity<>("Error al guardar la reserva: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> findAllBookings() {
        try {
            List<Bookings> bookings = bookingRepository.findAll();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
            return new ResponseEntity<>("Error al obtener las reservas: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBooking(@RequestBody Bookings booking) {
        try {
            if (!bookingRepository.existsById(booking.getId())) {
                return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
            }
            Bookings bookingSave = bookingRepository.save(booking);
            return new ResponseEntity<>(bookingSave, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
            return new ResponseEntity<>("Error al actualizar la reserva: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id) {
        try {
            if (!bookingRepository.existsById(id)) {
                return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
            }
            bookingRepository.deleteById(id);
            return new ResponseEntity<>("Reserva eliminada", HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = (e.getCause() != null) ? e.getCause().toString() : e.getMessage();
            return new ResponseEntity<>("Error al eliminar la reserva: " + errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
