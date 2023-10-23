package com.example.libraryproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginAdminController {

    @FXML
    private Button Login_btn;

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private PasswordField password;

    @FXML
    private TextField AdminNumber;

    private Connection connect;

    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;


    @FXML
    void login(ActionEvent event) {

        String sql = "SELECT * FROM Admin WHERE AdminNumber = ? and password = ?";

        connect = SqlController.connectDB();

        try{

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, AdminNumber.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if(AdminNumber.getText().isEmpty() || password.getText().isEmpty()){

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Message d'erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides.");
                alert.showAndWait();
            }else{
                if(result.next()){

                    //getData.studentNumber = studentNumber.getText();

//                    DON'T FORGET THIS!!!!
                    //getData.path = result.getString("image");

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Admin Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login!");
                    alert.showAndWait();

//                    TO HIDE THE LOGIN FORM
                    Login_btn.getScene().getWindow().hide();

//                    FOR DASHBOARD
                    Parent root = FXMLLoader.load(getClass().getResource("dashboardAdmin.fxml"));

                    Stage stage = new Stage();

                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.show();

                    //root.setOnMousePressed((MouseEvent event) ->{

                    //  x = event.getSceneX();
                    // y = event.getSceneY();

                    //});

                    //root.setOnMouseDragged((MouseEvent event) ->{

                    //  stage.setX(event.getScreenX() - x);
                    // stage.setY(event.getScreenY() - y);

                    //});

                    //stage.initStyle(StageStyle.TRANSPARENT);



                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message d'erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Nom d'utilisateur ou mot de passe incorrect");
                    alert.showAndWait();
                }
            }

        }catch(Exception e){e.printStackTrace();}

    }

    @FXML
    void etudiantcliqueici(ActionEvent event)  throws IOException {
        Login_btn.getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("LoginEtudiant.fxml"));

        Stage stage = new Stage();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void minimize() {
        Stage stage = (Stage)minimize.getScene().getWindow();
        stage.setIconified(true);

    }
    @FXML
    public void exit() {
        System.exit(0);

    }

}
