package edu.eci.cvds.proyect.booking.persistency.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.cvds.proyect.booking.persistency.entity.Booking;

@Repository
public interface BookingRepository extends MongoRepository<Booking, Integer> {

}
