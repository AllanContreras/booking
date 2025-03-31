






package edu.eci.cvds.proyect.booking.persistency.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.eci.cvds.proyect.booking.laboratorys.LaboratoryName;
import edu.eci.cvds.proyect.booking.persistency.dto.LaboratoryDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Laboratory;
import edu.eci.cvds.proyect.booking.persistency.service.LaboratoryService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LaboratoryController.class)
class LaboratoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LaboratoryService laboratoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllLaboratoriesSuccess() throws Exception {
        Laboratory lab1 = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");
        Laboratory lab2 = new Laboratory(2, LaboratoryName.REDES, "Building B");

        Mockito.when(laboratoryService.getAll()).thenReturn(Arrays.asList(lab1, lab2));

        mockMvc.perform(get("/Laboratory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testGetAllLaboratoriesFailure() throws Exception {
        Mockito.when(laboratoryService.getAll()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/Laboratory"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetOneLaboratorySuccess() throws Exception {
        Laboratory lab = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.getOne(1)).thenReturn(lab);

        mockMvc.perform(get("/Laboratory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("FUNDAMENTOS"));
    }

    @Test
    void testGetOneLaboratoryFailure() throws Exception {
        Mockito.when(laboratoryService.getOne(1)).thenThrow(new RuntimeException("Laboratory not found"));

        mockMvc.perform(get("/Laboratory/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al obtener el laboratorio"));
    }

    @Test
    void testSaveLaboratorySuccess() throws Exception {
        LaboratoryDto labDto = new LaboratoryDto(LaboratoryName.FUNDAMENTOS, "Building A");
        Laboratory lab = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.save(Mockito.any(LaboratoryDto.class))).thenReturn(lab);

        mockMvc.perform(post("/Laboratory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testSaveLaboratoryFailure() throws Exception {
        LaboratoryDto labDto = new LaboratoryDto(LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.save(Mockito.any(LaboratoryDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/Laboratory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al guardar el laboratorio"));
    }

    @Test
    void testUpdateLaboratorySuccess() throws Exception {
        LaboratoryDto labDto = new LaboratoryDto(LaboratoryName.FUNDAMENTOS, "Building A");
        Laboratory lab = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.update(Mockito.eq(1), Mockito.any(LaboratoryDto.class))).thenReturn(lab);

        mockMvc.perform(put("/Laboratory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateLaboratoryFailure() throws Exception {
        LaboratoryDto labDto = new LaboratoryDto(LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.update(Mockito.eq(1), Mockito.any(LaboratoryDto.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(put("/Laboratory/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al actualizar el laboratorio"));
    }

    @Test
    void testDeleteLaboratorySuccess() throws Exception {
        Laboratory lab = new Laboratory(1, LaboratoryName.FUNDAMENTOS, "Building A");

        Mockito.when(laboratoryService.delete(1)).thenReturn(lab);

        mockMvc.perform(delete("/Laboratory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message").value("Laboratorio eliminado correctamente"));
    }


    @Test
    void testDeleteLaboratoryFailure() throws Exception {
        Mockito.doThrow(new RuntimeException("Delete error")).when(laboratoryService).delete(1);

        mockMvc.perform(delete("/Laboratory/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.Error").value("Error al eliminar el laboratorio"));
    }}