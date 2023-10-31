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


public class AjouterMembreController {

    @FXML
    private Button cancelButton;


    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button saveButton;


    @FXML
    private void addMember(ActionEvent event) {
        String mnom = nom.getText();
        String mprenom = prenom.getText();

        if (mnom.isEmpty() || mprenom.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message d'erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides.");
            alert.showAndWait();
        }


        Connection connection = SqlController.connectDB();

        if (connection != null && !mnom.isEmpty() && !mprenom.isEmpty()) {
            Alert alert;
            try {
                // Créez la requête SQL d'insertion
                String insertQuery = "INSERT INTO student (Nom, Prénom, password)\n" +
                        "VALUES (?, ?, LEFT(MD5(CONCAT(Nom, Prénom, NOW())), 8))";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, mnom);
                preparedStatement.setString(2, mprenom);



                // Exécutez la requête
                int rowsAffected = preparedStatement.executeUpdate();


                if (rowsAffected > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Membre ajouté avec succès dans la base de données.");
                    alert.showAndWait();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Échec de l'ajout du membre.");
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
                alert.setContentText("Échec de l'ajout du membre.");
                alert.showAndWait();
            }
        } else {

        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

}
