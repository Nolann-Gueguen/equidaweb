<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Vente" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Equida - Détails de la vente</title>
        <link rel="stylesheet" 
              href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
              integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
              crossorigin="anonymous">
        <style>
            body { 
                padding-top: 50px; 
            }
            .special { 
                padding-top: 50px; 
            }
            .form-container {
                background-color: #f8f9fa;
                border-radius: 5px;
                padding: 20px;
                margin-top: 20px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .detail-row {
                margin-bottom: 15px;
            }
            .detail-label {
                font-weight: bold;
                color: #555;
            }
            .detail-value {
                padding-top: 7px;
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
                            Vente laVente = (Vente)request.getAttribute("pLaVente");
                            if(laVente != null) {
                        %>
                            <h2>Détails de la vente : <%= laVente.getNom() %></h2>
                            
                            <div class="row detail-row">
                                <div class="col-sm-3 detail-label">Identifiant</div>
                                <div class="col-sm-9 detail-value"><%= laVente.getId() %></div>
                            </div>

                            <div class="row detail-row">
                                <div class="col-sm-3 detail-label">Nom</div>
                                <div class="col-sm-9 detail-value"><%= laVente.getNom() %></div>
                            </div>

                            <div class="row detail-row">
                                <div class="col-sm-3 detail-label">Date de début</div>
                                <div class="col-sm-9 detail-value">
                                    <%= laVente.getDateDebutVente() != null ? laVente.getDateDebutVente() : "Non renseignée" %>
                                </div>
                            </div>

                            <div class="row detail-row">
                                <div class="col-sm-3 detail-label">Lieu</div>
                                <div class="col-sm-9 detail-value">
                                    <%= laVente.getLieu() != null ? laVente.getLieu().getVille() : "Non renseignée" %>
                                </div>
                            </div>

                            <div class="row" style="margin-top: 30px;">
                                <div class="col-sm-offset-3 col-sm-9">
                                    <a href="<%= request.getContextPath() %>/vente-servlet/list" class="btn btn-default">
                                        <span class="glyphicon glyphicon-arrow-left"></span> Retour à la liste
                                    </a>
                                    <!-- Vous pouvez ajouter d'autres boutons ici, comme Modifier ou Supprimer -->
                                </div>
                            </div>
                        <% } else { %>
                            <div class="alert alert-danger">
                                La vente demandé n'existe pas.
                            </div>
                            <a href="<%= request.getContextPath() %>/vente-servlet/list" class="btn btn-default">
                                <span class="glyphicon glyphicon-arrow-left"></span> Retour à la liste
                            </a>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Bootstrap JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </body>
</html>