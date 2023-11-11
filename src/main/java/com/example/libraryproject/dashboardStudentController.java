package com.example.libraryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class dashboardStudentController {
    @FXML
    private StackPane rootPane;

    @FXML
    void AjouterMembre(ActionEvent event) {

    }



    @FXML
    void TouslesLivres(ActionEvent event) {

    }

    @FXML
    void SeDeco(ActionEvent event) throws IOException {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("LoginEtudiant.fxml"));

        Stage stage2 = new Stage();

        Scene scene = new Scene(root);

        stage2.setScene(scene);
        stage2.show();

    }

}
