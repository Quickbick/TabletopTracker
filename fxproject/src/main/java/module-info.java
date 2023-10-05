module com.cs422.fxproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;


    opens com.cs422.fxproject to javafx.fxml;
    exports com.cs422.fxproject;
    exports com.cs422.fxproject.creatures to javafx.graphics;
}