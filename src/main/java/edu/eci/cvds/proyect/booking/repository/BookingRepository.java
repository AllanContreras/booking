package edu.eci.cvds.proyect.booking.repository;

import edu.eci.cvds.proyect.booking.documents.Bookings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Bookings, Integer> {

}
