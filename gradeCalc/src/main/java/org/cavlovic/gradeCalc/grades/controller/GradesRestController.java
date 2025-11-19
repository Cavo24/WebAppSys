package org.cavlovic.gradeCalc.grades.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.cavlovic.gradeCalc.grades.model.SchoolModuleDTO;
import org.cavlovic.gradeCalc.grades.service.GradesService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/rest")
public class GradesRestController {
	private final Logger log = org.slf4j.LoggerFactory.getLogger(GradesRestController.class);

    @Autowired
    private GradesService gradesService;

    @GetMapping(value ="/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CollectionModel<EntityModel<SchoolModuleDTO>>> list() {
        log.debug("RESTful: list() is called");
        
        List<SchoolModuleDTO> modules = gradesService.getAllModules();
        if (modules == null || modules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Nicht das Domain-Objekt mutieren; für jedes Modul ein EntityModel erstellen
        List<EntityModel<SchoolModuleDTO>> entityModels = modules.stream()
            .map(module -> EntityModel.of(module,
                    Link.of("/rest/modules/" + module.getId()).withSelfRel()))
            .collect(Collectors.toList());

        CollectionModel<EntityModel<SchoolModuleDTO>> collectionModel = CollectionModel.of(entityModels);
        collectionModel.add(Link.of("/rest/modules").withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    //ResponseEntity muss ? (Wildcard) zurückgeben, da entwerder SchoolModule oder NotFound zurückgegeben wird
    @GetMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EntityModel<SchoolModuleDTO>> findModuleById(@PathVariable("id") Long moduleId) {
       log.debug("RESTful: findModuleById() is called");
         SchoolModuleDTO module = gradesService.getModuleById(moduleId);
            if (module == null) {            
                return ResponseEntity.notFound().build();
            }
            Link selflink = Link.of("/rest/modules/" + moduleId).withSelfRel();
            EntityModel<SchoolModuleDTO> moduleResource = EntityModel.of(module, selflink);
            
            return ResponseEntity.ok(moduleResource);
        }
    
    @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addModule(@RequestBody SchoolModuleDTO module) {
        log.debug("RESTful: addModule() is called");
        if (module.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New module cannot already have an ID");
        }
        if (module.getName() == null || module.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Module name is required");
        }
        if (module.getCreditPoints() == null || module.getCreditPoints() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Module credits must be a positive number");
        }
        if (module.getGrade() == null || module.getGrade() < 1.0 || module.getGrade() > 5.0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Module grade must be between 1.0 and 5.0");
        }
        gradesService.addToList(module);
      
        return ResponseEntity.status(HttpStatus.CREATED).body(module);
    }

    @PutMapping(value = "modules/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateModule(@PathVariable("id") Long moduleId, @RequestBody SchoolModuleDTO updatedModule) {
        log.debug("RESTful: updateModule() is called");
        SchoolModuleDTO existingModule = gradesService.getModuleById(moduleId);
        if (existingModule == null) {
            return ResponseEntity.notFound().build();
        }
        gradesService.replaceModule(moduleId, updatedModule);
        return ResponseEntity.ok(updatedModule);
    }

    @DeleteMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteModule(@PathVariable("id") Long moduleId) {
        log.debug("RESTful: deleteModule() is called");
        SchoolModuleDTO existingModule = gradesService.getModuleById(moduleId);
        if (existingModule == null) {
            return ResponseEntity.notFound().build();
        }
        gradesService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    // Neue Root-Route: liefert ein minimales HTML-Frontend, das /js/modules.js lädt.
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> serveFrontend() {
        String html = """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="utf-8">
              <title>GradeCalc - Frontend</title>
              <style>table, th, td { border: 1px solid black; border-collapse: collapse; padding: 8px; }</style>
            </head>
            <body>
              <h1>Notenübersicht (API-Client)</h1>
              <table id="modulesTable">
                <thead>
                  <tr><th>ID</th><th>Fachname</th><th>Note</th><th>ECTS</th><th>Link (HATEOAS)</th></tr>
                </thead>
                <tbody></tbody>
              </table>
              <script src="/js/modules.js"></script>
            </body>
            </html>
            """;
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }

    
}

