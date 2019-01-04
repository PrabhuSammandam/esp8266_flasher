package org.jinjuamla.esp8266.imageflasher;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author psammand
 */
public class Esp8266Flasher {

    public static void main( String[] args ) {
        try {
            for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() ) {
                if ( "Nimbus".equals( info.getName() ) ) {
                    javax.swing.UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex ) {
            java.util.logging.Logger.getLogger( Esp8266FlasherFrame.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
        }

        Esp8266Flasher esp8266Flasher = new Esp8266Flasher();

        String jarPath = esp8266Flasher.GetExecutionPath();

//        String workingDir = Paths.get( "." ).toAbsolutePath().normalize().toString() + File.separator + "settings.xml";
        String workingDir = jarPath + File.separator + "settings.xml";
        System.out.println( "Loading settings file " + jarPath );

        File settingsFile = new File( workingDir );

        if ( !settingsFile.exists() ) {
            BufferedWriter bw = null;
            FileWriter fw = null;
            try {
                fw = new FileWriter( settingsFile );
                bw = new BufferedWriter( fw );
                bw.write( "<Esp8266FlasherSettings><configs></configs></Esp8266FlasherSettings>" );
                System.out.println( "Settings file created at " + settingsFile );
            } catch ( IOException ex ) {
                Logger.getLogger( Esp8266Flasher.class.getName() ).log( Level.SEVERE, null, ex );
            } finally {
                try {
                    if ( bw != null ) {
                        bw.close();
                    }

                    if ( fw != null ) {
                        fw.close();
                    }

                } catch ( IOException ex ) {
                }
            }
        }

        Settings.getInst().loadSettings( workingDir );
//        Settings.getInst().save();

        Esp8266FlasherFrame frame = new Esp8266FlasherFrame();

        frame.setExtendedState( MAXIMIZED_BOTH );
        frame.setVisible( true );

    }

    private String GetExecutionPath() {
        String absolutePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring( 0, absolutePath.lastIndexOf( "/" ) );
        absolutePath = absolutePath.replaceAll( "%20", " " ); // Surely need to do this here
        return absolutePath;
    }

}
