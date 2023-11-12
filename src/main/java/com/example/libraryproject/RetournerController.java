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
import java.sql.ResultSet;
import java.sql.SQLException;

public class RetournerController {

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
    void retourner(ActionEvent event) {
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
                    // Vérifier si le livre est emprunté par l'étudiant
                    String checkEmpruntQuery = "SELECT * FROM Emprunts WHERE livre_id = ? AND student_id = (SELECT ID FROM student WHERE StudentNumber = ?)";
                    PreparedStatement checkEmpruntStatement = connection.prepareStatement(checkEmpruntQuery);
                    checkEmpruntStatement.setString(1, misbn);
                    checkEmpruntStatement.setString(2, mstudnumber);

                    ResultSet checkEmpruntResult = checkEmpruntStatement.executeQuery();

                    if (checkEmpruntResult.next()) {
                        // Mettre à jour la disponibilité du livre
                        String updateAvailabilityQuery = "UPDATE Livre SET Disponible = 1 WHERE ISBN = ?";
                        PreparedStatement updateAvailabilityStatement = connection.prepareStatement(updateAvailabilityQuery);
                        updateAvailabilityStatement.setString(1, misbn);
                        updateAvailabilityStatement.executeUpdate();

                        // Supprimer l'emprunt de la table Emprunts
                        String deleteEmpruntQuery = "DELETE FROM Emprunts WHERE livre_id = ? AND student_id = (SELECT ID FROM student WHERE StudentNumber = ?)";
                        PreparedStatement deleteEmpruntStatement = connection.prepareStatement(deleteEmpruntQuery);
                        deleteEmpruntStatement.setString(1, misbn);
                        deleteEmpruntStatement.setString(2, mstudnumber);

                        // Exécuter la requête
                        int rowsAffected = deleteEmpruntStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Student Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Livre retourné avec succès.");
                            alert.showAndWait();
                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Message d'erreur");
                            alert.setHeaderText(null);
                            alert.setContentText("Échec du retour du livre.");
                            alert.showAndWait();
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Message d'erreur");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous n'avez pas emprunté ce livre.");
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec du retour du livre.");
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
