package org.jinjuamla.esp8266.imageflasher;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import org.apache.commons.exec.*;
import static org.apache.commons.exec.ExecuteWatchdog.INFINITE_TIMEOUT;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;

public class CommandEsptoolCk {

    public static final long FLASH_SIZE_512KB = 512 * 1024;
    public static final long FLASH_SIZE_1MB = 1024 * 1024;
    public static final long FLASH_SIZE_2MB = 2 * 1024 * 1024;
    public static final long FLASH_SIZE_4MB = 4 * 1024 * 1024;

    private String _blankFilePath;
    private String _espInitDataDefaultBinFilePath;

    Options _options;
    ToolSettings _toolSettings;
    ElfFile _elfFile;

    public CommandEsptoolCk( Options options, ToolSettings toolSettings ) {
        this._elfFile = null;
        this._options = options;
        this._toolSettings = toolSettings;
    }

    public boolean writeDefaultSettings() {
        boolean status = true;
        if ( !_toolSettings.checkToolsPath()
                //                || !_options.checkComPort()
                || !_toolSettings.checkSdkPath()
                || !checkBlankFile()
                || !checkEspInitDataDefaultBin() ) {
            return false;
        }

        CommandLine cmdLine = getCommandLine();

        long flashSize = getFlashSize();
        long blank1Addr = flashSize - 5 * 0x1000;
        long blank2Addr = flashSize - 2 * 0x1000;
        long defaultDataBinAddr = flashSize - 4 * 0x1000;

        if ( !_options.getBootMode().equals( "None" ) ) {
            BootLoaderSelectionDialog dialog = new BootLoaderSelectionDialog();
            dialog.setModal( true );
            FileFilter fileFilter = new PrefixFileFilter( "boot_" );
            File sdkDirFile = new File( _toolSettings.getSdkPath() + File.separatorChar + "bin" );
            File[] files = sdkDirFile.listFiles( fileFilter );

            DefaultListModel<String> listModel = new DefaultListModel<>();

            for ( File file : files ) {
                listModel.addElement( file.getAbsolutePath() );
            }

            dialog.setListModel( listModel );

            dialog.setVisible( true );

            if ( dialog.getResult() == 0 ) {
                return false;
            }

//            System.out.println( "Selected bootloader " + dialog.getBootloaderSelectionModel().getBootloaderFileName() );
            cmdLine.addArgument( "-ca" );
            cmdLine.addArgument( String.format( "0x%08x", 0 ) );
            cmdLine.addArgument( "-cf" );
            cmdLine.addArgument( dialog.getBootloaderSelectionModel().getBootloaderFileName(), true );

        }

        cmdLine.addArgument( "-ca" );
        cmdLine.addArgument( String.format( "0x%08x", blank1Addr ) );
        cmdLine.addArgument( "-cf" );
        cmdLine.addArgument( _blankFilePath, true );

        cmdLine.addArgument( "-ca" );
        cmdLine.addArgument( String.format( "0x%08x", defaultDataBinAddr ) );
        cmdLine.addArgument( "-cf" );
        cmdLine.addArgument( _espInitDataDefaultBinFilePath, true );

        cmdLine.addArgument( "-ca" );
        cmdLine.addArgument( String.format( "0x%08x", blank2Addr ) );
        cmdLine.addArgument( "-cf" );
        cmdLine.addArgument( _blankFilePath, true );

        FileUtils.printCommandLine( cmdLine );
        
        status = executeCommandWithTimeout( cmdLine, INFINITE_TIMEOUT );

        return status;
    }

    public boolean eraseFlash() {
        boolean status = true;
        if ( !_toolSettings.checkToolsPath()
                || !_options.checkComPort()
                || !_toolSettings.check1MbBlankFile() ) {
            return false;
        }

        CommandLine cmdLine = getCommandLine();

        long flashSize = getFlashSize();

        if ( flashSize >= 1024 * 1024 ) {
            for ( int i = 0; i < flashSize / (1024 * 1024); i++ ) {
                cmdLine.addArgument( "-ca" );
                cmdLine.addArgument( String.format( "0x%06x", (1024 * 1024) * i ) );
                cmdLine.addArgument( "-cf" );
                cmdLine.addArgument( _toolSettings.getBlankFilePath(), true );
            }
            FileUtils.printCommandLine( cmdLine );
            status = executeCommandWithTimeout( cmdLine, INFINITE_TIMEOUT );
        } else {
            System.out.println( "Flash Size less than 1MB, not erased" );
        }
        return status;
    }

