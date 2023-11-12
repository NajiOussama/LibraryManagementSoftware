package com.example.libraryproject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dashboardStudentController {
    @FXML
    private StackPane rootPane;
    @FXML
    private TableColumn<LivreE, String> AutCol;

    @FXML
    private TableColumn<LivreE, String> TitCol;

    @FXML
    private TableColumn<LivreE, String> isbnCol;

    @FXML
    private TableView<LivreE> tableView;


    @FXML
    private TextField MDP;

    @FXML
    private TextField NE;


    @FXML
    void AfficherEmprunts(ActionEvent event) {
        AutCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TitCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        tableView.setItems(getLivresFromDB());


        String studentNumber = NE.getText();
        String password = MDP.getText();

        if (studentNumber.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Connection connection = SqlController.connectDB();

        if (connection != null) {
            try {
                // Vérifier l'authentification de l'étudiant
                String authenticateQuery = "SELECT ID FROM student WHERE StudentNumber = ? AND password = ?";
                PreparedStatement authenticateStatement = connection.prepareStatement(authenticateQuery);
                authenticateStatement.setString(1, studentNumber);
                authenticateStatement.setString(2, password);

                ResultSet authenticateResult = authenticateStatement.executeQuery();

                if (authenticateResult.next()) {
                    int studentID = authenticateResult.getInt("ID");

                    // Récupérer les livres empruntés par l'étudiant
                    String empruntsQuery = "SELECT Livre.Auteur, Livre.Titre, Livre.ISBN " +
                            "FROM Emprunts " +
                            "JOIN Livre ON Emprunts.livre_id = Livre.ISBN " +
                            "WHERE Emprunts.student_id = ?";
                    PreparedStatement empruntsStatement = connection.prepareStatement(empruntsQuery);
                    empruntsStatement.setInt(1, studentID);

                    ResultSet empruntsResult = empruntsStatement.executeQuery();

                    ObservableList<LivreE> empruntsList = FXCollections.observableArrayList();

                    while (empruntsResult.next()) {
                        String auteur = empruntsResult.getString("Auteur");
                        String titre = empruntsResult.getString("Titre");
                        String isbn = empruntsResult.getString("ISBN");

                        empruntsList.add(new LivreE(titre, isbn, auteur));
                    }

                    // Mettre à jour la TableView
                    tableView.setItems(empruntsList);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur d'authentification", "Étudiant non trouvé avec les informations fournies.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des emprunts.");
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

    public static class LivreE {
        private final SimpleStringProperty title;
        private final SimpleStringProperty ISBN;
        private final SimpleStringProperty author;


        LivreE(String title, String id, String author) {
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

    public ObservableList<LivreE> getLivresFromDB() {
        ObservableList<LivreE> livresList = FXCollections.observableArrayList();

        try (Connection connect = SqlController.connectDB();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery("SELECT Titre, Auteur, ISBN, Disponible FROM Livre")) {

            while (result.next()) {
                String titre = result.getString("Titre");
                String auteur = result.getString("Auteur");
                String isbn = result.getString("ISBN");
                boolean disponible = result.getBoolean("Disponible");
                livresList.add(new LivreE(titre, isbn, auteur));
            }

            return livresList;
        } catch (SQLException e) {
            Logger.getLogger(ListeLivresController.class.getName()).log(Level.SEVERE, null, e);
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


    @FXML
    void TouslesLivres(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ListeLivres.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void emprunter(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Emprunter.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

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
