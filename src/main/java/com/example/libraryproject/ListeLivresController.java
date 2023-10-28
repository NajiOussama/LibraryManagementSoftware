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

public class ListeLivresController implements Initializable {

    @FXML
    private TableColumn<Livre, String> AuthorCol;

    @FXML
    private TableColumn<Livre, Boolean> AvailCol;

    @FXML
    private TableColumn<Livre, String> ISBNCol;

    @FXML
    private TableView<Livre> tableView;

    @FXML
    private TableColumn<Livre, String> TitleCol;

    @FXML
    private AnchorPane rootPane;
    private Connection connect = SqlController.connectDB();

    private Statement statement;
    private ResultSet result;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        AvailCol.setCellValueFactory(new PropertyValueFactory<>("dispo"));
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        tableView.setItems(getLivresFromDB());

    }

    public static class Livre{
        private final SimpleStringProperty title;
        private final SimpleStringProperty ISBN;
        private final SimpleStringProperty author;
        private final SimpleBooleanProperty dispo;

        Livre(String title,String id, String author, Boolean dispo){
            this.title = new SimpleStringProperty(title);
            this.ISBN = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.dispo = new SimpleBooleanProperty(dispo);
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

        public boolean isDispo() {
            return dispo.get();
        }

    }

    public ObservableList<Livre> getLivresFromDB() {
        ObservableList<Livre> livresList = FXCollections.observableArrayList();

        try {
            String query = "SELECT Titre, Auteur, ISBN, Disponible FROM Livre";
            connect = SqlController.connectDB();
            statement = connect.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                String titre = result.getString("Titre");
                String auteur = result.getString("Auteur");
                String isbn = result.getString("ISBN");
                boolean disponible = result.getBoolean("Disponible");
                livresList.add(new Livre(titre, isbn, auteur, disponible));
            }

            // Afficher les données récupérées
            for (Livre livre : livresList) {
                System.out.println("Titre: " + livre.getTitle() + ", Auteur: " + livre.getAuthor() + ", ISBN: " + livre.getISBN() + ", Disponible: " + livre.isDispo());
            }

            return livresList;
        } catch (SQLException e) {
            Logger.getLogger(ListeLivresController.class.getName()).log(Level.SEVERE, null, e);
            return null; // Assurez-vous de retourner une valeur par défaut en cas d'erreur
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ListeLivresController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }


    }










