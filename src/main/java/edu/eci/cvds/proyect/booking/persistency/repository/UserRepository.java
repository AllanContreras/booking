package edu.eci.cvds.proyect.booking.persistency.repository;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.cvds.proyect.booking.persistency.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
