package edu.eci.cvds.proyect.booking.exceptions;

public class BookingException  extends AppException{
    
    public BookingException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    /**
     * BookingNotFoundException is thrown when a task cannot be found in the database.
     */
    public static class BookingNotFoundException extends BookingException {

        /**
         * Constructor for BookingNotFoundException.
         * @param Booking The Booking that was not found.
         */
        public BookingNotFoundException(String task) {
            super("Booking: " + task + ", not found in the database.", 404);
        }
    }

    /**
     * BookingInvalidValueException is thrown when an invalid value is encountered.
     */
    public static class BookingInvalidValueException extends BookingException {

        /**
         * Constructor for BookingInvalidValueException.
         * @param value The invalid value encountered.
         */
        public BookingInvalidValueException(String value) {
            super("Invalid value for: " + value, 400);
        }
    }

    /**
     * BookingConflictException is thrown when there is a conflict, such as a duplicate Booking.
     */
    public static class BookingConflictException extends BookingException {

        /**
         * Constructor for BookingConflictException.
         * @param booking The Booking that caused the conflict.
         */
        public BookingConflictException(String booking) {
            super("Booking: " + booking + ", already exists in the database.", 409);
        }
    }
}
