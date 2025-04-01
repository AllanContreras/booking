package edu.eci.cvds.proyect.booking.persistency.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.persistency.dto.BookingDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.repository.BookingRepository;

@Service
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private static final String BOOKING_ID_NULL = "El ID de la reserva no puede ser null";
    private static final String BOOKING_ID_NOT_FOUND = "Reserva no encontrada con ID: ";
    private final Random random = new Random();
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
        if (bookingDto == null) {
            throw new RuntimeException("La reserva no puede ser nula");
        }
        Integer id = autoIncrement();
        Booking booking = new Booking(id, bookingDto.getUserId(), bookingDto.getLaboratoryName(), bookingDto.getDay(),bookingDto.getStartHour(),bookingDto.getEndHour(),bookingDto.getStatus(),bookingDto.getPriority());
        return bookingRepository.save(booking);
    }

    public Booking update(Integer id, BookingDto bookingDto){
        if (id == null) {
            throw new IllegalArgumentException(BOOKING_ID_NULL);
        }
        
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException(BOOKING_ID_NOT_FOUND + id));
        booking.setUserId(bookingDto.getUserId());
        booking.setLaboratoryName(bookingDto.getLaboratoryName());
        booking.setDay(bookingDto.getDay());
        booking.setStartHour(bookingDto.getStartHour());
        booking.setEndHour(bookingDto.getEndHour());
        booking.setStatus(bookingDto.getStatus());
        booking.setPriority(bookingDto.getPriority());
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
    public List<Booking> generateRandomBookings() {
        int count = random.nextInt(901) + 100; // Entre 100 y 1000
        List<Booking> bookings = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Integer id = autoIncrement();
            Integer userId = random.nextInt(1000) + 1;
            LaboratoryName laboratory = LaboratoryName.values()[random.nextInt(LaboratoryName.values().length)];
            Day day = Day.values()[random.nextInt(Day.values().length)];
            Hour startHour = Hour.values()[random.nextInt(Hour.values().length)];
            Hour endHour = Hour.values()[Math.min(startHour.ordinal() + 1, Hour.values().length - 1)];
            BookingStatus status = BookingStatus.values()[random.nextInt(BookingStatus.values().length)];
            Integer priority = random.nextInt(5) + 1;

            Booking booking = new Booking(id, userId, laboratory, day, startHour, endHour, status, priority);
            bookings.add(booking);
        }

        return bookingRepository.saveAll(bookings);
    }
    
}
