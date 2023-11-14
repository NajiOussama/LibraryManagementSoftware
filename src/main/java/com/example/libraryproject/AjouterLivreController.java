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
import java.sql.ResultSet;
import java.sql.SQLException;

public class AjouterLivreController {

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
    void addBook(ActionEvent event) {
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
            return; // Sortez de la méthode si les champs sont vides
        }

        // Vérifiez si le livre avec cet ISBN existe déjà
        if (isBookExists(isbn)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("Un livre avec cet ISBN existe déjà.");
            alert.showAndWait();
            return; // Sortez de la méthode si le livre existe déjà
        }

        // Autres validations


        // Établissez une connexion à la base de données
        Connection connection = SqlController.connectDB();

        if (connection != null ) {
            try {
                // Créez la requête SQL d'insertion
                String insertQuery = "INSERT INTO Livre (Titre, Auteur, ISBN) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, titre);
                preparedStatement.setString(2, auteur);
                preparedStatement.setString(3, isbn);

                // Exécutez la requête
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Livre inséré avec succès dans la base de données.");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec de l'insertion du livre.");
                    alert.showAndWait();
                }

                // Fermez la connexion et le statement
                preparedStatement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Échec de l'insertion du livre.");
                alert.showAndWait();
            }
        }
    }

    private boolean isBookExists(String isbn) {
        // Vérifiez si le livre avec cet ISBN existe déjà dans la base de données
        Connection connection = SqlController.connectDB();

        if (connection != null) {
            try {
                String checkBookQuery = "SELECT COUNT(*) FROM Livre WHERE ISBN = ?";
                PreparedStatement checkBookStatement = connection.prepareStatement(checkBookQuery);
                checkBookStatement.setString(1, isbn);

                ResultSet result = checkBookStatement.executeQuery();

                if (result.next() && result.getInt(1) > 0) {
                    // Le livre existe déjà
                    return true;
                }

                // Le livre n'existe pas encore
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }


}
