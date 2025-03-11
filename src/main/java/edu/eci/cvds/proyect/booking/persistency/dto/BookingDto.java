package edu.eci.cvds.proyect.booking.persistency.dto;

import edu.eci.cvds.proyect.booking.shedules.Hour;
import edu.eci.cvds.proyect.booking.bookings.BookingStatus;
import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;


public class BookingDto {
    private Integer userId;

    private LaboratoryName laboratoryName;

    private Day day;

    private Hour startHour;

    private Hour endHour;

    private BookingStatus status;

    public BookingDto(Integer userId, LaboratoryName laboratoryName, Day day, Hour startHour, Hour endHour,
            BookingStatus status) {
        this.userId = userId;
        this.laboratoryName = laboratoryName;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LaboratoryName getLaboratoryName() {
        return laboratoryName;
    }

    public void setLaboratoryName(LaboratoryName laboratoryName) {
        this.laboratoryName = laboratoryName;
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
