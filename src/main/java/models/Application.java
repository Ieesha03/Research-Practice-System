package models;

public class Application {
    private int id;
    private int studentId;
    private int professorId;
    private String studentName; // New field
    private String areaOfInterest;
    private String skills;
    private String publications;
    private String experience;

    // Constructor with studentName
    public Application(int id, int studentId, int professorId, String studentName, String areaOfInterest, String skills, String publications, String experience) {
        this.id = id;
        this.studentId = studentId;
        this.professorId = professorId;
        this.studentName = studentName;
        this.areaOfInterest = areaOfInterest;
        this.skills = skills;
        this.publications = publications;
        this.experience = experience;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getPublications() {
        return publications;
    }

    public void setPublications(String publications) {
        this.publications = publications;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
