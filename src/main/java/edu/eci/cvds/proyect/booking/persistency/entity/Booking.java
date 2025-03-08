package edu.eci.cvds.proyect.booking.persistency.entity;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.aggregation.DateOperators.Hour;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.shedules.Day;

@Document(collection = "Bookings")
public class Booking {
    @Id
    private Integer id;

    private Integer userId;

    private String resource;

    private Day day;

    private Hour startHour;

    private Hour endHour;

    private BookingStatus status;

    public Booking(Integer id, Integer userId, String resource, Day day, Hour startHour, Hour endHour,
            BookingStatus status) {
        this.id = id;
        this.userId = userId;
        this.resource = resource;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Hour getStartHour() {
        return startHour;
    }

    public void setStartHour(Hour startHour) {
        this.startHour = startHour;
    }

    public Hour getEndHour() {
        return endHour;
    }

    public void setEndHour(Hour endHour) {
        this.endHour = endHour;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    
}

