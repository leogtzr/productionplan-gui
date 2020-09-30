// TODO: Plan acumulativo ..., es decir, si aparecen nuevas órdenes ... que cotinue en el día que se quedó ...
package com.production;

import com.production.domain.Priority;
import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;
import com.production.util.Constants;
import com.production.util.Utils;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collections;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.production.util.TemplateFileUtils;
import com.production.util.logging.Logging;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Date;

import static com.production.util.Utils.extractWorkOrdersFromSheetFile;
import static com.production.util.Utils.numberOfTurnsFromWorkCenter;
import static com.production.util.Utils.createFileChooser;
import static com.production.util.Constants.DOBLADO_PART_MACHINE_FILE_NAME;
import static com.production.util.Constants.LASER_AND_PUNCH_PART_MACHINE_FILE_NAME;
import static com.production.util.Constants.ALLOWED_COLUMN_NUMBER_TO_BE_EDITED;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class MainWindow extends JFrame {
    
    private File fabLoadFilePath = null;
    private File ageByWCFilePath = null;
    private String jarPath = null;
    private Map<String, String> dobladoPartMachineInfo = new HashMap<>();
    private Map<String, String> laserAndPunchPartMachineInfo = new HashMap<>();
    private Optional<List<WorkOrderInformation>> workOrderInformationItems = Optional.empty();
    private final List<WorkOrderInformation> backupWorkOrderItems = new ArrayList<>();
    private final Properties configProps = new Properties();
    
    public MainWindow(final String saveDirectory) {
        initComponents();
        Utils.updateStatusBar(this.statusLabel, this.fabLoadFilePath, this.ageByWCFilePath);
        loadDobladoPartMachineInformation();
        loadLaserAndPunchPartMachineInformation();
        loadPropertiesFile(configProps, saveDirectory);
    }
    
    private void loadPropertiesFile(final Properties properties, final String saveDirectory) {
        try {
            final String jarFilepath = MainWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            final File jf = new File(jarFilepath);
            this.jarPath = URLDecoder.decode(jf.getParent(), "UTF-8");
            
            final Path configFilePath = Paths.get(this.jarPath, Constants.CONFIG_PROPERTIES_FILE_NAME);
            final File configFile = configFilePath.toFile();
            
            if (configFile.exists()) {
                try (final InputStream inputStream = new FileInputStream(configFile.getAbsolutePath())) {
                    properties.load(inputStream);
                }
                properties.put("saveDirectory", saveDirectory);
            } else {
                showErrorMessage(
                    String.format("%s not found", Constants.CONFIG_PROPERTIES_FILE_NAME), "ERROR loading configuration."
                );
            }
        } catch (final IOException ex) {
            showErrorMessage(ex.getMessage(), "ERROR loading configuration.");
        }
    }
    
    private void loadDobladoPartMachineInformation() {
        final File partMachineCSVFile = new File(DOBLADO_PART_MACHINE_FILE_NAME);
        if (!partMachineCSVFile.exists()) {
            showWarningMessage(String.format("El archivo '%s' no fue encontrado, los comentarios o máquinas no serán cargados.", 
                    DOBLADO_PART_MACHINE_FILE_NAME), "Warning ... ");
            return;
        }
            
        try {
            this.dobladoPartMachineInfo = Utils.loadCSVFile(DOBLADO_PART_MACHINE_FILE_NAME);
        } catch (final IOException ex) {
            showErrorMessage(String.format("error: %s", ex.getMessage()), "Error");
        }
    }
    
    private void loadLaserAndPunchPartMachineInformation() {
        final File partMachineCSVFile = new File(LASER_AND_PUNCH_PART_MACHINE_FILE_NAME);
        if (!partMachineCSVFile.exists()) {
            showWarningMessage(String.format("El archivo '%s' no fue encontrado, los comentarios o máquinas no serán cargados.", 
                    LASER_AND_PUNCH_PART_MACHINE_FILE_NAME), "Warning ... ");
            return;
        }
            
        try {
            this.laserAndPunchPartMachineInfo = Utils.loadCSVFile(LASER_AND_PUNCH_PART_MACHINE_FILE_NAME);
        } catch (final IOException ex) {
            showErrorMessage(String.format("error: %s", ex.getMessage()), "Error");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        optionsDialog = new javax.swing.JDialog();
        saveOptionsButton = new javax.swing.JButton();
        aboutDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        infoDialog = new javax.swing.JDialog();
        jScrollPane3 = new javax.swing.JScrollPane();
        infoTextPane = new javax.swing.JTextPane();
        okInfoDialogButton = new javax.swing.JButton();
        infoLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        workOrderTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        selectedPrioritiesTable = new javax.swing.JTable();
        moveToSelectedPrioritiesButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        wcDescriptions = new javax.swing.JComboBox<>();
        generatePlanBtn = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openFabLoadByWCMenuItem = new javax.swing.JMenuItem();
        openAgeByWCFileItem = new javax.swing.JMenuItem();
        findFilesInCurrentPathMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        operationsMenu = new javax.swing.JMenu();
        rollbackMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        optionsDialog.setTitle("Options");
        optionsDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        optionsDialog.setResizable(false);
        optionsDialog.setSize(new java.awt.Dimension(600, 400));

        saveOptionsButton.setMnemonic('S');
        saveOptionsButton.setText("Save");
        saveOptionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOptionsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optionsDialogLayout = new javax.swing.GroupLayout(optionsDialog.getContentPane());
        optionsDialog.getContentPane().setLayout(optionsDialogLayout);
        optionsDialogLayout.setHorizontalGroup(
            optionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsDialogLayout.createSequentialGroup()
                .addContainerGap(513, Short.MAX_VALUE)
                .addComponent(saveOptionsButton)
                .addGap(15, 15, 15))
        );
        optionsDialogLayout.setVerticalGroup(
            optionsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionsDialogLayout.createSequentialGroup()
                .addContainerGap(362, Short.MAX_VALUE)
                .addComponent(saveOptionsButton)
                .addGap(16, 16, 16))
        );

        aboutDialog.setTitle("About");
        aboutDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        aboutDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        aboutDialog.setResizable(false);
        aboutDialog.setSize(new java.awt.Dimension(384, 391));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Production Plan v.1.0");

        javax.swing.GroupLayout aboutDialogLayout = new javax.swing.GroupLayout(aboutDialog.getContentPane());
        aboutDialog.getContentPane().setLayout(aboutDialogLayout);
        aboutDialogLayout.setHorizontalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(216, Short.MAX_VALUE))
        );
        aboutDialogLayout.setVerticalGroup(
            aboutDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(361, Short.MAX_VALUE))
        );

        infoDialog.setTitle("Warning");
        infoDialog.setResizable(false);
        infoDialog.setSize(new java.awt.Dimension(888, 493));

        infoTextPane.setEditable(false);
        infoTextPane.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jScrollPane3.setViewportView(infoTextPane);

        okInfoDialogButton.setMnemonic('O');
        okInfoDialogButton.setText("OK");
        okInfoDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okInfoDialogButtonActionPerformed(evt);
            }
        });

        infoLabel.setText("Warning:");

        javax.swing.GroupLayout infoDialogLayout = new javax.swing.GroupLayout(infoDialog.getContentPane());
        infoDialog.getContentPane().setLayout(infoDialogLayout);
        infoDialogLayout.setHorizontalGroup(
            infoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okInfoDialogButton))
                    .addGroup(infoDialogLayout.createSequentialGroup()
                        .addComponent(infoLabel)
                        .addGap(0, 803, Short.MAX_VALUE)))
                .addContainerGap())
        );
        infoDialogLayout.setVerticalGroup(
            infoDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoDialogLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(infoLabel)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okInfoDialogButton)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Production Plan Priorities");
        setMaximumSize(new java.awt.Dimension(1000, 625));
        setMinimumSize(new java.awt.Dimension(1000, 625));
        setResizable(false);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusLabel.setText("Status:");

        workOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Part", "Hr", "Stup", "P/Hac", "Máquina"
            }
        )
        {
            @Override public boolean isCellEditable(final int row, final int column) {
                return true;
            }
        }

    );
    workOrderTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    workOrderTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    workOrderTable.getModel().addTableModelListener(
        new TableModelListener() {
            public void tableChanged(final TableModelEvent evt) {

                if (evt.getColumn() != ALLOWED_COLUMN_NUMBER_TO_BE_EDITED) {
                    return;
                }

                if (evt.getType() == TableModelEvent.UPDATE) {
                    final int row = evt.getFirstRow();
                    final int col = evt.getColumn();
                    final Object newMachineValue = workOrderTable.getValueAt(row, col);
                    final String currentWorkCenter = wcDescriptions.getSelectedItem().toString();
                    workOrderInformationItems
                    .ifPresent(items -> 
                        Utils.updateMachine(row, newMachineValue.toString(), currentWorkCenter, items));
                }
            }
        }
    );
    jScrollPane1.setViewportView(workOrderTable);

    selectedPrioritiesTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "#", "#Part Number"
        }
    )
    {
        @Override public boolean isCellEditable(final int row, final int column) {
            return false;
        }
    }
    );
    jScrollPane2.setViewportView(selectedPrioritiesTable);

    moveToSelectedPrioritiesButton.setMnemonic('n');
    moveToSelectedPrioritiesButton.setText(">");
    moveToSelectedPrioritiesButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/next-icon.png"))); // NOI18N
    moveToSelectedPrioritiesButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            moveToSelectedPrioritiesButtonActionPerformed(evt);
        }
    });

    clearButton.setMnemonic('C');
    clearButton.setText("Clear");
    clearButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            clearButtonActionPerformed(evt);
        }
    });

    wcDescriptions.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DOBLADO", "PUNZONADO", "SURTIR MATERIAL", "EMPAQUE FINAL", "EMPAQUE A PROVEEDOR", "INSPECCION DE ACABADOS", "LASER", "SOLDADURA", "SPOT WELD", "TIME SAVER", "PULIDO", "REBABEO", "LIMPIEZA", "INSERTOS-PEM", "ENSAMBLE", "MAQUINADO MANUAL", "MAQUINADO CNC", "TRATAMIENTO QUIMICO", "PINTURA EN POLVO", "LIMPIEZA LUZ NEGRA", "SERIGRAFIA" }));
    wcDescriptions.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            wcDescriptionsActionPerformed(evt);
        }
    });

    generatePlanBtn.setMnemonic('G');
    generatePlanBtn.setText("Generate Plan");
    generatePlanBtn.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            generatePlanBtnActionPerformed(evt);
        }
    });

    fileMenu.setMnemonic('F');
    fileMenu.setText("File");

    openFabLoadByWCMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    openFabLoadByWCMenuItem.setText("Open \"FAB Load by WC\" file");
    openFabLoadByWCMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            openFabLoadByWCMenuItemActionPerformed(evt);
        }
    });
    fileMenu.add(openFabLoadByWCMenuItem);

    openAgeByWCFileItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    openAgeByWCFileItem.setText("Open \"Age  by WC\" file");
    openAgeByWCFileItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            openAgeByWCFileItemActionPerformed(evt);
        }
    });
    fileMenu.add(openAgeByWCFileItem);

    findFilesInCurrentPathMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    findFilesInCurrentPathMenuItem.setText("Find files");
    findFilesInCurrentPathMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            findFilesInCurrentPathMenuItemActionPerformed(evt);
        }
    });
    fileMenu.add(findFilesInCurrentPathMenuItem);

    menuBar.add(fileMenu);

    editMenu.setMnemonic('E');
    editMenu.setText("Edit");
    menuBar.add(editMenu);

    operationsMenu.setMnemonic('O');
    operationsMenu.setText("Operations");

    rollbackMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    rollbackMenuItem.setText("Rollback");
    rollbackMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rollbackMenuItemActionPerformed(evt);
        }
    });
    operationsMenu.add(rollbackMenuItem);

    menuBar.add(operationsMenu);

    toolsMenu.setMnemonic('T');
    toolsMenu.setText("Tools");

    optionsMenuItem.setText("Options");
    optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            optionsMenuItemActionPerformed(evt);
        }
    });
    toolsMenu.add(optionsMenuItem);

    menuBar.add(toolsMenu);

    helpMenu.setMnemonic('H');
    helpMenu.setText("Help");

    aboutMenuItem.setMnemonic('A');
    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            aboutMenuItemActionPerformed(evt);
        }
    });
    helpMenu.add(aboutMenuItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(wcDescriptions, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearButton)
                            .addGap(90, 90, 90))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(moveToSelectedPrioritiesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(generatePlanBtn)))
                    .addContainerGap(34, Short.MAX_VALUE))))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clearButton)
                        .addComponent(generatePlanBtn))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(wcDescriptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(moveToSelectedPrioritiesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(229, 229, 229))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
            .addComponent(statusLabel)
            .addGap(55, 55, 55))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void extractWorkOrderItemsFromFile(final File file) throws IOException, InvalidFormatException {
        final List<WorkOrderInformation> workOrdersFromSheetFile = extractWorkOrdersFromSheetFile(file.getAbsolutePath());
        this.workOrderInformationItems = Optional.of(workOrdersFromSheetFile);
    }
    
    private void showErrorMessage(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title, ERROR_MESSAGE);
    }
    
    private void showInfoMessage(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showWarningMessage(final String message, final String title) {
        JOptionPane.showMessageDialog(this, message, title, WARNING_MESSAGE);
    }
    
    private void openFabLoadByWCMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFabLoadByWCMenuItemActionPerformed
        final JFileChooser jfc = Utils.genericXLSFileChooser();
        int returnValue = jfc.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            this.fabLoadFilePath = jfc.getSelectedFile();
            try {
                this.extractWorkOrderItemsFromFile(this.fabLoadFilePath);
            } catch (IOException | InvalidFormatException ex) {
                showErrorMessage(String.format("error loading Fab Load by WC File: %s", ex.getMessage()), "Error");
            }
        }
        Utils.updateStatusBar(this.statusLabel, this.fabLoadFilePath, this.ageByWCFilePath);
    }//GEN-LAST:event_openFabLoadByWCMenuItemActionPerformed

    private void reconcileInformationAndUpdateTable(final File file, final List<WorkOrderInformation> workOrderItems) 
            throws IOException, InvalidFormatException {
        Utils.reconcileInformationFromAgeFile(file.getAbsolutePath(), workOrderItems);
        this.updateTable(workOrderItems, this.workOrderTable);
        Utils.copyWorkOrderItems(this.backupWorkOrderItems, workOrderItems);
    }
    
    private void openAgeByWCFileItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openAgeByWCFileItemActionPerformed
        final JFileChooser jfc = Utils.genericXLSFileChooser();

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            this.ageByWCFilePath = jfc.getSelectedFile();
            this.workOrderInformationItems.ifPresent(workOrderItems -> {
                try {
                    this.reconcileInformationAndUpdateTable(this.ageByWCFilePath, workOrderItems);
                } catch (IOException | InvalidFormatException ex) {
                    showErrorMessage(String.format("error loading Age file: %s", ex.getMessage()), "Error");
                }
            });
        }
        Utils.updateStatusBar(this.statusLabel, this.fabLoadFilePath, this.ageByWCFilePath);
    }//GEN-LAST:event_openAgeByWCFileItemActionPerformed

    @MissingTests
    private void updateTable(final List<WorkOrderInformation> workOrderItems, final JTable table) {        
        final DefaultTableModel workOrdersModel = (DefaultTableModel) table.getModel();
        
        final String selectedWorkCenter = this.wcDescriptions.getSelectedItem().toString();
        final String workCenter = Utils.sanitizeWorkCenterName(selectedWorkCenter);
        
        // "#Part", "Hr", "Stup", "P/Hac", "Máquina"
        workOrderItems.forEach(item -> {
            final String machine = Utils.getMachineFromMachineInfoMaps(
                workCenter, item.getPartNumber(), this.dobladoPartMachineInfo, this.laserAndPunchPartMachineInfo
            );
            item.setMachine(machine);
            final Object[] data = {
                item.getPartNumber()
                , item.getRunHours()
                , item.getSetupHours()
                , item.getQty()
                , machine
            };
            workOrdersModel.addRow(data);
        });
    }
    
    private void moveToSelectedPrioritiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToSelectedPrioritiesButtonActionPerformed
        
        final int[] rowIndexesToRemove = this.workOrderTable.getSelectedRows();
        if (rowIndexesToRemove.length == 0) {
            return;
        }
        
        final DefaultTableModel selectedPrioritiesModel = (DefaultTableModel) this.selectedPrioritiesTable.getModel();
        final DefaultTableModel workOrdersModel = (DefaultTableModel) this.workOrderTable.getModel();
        
        final int rowCount = selectedPrioritiesModel.getRowCount();
        int priority = rowCount > 0 ? (rowCount + 1) : 1;
        for (int rowIndex : rowIndexesToRemove) {
            final String ptNumber = Utils.getPartNumberFromRow(this.workOrderTable.getModel(), rowIndex);
            final String[] data = {priority + "", ptNumber};
            selectedPrioritiesModel.addRow(data);
            priority++;
        }
        
        // Remove indexes from the table ...
        int numRows = workOrdersModel.getRowCount();
        
        for (final int rowIndexToRemove : rowIndexesToRemove) {
            workOrdersModel.removeRow(rowIndexToRemove);
            this.workOrderTable.clearSelection();
            workOrdersModel.setRowCount(--numRows);
            workOrdersModel.fireTableDataChanged();
            this.workOrderTable.repaint();
        }
        
    }//GEN-LAST:event_moveToSelectedPrioritiesButtonActionPerformed

    @MissingTests
    private void cleanTable(final JTable table) {
        ((DefaultTableModel)table.getModel()).setRowCount(0);
        table.clearSelection();
    }
    
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        ((DefaultTableModel) this.selectedPrioritiesTable.getModel()).setRowCount(0);
        this.workOrderTable.clearSelection();
        
        final String selectedWCDescription = this.wcDescriptions.getSelectedItem().toString();
        this.workOrderInformationItems.ifPresent(workOrderItems -> {
            cleanTable(this.workOrderTable);
            updateTableWithWCDescription(selectedWCDescription, workOrderItems, this.workOrderTable);
        });
    }//GEN-LAST:event_clearButtonActionPerformed

    private void findFilesInCurrentPathMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findFilesInCurrentPathMenuItemActionPerformed
        
        try {
            final String jarFilepath = MainWindow.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            final File jf = new File(jarFilepath);
            this.jarPath = URLDecoder.decode(jf.getParent(), "UTF-8");
            
            final Path fabLoadByWCPath = Paths.get(this.jarPath, Constants.FAB_LOAD_FILE_NAME);
            final File fabLoadByWCFile = fabLoadByWCPath.toFile();
            final Path ageByWCPath = Paths.get(this.jarPath, Constants.AGE_BY_WC_FILE_NAME);
            final File ageByWCFile = ageByWCPath.toFile();
            
            if (fabLoadByWCFile.exists() && ageByWCFile.exists()) {
                // Clean the tables ... 
                this.cleanTable(this.workOrderTable);
                this.cleanTable(this.selectedPrioritiesTable);
                
                this.fabLoadFilePath = fabLoadByWCFile;
                this.ageByWCFilePath = ageByWCFile;
                
                this.extractWorkOrderItemsFromFile(this.fabLoadFilePath);
                this.workOrderInformationItems.ifPresent(workOrderItems -> {
                    try {
                        this.reconcileInformationAndUpdateTable(this.ageByWCFilePath, workOrderItems);
                        this.wcDescriptions.setSelectedIndex(0);
                    } catch (IOException | InvalidFormatException ex) {
                        showErrorMessage(String.format("error loading Age file: %s", ex.getMessage()), "Error");
                    }
                });
                
            } else {
                showErrorMessage("Error: los archivos necesarios no existen en el directorio actual.", "Error");
                return;
            }
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Utils.updateStatusBar(this.statusLabel, this.fabLoadFilePath, this.ageByWCFilePath);
    }//GEN-LAST:event_findFilesInCurrentPathMenuItemActionPerformed

    @MissingTests
    private void updateTableWithWCDescription(
            final String wcDescription
            , final List<WorkOrderInformation> workOrderItems
            , final JTable table
    ) {
        
        final DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        final StringBuilder warnText = new StringBuilder();
        final boolean usesTurns = Utils.numberOfTurnsFromWorkCenter(wcDescription) > 0;
        
        // PENDING: the following code can be improved....
        workOrderItems
            .stream()
            .filter(wo -> wo.getWcDescription().equalsIgnoreCase(wcDescription))
            .forEach(item -> {

               if (usesTurns) {
                   final String machine = Utils.getMachineFromWorkCenter(
                       this.dobladoPartMachineInfo
                       , this.laserAndPunchPartMachineInfo
                       , item.getPartNumber()
                       , wcDescription
                   );
                   if (!machine.isBlank()) {
                       final Object[] data = {
                           item.getPartNumber()
                           , item.getRunHours()
                           , item.getSetupHours()
                           , item.getQty()
                           , machine
                       };
                       model.addRow(data);
                   } else {
                       if (warnText.toString().isEmpty()) {
                           warnText.append("The following part numbers will NOT show up in the report, they don't");
                           warnText.append(" have a machine associated for its work center\n\n");
                       }
                       Logging.warn("Couldn't find machine for: %s part with %s work center, it will not show up in the report", item.getPartNumber(), wcDescription);
                       warnText.append(String.format("%s, %s\n", item.getPartNumber(), wcDescription));
                   }
               } else {
                   final Object[] data = {
                       item.getPartNumber()
                       , item.getRunHours()
                       , item.getSetupHours()
                       , item.getQty()
                       , ""
                   };
                   model.addRow(data);
               }

            });
         if (!warnText.toString().isEmpty()) {
             warnText.append("\n\nPlease make sure these part have a machine associated in the CSV files.\n");
         }
         infoTextPane.setText(warnText.toString());
    }
    
    private void wcDescriptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wcDescriptionsActionPerformed
        this.cleanTable(this.selectedPrioritiesTable);
        this.infoTextPane.setText("");
        
        final String selectedWorkCenter = this.wcDescriptions.getSelectedItem().toString();
        this.workOrderInformationItems.ifPresent(workOrderItems -> {
            this.cleanTable(this.workOrderTable);
            this.updateTableWithWCDescription(selectedWorkCenter, workOrderItems, this.workOrderTable);
        });
        if (!this.infoTextPane.getText().isEmpty()) {
            this.infoDialog.setVisible(true);
        }
    }//GEN-LAST:event_wcDescriptionsActionPerformed
    
    // PENDING: we might need a suffix or date for the output/final file to avoid overriding files.
    @MissingTests
    private void saveListPlan(final String workCenter, final String htmlContent) throws IOException {
        
        final String saveDirectory = this.configProps.getProperty("saveDirectory", System.getProperty("user.home"));
        final boolean autoSave = Boolean.valueOf(configProps.getProperty("autoSavePlans", "false"));
        
        if (autoSave) {
            final Path outputDirectoryPlan = Paths.get(saveDirectory);
            outputDirectoryPlan.toFile().mkdir();
            final Path planFile = Utils.buildOutputPlanFile(workCenter, saveDirectory, new Date());
            saveFiles(planFile.toFile(), htmlContent);
            showInfoMessage("Plan saved correctly", "Plan generated correctly");
        } else {
            final JFileChooser saveFileChooser = createFileChooser(String.format("Save plan for '%s'", workCenter), saveDirectory);
            final int option = saveFileChooser.showSaveDialog(this);
 
            if (option == JFileChooser.APPROVE_OPTION) {
                final File directoryToSaveReports = saveFileChooser.getSelectedFile();
                final Path outputDirectoryPlan = directoryToSaveReports.toPath();
                outputDirectoryPlan.toFile().mkdir();
                final Path planFile = 
                        Utils.buildOutputPlanFile(workCenter, outputDirectoryPlan.toAbsolutePath().toString(), new Date());
                saveFiles(planFile.toFile(), htmlContent);
                showInfoMessage("Plan saved correctly", "Plan generated correctly");
            }
        }
        
    }
    
    private void generatePlanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePlanBtnActionPerformed
        final DefaultTableModel model = (DefaultTableModel) selectedPrioritiesTable.getModel();
        
        final String wcDescription = this.wcDescriptions.getSelectedItem().toString();
        final List<Priority> priorities = buildPrioritiesFromTable(model);
        
        // PENDING: separate per machine and generate N files if the file handles machines.
        this.workOrderInformationItems.ifPresentOrElse(workOrderItems -> {
            
            final List<WorkOrderInformation> workOrderItemsByWCDescription = workOrderItems
                    .stream()
                    .filter(wo -> wo.getWcDescription().equalsIgnoreCase(wcDescription))
                    .collect(Collectors.toList());
            
            final int numberOfTurns = numberOfTurnsFromWorkCenter(wcDescription);
            
            try {
                if (numberOfTurns == 0) {               // List
                    final String htmlContent = Utils.buildHtmlContent(wcDescription, workOrderItemsByWCDescription, priorities);
                    saveListPlan(wcDescription, htmlContent);
                } else {
                    final Map<String, List<WorkOrderInformation>> workOrderItemsPerMachine = Utils.workOrderItemsPerMachine(workOrderItemsByWCDescription);

                    workOrderItemsPerMachine.forEach(
                        (machine, woItemsPerMachine) -> {
                            
                            System.out.printf("DEBUG-save About to save for: %s -(%d)\n\tItems: ~~~~~~~~~~~~~~~~~~~~~~~~~~~\n", machine, woItemsPerMachine.size());
                            woItemsPerMachine.forEach(System.out::println);
                            System.out.println("DEBUG-save bye ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                            try {
                                final String htmlContent = Utils.buildHtmlContent(wcDescription, woItemsPerMachine, priorities);
                                this.saveTurnWithMachinesPlan(wcDescription, machine, htmlContent);
                            } catch (final IOException ex) {
                                showErrorMessage(ex.getMessage(), "ERROR");
                            }
                        }
                    );
                    showInfoMessage("Plans saved correctly", "Plans generated correctly");
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
                showErrorMessage(ex.getMessage(), "ERROR");
            }
            
        }, () -> {
            showErrorMessage("There are no Work Order information to build the plan", "ERROR");
        });
    }//GEN-LAST:event_generatePlanBtnActionPerformed

    private void copyStaticFilesToOutputDirectory(final File outputDirectory) {
        List.of("bootstrap.min.css", "bootstrap.min.js", "jquery-3.3.1.min.js").
            forEach(file -> {
                try {
                    final Path staticFilePathToCopyToUserDirectory = TemplateFileUtils.getTemplateFilePath(file);
                    TemplateFileUtils.copyFileFromTemplatesDirectoryTo(outputDirectory, staticFilePathToCopyToUserDirectory.toFile());
                } catch (final IOException ex) {
                    showErrorMessage(ex.getMessage(), "ERROR");
                }
            });
    }
    
    private void saveFiles(final File fileToSave, final String htmlContent) throws IOException {
        try (final BufferedWriter newBufferedWriter = Files.newBufferedWriter(fileToSave.toPath(), StandardCharsets.UTF_8)) {
            newBufferedWriter.write(htmlContent);
        }

        final File parentOutputDirectory = fileToSave.getParentFile();
        copyStaticFilesToOutputDirectory(parentOutputDirectory);
    }
    
    private void saveTurnWithMachinesPlan(final String workCenter, final String machine, final String htmlContent) 
            throws IOException {
        
        final String saveDirectory = this.configProps.getProperty("saveDirectory", System.getProperty("user.home"));
        final boolean autoSave = Boolean.valueOf(configProps.getProperty("autoSavePlans", "false"));
        
        if (autoSave) {
            final Path outputDirectoryPlan = Paths.get(saveDirectory, machine);
            Files.createDirectory(outputDirectoryPlan);
            final Path planFile = Utils.buildOutputPlanFile(workCenter, outputDirectoryPlan.toAbsolutePath().toString(), new Date());
            saveFiles(planFile.toFile(), htmlContent);
            showInfoMessage("Plan saved correctly", "Plan generated correctly");
        } else {
            final JFileChooser saveFileChooser = createFileChooser(
                String.format("Save plan for '%s', machine: '%s'", workCenter, machine), saveDirectory);
            final int option = saveFileChooser.showSaveDialog(this);

            if (option == JFileChooser.APPROVE_OPTION) {
                final File directoryToSaveReports = saveFileChooser.getSelectedFile();
                final Path outputDirectoryPlan = Paths.get(directoryToSaveReports.getAbsolutePath(), machine);
                Files.createDirectory(outputDirectoryPlan);
                final Path planFile = Utils.buildOutputPlanFile(workCenter, outputDirectoryPlan.toAbsolutePath().toString(), new Date());
                saveFiles(planFile.toFile(), htmlContent);
            }
        }
        
    }
    
    private void rollbackMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollbackMenuItemActionPerformed
        if (this.backupWorkOrderItems.isEmpty()) {
            return;
        }
        this.workOrderInformationItems.ifPresent(woItems -> Utils.copyWorkOrderItems(woItems, this.backupWorkOrderItems));
    }//GEN-LAST:event_rollbackMenuItemActionPerformed

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
        this.optionsDialog.setVisible(true);
    }//GEN-LAST:event_optionsMenuItemActionPerformed

    private void saveOptionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOptionsButtonActionPerformed
        this.optionsDialog.setVisible(false);
    }//GEN-LAST:event_saveOptionsButtonActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void okInfoDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okInfoDialogButtonActionPerformed
        this.infoDialog.setVisible(false);
    }//GEN-LAST:event_okInfoDialogButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        
        if (args.length == 0) {
            throw new RuntimeException("error: report file directory missing");
        }
        
        final String saveDirectory = args[0];
        
        Logging.info("Reports will be saved to: %s", saveDirectory);
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainWindow(saveDirectory).setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog aboutDialog;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton clearButton;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem findFilesInCurrentPathMenuItem;
    private javax.swing.JButton generatePlanBtn;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JDialog infoDialog;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JTextPane infoTextPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton moveToSelectedPrioritiesButton;
    private javax.swing.JButton okInfoDialogButton;
    private javax.swing.JMenuItem openAgeByWCFileItem;
    private javax.swing.JMenuItem openFabLoadByWCMenuItem;
    private javax.swing.JMenu operationsMenu;
    private javax.swing.JDialog optionsDialog;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem rollbackMenuItem;
    private javax.swing.JButton saveOptionsButton;
    private javax.swing.JTable selectedPrioritiesTable;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JComboBox<String> wcDescriptions;
    private javax.swing.JTable workOrderTable;
    // End of variables declaration//GEN-END:variables

    @MissingTests
    private List<Priority> buildPrioritiesFromTable(final DefaultTableModel model) {
        final int rowCount = model.getRowCount();
        if (rowCount <= 0) {
            return Collections.EMPTY_LIST;
        }
        final List<Priority> priorities = new ArrayList<>();
        
        for (int row = 0; row < rowCount; row++) {
            final Priority priority = new Priority();
            priority.setOrder(Integer.parseInt(model.getValueAt(row, 0).toString()));
            priority.setPartNumber(model.getValueAt(row, 1).toString());
            priorities.add(priority);
        }
        
        return priorities;
    }
    
}
