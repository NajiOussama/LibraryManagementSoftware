package com.example.libraryproject;

import com.example.libraryproject.SqlController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListeMembresController implements Initializable {

    @FXML
    private TableColumn<student, String> nomCol;

    @FXML
    private TableColumn<student, String> prenomCol;

    @FXML
    private TableColumn<student, Integer> IDCol;

    @FXML
    private TableColumn<student, String> MdpCol;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TableView<student> tableView;

    private Connection connect = SqlController.connectDB();
    private Statement statement;
    private ResultSet result;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MdpCol.setCellValueFactory(new PropertyValueFactory<>("motdepasse"));
        tableView.setItems(getMembresFromDB());
    }

    public ObservableList<student> getMembresFromDB() {
        ObservableList<student> studentList = FXCollections.observableArrayList();

        try {
            String query = "SELECT ID, Nom, Prénom, password FROM student";
            connect = SqlController.connectDB();
            statement = connect.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                String nom = result.getString("Nom");
                String prenom = result.getString("Prénom");
                int id = result.getInt("ID");
                String motdepasse = result.getString("password");
                studentList.add(new student(nom, prenom, id, motdepasse));
            }

            return studentList;
        } catch (SQLException e) {
            Logger.getLogger(ListeMembresController.class.getName()).log(Level.SEVERE, null, e);
            return FXCollections.observableArrayList(); // Retournez une liste vide en cas d'erreur
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
                Logger.getLogger(ListeMembresController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public static class student {
        private final SimpleStringProperty nom;
        private final SimpleStringProperty prenom;
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty motdepasse;

        student(String nom, String prenom, int id, String motdepasse) {
            this.nom = new SimpleStringProperty(nom);
            this.prenom = new SimpleStringProperty(prenom);
            this.id = new SimpleIntegerProperty(id);
            this.motdepasse = new SimpleStringProperty(motdepasse);
        }

        public String getNom() {
            return nom.get();
        }

        public String getPrenom() {
            return prenom.get();
        }

        public int getId() {
            return id.get();
        }

        public String getMotdepasse() {
            return motdepasse.get();
        }
    }
}
