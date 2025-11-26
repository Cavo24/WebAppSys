package org.cavlovic.gradeCalc.grades.repository;

import java.util.ArrayList;
import java.util.List;

import org.cavlovic.gradeCalc.grades.model.SchoolModule;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Repository
public class ModuleRepository {
    private final Logger log= org.slf4j.LoggerFactory.getLogger(ModuleRepository.class);
    private final List<SchoolModule> modules = new ArrayList<>();

    Long idCounter = 0L;

    @PostConstruct
    public void init() {
        log.debug("### Initialize Data ###");

        log.debug("create sample modules and grades");

        addModule (new SchoolModule(6.0, 1.3, "Software Engineering"));
        addModule (new SchoolModule(3.0, 2.0, "Databases"));
        addModule (new SchoolModule(3.0, 1.0, "Web Technologies"));
        addModule (new SchoolModule(4.5, 2.3, "Operating Systems"));
        log.debug("### Data initialized ###");
    }

    @PreDestroy
    public void cleanup() {
        log.debug("### Cleaning up ModuleRepository ###");
        int count = modules.size();
        modules.clear();
        log.debug("### Cleaned up ModuleRepository, removed " + count + " modules ###");
    }

    
    public synchronized void addModule(SchoolModule module) {
        log.debug("Repository: Adding module " + module.getName());
        module.setId(++idCounter);
        modules.add(module);
        log.debug("Repository: Added module " + module.getName());
    }

    public synchronized List<SchoolModule> getAllModules() {
        log.debug("Repository: Retrieving all modules, count: " + modules.size());
        return new ArrayList<>(modules);
    }

    public synchronized void clearModules() {
        log.debug("Repository: Clearing all modules, count before clear: " + modules.size());
        modules.clear();
        log.debug("Repository: All modules cleared");
    }

    public synchronized int getModuleCount() {
        return modules.size();
    }

    public synchronized void deleteModule(Long moduleId) { // Parameter wurde umbenannt zu moduleId zur Klarheit
    log.debug("Repository: Attempting to delete module with ID: " + moduleId);
    // Wir suchen das zu löschende Modul
    SchoolModule moduleToRemove = null;
    // Die for-Schleife wird verwendet, um das Element mit der passenden ID zu finden.
    for (SchoolModule module : modules) {
        // ACHTUNG: Die ID wird hier als Long gespeichert und muss geprüft werden
        if (module.getId() != null && module.getId().equals(moduleId)) { 
            moduleToRemove = module;
            break; // Modul gefunden, Schleife beenden
        }
    }
    if (moduleToRemove != null) {
        modules.remove(moduleToRemove); // Das gefundene Objekt löschen
        log.debug("Repository: Deleted module with ID " + moduleId + " (" + moduleToRemove.getName() + ")");
    } else {
        log.warn("Repository: Deletion failed. Module with ID " + moduleId + " not found.");
    }
}

    public synchronized void replaceModule(Long moduleId, SchoolModule updatedModule) {
        log.debug("Repository: Attempting to replace module with ID: " + moduleId);
        
        for (int i = 0; i < modules.size(); i++) {
            SchoolModule currentModule = modules.get(i);
            if (currentModule.getId() != null && currentModule.getId().equals(moduleId)) {
                updatedModule.setId(moduleId); // Ensure the ID remains the same
                modules.set(i, updatedModule);
                log.debug("Repository: Replaced module with ID " + moduleId + " with new module " + updatedModule.getName());
                return;
            }
        }
        
        log.warn("Repository: Replacement failed. Module with ID " + moduleId + " not found.");
    }
}
