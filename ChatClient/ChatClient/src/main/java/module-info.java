module com.server.chatclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.server.chatclient to javafx.fxml;
    exports com.server.chatclient;
}