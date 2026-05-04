<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Vente" %>
<%@ page import="model.Lieu" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Equida - Modifier une vente</title>
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <style>
        body { padding-top: 50px; }
        .special { padding-top: 50px; }
        .form-container {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 20px;
            margin-top: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<%= request.getContextPath() %>/vente-servlet/list">
                Gestion des ventes
            </a>
        </div>
    </div>
</nav>

<div class="container special">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="form-container">

                <%
                    Vente laVente    = (Vente)            request.getAttribute("pLaVente");
                    ArrayList<Lieu> lesLieux = (ArrayList<Lieu>) request.getAttribute("pLesLieux");

                    // Valeurs affichées (priorité au re-POST en cas d'erreur)
                    String nomVal  = request.getParameter("nom")            != null ? request.getParameter("nom")            : (laVente != null ? laVente.getNom() : "");
                    String dateVal = request.getParameter("dateDebutVente") != null ? request.getParameter("dateDebutVente") : (laVente != null && laVente.getDateDebutVente() != null ? laVente.getDateDebutVente() : "");
                    String selLieu = request.getParameter("lieu")           != null ? request.getParameter("lieu")           : (laVente != null && laVente.getLieu() != null ? String.valueOf(laVente.getLieu().getId()) : "");
                %>

                <h2>Modifier la vente : <%= laVente != null ? laVente.getNom() : "" %></h2>

                <% if (request.getAttribute("message") != null) { %>
                    <div class="alert alert-danger"><%= request.getAttribute("message") %></div>
                <% } %>

                <% if (laVente == null) { %>
                    <div class="alert alert-danger">Vente introuvable.</div>
                    <a href="<%= request.getContextPath() %>/vente-servlet/list" class="btn btn-default">Retour</a>
                <% } else { %>

                <form class="form-horizontal"
                      action="<%= request.getContextPath() %>/vente-servlet/edit"
                      method="POST">

                    <!-- ID caché -->
                    <input type="hidden" name="idVente" value="<%= laVente.getId() %>">

                    <!-- Nom -->
                    <div class="form-group">
                        <label for="nom" class="col-sm-3 control-label">Nom *</label>
                        <div class="col-sm-9">
                            <input type="text" name="nom" id="nom" class="form-control"
                                   value="<%= nomVal %>" required>
                        </div>
                    </div>

                    <!-- Date de début -->
                    <div class="form-group">
                        <label for="dateDebutVente" class="col-sm-3 control-label">Date de début</label>
                        <div class="col-sm-9">
                            <input type="date" name="dateDebutVente" id="dateDebutVente" class="form-control"
                                   value="<%= dateVal %>">
                        </div>
                    </div>

                    <!-- Lieu -->
                    <div class="form-group">
                        <label for="lieu" class="col-sm-3 control-label">Lieu *</label>
                        <div class="col-sm-9">
                            <select name="lieu" id="lieu" class="form-control" required>
                                <option value="">— Sélectionnez un lieu —</option>
                                <% if (lesLieux != null) {
                                       for (Lieu l : lesLieux) {
                                           boolean sel = selLieu.equals(String.valueOf(l.getId()));
                                %>
                                    <option value="<%= l.getId() %>" <%= sel ? "selected" : "" %>>
                                        <%= l.getVille() %>
                                    </option>
                                <% }} %>
                            </select>
                        </div>
                    </div>

                    <!-- Boutons -->
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="submit" class="btn btn-primary">
                                <span class="glyphicon glyphicon-floppy-disk"></span> Enregistrer les modifications
                            </button>
                            <a href="<%= request.getContextPath() %>/vente-servlet/show?idVente=<%= laVente.getId() %>"
                               class="btn btn-default">
                                <span class="glyphicon glyphicon-remove"></span> Annuler
                            </a>
                        </div>
                    </div>

                </form>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
