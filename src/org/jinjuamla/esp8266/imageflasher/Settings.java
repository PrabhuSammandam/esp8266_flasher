/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author psammand
 */
public class Settings {

    FileBasedConfigurationBuilder<XMLConfiguration> _configBuilder;

    private Settings() {
    }

    public void loadSettings( String settingsFilePath ) {
        Parameters params = new Parameters();
        _configBuilder = new FileBasedConfigurationBuilder<>( XMLConfiguration.class )
                .configure( params.xml()
                        .setFileName( settingsFilePath ) );
    }

    public Configuration getConfig() {
        try {
            return _configBuilder.getConfiguration();
        } catch ( ConfigurationException cex ) {
            // loading of the configuration file failed
        }
        return null;
    }

    public void save() {
        try {
            _configBuilder.save();
        } catch ( ConfigurationException ex ) {
            Logger.getLogger( Settings.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

    public int getConfigIndex( String configName ) {
        if ( configName == null || configName.isEmpty() ) {
            return -1;
        }
        List<Object> configList = getConfig().getList( "configs.config[@name]" );
        int lastSessionIndex = -1;

        for ( int i = 0; i < configList.size(); i++ ) {
            if ( configName.equals( configList.get( i ) ) ) {
                lastSessionIndex = i;
                break;
            }
        }
        return lastSessionIndex;
    }

    public static Settings getInst() {
        return SettingsHolder.INSTANCE;
    }

    private static class SettingsHolder {

        private static final Settings INSTANCE = new Settings();
    }
}
