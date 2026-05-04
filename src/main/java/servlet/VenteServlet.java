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
            System.out.println("INIT VenteServlet, schema=" + cnx.getSchema());
        } catch (SQLException ex) {
            Logger.getLogger(VenteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ─────────────────────────── GET ───────────────────────────

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String path = request.getPathInfo();
        System.out.println("[VenteServlet GET] path=" + path);

        // ── Liste ──
        if ("/list".equals(path)) {
            ArrayList<Vente> lesVentes = DaoVente.getLesVentes(cnx);
            request.setAttribute("pLesVentes", lesVentes);
            getServletContext().getRequestDispatcher("/WEB-INF/views/vente/list.jsp")
                    .forward(request, response);
            return;
        }

        // ── Détail ──
        if ("/show".equals(path)) {
            try {
                int idVente = Integer.parseInt(request.getParameter("idVente"));
                Vente laVente = DaoVente.getLaVente(cnx, idVente);
                if (laVente != null) {
                    ArrayList<Lot> lesLots = DaoVente.getLesLots(cnx, idVente);
                    request.setAttribute("pLaVente", laVente);
                    request.setAttribute("plesLots", lesLots);
                    getServletContext().getRequestDispatcher("/WEB-INF/views/vente/show.jsp")
                            .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
            }
            return;
        }

        // ── Formulaire ajout ──
        if ("/add".equals(path)) {
            chargerFormulaireAdd(request, response);
            return;
        }

        // ── Formulaire modification ──
        if ("/edit".equals(path)) {
            try {
                int idVente = Integer.parseInt(request.getParameter("idVente"));
                Vente laVente = DaoVente.getLaVente(cnx, idVente);
                if (laVente == null) {
                    response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
                    return;
                }
                request.setAttribute("pLaVente",  laVente);
                request.setAttribute("pLesLieux", DaoLieu.getLesLieux(cnx));
                getServletContext().getRequestDispatcher("/WEB-INF/views/vente/edit.jsp")
                        .forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/vente-servlet/list");
            }
            return;
        }
    }

    // ─────────────────────────── POST ───────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        System.out.println("[VenteServlet POST] path=" + path);

        // ── Ajout ──
        if ("/add".equals(path)) {
            try {
                Vente vente = extraireVente(request);
                if (DaoVente.ajouterVente(cnx, vente)) {
                    response.sendRedirect(request.getContextPath()
                            + "/vente-servlet/show?idVente=" + vente.getId());
                } else {
                    throw new Exception("Erreur lors de l'enregistrement en base de données.");
                }
            } catch (Exception e) {
                request.setAttribute("message", "Erreur : " + e.getMessage());
                chargerFormulaireAdd(request, response);
            }
            return;
        }

        // ── Modification ──
        if ("/edit".equals(path)) {
            try {
                String idStr = request.getParameter("idVente");
                if (idStr == null || idStr.isEmpty()) throw new Exception("Identifiant manquant.");
                int idVente = Integer.parseInt(idStr);

                Vente vente = extraireVente(request);
                vente.setId(idVente);

                if (DaoVente.modifierVente(cnx, vente)) {
                    response.sendRedirect(request.getContextPath()
                            + "/vente-servlet/show?idVente=" + idVente);
                } else {
                    throw new Exception("Erreur lors de la mise à jour en base de données.");
                }
            } catch (Exception e) {
                request.setAttribute("message", "Erreur : " + e.getMessage());
                try {
                    int idVente = Integer.parseInt(request.getParameter("idVente"));
                    request.setAttribute("pLaVente", DaoVente.getLaVente(cnx, idVente));
                } catch (NumberFormatException ex) { /* ignore */ }
                request.setAttribute("pLesLieux", DaoLieu.getLesLieux(cnx));
                getServletContext().getRequestDispatcher("/WEB-INF/views/vente/edit.jsp")
                        .forward(request, response);
            }
            return;
        }
    }

    // ─────────────────────────── Helpers ───────────────────────────

    private void chargerFormulaireAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Lieu> lesLieux = DaoLieu.getLesLieux(cnx);
        request.setAttribute("pLesLieux", lesLieux);
        getServletContext().getRequestDispatcher("/WEB-INF/views/vente/add.jsp")
                .forward(request, response);
    }

    /**
     * Lit les paramètres du formulaire (communs à /add et /edit)
     * et construit un objet Vente. Lève une Exception si données invalides.
     */
    private Vente extraireVente(HttpServletRequest request) throws Exception {
        String nom           = request.getParameter("nom");
        String dateDebutVente = request.getParameter("dateDebutVente");
        String lieuIdStr     = request.getParameter("lieu");

        if (nom == null || nom.trim().isEmpty()) {
            throw new Exception("Le nom de la vente est obligatoire.");
        }
        if (lieuIdStr == null || lieuIdStr.isEmpty()) {
            throw new Exception("Veuillez sélectionner un lieu.");
        }

        Vente vente = new Vente();
        vente.setNom(nom.trim());

        if (dateDebutVente != null && !dateDebutVente.isEmpty()) {
            vente.setDateDebutVente(dateDebutVente);
        }

        Lieu lieu = DaoLieu.getLieuById(cnx, Integer.parseInt(lieuIdStr));
        if (lieu == null) throw new Exception("Le lieu sélectionné n'existe pas.");
        vente.setLieu(lieu);

        return vente;
    }

    public void destroy() { }
}