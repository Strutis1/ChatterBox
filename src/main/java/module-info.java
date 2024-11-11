module com.crew.mif.chatterbox {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.crew.mif.chatterbox to javafx.fxml;
    exports com.crew.mif.chatterbox;
    exports registration;
    opens registration to javafx.fxml;
    exports lobby;
    opens lobby to javafx.fxml;
}