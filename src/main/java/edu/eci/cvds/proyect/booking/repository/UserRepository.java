package edu.eci.cvds.proyect.booking.repository;

import edu.eci.cvds.proyect.booking.documents.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Users, Integer> {

}
