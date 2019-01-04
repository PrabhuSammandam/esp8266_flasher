/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.NameFileFilter;

/**
 *
 * @author psammand
 */
public class CommandEsptool2 {

    Options _options;
    ToolSettings _toolSettings;
    boolean _toFlash = false;
    ElfFile _elfFile = null;

    public CommandEsptool2( Options options, ToolSettings toolSettings ) {
        this._options = options;
        this._toolSettings = toolSettings;

        if ( _options.checkInputFile() ) {
            _elfFile = new ElfFile( options, toolSettings );
            try {
                _elfFile.loadSegmentsAddr();
            } catch ( IOException ex ) {
                Logger.getLogger( CommandEsptool2.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
    }

    public boolean CreateImageAndFlash() {
        _toFlash = true;
        return CreateImage();
    }

    public boolean CreateImage() {
        if ( !_options.checkInputFile() || !_toolSettings.check() ) {
            return false;
        }
        CommandLine cmdLine = null;

        if ( "None".equals( _options.getBootMode() ) ) {
            long irom0StartAddr = 0;
            if ( _elfFile != null ) {
                irom0StartAddr = _elfFile.getIrom0FlashAddrAligned();
            }

            String irom0TextFilePath = _options.getInputFileDir().getAbsolutePath() + File.separatorChar + String.format( "0x%x.bin", irom0StartAddr );

            cmdLine = getLibOptionParameters( irom0TextFilePath );

            if ( !executeCommand( cmdLine, new CreateImageProcessCompleteHandler( irom0TextFilePath, false ) ) ) {
                return false;
            }
        }

        cmdLine = getBinOptionParameters( getBootloadOption(), getOutBinName() );

        FileUtils.printCommandLine( cmdLine );
        if ( !executeCommand( cmdLine, new CreateImageProcessCompleteHandler( getOutBinName(), _toFlash ) ) ) {
            return false;
        }
        return true;
    }

    private String getBootloadOption() {
        if ( "None".equals( _options.getBootMode() ) ) {
            return "-boot0";
        }
        return "Old".equals( _options.getBootMode() ) ? "-boot1" : "-boot2";
    }

    private String getOutBinName() {
        if ( "None".equals( _options.getBootMode() ) ) {
            return _options.getInputFileDir().getAbsolutePath() + File.separatorChar + "0x00000.bin";
        }

        long irom0StartAddr = 0;
        if ( _elfFile != null ) {
            irom0StartAddr = _elfFile.getIrom0FlashAddrAligned();
        }

        return _options.getInputFileDir().getAbsolutePath() + File.separatorChar + String.format( "0x%x.bin", irom0StartAddr );
    }

    private void handleProcessComplete( int exitValue ) {
        if ( exitValue == 0 && _toFlash == true ) {
            CommandEsptool cmdEsptoolck = new CommandEsptool( _options, _toolSettings );
            cmdEsptoolck.writeFlash();
        }
    }

    private boolean executeCommand( CommandLine cmdLine, IProcessCompleteHandler processCompleteHandler ) {
        boolean status = true;
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog( 60000 );
        executor.setWatchdog( watchdog );

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
            @Override
            public void onProcessComplete( int exitValue ) {
                super.onProcessComplete( exitValue );
                if ( processCompleteHandler != null ) {
                    processCompleteHandler.handleProcessComplete( exitValue );
                }
            }
        };
        executor.setWorkingDirectory( _options.getInputFileDir() );

        try {
            executor.execute( cmdLine, resultHandler );
        } catch ( IOException ex ) {
            status = false;
            Logger.getLogger( Esp8266Flasher.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return status;
    }

    private CommandLine getLibOptionParameters( String outfileName ) {
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptool2Path() );
        cmdLine.addArgument( "-quiet" );
        cmdLine.addArgument( "-lib" );
        cmdLine.addArgument( _options.getInputFileObj().getAbsolutePath(), true );
        cmdLine.addArgument( outfileName, true );

        return cmdLine;
    }

    private CommandLine getBinOptionParameters( String bootMode, String outfileName ) {
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptool2Path() );
        cmdLine.addArgument( "-quiet" );
        cmdLine.addArgument( "-bin" );
        cmdLine.addArgument( bootMode );
        cmdLine.addArgument( getEspTool2FlashMap() );
        cmdLine.addArgument( getEspTool2FlashMode() );
        cmdLine.addArgument( getEspTool2FlashSpeed() );
        cmdLine.addArgument( _options.getInputFileObj().getAbsolutePath(), true );
        cmdLine.addArgument( outfileName, true );
        cmdLine.addArgument( ".text" );
        cmdLine.addArgument( ".data" );
        cmdLine.addArgument( ".rodata" );

        return cmdLine;
    }

    private String getEspTool2FlashMap() {
        switch ( _options.getFlashSize() ) {
            case "512 KB [ 4 Mbit ]":
                return "-512";
            case "1 MB [ 8 Mbit ]":
                return "-1024";
            case "2 MB [ 16 Mbit ]":
                return "-2048";
            case "4 MB [ 32 Mbit ]":
                return "-4096";
        }
        return "";
    }

    private String getEspTool2FlashMode() {
        return "-" + _options.getFlashMode().toLowerCase();
    }

    private String getEspTool2FlashSpeed() {
        return "-" + _options.getFlashSpeed();
    }

    public interface IProcessCompleteHandler {

        void handleProcessComplete( int exitCode );
    }

    class CreateImageProcessCompleteHandler implements IProcessCompleteHandler {

        private final boolean _callbackFlashCommand;
        String _outFileName;

        public CreateImageProcessCompleteHandler( String outFileName, boolean callbackFlashCommand ) {
            this._outFileName = outFileName;
            this._callbackFlashCommand = callbackFlashCommand;
        }

        @Override
        public void handleProcessComplete( int exitCode ) {
            if ( exitCode == 0 ) {
                File inputFileDir = _options.getInputFileDir();

                FileFilter fileFilter = new NameFileFilter( FilenameUtils.getName( _outFileName ) );
                File[] files = inputFileDir.listFiles( fileFilter );

                if ( files.length > 0 ) {
                    System.out.println( "Created output image file " + _outFileName );
                    System.out.println( "" );

                    if ( _callbackFlashCommand == true ) {
                        CommandEsptoolCk cmdEsptoolCk = new CommandEsptoolCk( _options, _toolSettings );
                        cmdEsptoolCk.writeFlash();
                    }
                } else {
                    System.out.println( "Failed to created output image file " + _outFileName );
                }
            }
        }
    }

}
