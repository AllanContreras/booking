package edu.eci.cvds.proyect.booking.persistency.dto;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaboratoryDtoTest {
    @Test
    void getNameTest(){
        LaboratoryDto laboratoryDto =new LaboratoryDto(LaboratoryName.DESARROLLO_VJ,"B0");
        assertEquals(LaboratoryName.DESARROLLO_VJ,laboratoryDto.getName());
    }
    @Test
    void getLocationTest(){
        LaboratoryDto laboratoryDto =new LaboratoryDto(LaboratoryName.DESARROLLO_VJ,"B0");
        assertEquals("B0",laboratoryDto.getLocation());
    }

    @Test
    void setNameTest(){
        LaboratoryDto laboratoryDto =new LaboratoryDto(LaboratoryName.DESARROLLO_VJ,"B0");
        laboratoryDto.setName(LaboratoryName.FUNDAMENTOS);
        assertEquals(LaboratoryName.FUNDAMENTOS,laboratoryDto.getName());
    }
    @Test
    void setLocationTest(){
        LaboratoryDto laboratoryDto =new LaboratoryDto(LaboratoryName.DESARROLLO_VJ,"B0");
        laboratoryDto.setLocation("H-301");
        assertEquals("H-301",laboratoryDto.getLocation());
    }}

