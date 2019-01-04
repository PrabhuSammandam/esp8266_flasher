/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

/**
 *
 * @author psammand
 */
public class Options {

    public static final String FLASH_BIN_FILE_NAME = "eagle.flash.bin";
    public static final String IROM0_TEXT_BIN_FILE_NAME = "eagle.irom0text.bin";

    public static final String s_PROP_APPTYPE = "appType";
    public static final String s_PROP_BOOTMODE = "bootMode";
    public static final String s_PROP_COMPORT = "comPort";
    public static final String s_PROP_COMSPEED = "comSpeed";
    public static final String s_PROP_CURRENTSESSION = "currentSession";
    public static final String s_PROP_FLASHBINADDR = "flashBinAddr";
    public static final String s_PROP_FLASHMAP = "flashMap";
    public static final String s_PROP_FLASHMODE = "flashMode";
    public static final String s_PROP_FLASHSIZE = "flashSize";
    public static final String s_PROP_FLASHSPEED = "flashSpeed";
    public static final String s_PROP_INPUTFILE = "inputFile";
    public static final String s_PROP_IROM0TEXTBINADDR = "irom0textBinAddr";
    public static final String s_PROP_USER1BINADDR = "user1BinAddr";
    public static final String s_PROP_USER2BINADDR = "user2BinAddr";

    private String _bootMode = "New";
    private String _comPort = "";
    private String _comSpeed = "256000";
    private String _currentSession = "";
    private String _flashBinAddr = "";
    private File _flashBinFile;
    private String _flashMode = "QIO";
    private String _flashSize = "";
    private String _flashSpeed = "40";
    private String _inputFile = "";
    private File _inputFileDir;
    private File _inputFileObj;
    private boolean _inputFileValid = false;

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public boolean check() {
        return checkComPort() && checkInputFile();
    }

    public boolean checkComPort() {
        if ( getComPort() == null || getComPort().isEmpty() ) {
            System.out.println( "COM port not set\n" );
            return false;
        }
        return true;
    }

    public boolean checkInputFile() {
        if ( isInputFileValid() ) {
            return true;
        }

        if ( getInputFile() == null || getInputFile().isEmpty() ) {
            System.out.println( "Input file missing\n" );
            return false;
        }

        File inputFile = new File( getInputFile() );

        if ( !inputFile.exists() ) {
            System.out.println( "Input file not exists\n" );
            return false;
        }

        _inputFileObj = inputFile;
        _inputFileDir = new File( _inputFileObj.getParent() + File.separatorChar );
        _flashBinFile = new File( _inputFileDir.getAbsolutePath() + File.separatorChar + FLASH_BIN_FILE_NAME );

        setInputFileValid( true );

        return true;
    }

    public String getBootMode() {
        return _bootMode;
    }

    public void setBootMode( String bootMode ) {
        String oldBootMode = this._bootMode;
        this._bootMode = bootMode;
        propertyChangeSupport.firePropertyChange( s_PROP_BOOTMODE, oldBootMode, bootMode );
    }

    public String getComPort() {
        return _comPort;
    }

    public void setComPort( String comPort ) {
        String oldComPort = this._comPort;
        this._comPort = comPort;
        propertyChangeSupport.firePropertyChange( s_PROP_COMPORT, oldComPort, comPort );
    }

    public String getComSpeed() {
        return _comSpeed;
    }

    public void setComSpeed( String comSpeed ) {
        String oldComSpeed = this._comSpeed;
        this._comSpeed = comSpeed;
        propertyChangeSupport.firePropertyChange( s_PROP_COMSPEED, oldComSpeed, comSpeed );
    }

    public String getCurrentSession() {
        return _currentSession;
    }

    public void setCurrentSession( String currentSession ) {
        String oldCurrentSession = this._currentSession;
        this._currentSession = currentSession;
        propertyChangeSupport.firePropertyChange( s_PROP_CURRENTSESSION, oldCurrentSession, currentSession );
    }

    public String getFlashBinAddr() {
        return _flashBinAddr;
    }

    public void setFlashBinAddr( String flashBinAddr ) {
        String oldFlashBinAddr = this._flashBinAddr;
        this._flashBinAddr = flashBinAddr;
        propertyChangeSupport.firePropertyChange( s_PROP_FLASHBINADDR, oldFlashBinAddr, flashBinAddr );
    }

    public File getFlashBinFile() {
        return _flashBinFile;
    }

    public void setFlashBinFile( File flashBinFile ) {
        this._flashBinFile = flashBinFile;
    }

    public String getFlashMode() {
        return _flashMode;
    }

    public void setFlashMode( String flashMode ) {
        String oldFlashMode = this._flashMode;
        this._flashMode = flashMode;
        propertyChangeSupport.firePropertyChange( s_PROP_FLASHMODE, oldFlashMode, flashMode );
    }

    public String getFlashSize() {
        return _flashSize;
    }

    public void setFlashSize( String flashSize ) {
        String oldFlashSize = this._flashSize;
        this._flashSize = flashSize;
        propertyChangeSupport.firePropertyChange( s_PROP_FLASHSIZE, oldFlashSize, flashSize );
    }

    public String getFlashSpeed() {
        return _flashSpeed;
    }

    public void setFlashSpeed( String flashSpeed ) {
        String oldFlashSpeed = this._flashSpeed;
        this._flashSpeed = flashSpeed;
        propertyChangeSupport.firePropertyChange( s_PROP_FLASHSPEED, oldFlashSpeed, flashSpeed );
    }

    public String getInputFile() {
        return _inputFile;
    }

    public void setInputFile( String inputFile ) {
        String oldInputFile = this._inputFile;
        this._inputFile = inputFile;
        propertyChangeSupport.firePropertyChange( s_PROP_INPUTFILE, oldInputFile, inputFile );
        setInputFileValid( false );
    }

    public File getInputFileDir() {
        return _inputFileDir;
    }

    public void setInputFileDir( File inputFileDir ) {
        this._inputFileDir = inputFileDir;
    }

    public File getInputFileObj() {
        return _inputFileObj;
    }

    public void setInputFileObj( File inputFileObj ) {
        this._inputFileObj = inputFileObj;
    }

    public boolean isBootModeNew() {
        return _bootMode.equals( "New" );
    }

    public boolean isBootModeNone() {
        return _bootMode.equals( "None" );
    }

    public boolean isBootModeOld() {
        return _bootMode.equals( "Old" );
    }

    public boolean isInputFileValid() {
        return _inputFileValid;
    }

    public void setInputFileValid( boolean inputFileValid ) {
        this._inputFileValid = inputFileValid;
    }

    public String prependInputDir( String fileName ) {
        return this.getInputFileDir() + File.separator + fileName;
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

}
