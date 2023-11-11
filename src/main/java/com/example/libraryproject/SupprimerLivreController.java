package com.example.libraryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupprimerLivreController {

    @FXML
    private TextField Auteur;

    @FXML
    private TextField ISBN;

    @FXML
    private TextField Titre;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    boolean isNumeric(String str) {
        try {
            Double.parseDouble(str); // Essayez de convertir la chaîne en double
            return true; // Si la conversion réussit, la chaîne est un nombre
        } catch (NumberFormatException e) {
            return false; // Si une exception est levée, la chaîne n'est pas un nombre
        }
    }

    @FXML
    void removeBook(ActionEvent event) {
        // Récupérez les valeurs des champs Titre, Auteur et ISBN
        String titre = Titre.getText();
        String auteur = Auteur.getText();
        String isbn = ISBN.getText();
        Alert alert;
        // Vérifiez que les champs ne sont pas vides
        if (titre.isEmpty() || auteur.isEmpty() || isbn.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides.");
            alert.showAndWait();
        }

        if (!isNumeric(isbn) && !isbn.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("L'ISBN doit être un nombre.");
            alert.showAndWait();
        }

        // Établissez une connexion à la base de données
        Connection connection = SqlController.connectDB();

        if (connection != null && isNumeric(isbn)) {
            try {
                // Créez la requête SQL d'insertion
                String deleteQuery = "DELETE FROM Livre WHERE Titre = ? AND Auteur = ? AND ISBN = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, titre);
                preparedStatement.setString(2, auteur);
                preparedStatement.setString(3, isbn);


                // Exécutez la requête
                int rowsAffected = preparedStatement.executeUpdate();


                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Livre supprimé avec succès dans la base de données.");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec de la suppression du livre.");
                    alert.showAndWait();
                }


            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Échec de la suppression du livre.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


}
