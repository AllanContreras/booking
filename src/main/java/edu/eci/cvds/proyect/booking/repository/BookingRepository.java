package edu.eci.cvds.proyect.booking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.cvds.proyect.booking.entity.Booking;

public interface BookingRepository extends MongoRepository<Booking, Integer> {

}
