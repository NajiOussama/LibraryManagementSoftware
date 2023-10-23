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

public class LoginEtudiantController {
    @FXML
    private TextField studentNumber;

    @FXML
    private Button Login_btn;

    @FXML
    private Button close;

    @FXML
    private Button minimize;
    @FXML
    private Button cliqueici;
    @FXML
    private PasswordField password;

    private Connection connect;

    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    //private double x = 0;
    //private double y = 0;



    public void login(){

        String sql = "SELECT * FROM student WHERE studentNumber = ? and password = ?";

        connect = SqlController.connectDB();

        try{

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentNumber.getText());
            prepare.setString(2, password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if(studentNumber.getText().isEmpty() || password.getText().isEmpty()){

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
                    alert.setTitle("Message d'information");
                    alert.setHeaderText(null);
                    alert.setContentText("Connexion réussie!");
                    alert.showAndWait();

//                    TO HIDE THE LOGIN FORM
                    Login_btn.getScene().getWindow().hide();

//                    FOR DASHBOARD
                    Parent root = FXMLLoader.load(getClass().getResource("dashboardStudent.fxml"));

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
    public void admincliqueici(ActionEvent event) throws IOException {
        Login_btn.getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("LoginAdmin.fxml"));

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
