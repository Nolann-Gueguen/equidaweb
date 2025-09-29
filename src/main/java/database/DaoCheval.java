package database;

import model.Cheval;
import model.Race;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoCheval {
    Connection cnx;
    static PreparedStatement requeteSql = null;
    static ResultSet resultatRequete = null;

   
    public static ArrayList<Cheval> getLesChevaux(Connection cnx) {
        ArrayList<Cheval> lesChevaux = new ArrayList<Cheval>();
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT c.id as c_id, c.nom as c_nom, " +
                "r.id as r_id, r.nom as r_nom " +
                "FROM cheval c " +
                "INNER JOIN race r ON c.race_id = r.id"
            );
            resultatRequete = requeteSql.executeQuery();
            while (resultatRequete.next()) {
                Cheval c = new Cheval();
                c.setId(resultatRequete.getInt("c_id"));
                c.setNom(resultatRequete.getString("c_nom"));
                Race r = new Race();
                r.setId(resultatRequete.getInt("r_id"));
                r.setNom(resultatRequete.getString("r_nom"));
                c.setRace(r);
                lesChevaux.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête de getLesChevaux a généré une exception SQL");
        }
        return lesChevaux;
    }

    
    public static Cheval getLeCheval(Connection cnx, int idCheval) {
        Cheval cheval = null;
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT c.id as c_id, c.nom as c_nom, c.dateNaissance c_dateNaissance," +
                "r.id as r_id, r.nom as r_nom " +
                "FROM cheval c " +
                "INNER JOIN race r ON c.race_id = r.id " +
                "WHERE c.id = ?"
            );
            requeteSql.setInt(1, idCheval);
            resultatRequete = requeteSql.executeQuery();
            if (resultatRequete.next()) {
                cheval = new Cheval();
                cheval.setId(resultatRequete.getInt("c_id"));
                cheval.setNom(resultatRequete.getString("c_nom"));
                cheval.setDateNaissance(resultatRequete.getString("c_dateNaissance"));
                Race race = new Race();
                race.setId(resultatRequete.getInt("r_id"));
                race.setNom(resultatRequete.getString("r_nom"));
                cheval.setRace(race);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête de getLeCheval a généré une exception SQL");
        }
        return cheval;
    }
    
    public static boolean ajouterCheval(Connection cnx, Cheval cheval) {
    try {
        requeteSql = cnx.prepareStatement(
            "INSERT INTO cheval (nom, date_naissance, race_id) VALUES (?, ?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );
        requeteSql.setString(1, cheval.getNom());
        
        // Gestion de la date de naissance
        if (cheval.getDateNaissance() != null) {
            requeteSql.setDate(2, java.sql.Date.valueOf(cheval.getDateNaissance()));
        } else {
            requeteSql.setNull(2, java.sql.Types.DATE);
        }
        
        requeteSql.setInt(3, cheval.getRace().getId());
        
        int result = requeteSql.executeUpdate();
        
        if (result == 1) {
            // Récupération de l'id auto-généré
            ResultSet rs = requeteSql.getGeneratedKeys();
            if (rs.next()) {
                cheval.setId(rs.getInt(1));
            }
            return true;
        }
        return false;
        
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Erreur lors de l'ajout du cheval");
        return false;
    }
}
}