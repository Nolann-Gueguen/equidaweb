package test;

import model.*;


public class Testchevalcourse {

    public static void main (String args[]){

        // création d'une instance de cheval nommée c
        Cheval c = new Cheval();
        c.setId(2);
        c.setNom("Houri");

        // création d'une instance de race nommée r
        Course cc = new Course();
        cc.setId(1);
        cc.setNom("chevalier");
        cc.setVille("caen");

        //affectation de  la race au cheval grâce à la relation ManyToOne
        c.setCourse(cc);

        // Affichage des informations dans la console
        //voir notamment du nom de la race du cheval
        System.out.println("Cheval : " + c.getId() + " " + c.getNom() + " à fait la course : "
                + c.getRace().getId() + " " + c.getRace().getNom());
    }
}