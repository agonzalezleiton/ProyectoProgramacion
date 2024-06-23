module com.dugeonmvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.dungeonmvc to javafx.fxml;
    exports com.dungeonmvc;
    opens com.dungeonmvc.controllers to javafx.fxml;
    exports com.dungeonmvc.controllers;
}
