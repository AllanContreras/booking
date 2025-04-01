package edu.eci.cvds.proyect.booking.persistency.service;

import java.util.List;

import edu.eci.cvds.proyect.booking.exceptions.AppException;
import edu.eci.cvds.proyect.booking.persistency.entity.Booking;
import edu.eci.cvds.proyect.booking.persistency.entity.User;

/**
 * Interface for booking management services.
 * Provides methods for creating, reading, updating, and deleting bookings.
 */
public interface BookingsService {

    /**
     * Get all bookings.
     *
     * @return List of all available bookings.
     * @throws AppException if an error occurs while retrieving the bookings.
     */
    List<Booking> getAllBookings(User user) throws AppException;

    /**
     * Get a booking by its ID.
     *
     * @param id Identifier of the booking.
     * @return The booking corresponding to the provided ID.
     * @throws AppException if an error occurs while retrieving the booking.
     */
    Booking getBookingById(String id, User user) throws AppException;

    /**
     * Create a new booking.
     *
     * @param booking Booking model to create.
     * @return The created booking.
     * @throws AppException if an error occurs while creating the booking.
     */
    Booking createBooking(Booking booking, User user) throws AppException;

    /**
     * Update an existing booking by its ID.
     *
     * @param id   Identifier of the booking to update.
     * @param booking Updated bookings model.
     * @return The updated booking.
     * @throws AppException if an error occurs while updating the booking.
     */
    Booking updateBooking(String id, Booking booking, User user) throws AppException;

    /**
     * Delete a booking by its ID.
     *
     * @param id Identifier of the booking to delete.
     * @return The deleted booking.
     * @throws AppException if an error occurs while deleting the booking.
     */
    Booking deleteBooking(String id, User user) throws AppException;

    /**
     * Generate examples of bookings.
     *
     * @return List of generated example bookings.
     * @throws AppException if an error occurs while generating the bookings.
     */
    List<Booking> generateExamples(User user) throws AppException;

    /**
     * Delete all bookings.
     *
     * @return List of all deleted bookings.
     * @throws AppException if an error occurs while deleting the bookings.
     */
    List<Booking> deleteAllBookings(User user) throws AppException;

    /**
     * Validate a booking before insert in database.
     *
     * @param booking Booking model to validate.
     * @throws AppException if the booking is not valid.
     */
    void isValidBooking(Booking booking) throws AppException;
}