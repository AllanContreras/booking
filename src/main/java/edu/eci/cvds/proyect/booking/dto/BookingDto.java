package edu.eci.cvds.proyect.booking.dto;


import java.time.LocalDateTime;

import edu.eci.cvds.proyect.booking.entity.BookingStatus;

public class BookingDto {
    private String userId;

    private String resource;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BookingStatus status;

    public BookingDto(String userId, String resource, LocalDateTime startDate, LocalDateTime endDate, BookingStatus status) {
        this.userId = userId;
        this.resource = resource;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
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
