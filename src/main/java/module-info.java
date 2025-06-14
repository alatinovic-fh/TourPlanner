module at.fh.bif.swen.tourplanner {

    // Spring Boot
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.data.jpa;
    requires jakarta.persistence;

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;

    requires static lombok;
    requires org.hibernate.orm.core;
    requires org.kordamp.bootstrapfx.core;

    exports at.fh.bif.swen.tourplanner;
    exports at.fh.bif.swen.tourplanner.view;
    exports at.fh.bif.swen.tourplanner.viewmodel;
    exports at.fh.bif.swen.tourplanner.service;
    exports at.fh.bif.swen.tourplanner.persistence.entity;
    exports at.fh.bif.swen.tourplanner.persistence.repository;

    opens at.fh.bif.swen.tourplanner to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    opens at.fh.bif.swen.tourplanner.view to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    opens at.fh.bif.swen.tourplanner.viewmodel to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    opens at.fh.bif.swen.tourplanner.service to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    opens at.fh.bif.swen.tourplanner.persistence.repository to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;
    opens at.fh.bif.swen.tourplanner.util to javafx.fxml, spring.core, spring.beans, org.hibernate.orm.core;

    opens at.fh.bif.swen.tourplanner.persistence.entity;
}
