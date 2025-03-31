
package edu.eci.cvds.proyect.booking.persistency.service;

import edu.eci.cvds.proyect.booking.persistency.dto.LaboratoryDto;
import edu.eci.cvds.proyect.booking.persistency.entity.Laboratory;
import edu.eci.cvds.proyect.booking.persistency.entity.User;
import edu.eci.cvds.proyect.booking.persistency.repository.LabRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LaboratoryService {
    private final LabRepository labRepository;
    private static final String LAB_ID_NULL="El ID del laboratorio no puede ser null";
    private static final String LAB_ID_NOT_FOUND="Laboratorio no encontrado con ID";

    public LaboratoryService(LabRepository labRepository){this.labRepository=labRepository;}

    public List<Laboratory> getAll(){
        return labRepository.findAll();
    }
    public Laboratory getOne(Integer id){
        if(id==null){
            throw new IllegalArgumentException(LAB_ID_NULL);
        }
        return labRepository.findById(id).orElseThrow(()->new RuntimeException(LAB_ID_NOT_FOUND+id));
    }
    public Laboratory save(LaboratoryDto laboratoryDto){
        if (laboratoryDto==null){
            throw new IllegalArgumentException("El laboratorio no puede ser nulo");
        }
        Integer id = autoIncrement();
        Laboratory laboratory = new Laboratory(id, laboratoryDto.getName(), laboratoryDto.getLocation());
        return labRepository.save(laboratory);
    }

    public Laboratory update(Integer id, LaboratoryDto laboratoryDto){
        if (id==null){
            throw new IllegalArgumentException(LAB_ID_NULL);
        }
        Laboratory laboratory = labRepository.findById(id).orElseThrow(()->new RuntimeException(LAB_ID_NOT_FOUND+id));
        laboratory.setName(laboratoryDto.getName());
        laboratory.setLocation(laboratoryDto.getLocation());
        return labRepository.save(laboratory);

    }
    public Laboratory delete(Integer id){
        if(id==null){
            throw new IllegalArgumentException(LAB_ID_NULL);
        }
        Laboratory lab = labRepository.findById(id).orElseThrow(() -> new RuntimeException(LAB_ID_NOT_FOUND+id));
        labRepository.delete(lab);
        return lab;
    }

    public Integer autoIncrement(){
        List <Laboratory> laboratories= labRepository.findAll();
        return laboratories.isEmpty()?1:laboratories.stream()
                .max(Comparator.comparing(Laboratory::getId))
                .orElseThrow(()->new RuntimeException("No se pudo determinar el siguiente ID"))
                .getId()+1;
    }
}