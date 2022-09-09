module com.kozhanov.cinemafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires spring.context;
    requires spring.data.commons;
    requires java.sql;


    opens com.kozhanov.cinemafx to javafx.fxml;
    exports com.kozhanov.cinemafx;
    exports com.kozhanov.cinemafx.models;
    opens com.kozhanov.cinemafx.models to javafx.fxml;
    exports com.kozhanov.cinemafx.Controllers;
    opens com.kozhanov.cinemafx.Controllers to javafx.fxml;
}