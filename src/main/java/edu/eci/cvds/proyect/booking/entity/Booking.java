package edu.eci.cvds.proyect.booking.entity;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Bookings")
public class Booking {
    @Id
    private Integer id;

    private String userId;

    private String resource;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BookingStatus status;

    public Booking(Integer id, String userId, String resource, LocalDateTime startDate, LocalDateTime endDate, BookingStatus status) {
        super();
        this.id = id;
        this.userId = userId;
        this.resource = resource;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
