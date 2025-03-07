package edu.eci.cvds.proyect.booking.documents;

import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "Bookings")
public class Bookings {
    @Id
    private Integer id;

    private String userId;

    private String resource;

    private Date startDate;

    private Date endDate;

    private String status;

    public Bookings(Integer id, String userId, String resource, Date startDate, Date endDate, String status) {
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

    public void setId(Integer id) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
