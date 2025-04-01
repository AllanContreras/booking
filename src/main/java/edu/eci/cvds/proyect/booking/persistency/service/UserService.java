package edu.eci.cvds.proyect.booking.persistency.service;

import java.util.*;
import java.util.regex.Pattern;

import edu.eci.cvds.proyect.booking.exceptions.UserException;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.UserRepository;
import edu.eci.cvds.proyect.booking.users.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UsersService {
    @Autowired
    private UserRepository userRepository;

    private User createUser(User user) throws UserException {
        this.validateUser(user);
        user.setId(UUID.randomUUID().toString());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User createUserAsAdmin(User user) throws UserException {
        user.setRole(UserRole.ADMIN);
        return this.createUser(user);
    }

    public User createUserAsUser(User user, UserRole roles) throws UserException {
        this.validateRole(user.getRole());
        user.setRole(roles);
        return this.createUser(user);
    }

    public User updateUser(String id, User user) throws UserException {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();

            userToUpdate.setName(user.getName() == null ? userToUpdate.getName() : user.getName());
            userToUpdate.setEmail(user.getEmail() == null ? userToUpdate.getEmail() : user.getEmail());

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userToUpdate.setRole(user.getRole() == null || user.getRole().toString().isEmpty() ? userToUpdate.getRole()
                    : user.getRole());

            userRepository.save(userToUpdate);
            return userToUpdate;
        }

        throw new UserException.UserNotFoundException(user.getId());
    }

    public List<User> getAllUsers() throws UserException {
        return userRepository.findAll();
    }

    public User getUserById(String id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public User deleteUser(String id) throws UserException { // by id, username or gmail?
        return null;
    }

    public User loginUser(String name, String password) throws UserException {
        User user = this.userRepository.findByName(name);

        if (user == null) {
            throw new UserException.UserNotFoundException(name);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new UserException.UserInvalidValueException("Invalid credentials");
        }
    }

    public void validateUser(User user) throws UserException {
        if (user == null) {
            throw new UserException.UserInvalidValueException("User cannot be null");
        }
        if (user.getName() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new UserException.UserInvalidValueException("id or password or email cannot be null");
        }
        if (userRepository.findByName(user.getName()) != null) {
            throw new UserException.UserConflictException(user.getName());
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserException.UserConflictException(user.getEmail());
        }

        validateName(user.getName());
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());

        if (user.getRole() == null || user.getRole().toString().isEmpty()) {
            throw new UserException.UserInvalidValueException("All users must have a role");
        }

        this.validateRole(user.getRole());
    }

    private void validateName(String name) throws UserException.UserInvalidValueException {
        if (name.length() > 30 || name.length() < 5) {
            throw new UserException.UserInvalidValueException("Name must be between 5 and 30 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9._-]+$", name)) {
            throw new UserException.UserInvalidValueException(
                    "Username can only contain alphanumeric characters, dots, hyphens, and underscores");
        }
    }

    private void validateEmail(String email) throws UserException.UserInvalidValueException {
        String emailPattern = "^[\\w.-]+@[\\w-]+(\\.[\\w-]+)+$";
        if (email == null || !Pattern.matches(emailPattern, email)) {
            throw new UserException.UserInvalidValueException("Email format is invalid");
        }
    }

    private void validatePassword(String password) throws UserException.UserInvalidValueException {
        if (password == null) {
            throw new UserException.UserInvalidValueException("Password cannot be null");
        }
        if (password.length() < 6 || password.length() > 29) {
            throw new UserException.UserInvalidValueException("Password must be between 6 and 29 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9!@#$%^&*()\\-_=+]+$", password)) {
            throw new UserException.UserInvalidValueException("Illegal password, try another");
        }
    }

    public void validateRole(UserRole role) throws UserException {
        try {
            UserRole.valueOf(role.toString().toUpperCase());
        } catch (Exception e) {
            throw new UserException.UserInvalidValueException("role: " + role);
        }
    }
}
