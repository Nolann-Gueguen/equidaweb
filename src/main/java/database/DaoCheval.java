package database;

import model.Cheval;
import model.Race;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import model.ChevalCourse;
import model.Course;

public class DaoCheval {

    static PreparedStatement requeteSql = null;
    static ResultSet resultatRequete = null;

    /**
     * Récupère tous les chevaux triés par nom.
     * LEFT JOIN sur race pour inclure les chevaux sans race (important pour les listes père/mère).
     */
    public static ArrayList<Cheval> getLesChevaux(Connection cnx) {
        ArrayList<Cheval> lesChevaux = new ArrayList<>();
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT c.id AS c_id, c.nom AS c_nom, " +
                "r.id AS r_id, r.nom AS r_nom " +
                "FROM cheval c " +
                "LEFT JOIN race r ON c.race_id = r.id " +
                "ORDER BY c.nom"
            );
            resultatRequete = requeteSql.executeQuery();
            while (resultatRequete.next()) {
                Cheval c = new Cheval();
                c.setId(resultatRequete.getInt("c_id"));
                c.setNom(resultatRequete.getString("c_nom"));
                int raceId = resultatRequete.getInt("r_id");
                if (!resultatRequete.wasNull()) {
                    Race r = new Race();
                    r.setId(raceId);
                    r.setNom(resultatRequete.getString("r_nom"));
                    c.setRace(r);
                }
                lesChevaux.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête getLesChevaux a généré une exception SQL");
        }
        return lesChevaux;
    }

    /**
     * Récupère le détail complet d'un cheval (race, père, mère, courses).
     */
    public static Cheval getLeCheval(Connection cnx, int idCheval) {
        Cheval cheval = null;
        try {
            requeteSql = cnx.prepareStatement(
                "SELECT " +
                "c.id AS c_id, c.nom AS c_nom, c.dateNaissance AS c_dateNaissance, " +
                "c.pere_id, c.mere_id, " +
                "pere.nom AS pere_nom, mere.nom AS mere_nom, " +
                "r.id AS r_id, r.nom AS r_nom, " +
                "cc.position AS cc_position, cc.temps AS cc_temps, " +
                "co.nom AS co_nom, co.dateCourse AS co_dateCourse " +
                "FROM cheval c " +
                "LEFT JOIN race r ON c.race_id = r.id " +
                "LEFT JOIN cheval pere ON pere.id = c.pere_id " +
                "LEFT JOIN cheval mere ON mere.id = c.mere_id " +
                "LEFT JOIN cheval_course cc ON cc.cheval_id = c.id " +
                "LEFT JOIN course co ON co.id = cc.course_id " +
                "WHERE c.id = ?;"
            );
            requeteSql.setInt(1, idCheval);
            resultatRequete = requeteSql.executeQuery();

            while (resultatRequete.next()) {
                if (cheval == null) {
                    cheval = new Cheval();
                    cheval.setId(resultatRequete.getInt("c_id"));
                    cheval.setNom(resultatRequete.getString("c_nom"));

                    java.sql.Date sqlDate = resultatRequete.getDate("c_dateNaissance");
                    if (sqlDate != null) {
                        cheval.setDateNaissance(sqlDate.toLocalDate());
                    }

                    int raceId = resultatRequete.getInt("r_id");
                    if (!resultatRequete.wasNull()) {
                        Race race = new Race();
                        race.setId(raceId);
                        race.setNom(resultatRequete.getString("r_nom"));
                        cheval.setRace(race);
                    }

                    int idPere = resultatRequete.getInt("pere_id");
                    if (!resultatRequete.wasNull()) {
                        Cheval pere = new Cheval();
                        pere.setId(idPere);
                        pere.setNom(resultatRequete.getString("pere_nom"));
                        cheval.setPere(pere);
                    }

                    int idMere = resultatRequete.getInt("mere_id");
                    if (!resultatRequete.wasNull()) {
                        Cheval mere = new Cheval();
                        mere.setId(idMere);
                        mere.setNom(resultatRequete.getString("mere_nom"));
                        cheval.setMere(mere);
                    }
                }

                if (resultatRequete.getString("co_nom") != null) {
                    Course course = new Course();
                    course.setNom(resultatRequete.getString("co_nom"));
                    course.setDateCourse(resultatRequete.getString("co_dateCourse"));
                    ChevalCourse cc = new ChevalCourse();
                    cc.setPosition(resultatRequete.getInt("cc_position"));
                    cc.setTemps(resultatRequete.getString("cc_temps"));
                    cc.setCourse(course);
                    cc.setCheval(cheval);
                    cheval.addCheval(cc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête getLeCheval a généré une exception SQL");
        }
        return cheval;
    }

    /**
     * Insère un nouveau cheval (avec père et mère optionnels).
     */
    public static boolean ajouterCheval(Connection cnx, Cheval cheval) {
        try {
            requeteSql = cnx.prepareStatement(
                "INSERT INTO cheval (nom, dateNaissance, race_id, pere_id, mere_id) VALUES (?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );
            requeteSql.setString(1, cheval.getNom());

            if (cheval.getDateNaissance() != null) {
                requeteSql.setDate(2, java.sql.Date.valueOf(cheval.getDateNaissance()));
            } else {
                requeteSql.setNull(2, Types.DATE);
            }

            requeteSql.setInt(3, cheval.getRace().getId());

            if (cheval.getPere() != null) {
                requeteSql.setInt(4, cheval.getPere().getId());
            } else {
                requeteSql.setNull(4, Types.INTEGER);
            }

            if (cheval.getMere() != null) {
                requeteSql.setInt(5, cheval.getMere().getId());
            } else {
                requeteSql.setNull(5, Types.INTEGER);
            }

            int result = requeteSql.executeUpdate();
            if (result == 1) {
                ResultSet rs = requeteSql.getGeneratedKeys();
                if (rs.next()) {
                    cheval.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête ajouterCheval a généré une exception SQL");
        }
        return false;
    }

    /**
     * Met à jour un cheval existant (nom, date, race, père, mère).
     */
    public static boolean modifierCheval(Connection cnx, Cheval cheval) {
        try {
            requeteSql = cnx.prepareStatement(
                "UPDATE cheval SET nom = ?, dateNaissance = ?, race_id = ?, pere_id = ?, mere_id = ? WHERE id = ?"
            );
            requeteSql.setString(1, cheval.getNom());

            if (cheval.getDateNaissance() != null) {
                requeteSql.setDate(2, java.sql.Date.valueOf(cheval.getDateNaissance()));
            } else {
                requeteSql.setNull(2, Types.DATE);
            }

            requeteSql.setInt(3, cheval.getRace().getId());

            if (cheval.getPere() != null) {
                requeteSql.setInt(4, cheval.getPere().getId());
            } else {
                requeteSql.setNull(4, Types.INTEGER);
            }

            if (cheval.getMere() != null) {
                requeteSql.setInt(5, cheval.getMere().getId());
            } else {
                requeteSql.setNull(5, Types.INTEGER);
            }

            requeteSql.setInt(6, cheval.getId());

            return requeteSql.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("La requête modifierCheval a généré une exception SQL");
        }
        return false;
    }
}