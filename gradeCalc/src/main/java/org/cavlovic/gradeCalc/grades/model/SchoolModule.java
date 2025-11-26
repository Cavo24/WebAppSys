package org.cavlovic.gradeCalc.grades.model;


public class SchoolModule{
    String name;
    Double grade;
    Double creditPoints;
    Long id;

    public SchoolModule(Double creditPoints, Double grade, String name) {
        this.creditPoints = creditPoints;
        this.grade = grade;
        this.name = name;
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
