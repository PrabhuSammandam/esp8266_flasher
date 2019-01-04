package org.jinjuamla.esp8266.imageflasher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import static org.apache.commons.exec.ExecuteWatchdog.INFINITE_TIMEOUT;

/**
 *
 * @author psammand
 */
public class CommandEsptool {

    private String _blankFilePath;
    private String _espInitDataDefaultBinFilePath;

    Options _options;
    ToolSettings _toolSettings;

    public CommandEsptool( Options options, ToolSettings toolSettings ) {
        this._options = options;
        this._toolSettings = toolSettings;
    }

    public boolean createImage() {
        if ( !_options.checkInputFile()
                || !_toolSettings.checkToolsPath() ) {
            return false;
        }
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptoolPath() );

        cmdLine.addArgument( "elf2image" );

        if ( "None".equals( _options.getBootMode() ) ) {
            //cmdLine.addArgument( "-o" );
            //cmdLine.addArgument( "noboot." );
            cmdLine.addArgument( "--version" );
            cmdLine.addArgument( "1" );
        } else {
            //cmdLine.addArgument( "-o" );
            //cmdLine.addArgument( _options.getOutfileNameForBootloader() );
            cmdLine.addArgument( "--version" );
            cmdLine.addArgument( "2" );
        }

        cmdLine.addArgument( "--flash_freq" );
        cmdLine.addArgument( "${flash_freq}" );
        cmdLine.addArgument( "--flash_mode" );
        cmdLine.addArgument( "${flash_mode}" );
        cmdLine.addArgument( "--flash_size" );
        cmdLine.addArgument( "${flash_size}" );

        cmdLine.addArgument( _options.getInputFileObj().getAbsolutePath(), true );

        cmdLine.setSubstitutionMap( getEsptoolCommandMap() );
        FileUtils.printCommandLine( cmdLine );

        executeCommand( cmdLine, 60000, new CreateImageProcessCompleteHandler() );

