package edu.eci.cvds.proyect.booking.persistency.dto;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;

public class LaboratoryDto {
    private LaboratoryName name;
    private String location;

    public LaboratoryDto(LaboratoryName name, String location) {
        this.name = name;
        this.location = location;
    }
    public LaboratoryName getName() {
        return name;
    }
    public String getLocation( ){
        return location;
    }
    public void setName(LaboratoryName name) {
        this.name = name;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}