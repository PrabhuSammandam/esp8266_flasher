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
public class ToolSettings {

    public static final String ESPTOOL2 = "esptool2.exe";
    public static final String ESPTOOL_CK = "esptool-ck.exe";

    public static final String s_PROP_COMPILERPATH = "compilerPath";
    public static final String s_PROP_SDKPATH = "sdkPath";
    public static final String s_PROP_TOOLSPATH = "toolsPath";
    private String _blankFilePath;

    private String _compilerPath = "";
    private String _esptool2Path;
    private String _esptoolPath;
    private String _esptoolckPath;
    private boolean _isCompilerPathValid = false;
    private boolean _isToolPathValid = false;
    private File _objDumpFile;
    private String _sdkPath = "";
    private String _toolsBaseDir;
    private String _toolsPath = "";

    public String getEsptool2Path() {
        return _esptool2Path;
    }

    public void setEsptool2Path( String esptool2Path ) {
        this._esptool2Path = esptool2Path;
    }

    public String getEsptoolckPath() {
        return _esptoolckPath;
    }

    public void setEsptoolckPath( String esptoolckPath ) {
        this._esptoolckPath = esptoolckPath;
    }

    public boolean isIsCompilerPathValid() {
        return _isCompilerPathValid;
    }

    public void setIsCompilerPathValid( boolean isCompilerPathValid ) {
        this._isCompilerPathValid = isCompilerPathValid;
    }

    public boolean isIsToolPathValid() {
        return _isToolPathValid;
    }

    public void setIsToolPathValid( boolean isToolPathValid ) {
        this._isToolPathValid = isToolPathValid;
    }

    public File getObjDumpFile() {
        return _objDumpFile;
    }

    public void setObjDumpFile( File objDumpFile ) {
        this._objDumpFile = objDumpFile;
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this );

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( listener );
    }

    public String getCompilerPath() {
        return _compilerPath;
    }

    public void setCompilerPath( String compilerPath ) {
        setIsCompilerPathValid( false );
        String oldCompilerPath = this._compilerPath;
        this._compilerPath = compilerPath;
        propertyChangeSupport.firePropertyChange( s_PROP_COMPILERPATH, oldCompilerPath, compilerPath );
    }

    public String getSdkPath() {
        return _sdkPath;
    }

    public void setSdkPath( String sdkPath ) {
        String oldSdkPath = this._sdkPath;
        this._sdkPath = sdkPath;
        propertyChangeSupport.firePropertyChange( s_PROP_SDKPATH, oldSdkPath, sdkPath );
    }

    public String getToolsPath() {
        return _toolsPath;
    }

    public void setToolsPath( String toolsPath ) {
        setIsToolPathValid( false );
        String oldToolsPath = this._toolsPath;
        this._toolsPath = toolsPath;
        propertyChangeSupport.firePropertyChange( s_PROP_TOOLSPATH, oldToolsPath, toolsPath );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        propertyChangeSupport.removePropertyChangeListener( listener );
    }

    public boolean check() {
        return checkCompilerPath() && checkSdkPath() && checkToolsPath();
    }

    public boolean checkCompilerPath() {
        if ( _isCompilerPathValid == true ) {
            return true;
        }
        if ( getCompilerPath().isEmpty() ) {
            System.out.println( "Compiler path is not set\n" );
            return false;
        }

        File objdump = new File( getCompilerPath() + File.separatorChar + "bin/xtensa-lx106-elf-objdump.exe" );

        if ( !objdump.exists() ) {
            System.out.println( "Compiler path is not set correctly, unable to find the xtensa-lx106-elf-objdump.exe\n" );
            return false;
        }
        _isCompilerPathValid = true;
        _objDumpFile = objdump;

        return true;
    }

    public boolean checkSdkPath() {
        if ( getSdkPath().isEmpty() ) {
            System.out.println( "SDK path is not set\n" );
            return false;
        }
        return true;
    }

    public boolean checkToolsPath() {
        if ( _isToolPathValid == true ) {
            return true;
        }
        if ( getToolsPath().isEmpty() ) {
            System.out.println( "Tool path is not set\n" );
            return false;
        }
        File esptool2File = new File( getToolsPath() + File.separatorChar + "esptool2.exe" );

        if ( !esptool2File.exists() ) {
            System.out.println( "Tools path is not set correctly, unable to find the esptool2.exe\n" );
            return false;
        }

        File esptoolckFile = new File( getToolsPath() + File.separatorChar + "esptool-ck.exe" );

        if ( !esptoolckFile.exists() ) {
            System.out.println( "Tools path is not set correctly, unable to find the esptool-ck.exe\n" );
            return false;
        }

        File esptoolFile = new File( getToolsPath() + File.separatorChar + "esptool.exe" );

        if ( !esptoolFile.exists() ) {
            System.out.println( "Tools path is not set correctly, unable to find the esptool.exe\n" );
            return false;
        }

        _isToolPathValid = true;
        _esptoolPath = esptoolFile.getAbsolutePath();
        _esptool2Path = esptool2File.getAbsolutePath();
        _esptoolckPath = esptoolckFile.getAbsolutePath();
        _toolsBaseDir = esptool2File.getParent();

        return true;
    }

    public boolean check1MbBlankFile() {
        if ( !checkToolsPath() ) {
            return false;
        }

        File blankFile = new File( _toolsBaseDir + File.separatorChar + "blank_1MB.bin" );

        if ( !blankFile.exists() ) {
            System.out.println( "Blank file is not present in the tools directory " + _toolsBaseDir );
            return false;
        }

        _blankFilePath = blankFile.getAbsolutePath();

        return true;
    }

    public String getEsptoolPath() {
        return _esptoolPath;
    }

    public void setEsptoolPath( String esptoolPath ) {
        this._esptoolPath = esptoolPath;
    }

    public String getBlankFilePath() {
        return _blankFilePath;
    }

    public void setBlankFilePath( String blankFilePath ) {
        this._blankFilePath = blankFilePath;
    }

    public String getToolsBaseDir() {
        return _toolsBaseDir;
    }

    public void setToolsBaseDir( String toolsBaseDir ) {
        this._toolsBaseDir = toolsBaseDir;
    }
}
