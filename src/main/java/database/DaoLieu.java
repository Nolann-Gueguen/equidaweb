/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import model.Lieu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoLieu {
    static PreparedStatement requeteSql = null;
    static ResultSet resultatRequete = null;

    /**
     * Récupère toutes les lieux présentes dans la base de données
     * @param cnx La connexion active à la base de données
     * @return ArrayList<Lieu> La liste de toutes les lieux trouvées
     */
    public static ArrayList<Lieu> getLesLieux(Connection cnx) {
        ArrayList<Lieu> lesLieux = new ArrayList<Lieu>();
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT id, nom FROM lieu ORDER BY ville"
            );
            resultatRequete = requeteSql.executeQuery();
            while (resultatRequete.next()) {
                Lieu lieu = new Lieu();
                lieu.setId(resultatRequete.getInt("id"));
                lieu.setVille(resultatRequete.getString("ville"));
                lesLieux.add(lieu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête de getLesLieux a généré une exception SQL");
        }
        return lesLieux;
    }

    /**
     * Récupère une lieu spécifique par son identifiant
     * @param cnx La connexion active à la base de données
     * @param id L'identifiant de la lieu recherchée
     * @return lieu La lieu trouvée ou null si non trouvée
     */
    public static Lieu getLieuById(Connection cnx, int id) {
        Lieu lieu = null;
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT id, ville FROM lieu WHERE id = ?"
            );
            requeteSql.setInt(1, id);
            resultatRequete = requeteSql.executeQuery();
            if (resultatRequete.next()) {
                lieu = new Lieu();
                lieu.setId(resultatRequete.getInt("id"));
                lieu.setVille(resultatRequete.getString("ville"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête de getLieuById a généré une exception SQL");
        }
        return lieu;
    }
}
