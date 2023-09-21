module com.cs422.fxproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cs422.fxproject to javafx.fxml;
    exports com.cs422.fxproject;
}