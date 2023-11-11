package com.example.libraryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class dashboardAdminController {

    @FXML
    void AjouterLivre(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AjouterLivre.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void SeDeco(ActionEvent event) {

    }
    @FXML
    void AjouterMembre(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AjouterMembre.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void SupprimerLivre(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SupprimerLivre.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void SupprimerMembre(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SupprimerMembre.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void TouslesLivres(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ListeLivres.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void TouslesMembres(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ListeMembres.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}
