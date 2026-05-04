package servlet;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.DaoVente;
import database.DaoLieu;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Vente;
import model.Lieu;
import model.Lot;

@WebServlet(name = "venteServlet", value = "/vente-servlet/*")
public class VenteServlet extends HttpServlet {

    Connection cnx;

    @Override
    public void init() {
        ServletContext servletContext = getServletContext();
        cnx = (Connection) servletContext.getAttribute("connection");
        try {
            System.out.println("INIT SERVLET=" + cnx.getSchema());
        } catch (SQLException ex) {
            Logger.getLogger(VenteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String path = request.getPathInfo();
        System.out.println("PathInfo: " + path);

        if ("/list".equals(path)) {
            ArrayList<Vente> lesVentes = DaoVente.getLesVentes(cnx);
            request.setAttribute("pLesVentes", lesVentes);
            getServletContext().getRequestDispatcher("/WEB-INF/views/vente/list.jsp")
                    .forward(request, response);
        }

        if ("/show".equals(path)) {
            try {
                int idVente = Integer.parseInt(request.getParameter("idVente"));
                Vente laVente = DaoVente.getLaVente(cnx, idVente);
                if (laVente != null) {
                    request.setAttribute("pLaVente", laVente);
                    ArrayList<Lot> lesLots = DaoVente.getLesLots(cnx, idVente);
                    request.setAttribute("plesLots", lesLots);
                    getServletContext().getRequestDispatcher("/WEB-INF/views/vente/show.jsp")
                            .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
            }
        }
        

        if ("/add".equals(path)) {
            ArrayList<Lieu> lesLieux = DaoLieu.getLesLieux(cnx);
            request.setAttribute("pLesLieux", lesLieux);
            getServletContext().getRequestDispatcher("/WEB-INF/views/vente/add.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if ("/add".equals(path)) {
            try {
                // ── Récupération des champs ──
                String nom            = request.getParameter("nom");
                String dateDebutVente = request.getParameter("dateDebutVente");
                String lieuIdStr      = request.getParameter("lieuId");

                // Validations basiques
                if (nom == null || nom.trim().isEmpty()) {
                    throw new Exception("Le nom de la vente est obligatoire.");
                }
                if (dateDebutVente == null || dateDebutVente.trim().isEmpty()) {
                    throw new Exception("La date de début est obligatoire.");
                }
                if (lieuIdStr == null || lieuIdStr.isEmpty()) {
                    throw new Exception("Veuillez sélectionner un lieu.");
                }

                int lieuId = Integer.parseInt(lieuIdStr);

                // ── Récupération du lieu ──
                Lieu lieu = DaoLieu.getLieuById(cnx, lieuId);
                if (lieu == null) {
                    throw new Exception("Le lieu sélectionné n'existe pas.");
                }

                // ── Construction de l'objet Vente ──
                Vente nouvelleVente = new Vente();
                nouvelleVente.setNom(nom.trim());
                nouvelleVente.setDateDebutVente(dateDebutVente.trim());
                nouvelleVente.setLieu(lieu);

                // ── Insertion en base ──
                if (DaoVente.ajouterVente(cnx, nouvelleVente)) {
                    response.sendRedirect(request.getContextPath()
                            + "/vente-servlet/show?idVente=" + nouvelleVente.getId());
                } else {
                    throw new Exception("Erreur lors de l'enregistrement de la vente en base de données.");
                }

            } catch (NumberFormatException e) {
                request.setAttribute("message", "Erreur : identifiant de lieu invalide.");
                rechargerFormulaire(request, response);
            } catch (Exception e) {
                request.setAttribute("message", "Erreur : " + e.getMessage());
                rechargerFormulaire(request, response);
            }
        }
        
    }

    /** Recharge le formulaire d'ajout avec la liste des lieux. */
    private void rechargerFormulaire(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pLesLieux", DaoLieu.getLesLieux(cnx));
        getServletContext().getRequestDispatcher("/WEB-INF/views/vente/add.jsp")
                .forward(request, response);
    }
    

    public void destroy() { }
}