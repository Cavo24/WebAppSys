package org.cavlovic.gradeCalc.grades.controller;

import java.util.List;

import org.cavlovic.gradeCalc.grades.model.SchoolModule;
import org.cavlovic.gradeCalc.grades.service.GradesService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GradesRestController {
	private final Logger log = org.slf4j.LoggerFactory.getLogger(GradesRestController.class);

    @Autowired
    private GradesService gradesService;

    @QueryMapping
    public List<SchoolModule> allModules() {
        log.debug("GraphQL: allModules() is called");
        return (List<SchoolModule>)(List<?>)gradesService.getAllModules();
    }

    @QueryMapping
    public SchoolModule moduleById(@Argument Long id) {
        log.debug("GraphQL: moduleById() is called with id: " + id);
        return (SchoolModule) gradesService.getModuleById(id);
    }

    @QueryMapping
    public double averageGrade() {
        log.debug("GraphQL: averageGrade() is called");
        return gradesService.calculateGradeAverage();
    }

    @QueryMapping
    public double totalCreditPoints() {
        log.debug("GraphQL: totalCreditPoints() is called");
        return gradesService.calculateTotalCreditPoints();
    }

    @MutationMapping
    public SchoolModule addModule(@Argument String name, @Argument Double grade, @Argument Double creditPoints) {
        log.debug("GraphQL: addModule() is called");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Module name is required");
        }
        if (grade == null || grade < 1.0 || grade > 5.0) {
            throw new IllegalArgumentException("Module grade must be between 1.0 and 5.0");
        }
        if (creditPoints == null || creditPoints <= 0) {
            throw new IllegalArgumentException("Module credits must be a positive number");
        }
        SchoolModule module = new SchoolModule(creditPoints, grade, name);
        gradesService.addToList(module);
        return module;
    }

    @MutationMapping
    public SchoolModule updateModule(@Argument Long id, @Argument String name, 
                                      @Argument Double grade, 
                                      @Argument Double creditPoints) {
        log.debug("GraphQL: updateModule() is called with id: " + id);
        SchoolModule existing = (SchoolModule) gradesService.getModuleById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Module not found");
        }
        if (name != null) existing.setName(name);
        if (grade != null) existing.setGrade(grade);
        if (creditPoints != null) existing.setCreditPoints(creditPoints);
        gradesService.replaceModule(id, existing);
        return existing;
    }

    @MutationMapping
    public boolean deleteModule(@Argument Long id) {
        log.debug("GraphQL: deleteModule() is called with id: " + id);
        SchoolModule existing = (SchoolModule) gradesService.getModuleById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Module not found");
        }
        gradesService.deleteModule(id);
        return true;
    }
}

