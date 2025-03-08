package edu.eci.cvds.proyect.booking.persistency.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.cvds.proyect.booking.persistency.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {

}
