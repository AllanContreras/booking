package edu.eci.cvds.proyect.booking.persistency.controller;

import edu.eci.cvds.proyect.booking.persistency.dto.LaboratoryDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Laboratory;
import edu.eci.cvds.proyect.booking.persistency.service.LaboratoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Laboratory")

public class  LaboratoryController {

    private LaboratoryService laboratoryService;
    private static final String ERROR_KEY="Error";
    private static final String MESSAGE_KEY="Message";
    private static final Logger logger= LoggerFactory.getLogger(LaboratoryController.class);

    @Autowired
    public LaboratoryController(LaboratoryService laboratoryService){
        this.laboratoryService=laboratoryService;
    }

    @GetMapping
    public ResponseEntity<List<Laboratory>> getAll(){
        try {
            return  new  ResponseEntity<>(laboratoryService.getAll(), HttpStatus.OK);
        }catch (Exception e) {
            String errorMessage=(e.getCause()!=null)?e.getCause().toString(): e.getMessage();
            logger.error("Error al obtener los laboratorios: {}", errorMessage, e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable("id") Integer id){

        try{
            return new ResponseEntity<>(laboratoryService.getOne(id), HttpStatus.OK);
        }catch (Exception e){
            String errorMessage=(e.getCause()!=null)?e.getCause().toString():e.getMessage();
            logger.error("Error al obtener el laboratorio con ID {}: {}", id, errorMessage, e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, "Error al obtener el laboratorio");
            errorResponse.put(MESSAGE_KEY, errorMessage);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody LaboratoryDto laboratoryDto){
        try {
            Laboratory savedLaboratory=laboratoryService.save(laboratoryDto);
            return new ResponseEntity<>(savedLaboratory, HttpStatus.CREATED);
        }catch(Exception e){
            String errorMessage=(e.getCause()!=null)?e.getCause().toString():e.getMessage();
            logger.error("Error al obtener el laboratorio: {}", errorMessage, e);
            Map<String,String> errorResponse=new HashMap<>();
            errorResponse.put(ERROR_KEY,"Error al guardar el laboratorio");
            errorResponse.put(MESSAGE_KEY,errorMessage);
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object>update(@PathVariable("id") Integer id, @RequestBody LaboratoryDto laboratoryDto){
        try{
            return new ResponseEntity<>(laboratoryService.update(id, laboratoryDto),HttpStatus.OK);
        }catch (Exception e){
            String errorMessage=(e.getCause()!=null)?e.getCause().toString():e.getMessage();
            logger.error("Error al actualizar el laboratorio con ID {}: {}", id, errorMessage, e);
            Map<String,String> errorResponse=new HashMap<>();
            errorResponse.put(ERROR_KEY,"Error al actualizar el laboratorio");
            errorResponse.put(MESSAGE_KEY,errorMessage);
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id){
        try{
            laboratoryService.delete(id);
            return new ResponseEntity<>(Collections.singletonMap(MESSAGE_KEY,"Laboratorio eliminado correctamente"),HttpStatus.OK);
        }catch (Exception e){
            String errorMessage=(e.getCause()!=null)?e.getCause().toString():e.getMessage();
            logger.error("Error al eliminar el laboratorio con ID {}: {}", id, errorMessage, e);
            Map<String,String> errorResponse=new HashMap<>();
            errorResponse.put(ERROR_KEY,"Error al eliminar el laboratorio");
            errorResponse.put(MESSAGE_KEY,errorMessage);
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
