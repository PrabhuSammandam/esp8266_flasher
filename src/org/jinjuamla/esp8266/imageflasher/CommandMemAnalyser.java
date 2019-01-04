/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.*;

/**
 *
 * @author psammand
 */
public class CommandMemAnalyser {

    private String memAnalyserPath;

    Options _options;
    ToolSettings _toolSettings;
    String objDumpPath;

    public CommandMemAnalyser( Options options, ToolSettings toolSettings ) {
        this._options = options;
        this._toolSettings = toolSettings;
    }

    public boolean executeCommand() {
        if ( !_toolSettings.checkCompilerPath()
                || !_options.checkInputFile() ) {
            return false;
        }

        if ( !checkObjDumpFile() || !checkMemAnalyserFile() ) {
            return false;
        }

        CommandLine cmdLine = new CommandLine( memAnalyserPath );
        cmdLine.addArgument( objDumpPath, true );
        cmdLine.addArgument( _options.getInputFileObj().getAbsolutePath() );

        System.out.println( "Command Line " + cmdLine.toString() );

        executeCommand( cmdLine );
        return true;
    }

    private boolean executeCommand( CommandLine cmdLine ) {
        boolean status = true;

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog( 60000 );

        executor.setWatchdog( watchdog );
        executor.setWorkingDirectory( _options.getInputFileDir() );

        try {
            executor.execute( cmdLine, resultHandler );
        } catch ( ExecuteException exec ) {

        } catch ( IOException ex ) {
            status = false;
            Logger.getLogger( Esp8266Flasher.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return status;
    }

    private boolean checkMemAnalyserFile() {
        File memAnalyserFile = new File( _toolSettings.getToolsPath() + File.separatorChar + "MemAnalyzer.exe" );

        if ( !memAnalyserFile.exists() ) {
            System.out.println( "MemAnalyzer.exe file not found in the tools path" );
            return false;
        }

        memAnalyserPath = memAnalyserFile.getAbsolutePath();

        return true;
    }

    private boolean checkObjDumpFile() {
        File objDumpFile = new File( _toolSettings.getCompilerPath() + File.separatorChar + "bin/xtensa-lx106-elf-objdump.exe" );

        if ( !objDumpFile.exists() ) {
            System.out.println( "xtensa-lx106-elf-objdump.exe file not found in the compiler path" );
            return false;
        }

        objDumpPath = objDumpFile.getAbsolutePath();

        return true;
    }
}
