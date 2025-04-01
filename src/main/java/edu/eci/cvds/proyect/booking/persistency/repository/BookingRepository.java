package edu.eci.cvds.proyect.booking.persistency.repository;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.DateOperators.Hour;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.eci.cvds.proyect.booking.persistency.entity.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByOwnerIdsContaining(String ownerId);
    Booking findFirstByOwnerIdsContainingAndId(String ownerId, String id);
    Booking deleteByIdAndOwnerIdsContaining(String ownerId, String id);


    List<Booking> findByOwnerIdsContainingAndAvailableTrue(String ownerId);
    List<Booking> findByOwnerIdsContainingAndAvailableFalse(String ownerId);
    List<Booking> findByOwnerIdsContainingAndPriorityBetween(String ownerId, int minPriority, int maxPriority);
    List<Booking> findByOwnerIdsContainingAndStartHourBefore(String ownerId, Hour startHour);
    List<Booking> findByOwnerIdsContainingAndEndHourAfter(String ownerId, Hour endHour);
}
