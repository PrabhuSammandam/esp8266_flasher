/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author psammand
 */
public class BootloaderSelectionModel {

    public static final String s_PROP_BOOTLOADERFILENAME = "bootloaderFileName";
    public static final String s_PROP_CUSTOMBOOTLOADER = "customBootloader";

    private String _bootloaderFileName = "";
    private String _customBootloader = "";

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public String getBootloaderFileName() {
        return _bootloaderFileName;
    }

    public void setBootloaderFileName( String bootloaderFileName ) {
        String oldBootloaderFileName = this._bootloaderFileName;
        this._bootloaderFileName = bootloaderFileName;
        propertyChangeSupport.firePropertyChange( s_PROP_BOOTLOADERFILENAME, oldBootloaderFileName, bootloaderFileName );
    }

    public String getCustomBootloader() {
        return _customBootloader;
    }

    public void setCustomBootloader( String customBootloader ) {
        String oldCustomBootloader = this._customBootloader;
        this._customBootloader = customBootloader;
        propertyChangeSupport.firePropertyChange( s_PROP_CUSTOMBOOTLOADER, oldCustomBootloader, customBootloader );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

}
