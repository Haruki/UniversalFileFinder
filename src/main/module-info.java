module com.pimpelkram.univeralfilefinder {
requires javafx.base;
requires javafx.controls;
requires javafx.fxml;
    requires slf4j.api;
    requires ignite.guice;
    requires javax.inject;
    requires guice;
    requires controlsfx;
    requires javax.json;
    //requires univeralfilefinder.resources;
    exports com.pimpelkram.universalfilefinder;
}