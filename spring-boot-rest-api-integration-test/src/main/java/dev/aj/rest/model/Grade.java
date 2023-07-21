package rest.model;

public interface Grade {
    double getGrade();

    Long getId();

    void setId(Long id);

    Long getStudentId();

    void setStudentId(Long studentId);

    void setGrade(double grade);
}
