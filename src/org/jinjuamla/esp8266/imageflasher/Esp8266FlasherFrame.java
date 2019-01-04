package org.jinjuamla.esp8266.imageflasher;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import jssc.SerialPortList;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;

public class Esp8266FlasherFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public Esp8266FlasherFrame() {
        initComponents();

        PrintStream printStream = new PrintStream( new CustomOutputStream( _outputViewTxt ) );
        System.setOut( printStream );
        System.setErr( printStream );

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource("memory_details.html");
        try {
			_memoryDetailsEditor.setPage( resource );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//        File file = new File( Paths.get( "." ).toAbsolutePath().normalize().toString() + File.separator + "memory_details.html" );
//
//        if ( file.exists() ) {
//            try {
//                URL fileUrl = file.toURI().toURL();
//                _memoryDetailsEditor.setPage( fileUrl );
//            } catch ( MalformedURLException ex ) {
//                Logger.getLogger( Esp8266FlasherFrame.class.getName() ).log( Level.SEVERE, null, ex );
//            } catch ( IOException ex ) {
//                Logger.getLogger( Esp8266FlasherFrame.class.getName() ).log( Level.SEVERE, null, ex );
//            }
//        }

        updateComPort();
        reloadSessions();
    }

    public final void reloadSessions() {
        _sessionCombo.setModel( getConfigComboBoxModel() );

        String lastSession = ( String ) Settings.getInst().getConfig().getProperty( "last_session" );
        options.setCurrentSession( lastSession );
        _sessionCombo.setSelectedItem( lastSession );
    }

    public final void loadConfig( int configIndex ) {
        Configuration config = Settings.getInst().getConfig();
        String configKey = String.format( "configs.config(%d).", configIndex );

        String toolsPath = Settings.getInst().getConfig().getString( configKey + "tools_path" );
        toolSettings.setToolsPath( toolsPath );
        toolSettings.setSdkPath( config.getString( configKey + "sdk_path" ) );
        toolSettings.setCompilerPath( config.getString( configKey + "compiler_path" ) );
        options.setFlashSize( config.getString( configKey + "flash_size" ) );
        options.setFlashSpeed( config.getString( configKey + "flash_speed" ) );
        options.setFlashMode( config.getString( configKey + "flash_mode" ) );
        options.setBootMode( config.getString( configKey + "boot_mode" ) );
        options.setInputFile( config.getString( configKey + "outfilepath" ) );
        options.setComSpeed( config.getString( configKey + "com_speed" ) );
    }

    final DefaultComboBoxModel<String> getConfigComboBoxModel() {
        XMLConfiguration config = ( XMLConfiguration ) Settings.getInst().getConfig();
        List<Object> configList = config.getList( "configs.config[@name]" );

        DefaultComboBoxModel<String> configComboBoxModel = new DefaultComboBoxModel<>();

        configList.forEach( (Object item) -> {
            configComboBoxModel.addElement( ( String ) item );
        } );

        return configComboBoxModel;
    }

    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        options = new Options();
        toolSettings = new ToolSettings();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        _inputFileTxt = new javax.swing.JTextField();
        _inputFileTxt.setDropTarget(new BinaryFileDropTarget());
        _inputFileBut = new javax.swing.JButton();
        _inputFileOpenExplorerBut = new javax.swing.JButton();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        _bootCombo = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        _flashModeCombo = new javax.swing.JComboBox<>();
        _flashSpeedCombo = new javax.swing.JComboBox<>();
        _flashSizeCombo = new javax.swing.JComboBox<>();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        _comSpeedCombo = new javax.swing.JComboBox<>();
        _comPortBut = new javax.swing.JButton();
        _comPortCombo = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel8 = new javax.swing.JLabel();
        _toolsPathTxt = new javax.swing.JTextField();
        _toolsPathTxt.setDropTarget(new DirectoryDropTarget(_toolsPathTxt));
        _toolsPathBut = new javax.swing.JButton();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();
        _sdkPathTxt = new javax.swing.JTextField();
        _sdkPathTxt.setDropTarget(new DirectoryDropTarget(_sdkPathTxt));
        _sdkPathBut = new javax.swing.JButton();
        javax.swing.JLabel jLabel11 = new javax.swing.JLabel();
        _compilerPathTxt = new javax.swing.JTextField();
        _compilerPathTxt.setDropTarget(new DirectoryDropTarget(_compilerPathTxt));
        _compilerPathBut = new javax.swing.JButton();
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        _createBinaryBut = new javax.swing.JButton();
        _flashBinaryBut = new javax.swing.JButton();
        _createAndFlashBut = new javax.swing.JButton();
        _fullEraseFlashBut = new javax.swing.JButton();
        _flashDefaultSettingsBut = new javax.swing.JButton();
        _getFlashIdBut = new javax.swing.JButton();
        _memUpdateBut = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        _clearOutputViewBut = new javax.swing.JButton();
        javax.swing.JPanel jPanel7 = new javax.swing.JPanel();
        _saveSessionBut = new javax.swing.JButton();
        _sessionCombo = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        _newSessionBut = new javax.swing.JButton();
        _deleteSessionBut = new javax.swing.JButton();
        _saveAsSessionBut = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        _outputViewTxt = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        _memoryDetailsEditor = new javax.swing.JEditorPane();
        _memoryDetailsEditor.setFont(new Font(Font.SANS_SERIF, 3, 16));

