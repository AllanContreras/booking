package edu.eci.cvds.proyect.booking.persistency.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.cvds.proyect.booking.persistency.dto.UserDto;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.persistency.security.CustomPasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final String USER_ID_NULL = "El ID del usuario no puede ser null";
    private static final String USER_ID_NOT_FOUND = "Usuario no encontrado con ID: ";
    @Autowired
    private CustomPasswordEncoder passwordEncoder;


    public List<User> getAll(){
        return userRepository.findAll();
    }
    @Autowired
    public UserService(UserRepository userRepository, CustomPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public User getOne(Integer id){
        if (id == null) {
            throw new IllegalArgumentException(USER_ID_NULL);
        }
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException(USER_ID_NOT_FOUND + id));
    }

    public User save(UserDto userDto){
        if (userDto == null) {
            throw new RuntimeException("El usuario no puede ser nulo");
        }
        Integer id = autoIncrement();
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword()); // Encripta la contraseña
        User user = new User(id, userDto.getName(), userDto.getEmail(), userDto.getRole(), encryptedPassword);
        return userRepository.save(user);
    }
    public User save(UserDto userDto, String requesterRole) {
        

        Integer id = autoIncrement();
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(id, userDto.getName(), userDto.getEmail(), userDto.getRole(), encryptedPassword);
        return userRepository.save(user);
    }

    public User update(Integer id, UserDto userDto){
        if (id == null) {
            throw new IllegalArgumentException(USER_ID_NULL);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(USER_ID_NOT_FOUND + id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword());
        return userRepository.save(user);
    }

    public User delete(Integer id){
        if (id == null) {
            throw new IllegalArgumentException(USER_ID_NULL);
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(USER_ID_NOT_FOUND + id));
        userRepository.delete(user);
        return user;
    }

    private Integer autoIncrement(){
        List<User> users= userRepository.findAll();
        return users.isEmpty() ? 1 : users.stream()
                .max(Comparator.comparing(User::getId))
                .orElseThrow(() -> new RuntimeException("No se pudo determinar el siguiente ID"))
                .getId() + 1;    
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean authenticate(String email, String password) {
        Optional<User> user = findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
