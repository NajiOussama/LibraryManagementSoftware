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


public class SupprimerMembreController {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField StudentNumber;


    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button saveButton;


    @FXML
    private void removeMember(ActionEvent event) {
        String mnom = nom.getText();
        String mprenom = prenom.getText();
        String mStudentNumber = StudentNumber.getText();

        if (mnom.isEmpty() || mprenom.isEmpty()|| mStudentNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides.");
            alert.showAndWait();
        }


        Connection connection = SqlController.connectDB();

        if (connection != null && !mnom.isEmpty() && !mprenom.isEmpty() && !mStudentNumber.isEmpty()) {
            Alert alert;
            try {
                // Créez la requête SQL d'insertion
                String deleteQuery = "DELETE FROM student WHERE Nom = ? AND Prénom = ? AND StudentNumber = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setString(1, mnom);
                preparedStatement.setString(2, mprenom);
                preparedStatement.setString(3, mStudentNumber);

                // Exécutez la requête de suppression
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("Membre supprimé avec succès.");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Aucun membre trouvé avec les informations fournies.");
                    alert.showAndWait();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Échec de la suppression du membre.");
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
