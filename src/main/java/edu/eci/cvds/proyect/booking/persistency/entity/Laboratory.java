package edu.eci.cvds.proyect.booking.persistency.entity;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Laboratories")

public class    Laboratory {

    private Integer id;
    private LaboratoryName name;
    private String location;

    public Laboratory(Integer id,LaboratoryName name , String location){
        this.id=id;
        this.name=name;
        this.location=location;
    }

    public Integer getId(){
        return id;
    }

    public LaboratoryName getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
    public void setName(LaboratoryName name){
        this.name=name;
    }

    public void setId(Integer id){
        this.id=id;
    }

    public void setLocation(String location){
        this.location=location;
    }


}
