/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author sio2
 */
public class Course {
    private int id;
    private String nom;
    private String dateCourse;
    private ArrayList<ChevalCourse> LeschevauxCourse;

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
    

    public String getDateCourse() {
        return dateCourse;
    }

    public void setDateCourse(String dateCourse) {
        this.dateCourse = dateCourse;
    }

    public ArrayList<ChevalCourse> getLeschevauxCourse() {
        return LeschevauxCourse;
    }

    public void setLesLots(ArrayList<ChevalCourse> LeschevauxCourse) {
        this.LeschevauxCourse = LeschevauxCourse;
    }
    
    public void addCourse(ChevalCourse unChevalCourse){
        if (LeschevauxCourse ==null ){
            LeschevauxCourse = new ArrayList<ChevalCourse>();
        }
        LeschevauxCourse.add(unChevalCourse);
    }
}
