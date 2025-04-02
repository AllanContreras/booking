package edu.eci.cvds.proyect.booking.persistency.entity;

import javax.persistence.Id;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.cvds.proyect.booking.users.UserRole;
import jakarta.validation.constraints.Pattern;

@Document(collection = "Users")
public class User {
    @Id
    private Integer id;

    private String name;

    @Email(message = "Debe ser un email válido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@escuelaing.edu.co$", message = "El email debe ser del dominio @escuelaing.edu.co")
    private String email;

    private UserRole role;

    private String password;

    public User(Integer id, String name, String email, UserRole role, String password) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
