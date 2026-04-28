package servlet;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import database.DaoCheval;
import database.DaoRace;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Cheval;
import model.Race;

@WebServlet(name = "chevalServlet", value = "/cheval-servlet/*")
public class ChevalServlet extends HttpServlet {

    Connection cnx;
    
    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        cnx = (Connection)servletContext.getAttribute("connection");
        try {
            System.out.println("INIT SERVLET=" + cnx.getSchema());
        } catch (SQLException ex) {
            Logger.getLogger(ChevalServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getPathInfo();
        System.out.println("PathInfo: " + path);

            // On récupère la fin de l'URL
        String action = request.getServletPath();
    
        // CAS 1 : On demande la suppression
        // Si l'URL finit par /supprimer
        if ("/supprimer".equals(path)) {

            // 1. Récupérer l'ID (Assure-toi que dans ta JSP le paramètre s'appelle bien "id")
            String idStr = request.getParameter("id");

            if (idStr != null) {
                int id = Integer.parseInt(idStr);

                // 2. Appeler le DAO (On utilise la connexion 'cnx' de la classe, déjà init)
                DaoCheval.supprimerCheval(cnx, id);
            }

            // 3. Rediriger vers la liste (on remonte d'un cran)
            response.sendRedirect("list");
            return; // Important pour arrêter l'exécution ici
        }
    
        if ("/list".equals(path)) {
            ArrayList<Cheval> lesChevaux = DaoCheval.getLesChevaux(cnx);
            request.setAttribute("pLesChevaux", lesChevaux);
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/list.jsp").forward(request, response);
        }
        if ("/show".equals(path)) {
            try {
                int idCheval = Integer.parseInt(request.getParameter("idCheval"));
                Cheval leCheval = DaoCheval.getLeCheval(cnx, idCheval);

                if (leCheval != null) {
                    request.setAttribute("pLeCheval", leCheval);
                    this.getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/show.jsp").forward(request, response);
                } else {
                    // Correction : "/cheval-servlet/lister" → "/cheval-servlet/list"
                    response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : l'id du cheval n'est pas un nombre valide");
                // Correction : "/cheval-servlet/lister" → "/cheval-servlet/list"
                response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
            }
        }

        if ("/add".equals(path)) {
            ArrayList<Race> lesRaces = DaoRace.getLesRaces(cnx);
            request.setAttribute("pLesRaces", lesRaces);
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/add.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if ("/add".equals(path)) {
            try {
                // Récupération des données du formulaire
                String nom = request.getParameter("nom");
                String dateNaissance = request.getParameter("dateNaissance");
                int raceId = Integer.parseInt(request.getParameter("race"));

                // Création d'un nouveau cheval
                Cheval nouveauCheval = new Cheval();
                nouveauCheval.setNom(nom);

                // Gestion de la date de naissance
                if (dateNaissance != null && !dateNaissance.isEmpty()) {
                    try {
                        nouveauCheval.setDateNaissance(LocalDate.parse(dateNaissance)); // <-- Conversion String vers LocalDate
                    } catch (DateTimeParseException e) {
                        throw new Exception("Le format de la date de naissance est invalide.");
                    }
                }

                // Récupération et attribution de la race
                Race race = DaoRace.getRaceById(cnx, raceId);
                if (race != null) {
                    nouveauCheval.setRace(race);
                } else {
                    throw new Exception("La race sélectionnée n'existe pas");
                }

                // Tentative d'ajout en base de données
                if (DaoCheval.ajouterCheval(cnx, nouveauCheval)) {
                    // Redirection vers la page de consultation du cheval nouvellement créé
                    response.sendRedirect(request.getContextPath() + "/cheval-servlet/show?idCheval=" + nouveauCheval.getId());
                } else {
                    throw new Exception("Erreur lors de l'enregistrement du cheval");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("message", "Erreur : la race sélectionnée n'est pas valide");
                request.setAttribute("pLesRaces", DaoRace.getLesRaces(cnx));
                this.getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/add.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("message", "Erreur : " + e.getMessage());
                request.setAttribute("pLesRaces", DaoRace.getLesRaces(cnx));
                this.getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/add.jsp").forward(request, response);
            }
        }
        
    }

    public void destroy() {
    }
}