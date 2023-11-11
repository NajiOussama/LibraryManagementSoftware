package com.example.libraryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmprunterController {

    @FXML
    private TextField StudentNumber;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField isbn;

    @FXML
    private TextField password;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button saveButton;

    @FXML
    private TextField titre;


    @FXML
    void emprunter(ActionEvent event) {
        String mstudnumber = StudentNumber.getText();
        String mpass = password.getText();
        String mtit = titre.getText();
        String misbn = isbn.getText();
        Alert alert;

        if (mstudnumber.isEmpty() || mpass.isEmpty() || mtit.isEmpty() || misbn.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides.");
            alert.showAndWait();
        } else {
            Connection connection = SqlController.connectDB();

            if (connection != null) {
                try {
                    // Vérifier si le livre est disponible
                    String checkAvailabilityQuery = "SELECT Disponible FROM Livre WHERE ISBN = ?";
                    PreparedStatement checkAvailabilityStatement = connection.prepareStatement(checkAvailabilityQuery);
                    checkAvailabilityStatement.setString(1, misbn);
                    var availabilityResult = checkAvailabilityStatement.executeQuery();

                    if (availabilityResult.next() && availabilityResult.getBoolean("Disponible")) {
                        // Mettre à jour la disponibilité du livre
                        String updateAvailabilityQuery = "UPDATE Livre SET Disponible = 0 WHERE ISBN = ?";
                        PreparedStatement updateAvailabilityStatement = connection.prepareStatement(updateAvailabilityQuery);
                        updateAvailabilityStatement.setString(1, misbn);
                        updateAvailabilityStatement.executeUpdate();

                        // Insérer l'emprunt dans la table Emprunts
                        String insertEmpruntQuery = "INSERT INTO Emprunts (livre_id, student_id) " +
                                "VALUES (?, (SELECT ID FROM student WHERE StudentNumber = ?))";
                        PreparedStatement insertEmpruntStatement = connection.prepareStatement(insertEmpruntQuery);
                        insertEmpruntStatement.setString(1, misbn);
                        insertEmpruntStatement.setString(2, mstudnumber);

                        // Exécuter la requête
                        int rowsAffected = insertEmpruntStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Admin Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Livre emprunté avec succès.");
                            alert.showAndWait();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Message d'erreur");
                            alert.setHeaderText(null);
                            alert.setContentText("Échec de l'emprunt du livre.");
                            alert.showAndWait();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Message d'erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Le livre n'est pas disponible.");
                        alert.showAndWait();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec de l'emprunt du livre.");
                    alert.showAndWait();
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
        }
    }




    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
