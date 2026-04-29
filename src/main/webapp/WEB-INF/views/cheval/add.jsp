<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Race" %>
<%@ page import="model.Cheval" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Equida - Ajouter un cheval</title>
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
            <a class="navbar-brand" href="<%= request.getContextPath() %>/cheval-servlet/list">
                Gestion des chevaux
            </a>
        </div>
    </div>
</nav>

<div class="container special">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="form-container">
                <h2>Ajouter un nouveau cheval</h2>

                <% if (request.getAttribute("message") != null) { %>
                    <div class="alert alert-danger"><%= request.getAttribute("message") %></div>
                <% } %>

                <%
                    ArrayList<Race>   lesRaces   = (ArrayList<Race>)   request.getAttribute("pLesRaces");
                    ArrayList<Cheval> lesChevaux = (ArrayList<Cheval>) request.getAttribute("pLesChevaux");
                    String selRace = request.getParameter("race");
                    String selPere = request.getParameter("pereId");
                    String selMere = request.getParameter("mereId");
                %>

                <form class="form-horizontal"
                      action="<%= request.getContextPath() %>/cheval-servlet/add"
                      method="POST">

                    <!-- Nom -->
                    <div class="form-group">
                        <label for="nom" class="col-sm-3 control-label">Nom *</label>
                        <div class="col-sm-9">
                            <input type="text" name="nom" id="nom" class="form-control"
                                   placeholder="Ex : Eclipse"
                                   value="<%= request.getParameter("nom") != null ? request.getParameter("nom") : "" %>"
                                   required>
                        </div>
                    </div>

                    <!-- Date de naissance -->
                    <div class="form-group">
                        <label for="dateNaissance" class="col-sm-3 control-label">Date de naissance</label>
                        <div class="col-sm-9">
                            <input type="date" name="dateNaissance" id="dateNaissance" class="form-control"
                                   value="<%= request.getParameter("dateNaissance") != null ? request.getParameter("dateNaissance") : "" %>">
                        </div>
                    </div>

                    <!-- Race -->
                    <div class="form-group">
                        <label for="race" class="col-sm-3 control-label">Race *</label>
                        <div class="col-sm-9">
                            <select name="race" id="race" class="form-control" required>
                                <option value="">— Sélectionnez une race —</option>
                                <% if (lesRaces != null) {
                                       for (Race race : lesRaces) {
                                           boolean sel = selRace != null && selRace.equals(String.valueOf(race.getId()));
                                %>
                                    <option value="<%= race.getId() %>" <%= sel ? "selected" : "" %>>
                                        <%= race.getNom() %>
                                    </option>
                                <% }} %>
                            </select>
                        </div>
                    </div>

                    <hr>
                    <h4>Filiation <small>(optionnel)</small></h4>

                    <!-- Père -->
                    <div class="form-group">
                        <label for="pereId" class="col-sm-3 control-label">Père</label>
                        <div class="col-sm-9">
                            <select name="pereId" id="pereId" class="form-control">
                                <option value="">— Non renseigné —</option>
                                <% if (lesChevaux != null) {
                                       for (Cheval c : lesChevaux) {
                                           boolean sel = selPere != null && selPere.equals(String.valueOf(c.getId()));
                                %>
                                    <option value="<%= c.getId() %>" <%= sel ? "selected" : "" %>>
                                        <%= c.getNom() %>
                                    </option>
                                <% }} %>
                            </select>
                        </div>
                    </div>

                    <!-- Mère -->
                    <div class="form-group">
                        <label for="mereId" class="col-sm-3 control-label">Mère</label>
                        <div class="col-sm-9">
                            <select name="mereId" id="mereId" class="form-control">
                                <option value="">— Non renseigné —</option>
                                <% if (lesChevaux != null) {
                                       for (Cheval c : lesChevaux) {
                                           boolean sel = selMere != null && selMere.equals(String.valueOf(c.getId()));
                                %>
                                    <option value="<%= c.getId() %>" <%= sel ? "selected" : "" %>>
                                        <%= c.getNom() %>
                                    </option>
                                <% }} %>
                            </select>
                        </div>
                    </div>

                    <!-- Boutons -->
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="submit" class="btn btn-primary">
                                <span class="glyphicon glyphicon-plus"></span> Ajouter
                            </button>
                            <a href="<%= request.getContextPath() %>/cheval-servlet/list" class="btn btn-default">
                                <span class="glyphicon glyphicon-remove"></span> Annuler
                            </a>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>
</html>
