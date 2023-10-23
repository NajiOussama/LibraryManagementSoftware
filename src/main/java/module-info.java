module com.example.libraryproject {
    requires javafx.controls;
    requires javafx.fxml;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;



    opens com.example.libraryproject to javafx.fxml;
    exports com.example.libraryproject;
}