        return true;
    }

    public boolean writeDefaultSettings() {
        boolean status = true;
        if ( !_toolSettings.checkToolsPath()
                || !_options.checkComPort()
                || !_toolSettings.checkSdkPath()
                || !checkBlankFile()
                || !checkEspInitDataDefaultBin() ) {
            return false;
        }

        CommandLine cmdLine = getEsptoolWriteFlashCommandLine();

        long flashSize = getFlashSizeValue();
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

            String bootloaderName = null;

            BootloaderSelectionModel bootloaderSelectionModel = dialog.getBootloaderSelectionModel();
            if ( bootloaderSelectionModel.getCustomBootloader() != null
                    && !bootloaderSelectionModel.getCustomBootloader().isEmpty() ) {
                bootloaderName = bootloaderSelectionModel.getCustomBootloader();
            } else {
                if ( bootloaderSelectionModel.getBootloaderFileName() != null
                        && !bootloaderSelectionModel.getBootloaderFileName().isEmpty() ) {
                    bootloaderName = bootloaderSelectionModel.getBootloaderFileName();
                }
            }

            if ( bootloaderName == null || bootloaderName.isEmpty() ) {
                return false;
            }

            cmdLine.addArgument( String.format( "0x%08x", 0 ) );
            cmdLine.addArgument( dialog.getBootloaderSelectionModel().getBootloaderFileName(), true );

        }

        cmdLine.addArgument( String.format( "0x%08x", blank1Addr ) );
        cmdLine.addArgument( _blankFilePath, true );

        cmdLine.addArgument( String.format( "0x%08x", defaultDataBinAddr ) );
        cmdLine.addArgument( _espInitDataDefaultBinFilePath, true );

        cmdLine.addArgument( String.format( "0x%08x", blank2Addr ) );
        cmdLine.addArgument( _blankFilePath, true );

        cmdLine.setSubstitutionMap( getEsptoolCommandMap() );

        FileUtils.printCommandLine( cmdLine );

        status = executeCommand( cmdLine, INFINITE_TIMEOUT, null );
        return status;
    }

    public boolean eraseFlash() {
        if ( !_toolSettings.checkToolsPath()
                || !_options.checkComPort()
                || !_toolSettings.check1MbBlankFile() ) {
            return false;
        }

        CommandLine cmdLine = getEsptoolWriteFlashCommandLine();
        long flashSize = getFlashSizeValue();

        if ( flashSize >= 1024 * 1024 ) {
            for ( int i = 0; i < flashSize / (1024 * 1024); i++ ) {
                cmdLine.addArgument( String.format( "0x%06x", (1024 * 1024) * i ) );
                cmdLine.addArgument( _toolSettings.getBlankFilePath(), true );
            }
            cmdLine.setSubstitutionMap( getEsptoolCommandMap() );
            FileUtils.printCommandLine( cmdLine );
            executeCommand( cmdLine, INFINITE_TIMEOUT, null );
        } else {
            System.out.println( "Flash Size less than 1MB, not erased" );
        }
        return true;
    }

    public boolean writeFlash() {
        boolean status = true;
        if ( !_options.checkComPort()
                || !_options.checkInputFile()
                || !_toolSettings.check() ) {
            return false;
        }
        CommandLine cmdLine = getEsptoolWriteFlashCommandLine();

        if ( _options.isBootModeNone() ) {
            cmdLine.addArgument( "0x00000" );
            cmdLine.addArgument( _options.getFlashBinFile().getAbsolutePath(), true );

//            cmdLine.addArgument( _options.getIrom0textBinAddr() );
//            cmdLine.addArgument( _options.getIrom0textBinFile().getAbsolutePath(), true );
        } else {
//            cmdLine.addArgument( getBinAddrForBootType() );
//            cmdLine.addArgument( _options.getInputFileDir().getAbsolutePath() + File.separatorChar + _options.getOutfileNameForBootloader() );
        }

        cmdLine.setSubstitutionMap( getEsptoolCommandMap() );

        System.out.println( "Flash Command " + cmdLine.toString() );
        System.out.println();

        //status = executeCommand( cmdLine );
        return status;
    }

    public boolean getFlashSize() {
        if ( !_toolSettings.checkToolsPath()
                || !_options.checkComPort() ) {
            return false;
        }
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptoolPath() );
        cmdLine.addArgument( "-p" );
        cmdLine.addArgument( _options.getComPort() );
        cmdLine.addArgument( "-b" );
        cmdLine.addArgument( _options.getComSpeed() );

        cmdLine.addArgument( "flash_id" );

        FileUtils.printCommandLine( cmdLine );
        System.out.println( "" );

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler( stdout );
        DefaultExecutor executor = new DefaultExecutor();
        ExecuteWatchdog watchdog = new ExecuteWatchdog( 60000 );

        executor.setWatchdog( watchdog );
        executor.setWorkingDirectory( _options.getInputFileDir() );
        executor.setStreamHandler( psh );

        try {
            executor.execute( cmdLine );
        } catch ( ExecuteException exec ) {

        } catch ( IOException ex ) {
            Logger.getLogger( Esp8266Flasher.class.getName() ).log( Level.SEVERE, null, ex );
        }

        String[] split = stdout.toString().split( "\n" );

        for ( String string : split ) {
            if ( string.startsWith( "Device:" ) ) {
                String[] idToken = string.split( ":" );
                int size = Integer.parseInt( idToken[ 1 ].trim(), 16 ) & 0xFF;
                printFlashSize( size );
                break;
            }
        }
        return true;
    }

    private boolean executeCommand( CommandLine cmdLine, long timeout, IProcessCompleteHandler completeHndler ) {
        boolean status = true;

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler() {
            @Override
            public void onProcessComplete( int exitValue ) {
                super.onProcessComplete( exitValue );

                if ( completeHndler != null ) {
                    completeHndler.handleProcessComplete( exitValue );
                }
            }
        };

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

    private Map<String, String> getEsptoolCommandMap() {
        Map<String, String> map = new HashMap<>();

        map.put( "flash_freq", getEsptoolFlashFreq() );
        map.put( "flash_mode", _options.getFlashMode().toLowerCase() );
        map.put( "flash_size", getEsptoolFlashSize() );

        return map;
    }

    private long getFlashSizeValue() {
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

    private String getEsptoolFlashFreq() {
        switch ( _options.getFlashSpeed() ) {
            case "40":
                return "40m";
            case "26.7":
                return "26m";
            case "20":
                return "20m";
            case "80":
                return "80m";
        }
        return "";
    }

    private String getEsptoolFlashSize() {
        switch ( _options.getFlashSize() ) {
            case "512 KB [ 4 Mbit ]":
                return "4m";
            case "1 MB [ 8 Mbit ]":
                return "8m";
            case "2 MB [ 16 Mbit ]":
                return "16m";
            case "4 MB [ 32 Mbit ]":
                return "32m";
        }
        return "";
    }

    private CommandLine getEsptoolWriteFlashCommandLine() {
        CommandLine cmdLine = new CommandLine( _toolSettings.getEsptoolPath() );
        cmdLine.addArgument( "-p" );
        cmdLine.addArgument( _options.getComPort() );
        cmdLine.addArgument( "-b" );
        cmdLine.addArgument( _options.getComSpeed() );
        cmdLine.addArgument( "write_flash" );
        cmdLine.addArgument( "--no-progress" );
        cmdLine.addArgument( "-ff" );
        cmdLine.addArgument( "${flash_freq}" );
        cmdLine.addArgument( "-fm" );
        cmdLine.addArgument( "${flash_mode}" );
        cmdLine.addArgument( "-fs" );
        cmdLine.addArgument( "${flash_size}" );

        return cmdLine;
    }

    private void printFlashSize( int size ) {
        switch ( size ) {
            case 18:
                System.out.println( "Flash Size = 256KB [2Mbit]" );
                break;
            case 19:
                System.out.println( "Flash Size = 512KB [4Mbit]" );
                break;
            case 20:
                System.out.println( "Flash Size = 1024KB [8Mbit]" );
                break;
            case 21:
                System.out.println( "Flash Size = 2048KB [16Mbit]" );
                break;
            case 22:
                System.out.println( "Flash Size = 4096KB [32Mbit]" );
                break;
        }
    }

    public interface IProcessCompleteHandler {

        void handleProcessComplete( int exitCode );
    }

    class CreateImageProcessCompleteHandler implements IProcessCompleteHandler {

        @Override
        public void handleProcessComplete( int exitCode ) {
            if ( exitCode == 0 ) {
                File inputFileDir = _options.getInputFileDir();
                System.out.println( "Following images are created" );
                String inFileNamePrefix;

                if ( "None".equals( _options.getBootMode() ) ) {
                    inFileNamePrefix = _options.getInputFileObj().getName() + "-";
                } else {
                    inFileNamePrefix = FilenameUtils.getBaseName( _options.getInputFileObj().getName() ) + "-";
                }

                FileFilter fileFilter = new RegexFileFilter( "^" + inFileNamePrefix + ".*" + "\\.bin$" );
                File[] files = inputFileDir.listFiles( fileFilter );
                for ( File file : files ) {
                    System.out.println( file );
                }
            }
        }
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

}
