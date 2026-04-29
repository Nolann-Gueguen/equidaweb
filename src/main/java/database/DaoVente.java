package database;

import java.sql.*;
import java.util.ArrayList;
import model.*;

public class DaoVente {

    /**
     * Récupère toutes les ventes avec leur lieu.
     */
    public static ArrayList<Vente> getLesVentes(Connection cnx) {
        ArrayList<Vente> lesVentes = new ArrayList<>();
        String sql = "SELECT v.id AS v_id, v.nom AS v_nom, " +
                     "l.id AS l_id, l.ville AS l_ville " +
                     "FROM vente v INNER JOIN lieu l ON v.lieu_id = l.id " +
                     "ORDER BY v.dateDebutVente DESC";

        try (PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vente v = new Vente();
                v.setId(rs.getInt("v_id"));
                v.setNom(rs.getString("v_nom"));
                Lieu l = new Lieu();
                l.setId(rs.getInt("l_id"));
                l.setVille(rs.getString("l_ville"));
                v.setLieu(l);
                lesVentes.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête getLesVentes a échoué");
        }
        return lesVentes;
    }

    /**
     * Récupère le détail d'une vente par son id.
     */
    public static Vente getLaVente(Connection cnx, int idVente) {
        Vente vente = null;
        String sql = "SELECT v.id AS v_id, v.nom AS v_nom, v.dateDebutVente AS v_dateDebutVente, " +
                     "l.id AS l_id, l.ville AS l_ville " +
                     "FROM vente v INNER JOIN lieu l ON v.lieu_id = l.id " +
                     "WHERE v.id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idVente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vente = new Vente();
                    vente.setId(rs.getInt("v_id"));
                    vente.setNom(rs.getString("v_nom"));
                    vente.setDateDebutVente(rs.getString("v_dateDebutVente"));
                    Lieu lieu = new Lieu();
                    lieu.setId(rs.getInt("l_id"));
                    lieu.setVille(rs.getString("l_ville"));
                    vente.setLieu(lieu);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête getLaVente a échoué");
        }
        return vente;
    }

    /**
     * Insère une nouvelle vente.
     * Utilise 'C01' comme categ_code par défaut (Vente aux enchères).
     * La colonne id n'étant pas AUTO_INCREMENT dans le schéma actuel,
     * on calcule MAX(id)+1 pour éviter les conflits.
     */
    public static boolean ajouterVente(Connection cnx, Vente vente) {
        // Calcul du prochain id (la table vente n'a pas AUTO_INCREMENT dans le schéma fourni)
        int nextId = 1;
        String sqlMax = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM vente";
        try (PreparedStatement psMax = cnx.prepareStatement(sqlMax);
             ResultSet rsMax = psMax.executeQuery()) {
            if (rsMax.next()) {
                nextId = rsMax.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO vente (id, nom, dateDebutVente, categ_code, lieu_id) " +
                     "VALUES (?, ?, ?, 'C01', ?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setString(2, vente.getNom());
            ps.setString(3, vente.getDateDebutVente());
            ps.setInt(4, vente.getLieu().getId());

            int result = ps.executeUpdate();
            if (result == 1) {
                vente.setId(nextId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de la vente");
        }
        return false;
    }
    

    /**
     * Récupère les lots d'une vente avec le cheval associé.
     */
    public static ArrayList<Lot> getLesLots(Connection cnx, int idVente) {
        ArrayList<Lot> lesLots = new ArrayList<>();
        String sql = "SELECT l.id AS lot_id, l.prixDepart AS lot_prixDepart, " +
                     "c.id AS c_id, c.nom AS c_nom " +
                     "FROM lot l " +
                     "INNER JOIN cheval c ON l.cheval_id = c.id " +
                     "WHERE l.vente_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, idVente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cheval cheval = new Cheval();
                    cheval.setId(rs.getInt("c_id"));
                    cheval.setNom(rs.getString("c_nom"));

                    Lot lot = new Lot();
                    lot.setId(rs.getInt("lot_id"));
                    lot.setPrixDepart(rs.getString("lot_prixDepart"));
                    lot.setCheval(cheval);

                    lesLots.add(lot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête getLesLots a échoué");
        }
        return lesLots;
    }
}