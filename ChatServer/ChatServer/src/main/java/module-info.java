module com.server.chatserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.server.chatserver to javafx.fxml;
    exports com.server.chatserver;
}