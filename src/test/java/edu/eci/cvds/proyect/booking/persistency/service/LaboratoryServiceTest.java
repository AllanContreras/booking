package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.persistency.dto.LaboratoryDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Laboratory;
import edu.eci.cvds.proyect.booking.persistency.repository.LabRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LaboratoryServiceTest {

    @Mock
    private LabRepository laboratoryRepository;

    @InjectMocks
    private LaboratoryService laboratoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllLaboratoriesSuccess() {
        Laboratory lab1 = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");
        Laboratory lab2 = new Laboratory(2, LaboratoryName.DESARROLLO_VJ, "Building B");

        when(laboratoryRepository.findAll()).thenReturn(Arrays.asList(lab1, lab2));

        List<Laboratory> laboratories = laboratoryService.getAll();

        assertNotNull(laboratories);
        assertEquals(2, laboratories.size());
        assertEquals(LaboratoryName.FUNDAMENTOS, laboratories.get(0).getName());
        verify(laboratoryRepository, times(1)).findAll();
    }

    @Test
    void testGetAllLaboratoriesFailure() {
        when(laboratoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Laboratory> laboratories = laboratoryService.getAll();

        assertNotNull(laboratories);
        assertTrue(laboratories.isEmpty(), "La lista de laboratorios debería estar vacía");
        verify(laboratoryRepository, times(1)).findAll();
    }

    @Test
    void testGetOneLaboratorySuccess() {
        Laboratory lab = new Laboratory(1, LaboratoryName.INGENERIA_SW, "Building A");

        when(laboratoryRepository.findById(1)).thenReturn(Optional.of(lab));

        Laboratory foundLab = laboratoryService.getOne(1);

        assertNotNull(foundLab);
        assertEquals(LaboratoryName.INGENERIA_SW, foundLab.getName());
        verify(laboratoryRepository, times(1)).findById(1);
    }

    @Test
    void testGetOneLaboratoryFailure() {
        when(laboratoryRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> laboratoryService.getOne(99));

        assertEquals("Laboratorio no encontrado con ID99", exception.getMessage());
        verify(laboratoryRepository, times(1)).findById(99);
    }

    @Test
    void testSaveLaboratorySuccess() {
        LaboratoryDto labDto = new LaboratoryDto(LaboratoryName.REDES, "Building A");
        Laboratory lab = new Laboratory(1, LaboratoryName.REDES, "Building A");

        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(lab);

        Laboratory savedLab = laboratoryService.save(labDto);

        assertNotNull(savedLab);
        assertEquals(LaboratoryName.REDES, savedLab.getName());
        verify(laboratoryRepository, times(1)).save(any(Laboratory.class));
    }

    @Test
    void testSaveLaboratoryFailure() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> laboratoryService.save(null));

        assertEquals("El laboratorio no puede ser nulo", exception.getMessage());
        verify(laboratoryRepository, never()).save(any(Laboratory.class));
    }

    @Test
    void testUpdateLaboratorySuccess() {
        Laboratory existingLab = new Laboratory(1, LaboratoryName.REDES, "Building A");
        LaboratoryDto updatedLabDto = new LaboratoryDto(LaboratoryName.DESARROLLO_VJ, "Building C");


        when(laboratoryRepository.findById(1)).thenReturn(Optional.of(existingLab));
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(existingLab);

        Laboratory updatedLab = laboratoryService.update(1, updatedLabDto);

        assertNotNull(updatedLab);
        assertEquals(LaboratoryName.DESARROLLO_VJ, updatedLab.getName());
        assertEquals("Building C", updatedLab.getLocation());
        verify(laboratoryRepository, times(1)).save(existingLab);
    }

    @Test
    void testUpdateLaboratoryFailure() {
        LaboratoryDto updatedLabDto = new LaboratoryDto(LaboratoryName.INTERACTIVA, "Building C");

        when(laboratoryRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> laboratoryService.update(99, updatedLabDto));

        assertEquals("Laboratorio no encontrado con ID99", exception.getMessage());
        verify(laboratoryRepository, never()).save(any(Laboratory.class));
    }

    @Test
    void testDeleteLaboratorySuccess() {
        Laboratory lab = new Laboratory(1, LaboratoryName.PLATAFORMAS, "Building A");

        when(laboratoryRepository.findById(1)).thenReturn(Optional.of(lab));
        doNothing().when(laboratoryRepository).delete(lab);

        Laboratory deletedLab = laboratoryService.delete(1);

        assertNotNull(deletedLab);
        assertEquals(1, deletedLab.getId());
        verify(laboratoryRepository, times(1)).delete(lab);
    }

    @Test
    void testDeleteLaboratoryFailure() {
        when(laboratoryRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> laboratoryService.delete(99));

        assertEquals("Laboratorio no encontrado con ID99", exception.getMessage());
        verify(laboratoryRepository, never()).delete(any(Laboratory.class));
    }
}