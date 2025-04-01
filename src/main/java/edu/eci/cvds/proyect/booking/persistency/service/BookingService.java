package edu.eci.cvds.proyect.booking.persistency.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.exceptions.BookingException;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;
import edu.eci.cvds.proyect.booking.shedules.Hour;
import edu.eci.cvds.proyect.booking.persistency.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.persistency.dto.BookingDto;
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
        isValidBooking(booking);

        booking.setId(UUID.randomUUID().toString());
        booking.setLaboratoryName(booking.getLaboratoryName());
        booking.setStartHour(booking.getStartHour());
        booking.setEndHour(booking.getEndHour());
        booking.setOwnerIds(Collections.singletonList(user.getId()));

        return bookingRepository.insert(booking);
    }

    @Override
    public Booking updateBooking(String id, Booking booking, User user) throws AppException {

        Optional<Booking> existingBooking = bookingRepository.findById(id);

        if (existingBooking.isPresent()) {
            Booking bookingToUpdate = existingBooking.get();

            bookingToUpdate.setName(booking.getLaboratoryName() == null ? bookingToUpdate.getName() : booking.getName());
            bookingToUpdate.setDescription(booking.getDescription() == null ? bookingToUpdate.getDescription() : booking.getDescription());
            bookingToUpdate.setDeadline(booking.getDeadline() == null ? bookingToUpdate.getDeadline() : booking.getDeadline());
            bookingToUpdate.setPriority(booking.getPriority() == 0 ? bookingToUpdate.getPriority() : booking.getPriority());
            bookingToUpdate.setDifficulty(booking.getDifficulty() == null ? bookingToUpdate.getDifficulty() : booking.getDifficulty());
            bookingToUpdate.setDone(booking.isDone());

            bookingToUpdate.setUpdatedAt(LocalDateTime.now());
            this.isValidBooking(bookingToUpdate);

            this.bookingRepository.save(bookingToUpdate);

            return bookingToUpdate;
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

        for (int i = 0; i < numberOfBookings; i++) {
            Booking booking = new Booking();

            booking.setId(UUID.randomUUID().toString());

            booking.setName("Task: " + (i + 1));
            booking.setDescription("Description for Task " + (i + 1));
            booking.setPriority(random.nextInt(5) + 1);
            booking.setDifficulty(String.valueOf(Difficulty.values()[random.nextInt(Difficulty.values().length)]));
            booking.setDone(random.nextBoolean());

            booking.setDeadline(this.getRandomDateTime(LocalDate.now().plusDays(-5), LocalTime.now(), 25));

            final LocalDateTime randomDateTime = this.getRandomDateTime(LocalDate.now(), LocalTime.now(), 30);
            booking.setCreatedAt(randomDateTime);
            booking.setUpdatedAt(randomDateTime);

            booking.setOwnerIds(Collections.singletonList(user.getId()));

            this.isValidTask(booking);

            bookings.add(booking);
        }
        this.bookingRepository.insert(bookings);
        return bookings;
    }

    public LocalDateTime getRandomDateTime(final LocalDate startDate, final LocalTime startTime, final int daysOfRange) {
        long minDay = LocalDateTime.of(startDate, startTime).toEpochSecond(ZoneOffset.UTC);
        long maxDay = LocalDateTime.of(LocalDate.now().plusDays(daysOfRange), startTime).toEpochSecond(ZoneOffset.UTC);

        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.UTC);
    }

    public List<Booking> deleteAllBookings(User user) throws AppException {
        List<Booking> bookingsDeleted = getAllBookings(user);
        bookingsDeleted.forEach(task -> bookingRepository.deleteByIdAndOwnerIdsContaining(user.getId(), booking.getId()));

        return bookingsDeleted;
    }

    public void isValidBooking(Booking booking) throws AppException {
        if (booking.getLaboratoryName() == null) {
            throw new BookingException.BookingInvalidValueException("Booking laboratory name is required");
        }
        if (booking.getPriority() < 0 || 5 < booking.getPriority()) {
            throw new BookingException.BookingInvalidValueException("Booking priority invalid value, out of range [0, 1, 2, 3, 4, 5]");
        }

        if (booking.getDifficulty() != null) {

            try {
                Difficulty.valueOf(booking.getDifficulty().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BookingException.BookingInvalidValueException("Booking difficulty is invalid");
            }
        }
        if (booking.getUpdatedAt() != null && booking.getCreatedAt() != null && booking.getUpdatedAt().isBefore(booking.getCreatedAt())) {
            throw new BookingException.BookingInvalidValueException("Task updated at is before created at!");
        }
    }
    
}
