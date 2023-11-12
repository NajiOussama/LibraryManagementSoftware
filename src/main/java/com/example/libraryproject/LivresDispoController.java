package com.example.libraryproject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LivresDispoController implements Initializable {

    @FXML
    private TableColumn<LivreD, String> AuthorCol;

    @FXML
    private TableColumn<LivreD, String> ISBNCol;

    @FXML
    private TableView<LivreD> tableView;

    @FXML
    private TableColumn<LivreD, String> TitleCol;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        tableView.setItems(getLivresFromDB());
    }

    public static class LivreD {
        private final SimpleStringProperty title;
        private final SimpleStringProperty ISBN;
        private final SimpleStringProperty author;


        LivreD(String title, String id, String author) {
            this.title = new SimpleStringProperty(title);
            this.ISBN = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
        }

        public String getTitle() {
            return title.get();
        }

        public String getISBN() {
            return ISBN.get();
        }

        public String getAuthor() {
            return author.get();
        }


    }

    public ObservableList<LivreD> getLivresFromDB() {
        ObservableList<LivreD> livresList = FXCollections.observableArrayList();

        try (Connection connect = SqlController.connectDB();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery("SELECT Titre, Auteur, ISBN FROM Livre WHERE Disponible = true")) {

            while (result.next()) {
                String titre = result.getString("Titre");
                String auteur = result.getString("Auteur");
                String isbn = result.getString("ISBN");
                livresList.add(new LivreD(titre, isbn, auteur));
            }

            return livresList;
        } catch (SQLException e) {
            Logger.getLogger(LivresDispoController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des livres.");
            return FXCollections.observableArrayList(); // Retourner une liste vide en cas d'erreur
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