    public boolean writeFlash() {
        boolean status = true;
        if ( !_options.checkComPort()
                || !_options.checkInputFile()
                || !_toolSettings.check() ) {
            return false;
        }

        CommandLine cmdLine = getCommandLine();

        if ( _options.isBootModeNone() ) {
            cmdLine.addArgument( "-ca" );
            cmdLine.addArgument( "0x00000" );
            cmdLine.addArgument( "-cf" );
            cmdLine.addArgument( String.format( "%s%s%s", _options.getInputFileDir(), File.separatorChar, "0x00000.bin" ), true );

            cmdLine.addArgument( "-ca" );
            cmdLine.addArgument( getIrom0textBinAddr() );
            cmdLine.addArgument( "-cf" );
            cmdLine.addArgument( String.format( "%s%s%s", _options.getInputFileDir(), File.separatorChar, getIrom0TextBinFileName() ), true );
        } else {
            cmdLine.addArgument( "-ca" );
            cmdLine.addArgument( getIrom0textBinAddr() );

            cmdLine.addArgument( "-cf" );
            cmdLine.addArgument( String.format( "%s%s%s", _options.getInputFileDir(), File.separatorChar, getIrom0TextBinFileName() ), true );
        }

        FileUtils.printCommandLine( cmdLine );

        status = executeCommand( cmdLine );
        return status;
    }

    private boolean checkBlankFile() {
        if ( !_toolSettings.checkSdkPath() ) {
            return false;
        }

        File blankFile = new File( _toolSettings.getSdkPath() + File.separatorChar + "bin/blank.bin" );

        if ( !blankFile.exists() ) {
            return false;
        }

        _blankFilePath = blankFile.getAbsolutePath();

        return true;
    }

    private boolean checkEspInitDataDefaultBin() {
        if ( !_toolSettings.checkSdkPath() ) {
            return false;
        }

        File espInitDataDefaultBinFile = new File( _toolSettings.getSdkPath() + File.separatorChar + "bin/esp_init_data_default.bin" );

        if ( !espInitDataDefaultBinFile.exists() ) {
            return false;
        }

        _espInitDataDefaultBinFilePath = espInitDataDefaultBinFile.getAbsolutePath();

        return true;
    }

    private long getFlashSize() {
        switch ( _options.getFlashSize() ) {
            case "512 KB [ 4 Mbit ]":
                return 512 * 1024;
            case "1 MB [ 8 Mbit ]":
                return 1 * 1024 * 1024;
            case "2 MB [ 16 Mbit ]":
                return 2 * 1024 * 1024;
            case "4 MB [ 32 Mbit ]":
                return 4 * 1024 * 1024;
        }
        return 0;
    }

    private String getIrom0TextBinFileName() {
        return getIrom0textBinAddr() + ".bin";
    }

    private String getIrom0textBinAddr() {
        if ( _elfFile == null ) {
            _elfFile = new ElfFile( _options, _toolSettings );
            try {
                _elfFile.loadSegmentsAddr();
            } catch ( IOException ex ) {
                Logger.getLogger( CommandEsptoolCk.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }

        return String.format( "0x%x", _elfFile.getIrom0FlashAddrAligned() );
    }

    private CommandLine getCommandLine() {
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptoolckPath() );

        cmdLine.addArgument( "-v" );
        cmdLine.addArgument( "-cd" );
        cmdLine.addArgument( "ck" );
        cmdLine.addArgument( "-cb" );
        cmdLine.addArgument( _options.getComSpeed() );
        cmdLine.addArgument( "-cp" );
        cmdLine.addArgument( _options.getComPort() );
        return cmdLine;
    }

    private boolean executeCommand( CommandLine cmdLine ) {
        return executeCommandWithTimeout( cmdLine, 60000 );
    }

    private boolean executeCommandWithTimeout( CommandLine cmdLine, long timeout ) {
        boolean status = true;

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog( timeout );

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

}
