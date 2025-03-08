package edu.eci.cvds.proyect.booking.persistency.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.cvds.proyect.booking.persistency.entity.Booking;

public interface BookingRepository extends MongoRepository<Booking, Integer> {

}
