package model;

import java.time.LocalDate;
import java.util.ArrayList;
import model.*;
public class Cheval {

    private int id;
    private String nom;
    private String dateNaissance;
    private Lot lot;
    private Race race;
    private Cheval pere;
    private Cheval mere;
    private ArrayList<ChevalCourse> LesChevauxCourse;


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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    public Race getRace() {
        return race;
    }
    public void setRace(Race race) {
        this.race = race;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
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

    public ArrayList<ChevalCourse> getLesChevauxCourse() {
        return LesChevauxCourse;
    }

    public void setLesChevauxCourse(ArrayList<ChevalCourse> LesChevauxCourse) {
        this.LesChevauxCourse = LesChevauxCourse;
    }
    
    public void addCheval(ChevalCourse unChevalCourse){
        if (LesChevauxCourse ==null ){
            LesChevauxCourse = new ArrayList<ChevalCourse>();
        }
        LesChevauxCourse.add(unChevalCourse);
    }
    
}
