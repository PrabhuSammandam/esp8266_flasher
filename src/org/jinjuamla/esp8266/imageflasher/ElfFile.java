/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.esp8266.imageflasher;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.exec.*;

/**
 *
 * @author psammand
 */
public class ElfFile {

    File _file;
    Options _options;
    ToolSettings _toolSettings;
    HashMap<String, Segment> _segments;

    public ElfFile( Options options, ToolSettings toolSettings ) {
        this._segments = new HashMap<>();
        this._options = options;
        this._toolSettings = toolSettings;
    }

    public long getIrom0StartAddr() {
        boolean loadStatus = false;
        if ( _segments.size() <= 0 ) {
            try {
                loadStatus = loadSegmentsAddr();
            } catch ( IOException ex ) {
                Logger.getLogger( ElfFile.class.getName() ).log( Level.SEVERE, null, ex );
            }
        } else {
            loadStatus = true;
        }

        if ( !loadStatus ) {
            return 0;
        }

        if ( _segments.containsKey( ".irom0.text" ) ) {
            return _segments.get( ".irom0.text" ).getStartAddr();
        }

        return 0;
    }

    public long getIrom0FlashAddr() {
        return getIrom0StartAddr() - 0x40200000;
    }

    public long getIrom0FlashAddrAligned() {
        long addr = getIrom0StartAddr() - 0x40200000;
        return addr & ~(0x1000 - 1);
    }

    public boolean loadSegmentsAddr() throws IOException {
        if ( !_options.checkInputFile()
                || !_toolSettings.checkCompilerPath() ) {
            return false;
        }

        File readElf = new File( _toolSettings.getCompilerPath() + File.separatorChar + "bin/xtensa-lx106-elf-readelf.exe" );

        if ( !readElf.exists() ) {
            System.out.println( "xtensa-lx106-elf-readelf.exe not exist" );
        }

        CommandLine cmdLine = new CommandLine( readElf.getAbsoluteFile() );

        cmdLine.addArgument( "-S" );
        cmdLine.addArgument( _options.getInputFileObj().getAbsolutePath(), true );

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PumpStreamHandler ps = new PumpStreamHandler( os );
        ExecuteWatchdog watchdog = new ExecuteWatchdog( 1000000 );
        Executor executor = new DefaultExecutor();
        executor.setWatchdog( watchdog );
        executor.setStreamHandler( ps );
        try {
            executor.execute( cmdLine );
        } catch ( IOException ex ) {
            Logger.getLogger( ElfFile.class.getName() ).log( Level.SEVERE, null, ex );
        }

        Reader reader = new InputStreamReader( new ByteArrayInputStream( os.toByteArray() ) );
        BufferedReader r = new BufferedReader( reader );
        String tmp = null;
        Pattern iromPattern = Pattern.compile( ".*(\\.irom0.text) *PROGBITS *([a-f0-9]*) ([a-f0-9]*) ([a-f0-9]*).*" );
        Pattern dataPattern = Pattern.compile( ".*(\\.data) *PROGBITS *([a-f0-9]*) ([a-f0-9]*) ([a-f0-9]*).*" );
        Pattern rodataPattern = Pattern.compile( ".*(\\.rodata) *PROGBITS *([a-f0-9]*) ([a-f0-9]*) ([a-f0-9]*).*" );
        Pattern bssPattern = Pattern.compile( ".*(\\.bss) *NOBITS *([a-f0-9]*) ([a-f0-9]*) ([a-f0-9]*).*" );
        Pattern textPattern = Pattern.compile( ".*(\\.text) *PROGBITS *([a-f0-9]*) ([a-f0-9]*) ([a-f0-9]*).*" );

        while ( (tmp = r.readLine()) != null ) {
            Matcher iromPatternMatcher = iromPattern.matcher( tmp );

            if ( iromPatternMatcher.matches() ) {
                Segment segment = getSegment( iromPatternMatcher );
                _segments.put( segment.getName(), segment );
                continue;
            }
            Matcher dataPatternMatcher = dataPattern.matcher( tmp );

            if ( dataPatternMatcher.matches() ) {
                Segment segment = getSegment( dataPatternMatcher );
                _segments.put( segment.getName(), segment );
                continue;
            }
            Matcher rodataPatternMatcher = rodataPattern.matcher( tmp );

            if ( rodataPatternMatcher.matches() ) {
                Segment segment = getSegment( rodataPatternMatcher );
                _segments.put( segment.getName(), segment );
                continue;
            }
            Matcher bssPatternMatcher = bssPattern.matcher( tmp );

            if ( bssPatternMatcher.matches() ) {
                Segment segment = getSegment( bssPatternMatcher );
                _segments.put( segment.getName(), segment );
                continue;
            }
            Matcher textPatternMatcher = textPattern.matcher( tmp );

            if ( textPatternMatcher.matches() ) {
                Segment segment = getSegment( textPatternMatcher );
                _segments.put( segment.getName(), segment );
            }
        }

        return true;
    }

    private Segment getSegment( Matcher matcher ) {
        long startAddr = 0;
        long offset = 0;
        long size = 0;

        if ( matcher.group( 2 ) != null && !matcher.group( 2 ).isEmpty() ) {
            startAddr = Long.parseLong( matcher.group( 2 ), 16 );
        }
        if ( matcher.group( 3 ) != null && !matcher.group( 3 ).isEmpty() ) {
            offset = Long.parseLong( matcher.group( 3 ), 16 );
        }
        if ( matcher.group( 4 ) != null && !matcher.group( 4 ).isEmpty() ) {
            size = Long.parseLong( matcher.group( 4 ), 16 );
        }

        return new Segment( matcher.group( 1 ), startAddr, size, offset );
    }

}
