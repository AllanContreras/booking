package edu.eci.cvds.proyect.booking.persistency.entity;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaboratoryTest {
    @Test
    void getIdTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        assertEquals(1, lab.getId());
    }
    @Test
    void getNameTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        assertEquals(LaboratoryName.INGENERIA_SW, lab.getName());
    }

    @Test
    void getLocationTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        assertEquals("B0", lab.getLocation());
    }
    @Test
    void setIdTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        lab.setId(22);
        assertEquals(22, lab.getId());
    }
    @Test
    void setNameTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        lab.setName(LaboratoryName.INTERACTIVA );
        assertEquals(LaboratoryName.INTERACTIVA, lab.getName());
    }

    @Test
    void setLocationTest() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "B0");
        lab.setLocation("H-301");
        assertEquals("H-301", lab.getLocation());
    }

}
