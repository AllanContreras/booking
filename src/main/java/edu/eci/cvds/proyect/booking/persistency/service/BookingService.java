package edu.eci.cvds.proyect.booking.persistency.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.exceptions.BookingException;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import edu.eci.cvds.proyect.booking.persistency.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.repository.BookingRepository;

@Service
public class BookingService implements BookingsService{
    
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getAllBookings(User user) throws AppException {
        return bookingRepository.findByOwnerIdsContaining(user.getId());
    }

    @Override
    public Booking getBookingById(String id, User user) throws AppException {
        Booking result = bookingRepository.findFirstByOwnerIdsContainingAndId(user.getId(), id);

        if (result != null) {
            return result;
        }

        throw new BookingException.BookingNotFoundException(id);
    }

    @Override
    public Booking createBooking(Booking booking, User user) throws AppException {
        // Crear una nueva instancia con un ID único
        Booking newBooking = new Booking(UUID.randomUUID().toString());

        // Copiar los atributos necesarios
        newBooking.setLaboratoryName(booking.getLaboratoryName());
        newBooking.setDay(booking.getDay());
        newBooking.setStartHour(booking.getStartHour());
        newBooking.setEndHour(booking.getEndHour());
        newBooking.setAvailable(true);

        // Asignar prioridad por defecto si es necesario
        newBooking.setPriority(booking.getPriority() != null ? booking.getPriority() : 3); // <-- Prioridad asignada aquí

        // Validar la nueva reserva (no la de entrada)
        isValidBooking(newBooking); // <-- Validación después de asignar valores

        // Inicializar ownerIds con el usuario actual
        newBooking.setOwnerIds(Collections.singletonList(user.getId()));

        // Guardar en la base de datos
        return bookingRepository.insert(newBooking);
    }


    @Override
    public Booking updateBooking(String id, Booking booking, User user) throws AppException {
        Optional<Booking> existingBookingOpt = bookingRepository.findById(id);

        if (existingBookingOpt.isPresent()) {
            Booking existingBooking = existingBookingOpt.get();

            // Actualizar solo los atributos que no sean nulos
            if (booking.getLaboratoryName() != null) {
                existingBooking.setLaboratoryName(booking.getLaboratoryName());
            }
            if (booking.getDay() != null) {
                existingBooking.setDay(booking.getDay());
            }
            if (booking.getStartHour() != null) {
                existingBooking.setStartHour(booking.getStartHour());
            }
            if (booking.getEndHour() != null) {
                existingBooking.setEndHour(booking.getEndHour());
            }
            if (booking.getAvailable() != null) {
                existingBooking.setAvailable(booking.getAvailable());
            }
            if (booking.getPriority() != null) { // Evita problemas con 0
                existingBooking.setPriority(booking.getPriority());
            }

            // Validar la reserva antes de guardarla
            isValidBooking(existingBooking);

            // Guardar la reserva actualizada en la base de datos
            return bookingRepository.save(existingBooking);
        }

        throw new BookingException.BookingNotFoundException(id);
    }


    @Override
    public Booking deleteBooking(String id, User user) throws AppException {
        Booking bookingToDelete = bookingRepository.findFirstByOwnerIdsContainingAndId(user.getId(), id);

        if (bookingToDelete != null) {
            bookingRepository.delete(bookingToDelete);
            return bookingToDelete;
        }

        throw new BookingException.BookingNotFoundException(id);
    }

    @Override
    public List<Booking> generateExamples(User user) throws AppException {
        Random random = new Random();
        int numberOfBookings = random.nextInt(901) + 100;
        List<Booking> bookings = new ArrayList<>();
        Hour[] hours = Hour.values();

        for (int i = 0; i < numberOfBookings; i++) {
            Booking booking = new Booking(UUID.randomUUID().toString());

            // Asegurar que startHour tenga espacio para endHour
            int startIndex = random.nextInt(hours.length - 1); // No puede ser la última hora
            int endIndex = startIndex + 1 + random.nextInt(hours.length - startIndex - 1);

            booking.setLaboratoryName(LaboratoryName.values()[random.nextInt(LaboratoryName.values().length)]);
            booking.setDay(Day.values()[random.nextInt(Day.values().length)]);
            booking.setStartHour(hours[startIndex]);
            booking.setEndHour(hours[endIndex]);
            booking.setPriority(random.nextInt(5) + 1);
            booking.setAvailable(random.nextBoolean());
            booking.setOwnerIds(Collections.singletonList(user.getId()));

            isValidBooking(booking);
            bookings.add(booking);
        }

        bookingRepository.insert(bookings);
        return bookings;
    }



    public LocalDateTime getRandomDateTime(final LocalDate startDate, final int daysOfRange) {
        Day[] validDays = Day.values();
        LocalDate randomDate;

        do {
            int randomDays = ThreadLocalRandom.current().nextInt(0, daysOfRange + 1);
            randomDate = startDate.plusDays(randomDays);
        } while (!isValidDay(randomDate, validDays));

        // Asegurar que el índice no se salga del array
        Hour[] hours = Hour.values();
        int startIndex = ThreadLocalRandom.current().nextInt(hours.length - 3); // -3 para permitir hasta 3 horas de rango
        int endIndex = startIndex + 1 + ThreadLocalRandom.current().nextInt(3); // 1 a 3 horas de diferencia

        // Convertir la hora del enum a LocalTime correctamente
        String[] partes = hours[startIndex].getHour().split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);

        LocalTime randomTime = LocalTime.of(horas, minutos);

        return LocalDateTime.of(randomDate, randomTime);
    }

    public List<Booking> deleteAllBookings(User user) throws AppException {
        List<Booking> bookingsDeleted = getAllBookings(user);

        // Se eliminan los bookings asegurando que el usuario sea propietario
        bookingsDeleted.forEach(booking -> bookingRepository.deleteById(booking.getId()));

        return bookingsDeleted;
    }


    public void isValidBooking(Booking booking) throws AppException {
        if (booking.getLaboratoryName() == null) {
            throw new BookingException.BookingInvalidValueException("Booking laboratory name is required");
        }

        if (booking.getPriority() == null || booking.getPriority() < 1 || booking.getPriority() > 5) {
            throw new BookingException.BookingInvalidValueException("Booking priority invalid value, must be in range [1, 2, 3, 4, 5]");
        }

        if (booking.getStartHour() == null || booking.getEndHour() == null) {
            throw new BookingException.BookingInvalidValueException("Booking start and end hours are required");
        }

        if (booking.getStartHour().ordinal() >= booking.getEndHour().ordinal()) {
            throw new BookingException.BookingInvalidValueException("Booking start hour must be before end hour");
        }
    }

    public boolean isValidDay(LocalDate date, Day[] validDays) {
        Day dayOfWeek = Day.fromDayOfWeek(date.getDayOfWeek()); // <-- Método clave
        for (Day validDay : validDays) {
            if (dayOfWeek == validDay) {
                return true;
            }
        }
        return false;
    }
}