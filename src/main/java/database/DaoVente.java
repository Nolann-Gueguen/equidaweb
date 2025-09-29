package database;

import java.sql.*;
import java.util.ArrayList;
import model.Vente;
import model.Lieu;

public class DaoVente {

    public static ArrayList<Vente> getLesVentes(Connection cnx) {
        ArrayList<Vente> lesVentes = new ArrayList<>();
        String sql = "SELECT v.id as v_id, v.nom as v_nom, " +
                     "l.id as l_id, l.ville as l_ville " +
                     "FROM vente v INNER JOIN lieu l ON v.lieu_id = l.id";

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

    public static Vente getLaVente(Connection cnx, int idVente) {
        Vente vente = null;
        String sql = "SELECT v.id as v_id, v.nom as v_nom, v.dateDebutVente as v_dateDebutVente," +
                     "l.id as l_id, l.ville as l_ville " +
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

    public static boolean ajouterVente(Connection cnx, Vente vente) {
        String sql = "INSERT INTO vente (nom, lieu_id) VALUES (?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vente.getNom());
            ps.setInt(2, vente.getLieu().getId());

            int result = ps.executeUpdate();

            if (result == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        vente.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de la vente");
        }
        return false;
    }
}
