/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import model.*;
public class Vente {

    private int id;
    private String nom;
    private String dateDebutVente;
    private String dateEnvoieMessage;
    private String objetMessage;
    private String corpsMessage;
    private CategVente categVente;
    private ArrayList<Lot> lesLots ;
    private Lieu lieu;
    
    public Vente() {
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

    public String getDateDebutVente() {
        return dateDebutVente;
    }

    public void setDateDebutVente(String dateDebutVente) {
        this.dateDebutVente = dateDebutVente;
    }

    public String getDateEnvoieMessage() {
        return dateEnvoieMessage;
    }

    public void setDateEnvoieMessage(String dateEnvoieMessage) {
        this.dateEnvoieMessage = dateEnvoieMessage;
    }

    public String getObjetMessage() {
        return objetMessage;
    }

    public void setObjetMessage(String objetMessage) {
        this.objetMessage = objetMessage;
    }

    public String getCorpsMessage() {
        return corpsMessage;
    }

    public void setCorpsMessage(String corpsMessage) {
        this.corpsMessage = corpsMessage;
    }

    public ArrayList<Lot> getlesLots() {
        return lesLots;
    }

    public void setlesLots(ArrayList<Lot> lesLots) {
        this.lesLots = lesLots;
    }

    public ArrayList<Lot> getLesLots() {
        return lesLots;
    }

    public void addVente(Vente v) {
    }

    public CategVente getCategVente() {
        return categVente;
    }

    public void setCategVente(CategVente categVente) {
        this.categVente = categVente;
    }

    public Lieu getLieu() {
        return lieu;
    }

    public void setLieu(Lieu lieu) {
        this.lieu = lieu;
    }

    
   

}
    
    
    
