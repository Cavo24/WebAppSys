package org.cavlovic.gradeCalc.grades.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cavlovic.gradeCalc.grades.model.SchoolModule;
import org.cavlovic.gradeCalc.grades.model.SchoolModuleDTO;
import org.cavlovic.gradeCalc.grades.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GradesService {
    System.Logger logger = System.getLogger(GradesService.class.getName());

    @Autowired
    ModuleRepository moduleRepository;

    public void addToList(SchoolModuleDTO module){
        logger.log(System.Logger.Level.INFO, "Service: Adding module " + module.getName() + " to the list");
        SchoolModule moduleEntity = convertToEntity(module);
        moduleRepository.save(moduleEntity);
    }

    public List<SchoolModuleDTO> getAllModules(){
        logger.log(System.Logger.Level.INFO, "Service: Retrieving all modules");
        List<SchoolModule> entities = moduleRepository.findAll();

        // Konvertiert die Liste der Entitäten in eine Liste von DTOs
        return entities.stream()
        .map(this::convertToDto) // Wendet die Konvertierung auf jedes Element an
        .collect(Collectors.toList());
        
    }

    public double calculateGradeAverage() {
        double totalWeightedGrades = 0;
        double totalCreditPoints = 0;
        

        for (SchoolModule module : moduleRepository.findAll()) {
            totalWeightedGrades +=  module.getGrade()* module.getCreditPoints();
            totalCreditPoints += module.getCreditPoints();
        }


        if (totalCreditPoints == 0) {
            return 0; // Avoid division by zero
        }

        return totalWeightedGrades / totalCreditPoints;
    }

    public double calculateTotalCreditPoints() {
        double totalCreditPoints = 0;

        for (SchoolModule module : moduleRepository.findAll()) {
            totalCreditPoints += module.getCreditPoints();
        }

        return totalCreditPoints;
    }   

    public void deleteModule(Long index) {
        logger.log(System.Logger.Level.INFO, "Service: Deleting module " + index);
        moduleRepository.deleteById(index);
    }

    public SchoolModuleDTO getModuleById(Long moduleId) {
        logger.log(System.Logger.Level.INFO, "Service: Retrieving module by ID " + moduleId);
        Optional <SchoolModule> optionalModule = moduleRepository.findById(moduleId);
        if (optionalModule.isPresent()) {
            SchoolModule entity = optionalModule.get();
            return convertToDto(entity);
        }
        return null; // Return null if not found
    }

   public void replaceModule(Long moduleId, SchoolModuleDTO updatedModule) {
        logger.log(System.Logger.Level.INFO, "Service: Replacing module with ID " + moduleId);
        SchoolModule moduleEntity = convertToEntity(updatedModule);
        moduleEntity.setId(moduleId); // Ensure the ID remains the same
        moduleRepository.save(moduleEntity);
            }

    
    private SchoolModuleDTO convertToDto(SchoolModule entity) {
    // 1. Erzeuge ein neues DTO-Objekt und kopiere die Werte
    SchoolModuleDTO dto = new SchoolModuleDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setGrade(entity.getGrade());
    dto.setCreditPoints(entity.getCreditPoints());
    
    // 2. Führe hier die HATEOAS-Link-Logik aus (optional, oder im Controller)
    // Beispiel: dto.add(Link.of(...).withSelfRel()); 
    
    return dto;
    }

    private SchoolModule convertToEntity(SchoolModuleDTO dto) {
    // 1. Erzeuge ein neues Entity-Objekt und kopiere die Werte
    SchoolModule entity = new SchoolModule();
        entity.setName(dto.getName());
        entity.setGrade(dto.getGrade());
        entity.setCreditPoints(dto.getCreditPoints());
    return entity;
        }
    }


