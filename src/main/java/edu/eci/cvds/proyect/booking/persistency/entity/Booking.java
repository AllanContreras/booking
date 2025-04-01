package edu.eci.cvds.proyect.booking.persistency.entity;

import javax.persistence.Id;
import edu.eci.cvds.proyect.booking.shedules.Hour;


import java.util.List;
import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.shedules.Day;

@Document(collection = "Bookings")
public class Booking {
    @Id
    private String id;

    private LaboratoryName laboratoryName;

    private Day day;

    private Hour startHour;

    private Hour endHour;

    private Boolean available;

    private List<String> ownerIds;


    private Integer priority;

    public Booking() {
    }

    public Booking(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<String> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(List<String> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public Integer getPriority() {
        return priority;
    }

    public void addOwnerId(String ownerId) {
        this.ownerIds.add(ownerId);
    }

    public void removeOwnerId(String ownerId) {
        this.ownerIds.remove(ownerId);

    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", laboratoryName='" + laboratoryName + '\'' +
                ", day='" + day + '\'' +
                ", startHour='" + startHour + '\'' +
                ", endHour='" + endHour + '\'' +
                ", available='" + available + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Booking booking = (Booking) obj;
        return available == booking.available &&
                Objects.equals(id, booking.id) &&
                Objects.equals(laboratoryName, booking.laboratoryName) &&
                Objects.equals(day, booking.day) &&
                Objects.equals(startHour, booking.startHour) &&
                Objects.equals(endHour, booking.endHour) &&
                Objects.equals(available, booking.available);
    }

}
