package com.example.libraryproject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;

public class dashboardAdminController implements Initializable {
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Emprunt> tableview;

    @FXML
    private TableColumn<Emprunt, String> Eauteur;

    @FXML
    private TableColumn<Emprunt, String> Eisbn;

    @FXML
    private TableColumn<Emprunt, String> Enom;

    @FXML
    private TableColumn<Emprunt, String> Eprenom;

    @FXML
    private TableColumn<Emprunt, String> Esn;

    @FXML
    private TableColumn<Emprunt, String> Etitre;

    @FXML
    private TableColumn<Emprunt, String> date_debut;


    @FXML
    private TextField champSN;

    @FXML
    private TableView<Historique> tableviewhistorique;

    @FXML
    private TableColumn<Historique, String> hauteur;

    @FXML
    private TableColumn<Historique, String> hdate_pret;

    @FXML
    private TableColumn<Historique, String> hdate_retour;

    @FXML
    private TableColumn<Historique, String> hisbn;

    @FXML
    private TableColumn<Historique, String> htitre;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Enom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Eprenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        Esn.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        Etitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        Eauteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        Eisbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        date_debut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));

        tableview.setItems(getEmpruntsFromDB());


    }

    @FXML
    void AfficherHistorique(ActionEvent event) {
        String studentNumber = champSN.getText();


        if (!studentNumber.isEmpty()) {
            ObservableList<Historique> historiqueList = getHistoriqueFromDB(studentNumber);

            hauteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
            hdate_pret.setCellValueFactory(new PropertyValueFactory<>("datePret"));
            hdate_retour.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
            hisbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
            htitre.setCellValueFactory(new PropertyValueFactory<>("titre"));

            tableviewhistorique.setItems(historiqueList);
        } else {
            showAlert("Veuillez saisir le numéro d'étudiant (Student Number).");
        }
    }

    private ObservableList<Historique> getHistoriqueFromDB(String studentNumber) {
        ObservableList<Historique> historiqueList = FXCollections.observableArrayList();

        Connection connection = SqlController.connectDB();

        if (connection != null) {
            try {
                String historiqueQuery = "SELECT Livre.Auteur, Historique.date_pret, Historique.date_retour, Livre.ISBN, Livre.Titre " +
                        "FROM Historique " +
                        "JOIN Livre ON Historique.livre_id = Livre.ISBN " +
                        "JOIN student ON Historique.student_id = student.ID " +
                        "WHERE student.StudentNumber = ?";

                PreparedStatement historiqueStatement = connection.prepareStatement(historiqueQuery);
                historiqueStatement.setString(1, studentNumber);
                ResultSet historiqueResult = historiqueStatement.executeQuery();

                while (historiqueResult.next()) {
                    String auteur = historiqueResult.getString("Auteur");
                    String datePret = historiqueResult.getString("date_pret");
                    String dateRetour = historiqueResult.getString("date_retour");
                    String isbn = historiqueResult.getString("ISBN");
                    String titre = historiqueResult.getString("Titre");

                    historiqueList.add(new Historique(auteur, datePret, dateRetour, isbn, titre));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs SQL
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

        return historiqueList;
    }

    public class Historique {
        private final SimpleStringProperty auteur;
        private final SimpleStringProperty datePret;
        private final SimpleStringProperty dateRetour;
        private final SimpleStringProperty isbn;
        private final SimpleStringProperty titre;

        public Historique(String auteur, String datePret, String dateRetour, String isbn, String titre) {
            this.auteur = new SimpleStringProperty(auteur);
            this.datePret = new SimpleStringProperty(datePret);
            this.dateRetour = new SimpleStringProperty(dateRetour);
            this.isbn = new SimpleStringProperty(isbn);
            this.titre = new SimpleStringProperty(titre);
        }

        public String getAuteur() {
            return auteur.get();
        }

        public String getDatePret() {
            return datePret.get();
        }

        public String getDateRetour() {
            return dateRetour.get();
        }

        public String getIsbn() {
            return isbn.get();
        }

        public String getTitre() {
            return titre.get();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class Emprunt {
        private final SimpleStringProperty nom;
        private final SimpleStringProperty prenom;
        private final SimpleStringProperty studentNumber;
        private final SimpleStringProperty titre;
        private final SimpleStringProperty auteur;
        private final SimpleStringProperty isbn;
        private final SimpleStringProperty dateDebut;


        Emprunt(String nom, String prenom, String studentNumber, String titre, String auteur, String isbn, String dateDebut) {
            this.nom = new SimpleStringProperty(nom);
            this.prenom = new SimpleStringProperty(prenom);
            this.studentNumber = new SimpleStringProperty(studentNumber);
            this.titre = new SimpleStringProperty(titre);
            this.auteur = new SimpleStringProperty(auteur);
            this.isbn = new SimpleStringProperty(isbn);
            this.dateDebut = new SimpleStringProperty(dateDebut);

        }

        public String getDateDebut() {
            return dateDebut.get();
        }



        public String getNom() {
            return nom.get();
        }

        public String getPrenom() {
            return prenom.get();
        }

        public String getStudentNumber() {
            return studentNumber.get();
        }

        public String getTitre() {
            return titre.get();
        }

        public String getAuteur() {
            return auteur.get();
        }

        public String getIsbn() {
            return isbn.get();
        }
    }


    private ObservableList<Emprunt> getEmpruntsFromDB() {
        ObservableList<Emprunt> empruntsList = FXCollections.observableArrayList();

        Connection connection = SqlController.connectDB();

        if (connection != null) {
            try {
                String empruntsQuery = "SELECT student.Nom, student.Prénom, student.StudentNumber, Livre.Titre, Livre.Auteur, Livre.ISBN, Historique.date_pret " +
                        "FROM Emprunts " +
                        "JOIN student ON Emprunts.student_id = student.ID " +
                        "JOIN Livre ON Emprunts.livre_id = Livre.ISBN " +
                        "JOIN Historique ON Emprunts.livre_id = Historique.livre_id";

                PreparedStatement empruntsStatement = connection.prepareStatement(empruntsQuery);
                ResultSet empruntsResult = empruntsStatement.executeQuery();

                while (empruntsResult.next()) {
                    String nom = empruntsResult.getString("Nom");
                    String prenom = empruntsResult.getString("Prénom");
                    String studentNumber = empruntsResult.getString("StudentNumber");
                    String titre = empruntsResult.getString("Titre");
                    String auteur = empruntsResult.getString("Auteur");
                    String misbn = empruntsResult.getString("ISBN");
                    String mdate_debut = empruntsResult.getString("date_pret");


                    empruntsList.add(new Emprunt(nom, prenom, studentNumber, titre, auteur, misbn,mdate_debut));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs SQL
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

        return empruntsList;
    }




    @FXML
    void AjouterLivre(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AjouterLivre.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

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
    @FXML
    void SeDeco(ActionEvent event) throws IOException {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("LoginAdmin.fxml"));

        Stage stage2 = new Stage();

        Scene scene = new Scene(root);

        stage2.setScene(scene);
        stage2.show();

    }



}
