package org.cavlovic.gradeCalc.grades.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SchoolModule {
    String name;
    Double grade;
    Double creditPoints;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public SchoolModule() {
    }

    


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getGrade() {
        return grade;
    }
    public void setGrade(Double grade) {
        this.grade = grade;
    }
    public Double getCreditPoints() {
        return creditPoints;
    }
    public void setCreditPoints(Double creditPoints) {
        this.creditPoints = creditPoints;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    
    

    
}


