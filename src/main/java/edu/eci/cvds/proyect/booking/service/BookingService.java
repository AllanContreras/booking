package edu.eci.cvds.proyect.booking.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.dto.BookingDto;
import edu.eci.cvds.proyect.booking.entity.Booking;
import edu.eci.cvds.proyect.booking.repository.BookingRepository;

@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private static final String BOOKING_ID_NULL = "El ID de la reserva no puede ser null";
    private static final String BOOKING_ID_NOT_FOUND = "Reserva no encontrada con ID: ";

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAll(){
        return bookingRepository.findAll();
    }

    public Booking getOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException(BOOKING_ID_NULL);
        }
        return bookingRepository.findById(id).orElseThrow(() -> new RuntimeException(BOOKING_ID_NOT_FOUND + id));
    }
    

    public Booking save(BookingDto bookingDto){
        Integer id = autoIncrement();
        Booking booking = new Booking(id, bookingDto.getUserId(), bookingDto.getResource(), bookingDto.getStartDate(), bookingDto.getEndDate(),bookingDto.getStatus());
        return bookingRepository.save(booking);
    }

    public Booking update(Integer id, BookingDto bookingDto){
        if (id == null) {
            throw new IllegalArgumentException(BOOKING_ID_NULL);
        }
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException(BOOKING_ID_NOT_FOUND + id));
        booking.setUserId(bookingDto.getUserId());
        booking.setResource(bookingDto.getResource());
        booking.setStartDate(bookingDto.getStartDate());
        booking.setEndDate(bookingDto.getEndDate());
        booking.setStatus(bookingDto.getStatus());
        return bookingRepository.save(booking);
    }

    public Booking delete(Integer id){
        if (id == null) {
            throw new IllegalArgumentException(BOOKING_ID_NULL);
        }
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException(BOOKING_ID_NOT_FOUND + id));
        bookingRepository.delete(booking);
        return booking;
    }

    //private methods
    private Integer autoIncrement() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.isEmpty() ? 1 : bookings.stream()
                .max(Comparator.comparing(Booking::getId))
                .orElseThrow(() -> new RuntimeException("No se pudo determinar el siguiente ID"))
                .getId() + 1;
    }
    
}
