package com.pimpelkram.universalfilefinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.pimpelkram.universalfilefinder.config.Settings;
import com.pimpelkram.universalfilefinder.config.SettingsFileLoader;

public class DefaultModule extends AbstractModule {

    Logger logger = LoggerFactory.getLogger(DefaultModule.class);

    private Settings settings;

    @Override
    protected void configure() {
        // bind(Service.class).to(Service.class);
    }

    @Provides
    Settings provideSettings() {
        if (settings != null) {
            return settings;
        } else {
            final String pathToHome = System.getProperty("user.home");
            logger.debug("User home directory: " + pathToHome);
            settings = new SettingsFileLoader().getSettings();
            return settings;
        }
    }
}
