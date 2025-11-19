package org.cavlovic.gradeCalc.grades.service;

import java.util.List;

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
        moduleRepository.addModule(module);
    }

    public List<SchoolModuleDTO> getAllModules(){
        logger.log(System.Logger.Level.INFO, "Service: Retrieving all modules");
        return moduleRepository.getAllModules();
    }

    public double calculateGradeAverage() {
        double totalWeightedGrades = 0;
        double totalCreditPoints = 0;
        

        for (SchoolModuleDTO module : moduleRepository.getAllModules()) {
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

        for (SchoolModuleDTO module : moduleRepository.getAllModules()) {
            totalCreditPoints += module.getCreditPoints();
        }

        return totalCreditPoints;
    }   

    public void deleteModule(Long index) {
        logger.log(System.Logger.Level.INFO, "Service: Deleting module " + index);
        moduleRepository.deleteModule(index);
    }

    public SchoolModuleDTO getModuleById(Long moduleId) {
        logger.log(System.Logger.Level.INFO, "Service: Retrieving module by ID " + moduleId);
        for (SchoolModuleDTO module : moduleRepository.getAllModules()) {
            if (module.getId().equals(moduleId)) {
                return module;
            }
        }
        return null; // Return null if not found
    }

   public void replaceModule(Long moduleId, SchoolModuleDTO updatedModule) {
        logger.log(System.Logger.Level.INFO, "Service: Replacing module with ID " + moduleId);
        moduleRepository.replaceModule(moduleId, updatedModule);
        
            }
        }



