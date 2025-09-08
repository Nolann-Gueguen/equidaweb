/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;
import model.Vente;
import model.CategVente;
/**
 *
 * @author sio2
 */
public class TestVente {
    public static void main (String args[]){

        // création d'une instance de cheval nommée c
        Vente v = new Vente();
        v.setId(2);
        v.setNom("vente 1");
        v.setDateDebutVente("06/18/2019");

        // création d'une instance de race nommée r
        CategVente c = new CategVente();
        c.setCode(1);
        c.setLibelle("vente");
        

        //affectation de  la race au cheval grâce à la relation ManyToOne
        v.setCategVente(c);

        // Affichage des informations dans la console
        //voir notamment du nom de la race du cheval
        System.out.println("Vente : " + v.getId() + " " + v.getNom() + " "
                + v.getCategVente().getCode() + " " + v.getCategVente().getLibelle());
    }
}
