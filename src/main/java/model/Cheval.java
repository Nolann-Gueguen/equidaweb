package model;

import java.time.LocalDate;
import model.*;
public class Cheval {

    private int id;
    private String nom;
    private LocalDate dateNaissance;
    private String taille;
    private String poids;
    private String cheval_mere;
    private String cheval_pere;
    private int code_sire;
    private Race race;
    private Cheval pere;
    private Cheval mere;
    private Course course;

    public Cheval() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getPoids() {
        return poids;
    }

    public void setPoids(String poids) {
        this.poids = poids;
    }

    public String getCheval_mere() {
        return cheval_mere;
    }

    public void setCheval_mere(String cheval_mere) {
        this.cheval_mere = cheval_mere;
    }

    public String getCheval_pere() {
        return cheval_pere;
    }

    public void setCheval_pere(String cheval_pere) {
        this.cheval_pere = cheval_pere;
    }

    public int getCode_sire() {
        return code_sire;
    }

    public void setCode_sire(int code_sire) {
        this.code_sire = code_sire;
    }
    
    public Race getRace() {
        return race;
    }
    public void setRace(Race race) {
        this.race = race;
    }

    public Cheval getPere() {
        return pere;
    }

    public void setPere(Cheval pere) {
        this.pere = pere;
    }

    public Cheval getMere() {
        return mere;
    }

    public void setMere(Cheval mere) {
        this.mere = mere;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    
}
