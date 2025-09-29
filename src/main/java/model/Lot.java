/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
import model.*;
/**
 *
 * @author sio2
 */
public class Lot {
    private int id;
    private String prixDepart;
    private ArrayList<Cheval> lesChevaux ;
    private Vente vente;
    
    public Lot() {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrixDepart() {
        return prixDepart;
    }

    public void setPrixDepart(String prixDepart) {
        this.prixDepart = prixDepart;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public ArrayList<Cheval> getLesChevaux() {
        return lesChevaux;
    }

    public void setLesChevaux(ArrayList<Cheval> lesChevaux) {
        this.lesChevaux = lesChevaux;
    }
    
    public void addCheval(Cheval unCheval){
        if (lesChevaux ==null ){
            lesChevaux = new ArrayList<Cheval>();
        }
        lesChevaux.add(unCheval);
    }
    
    
}
