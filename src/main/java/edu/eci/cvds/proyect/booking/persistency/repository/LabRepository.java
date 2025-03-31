package edu.eci.cvds.proyect.booking.persistency.repository;

import edu.eci.cvds.proyect.booking.persistency.entity.Laboratory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends MongoRepository<Laboratory, Integer> {
}