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
        cnx = (Connection) servletContext.getAttribute("connection");
        try {
            System.out.println("INIT ChevalServlet, schema=" + cnx.getSchema());
        } catch (SQLException ex) {
            Logger.getLogger(ChevalServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ─────────────────────────── GET ───────────────────────────

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String path = request.getPathInfo();
        System.out.println("[ChevalServlet GET] path=" + path);

        // ── Liste ──
        if ("/list".equals(path)) {
            ArrayList<Cheval> lesChevaux = DaoCheval.getLesChevaux(cnx);
            request.setAttribute("pLesChevaux", lesChevaux);
            getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/list.jsp")
                    .forward(request, response);
            return;
        }

        // ── Détail ──
        if ("/show".equals(path)) {
            try {
                int idCheval = Integer.parseInt(request.getParameter("idCheval"));
                Cheval leCheval = DaoCheval.getLeCheval(cnx, idCheval);
                if (leCheval != null) {
                    request.setAttribute("pLeCheval", leCheval);
                    getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/show.jsp")
                            .forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
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
                int idCheval = Integer.parseInt(request.getParameter("idCheval"));
                Cheval leCheval = DaoCheval.getLeCheval(cnx, idCheval);
                if (leCheval == null) {
                    response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
                    return;
                }
                request.setAttribute("pLeCheval",   leCheval);
                request.setAttribute("pLesRaces",   DaoRace.getLesRaces(cnx));
                request.setAttribute("pLesChevaux", DaoCheval.getLesChevaux(cnx));
                getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/edit.jsp")
                        .forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/cheval-servlet/list");
            }
            return;
        }
    }

    // ─────────────────────────── POST ───────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();
        System.out.println("[ChevalServlet POST] path=" + path);

        // ── Ajout ──
        if ("/add".equals(path)) {
            try {
                Cheval cheval = extraireCheval(request);
                if (DaoCheval.ajouterCheval(cnx, cheval)) {
                    response.sendRedirect(request.getContextPath()
                            + "/cheval-servlet/show?idCheval=" + cheval.getId());
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
                String idStr = request.getParameter("idCheval");
                if (idStr == null || idStr.isEmpty()) throw new Exception("Identifiant manquant.");
                int idCheval = Integer.parseInt(idStr);

                Cheval cheval = extraireCheval(request);
                cheval.setId(idCheval);

                if (DaoCheval.modifierCheval(cnx, cheval)) {
                    response.sendRedirect(request.getContextPath()
                            + "/cheval-servlet/show?idCheval=" + idCheval);
                } else {
                    throw new Exception("Erreur lors de la mise à jour en base de données.");
                }
            } catch (Exception e) {
                request.setAttribute("message", "Erreur : " + e.getMessage());
                // Recharger le formulaire d'édition avec les listes
                try {
                    int idCheval = Integer.parseInt(request.getParameter("idCheval"));
                    request.setAttribute("pLeCheval",   DaoCheval.getLeCheval(cnx, idCheval));
                } catch (NumberFormatException ex) { /* ignore */ }
                request.setAttribute("pLesRaces",   DaoRace.getLesRaces(cnx));
                request.setAttribute("pLesChevaux", DaoCheval.getLesChevaux(cnx));
                getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/edit.jsp")
                        .forward(request, response);
            }
            return;
        }
    }

    // ─────────────────────────── Helpers ───────────────────────────

    /** Charge le formulaire /add avec les races et la liste de tous les chevaux. */
    private void chargerFormulaireAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Race>   lesRaces   = DaoRace.getLesRaces(cnx);
        ArrayList<Cheval> lesChevaux = DaoCheval.getLesChevaux(cnx);

        System.out.println("[ChevalServlet] getLesRaces   -> " + lesRaces.size()   + " races");
        System.out.println("[ChevalServlet] getLesChevaux -> " + lesChevaux.size() + " chevaux");

        request.setAttribute("pLesRaces",   lesRaces);
        request.setAttribute("pLesChevaux", lesChevaux);
        getServletContext().getRequestDispatcher("/WEB-INF/views/cheval/add.jsp")
                
                .forward(request, response);
    }

    /**
     * Lit les paramètres du formulaire (communs à /add et /edit)
     * et construit un objet Cheval. Lève une Exception si données invalides.
     */
    private Cheval extraireCheval(HttpServletRequest request) throws Exception {
        String nom       = request.getParameter("nom");
        String dateStr   = request.getParameter("dateNaissance");
        String raceIdStr = request.getParameter("race");
        String pereIdStr = request.getParameter("pereId");
        String mereIdStr = request.getParameter("mereId");

        if (nom == null || nom.trim().isEmpty()) {
            throw new Exception("Le nom du cheval est obligatoire.");
        }
        if (raceIdStr == null || raceIdStr.isEmpty()) {
            throw new Exception("Veuillez sélectionner une race.");
        }

        Cheval cheval = new Cheval();
        cheval.setNom(nom.trim());

        // Date
        if (dateStr != null && !dateStr.isEmpty()) {
            try {
                cheval.setDateNaissance(LocalDate.parse(dateStr));
            } catch (DateTimeParseException e) {
                throw new Exception("Format de date invalide (attendu : AAAA-MM-JJ).");
            }
        }

        // Race
        Race race = DaoRace.getRaceById(cnx, Integer.parseInt(raceIdStr));
        if (race == null) throw new Exception("La race sélectionnée n'existe pas.");
        cheval.setRace(race);

        // Père
        if (pereIdStr != null && !pereIdStr.isEmpty()) {
            Cheval pere = new Cheval();
            pere.setId(Integer.parseInt(pereIdStr));
            cheval.setPere(pere);
        }

        // Mère
        if (mereIdStr != null && !mereIdStr.isEmpty()) {
            Cheval mere = new Cheval();
            mere.setId(Integer.parseInt(mereIdStr));
            cheval.setMere(mere);
        }

        return cheval;
    }

    public void destroy() { }
}