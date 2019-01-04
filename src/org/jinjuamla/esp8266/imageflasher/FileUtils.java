/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.exec.CommandLine;

/**
 *
 * @author psammand
 */
public class FileUtils {

    static String getBinaryFile( Component parent ) {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter( new FileFilter() {
            @Override
            public boolean accept( File f ) {
                return f.isDirectory() ? true : f.getName().endsWith( ".out" );
            }

            @Override
            public String getDescription() {
                return "ESP8266 binary file with *.out";
            }
        } );

        int returnVal = fc.showOpenDialog( parent );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }

    static String getDirectory( Component parent ) {
        final JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        int returnVal = fc.showOpenDialog( parent );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File file = fc.getSelectedFile();
            return file.getAbsolutePath();
        }
        return null;
    }

    static void printCommandLine( CommandLine cmdLine ) {
        for ( String cmd : cmdLine.toStrings() ) {
            System.out.print( cmd + " " );
        }
        System.out.println();
    }
}
