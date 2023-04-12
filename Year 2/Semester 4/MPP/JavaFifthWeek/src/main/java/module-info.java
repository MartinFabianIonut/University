module com.example.temasaptamana5 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens com.example.temasaptamana5 to javafx.fxml;
    exports com.example.temasaptamana5;
    opens com.example.temasaptamana5.controller to javafx.fxml;
    exports com.example.temasaptamana5.controller;
    opens com.example.temasaptamana5.domain to javafx.fxml;
    exports com.example.temasaptamana5.domain;
    exports com.example.temasaptamana5.service;
    exports com.example.temasaptamana5.repository;
    exports com.example.temasaptamana5.utils.utils;
    exports com.example.temasaptamana5.utils.observer;
    exports com.example.temasaptamana5.repository.interfaces;

}