        FormListener formListener = new FormListener();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Esp8266 Flasher");
        setPreferredSize(new java.awt.Dimension(1000, 700));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Input File"));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org.jinjuamla.esp8266.imageflasher.Bundle"); // NOI18N
        _inputFileTxt.setToolTipText(bundle.getString("inputFileText")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${inputFile}"), _inputFileTxt, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        _inputFileBut.setText("Browse...");
        _inputFileBut.addActionListener(formListener);

        _inputFileOpenExplorerBut.setText("Open Explorer");
        _inputFileOpenExplorerBut.addActionListener(formListener);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_inputFileTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_inputFileBut, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_inputFileOpenExplorerBut))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(_inputFileTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(_inputFileBut)
                .addComponent(_inputFileOpenExplorerBut))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));

        jLabel1.setText("Boot Mode:");

        _bootCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "New", "Old" }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${bootMode}"), _bootCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _bootCombo.addActionListener(formListener);

        jLabel2.setText("Flash Mode :");

        jLabel4.setText("Flash Speed :");

        jLabel5.setText("Flash Size :");

        _flashModeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "QIO", "QOUT", "DIO", "DOUT" }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${flashMode}"), _flashModeCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _flashSpeedCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "40", "26.7", "20", "80" }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${flashSpeed}"), _flashSpeedCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _flashSizeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "512 KB [ 4 Mbit ]", "1 MB [ 8 Mbit ]", "2 MB [ 16 Mbit ]", "4 MB [ 32 Mbit ]" }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${flashSize}"), _flashSizeCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _flashSizeCombo.addActionListener(formListener);

        jLabel6.setText("COM Port :");

        jLabel7.setText("COM Speed :");

        _comSpeedCombo.setEditable(true);
        _comSpeedCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600" }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${comSpeed}"), _comSpeedCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _comSpeedCombo.addActionListener(formListener);

        _comPortBut.setText("Detect");
        _comPortBut.addActionListener(formListener);

        _comPortCombo.setEditable(true);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${comPort}"), _comPortCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_flashSpeedCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_bootCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_comSpeedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(_comPortCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_comPortBut))
                    .addComponent(_flashModeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_flashSizeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(_comSpeedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(_comPortCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_comPortBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(_bootCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(_flashModeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(_flashSpeedCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(_flashSizeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Tool Setting"));

        jLabel8.setText("Tools Path :");

        _toolsPathTxt.setToolTipText(bundle.getString("_toolPath")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, toolSettings, org.jdesktop.beansbinding.ELProperty.create("${toolsPath}"), _toolsPathTxt, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        _toolsPathBut.setText("...");
        _toolsPathBut.addActionListener(formListener);

        jLabel10.setText("SDK Path :");

        _sdkPathTxt.setToolTipText(bundle.getString("sdkPath")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, toolSettings, org.jdesktop.beansbinding.ELProperty.create("${sdkPath}"), _sdkPathTxt, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        _sdkPathBut.setText("...");
        _sdkPathBut.addActionListener(formListener);

        jLabel11.setText("Compiler Path :");

        _compilerPathTxt.setToolTipText(bundle.getString("compilerPath")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, toolSettings, org.jdesktop.beansbinding.ELProperty.create("${compilerPath}"), _compilerPathTxt, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        _compilerPathBut.setText("...");
        _compilerPathBut.addActionListener(formListener);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(_toolsPathTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_toolsPathBut))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(_sdkPathTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_sdkPathBut))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(_compilerPathTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_compilerPathBut))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(_toolsPathTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(_toolsPathBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(_sdkPathTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(_sdkPathBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(_compilerPathTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(_compilerPathBut)))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Command"));

        _createBinaryBut.setText("Create Image");
        _createBinaryBut.addActionListener(formListener);

        _flashBinaryBut.setText("Flash Image");
        _flashBinaryBut.addActionListener(formListener);

        _createAndFlashBut.setText("Create & Flash Image");
        _createAndFlashBut.addActionListener(formListener);

        _fullEraseFlashBut.setText("Erase Flash");
        _fullEraseFlashBut.setActionCommand("");
        _fullEraseFlashBut.addActionListener(formListener);

        _flashDefaultSettingsBut.setText("Flash Default Settings");
        _flashDefaultSettingsBut.addActionListener(formListener);

        _getFlashIdBut.setText("Get Flash Size");
        _getFlashIdBut.addActionListener(formListener);

        _memUpdateBut.setText("Get Mem Usage");
        _memUpdateBut.addActionListener(formListener);

        _clearOutputViewBut.setText("Clear Output View");
        _clearOutputViewBut.addActionListener(formListener);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(_getFlashIdBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_flashDefaultSettingsBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_fullEraseFlashBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_memUpdateBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_createAndFlashBut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_flashBinaryBut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(_createBinaryBut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addComponent(_clearOutputViewBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(_createBinaryBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_flashBinaryBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_createAndFlashBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_memUpdateBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(_fullEraseFlashBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_flashDefaultSettingsBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_getFlashIdBut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(_clearOutputViewBut)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Session"));

        _saveSessionBut.setText("Save");
        _saveSessionBut.addActionListener(formListener);

        _sessionCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, options, org.jdesktop.beansbinding.ELProperty.create("${currentSession}"), _sessionCombo, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        _sessionCombo.addActionListener(formListener);

        jLabel20.setText("Load :");

        _newSessionBut.setText("New");
        _newSessionBut.addActionListener(formListener);

        _deleteSessionBut.setText("Delete");
        _deleteSessionBut.addActionListener(formListener);

        _saveAsSessionBut.setText("Save As");
        _saveAsSessionBut.addActionListener(formListener);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_sessionCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(_saveSessionBut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                            .addComponent(_newSessionBut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(_deleteSessionBut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(_saveAsSessionBut, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_sessionCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_newSessionBut)
                    .addComponent(_deleteSessionBut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_saveSessionBut)
                    .addComponent(_saveAsSessionBut)))
        );

        _outputViewTxt.setColumns(20);
        _outputViewTxt.setRows(5);
        jScrollPane1.setViewportView(_outputViewTxt);

        jTabbedPane1.addTab("Output", jScrollPane1);

        _memoryDetailsEditor.setEditable(false);
        _memoryDetailsEditor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jScrollPane2.setViewportView(_memoryDetailsEditor);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 905, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Memory Details", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.ActionListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == _inputFileBut) {
                Esp8266FlasherFrame.this._inputFileButActionPerformed(evt);
            }
            else if (evt.getSource() == _inputFileOpenExplorerBut) {
                Esp8266FlasherFrame.this._inputFileOpenExplorerButActionPerformed(evt);
            }
            else if (evt.getSource() == _bootCombo) {
                Esp8266FlasherFrame.this._bootComboActionPerformed(evt);
            }
            else if (evt.getSource() == _flashSizeCombo) {
                Esp8266FlasherFrame.this._flashSizeComboActionPerformed(evt);
            }
            else if (evt.getSource() == _comSpeedCombo) {
                Esp8266FlasherFrame.this._comSpeedComboActionPerformed(evt);
            }
            else if (evt.getSource() == _comPortBut) {
                Esp8266FlasherFrame.this._comPortButActionPerformed(evt);
            }
            else if (evt.getSource() == _toolsPathBut) {
                Esp8266FlasherFrame.this._toolsPathButActionPerformed(evt);
            }
            else if (evt.getSource() == _sdkPathBut) {
                Esp8266FlasherFrame.this._sdkPathButActionPerformed(evt);
            }
            else if (evt.getSource() == _compilerPathBut) {
                Esp8266FlasherFrame.this._compilerPathButActionPerformed(evt);
            }
            else if (evt.getSource() == _createBinaryBut) {
                Esp8266FlasherFrame.this._createBinaryButActionPerformed(evt);
            }
            else if (evt.getSource() == _flashBinaryBut) {
                Esp8266FlasherFrame.this._flashBinaryButActionPerformed(evt);
            }
            else if (evt.getSource() == _createAndFlashBut) {
                Esp8266FlasherFrame.this._createAndFlashButActionPerformed(evt);
            }
            else if (evt.getSource() == _fullEraseFlashBut) {
                Esp8266FlasherFrame.this._fullEraseFlashButActionPerformed(evt);
            }
            else if (evt.getSource() == _flashDefaultSettingsBut) {
                Esp8266FlasherFrame.this._flashDefaultSettingsButActionPerformed(evt);
            }
            else if (evt.getSource() == _getFlashIdBut) {
                Esp8266FlasherFrame.this._getFlashIdButActionPerformed(evt);
            }
            else if (evt.getSource() == _memUpdateBut) {
                Esp8266FlasherFrame.this._memUpdateButActionPerformed(evt);
            }
            else if (evt.getSource() == _clearOutputViewBut) {
                Esp8266FlasherFrame.this._clearOutputViewButActionPerformed(evt);
            }
            else if (evt.getSource() == _saveSessionBut) {
                Esp8266FlasherFrame.this._saveSessionButActionPerformed(evt);
            }
            else if (evt.getSource() == _sessionCombo) {
                Esp8266FlasherFrame.this._sessionComboActionPerformed(evt);
            }
            else if (evt.getSource() == _newSessionBut) {
                Esp8266FlasherFrame.this._newSessionButActionPerformed(evt);
            }
            else if (evt.getSource() == _deleteSessionBut) {
                Esp8266FlasherFrame.this._deleteSessionButActionPerformed(evt);
            }
            else if (evt.getSource() == _saveAsSessionBut) {
                Esp8266FlasherFrame.this._saveAsSessionButActionPerformed(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents

    private void _comPortButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__comPortButActionPerformed
        updateComPort();
    }//GEN-LAST:event__comPortButActionPerformed

    private void updateComPort() {
        String[] portNames = SerialPortList.getPortNames();

        if ( portNames == null ) {
            return;
        }

        DefaultComboBoxModel<String> conmboBoxModel = new DefaultComboBoxModel<>();

        for ( String portName : portNames ) {
            conmboBoxModel.addElement( portName );
        }

        _comPortCombo.setModel( conmboBoxModel );
    }

    private void _inputFileButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__inputFileButActionPerformed
        String binaryFile = FileUtils.getBinaryFile( this );

        if ( binaryFile != null ) {
            _inputFileTxt.setText( binaryFile );
        }
    }//GEN-LAST:event__inputFileButActionPerformed

    private void _comSpeedComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__comSpeedComboActionPerformed
//        _outputViewTxt.setText( options.getComSpeed() );
    }//GEN-LAST:event__comSpeedComboActionPerformed

    private void _createAndFlashButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__createAndFlashButActionPerformed
        clearOutput();
        CommandEsptool2 cmdEsptool2 = new CommandEsptool2( options, toolSettings );
        cmdEsptool2.CreateImageAndFlash();
    }//GEN-LAST:event__createAndFlashButActionPerformed

    private void _toolsPathButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__toolsPathButActionPerformed
        String toolsDir = FileUtils.getDirectory( this );
        if ( toolsDir != null ) {
            _toolsPathTxt.setText( toolsDir );
        }
    }//GEN-LAST:event__toolsPathButActionPerformed

    private void _sdkPathButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__sdkPathButActionPerformed
        String sdkDir = FileUtils.getDirectory( this );
        if ( sdkDir != null ) {
            _sdkPathTxt.setText( sdkDir );
        }
    }//GEN-LAST:event__sdkPathButActionPerformed

    private void _compilerPathButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__compilerPathButActionPerformed
        String compilerDir = FileUtils.getDirectory( this );
        if ( compilerDir != null ) {
            _compilerPathTxt.setText( compilerDir );
        }
    }//GEN-LAST:event__compilerPathButActionPerformed

    private void _bootComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bootComboActionPerformed
        updateAddressFields();
    }//GEN-LAST:event__bootComboActionPerformed

    private void _flashSizeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__flashSizeComboActionPerformed
        //updateAddressFields();
    }//GEN-LAST:event__flashSizeComboActionPerformed

    private void _clearOutputViewButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__clearOutputViewButActionPerformed
        clearOutput();
    }//GEN-LAST:event__clearOutputViewButActionPerformed

    private void _createBinaryButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__createBinaryButActionPerformed
        clearOutput();
        CommandEsptool2 cmdEsptool2 = new CommandEsptool2( options, toolSettings );
        cmdEsptool2.CreateImage();
//        CommandEsptool cmdEsptoolck = new CommandEsptool( options, toolSettings );
//        cmdEsptoolck.createImage();
    }//GEN-LAST:event__createBinaryButActionPerformed

    private void _flashBinaryButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__flashBinaryButActionPerformed
        clearOutput();
        CommandEsptool cmdEsptool = new CommandEsptool( options, toolSettings );
        cmdEsptool.writeFlash();
//        CommandEsptoolCk cmdEsptoolCk = new CommandEsptoolCk( options, toolSettings );
//        cmdEsptoolCk.writeFlash();
    }//GEN-LAST:event__flashBinaryButActionPerformed

    private void _fullEraseFlashButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__fullEraseFlashButActionPerformed
        int response = JOptionPane.showConfirmDialog( null, "Are you sure to erase full flash?", "Full flash erase", JOptionPane.OK_CANCEL_OPTION );

        if ( response != JOptionPane.YES_OPTION ) {
            return;
        }
        clearOutput();
        CommandEsptool cmdEsptool = new CommandEsptool( options, toolSettings );
        cmdEsptool.eraseFlash();
//        CommandEsptoolCk cmdEsptoolCk = new CommandEsptoolCk( options, toolSettings );
//        cmdEsptoolCk.eraseFlash();
    }//GEN-LAST:event__fullEraseFlashButActionPerformed

    private void _memUpdateButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__memUpdateButActionPerformed
        CommandMemAnalyser cmdMemAnalyser = new CommandMemAnalyser( options, toolSettings );
        cmdMemAnalyser.executeCommand();
    }//GEN-LAST:event__memUpdateButActionPerformed

    private void _getFlashIdButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__getFlashIdButActionPerformed
        CommandEsptool cmdEsptool2 = new CommandEsptool( options, toolSettings );
        cmdEsptool2.getFlashSize();
    }//GEN-LAST:event__getFlashIdButActionPerformed

    private void _flashDefaultSettingsButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__flashDefaultSettingsButActionPerformed
        int response = JOptionPane.showConfirmDialog( null, "Are you sure to write default settings?", "Write Default Settings", JOptionPane.OK_CANCEL_OPTION );

        if ( response != JOptionPane.YES_OPTION ) {
            return;
        }
        clearOutput();
//        CommandEsptoolCk cmdEsptoolCk = new CommandEsptoolCk( options, toolSettings );
//        cmdEsptoolCk.writeDefaultSettings();
        CommandEsptool cmdEsptool = new CommandEsptool( options, toolSettings );
        cmdEsptool.writeDefaultSettings();
    }//GEN-LAST:event__flashDefaultSettingsButActionPerformed

    private void _deleteSessionButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__deleteSessionButActionPerformed
        XMLConfiguration config = ( XMLConfiguration ) Settings.getInst().getConfig();
        int configIndex = Settings.getInst().getConfigIndex( options.getCurrentSession() );

        if ( configIndex != -1 ) {

            String configKey = String.format( "configs.config(%d)", configIndex );
            config.clearTree( configKey );
            Settings.getInst().save();
            _sessionCombo.setModel( getConfigComboBoxModel() );
            _sessionCombo.setSelectedIndex( 0 );
        }
    }//GEN-LAST:event__deleteSessionButActionPerformed

    private void _sessionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__sessionComboActionPerformed
        int lastSessionIndex = Settings.getInst().getConfigIndex( options.getCurrentSession() );
        if ( lastSessionIndex != -1 ) {
            loadConfig( lastSessionIndex );
        }
    }//GEN-LAST:event__sessionComboActionPerformed

    private void _newSessionButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__newSessionButActionPerformed
        String sessionName = JOptionPane.showInputDialog( this, "Enter Session name :", "New Session", JOptionPane.PLAIN_MESSAGE );
        XMLConfiguration config = ( XMLConfiguration ) Settings.getInst().getConfig();

        if ( sessionName != null && !sessionName.isEmpty() ) {
            config.addProperty( "configs.config(-1)[@name]", sessionName );
            Settings.getInst().save();
            config.setProperty( "last_session", sessionName );
            reloadSessions();
        }
    }//GEN-LAST:event__newSessionButActionPerformed

    private void _saveSessionButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__saveSessionButActionPerformed
        XMLConfiguration config = ( XMLConfiguration ) Settings.getInst().getConfig();
        int configIndex = Settings.getInst().getConfigIndex( options.getCurrentSession() );

        int response = JOptionPane.showConfirmDialog( null, "Are you sure to overwrite current session?", "Save Session", JOptionPane.OK_CANCEL_OPTION );

        if ( response != JOptionPane.YES_OPTION ) {
            return;
        }

        if ( configIndex != -1 ) {
            String configKey = String.format( "configs.config(%d)", configIndex );
            config.setProperty( configKey + ".com_speed", options.getComSpeed() );
            config.setProperty( configKey + ".outfilepath", options.getInputFile() );
            config.setProperty( configKey + ".tools_path", toolSettings.getToolsPath() );
            config.setProperty( configKey + ".sdk_path", toolSettings.getSdkPath() );
            config.setProperty( configKey + ".compiler_path", toolSettings.getCompilerPath() );
            config.setProperty( configKey + ".boot_mode", options.getBootMode() );
            config.setProperty( configKey + ".flash_mode", options.getFlashMode() );
            config.setProperty( configKey + ".flash_speed", options.getFlashSpeed() );
            config.setProperty( configKey + ".flash_size", options.getFlashSize() );
            Settings.getInst().save();
            loadConfig( configIndex );
        }

    }//GEN-LAST:event__saveSessionButActionPerformed

    private void _saveAsSessionButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__saveAsSessionButActionPerformed
        String sessionName = JOptionPane.showInputDialog( this, "Enter Session name :", "Save As Session", JOptionPane.PLAIN_MESSAGE );
        XMLConfiguration config = ( XMLConfiguration ) Settings.getInst().getConfig();

        if ( sessionName != null && !sessionName.isEmpty() ) {
            config.addProperty( "configs.config(-1)[@name]", sessionName );
            Settings.getInst().save();
            config.setProperty( "last_session", sessionName );

            int configIndex = Settings.getInst().getConfigIndex( sessionName );

            if ( configIndex != -1 ) {
                String configKey = String.format( "configs.config(%d)", configIndex );
                config.setProperty( configKey + ".com_speed", options.getComSpeed() );
                config.setProperty( configKey + ".outfilepath", options.getInputFile() );
                config.setProperty( configKey + ".tools_path", toolSettings.getToolsPath() );
                config.setProperty( configKey + ".sdk_path", toolSettings.getSdkPath() );
                config.setProperty( configKey + ".compiler_path", toolSettings.getCompilerPath() );
                config.setProperty( configKey + ".boot_mode", options.getBootMode() );
                config.setProperty( configKey + ".flash_mode", options.getFlashMode() );
                config.setProperty( configKey + ".flash_speed", options.getFlashSpeed() );
                config.setProperty( configKey + ".flash_size", options.getFlashSize() );
                Settings.getInst().save();

                reloadSessions();
            }
        }
    }//GEN-LAST:event__saveAsSessionButActionPerformed

    private void _inputFileOpenExplorerButActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__inputFileOpenExplorerButActionPerformed
        try {
            options.checkInputFile();
            if ( options.getInputFileDir() != null && options.getInputFileDir().exists() ) {
                Desktop.getDesktop().open( options.getInputFileDir() );
            }
        } catch ( IOException ex ) {
            Logger.getLogger( Esp8266FlasherFrame.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }//GEN-LAST:event__inputFileOpenExplorerButActionPerformed

    private void clearOutput() {
        _outputViewTxt.setText( "" );
    }

    private void updateAddressFields() {
    }

    class DirectoryDropTarget extends DropTarget {

        private static final long serialVersionUID = 1L;
        JTextField _textField;

        public DirectoryDropTarget( JTextField textField ) {
            this._textField = textField;
        }

        @Override
        public synchronized void drop( DropTargetDropEvent evt ) {
            try {
                evt.acceptDrop( DnDConstants.ACTION_COPY );
                @SuppressWarnings( "unchecked" )
                List<File> droppedFiles = ( List<File> ) evt.getTransferable().getTransferData( DataFlavor.javaFileListFlavor );

                if ( droppedFiles.size() <= 0 ) {
                    return;
                }

                File droppedFile = droppedFiles.get( 0 );

                if ( droppedFile.isDirectory() ) {
                    this._textField.setText( droppedFile.getAbsolutePath() );
                }
            } catch ( UnsupportedFlavorException | IOException ex ) {
            }
        }

    }

    class BinaryFileDropTarget extends DropTarget {

        private static final long serialVersionUID = 1L;

        @Override
        public synchronized void drop( DropTargetDropEvent evt ) {
            try {
                evt.acceptDrop( DnDConstants.ACTION_COPY );
                @SuppressWarnings( "unchecked" )
                List<File> droppedFiles = ( List<File> ) evt.getTransferable().getTransferData( DataFlavor.javaFileListFlavor );

                if ( droppedFiles.size() <= 0 ) {
                    return;
                }

                File droppedFile = droppedFiles.get( 0 );

                if ( droppedFile.isDirectory() ) {
                    File[] binFiles = droppedFile.listFiles( (File pathname) -> {
                        if ( pathname.isFile() ) {
                            String filename = pathname.getName();

                            if ( filename.endsWith( ".out" ) ) {
                                return true;
                            }
                        }
                        return false;
                    } );

                    if ( binFiles.length > 0 ) {
                        _inputFileTxt.setText( binFiles[ 0 ].getAbsolutePath() );
                    }
                } else {
                    _inputFileTxt.setText( droppedFile.getAbsolutePath() );
                }
            } catch ( UnsupportedFlavorException | IOException ex ) {
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> _bootCombo;
    private javax.swing.JButton _clearOutputViewBut;
    private javax.swing.JButton _comPortBut;
    private javax.swing.JComboBox<String> _comPortCombo;
    private javax.swing.JComboBox<String> _comSpeedCombo;
    private javax.swing.JButton _compilerPathBut;
    private javax.swing.JTextField _compilerPathTxt;
    private javax.swing.JButton _createAndFlashBut;
    private javax.swing.JButton _createBinaryBut;
    private javax.swing.JButton _deleteSessionBut;
    private javax.swing.JButton _flashBinaryBut;
    private javax.swing.JButton _flashDefaultSettingsBut;
    private javax.swing.JComboBox<String> _flashModeCombo;
    private javax.swing.JComboBox<String> _flashSizeCombo;
    private javax.swing.JComboBox<String> _flashSpeedCombo;
    private javax.swing.JButton _fullEraseFlashBut;
    private javax.swing.JButton _getFlashIdBut;
    private javax.swing.JButton _inputFileBut;
    private javax.swing.JButton _inputFileOpenExplorerBut;
    private javax.swing.JTextField _inputFileTxt;
    private javax.swing.JButton _memUpdateBut;
    private javax.swing.JEditorPane _memoryDetailsEditor;
    private javax.swing.JButton _newSessionBut;
    private javax.swing.JTextArea _outputViewTxt;
    private javax.swing.JButton _saveAsSessionBut;
    private javax.swing.JButton _saveSessionBut;
    private javax.swing.JButton _sdkPathBut;
    private javax.swing.JTextField _sdkPathTxt;
    private javax.swing.JComboBox<String> _sessionCombo;
    private javax.swing.JButton _toolsPathBut;
    private javax.swing.JTextField _toolsPathTxt;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private Options options;
    private ToolSettings toolSettings;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
