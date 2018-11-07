package edu.umassmed.omega.commons.trajectoryTool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.gui.GenericStatusPanel;
import edu.umassmed.omega.commons.gui.dialogs.GenericDialog;
import edu.umassmed.omega.commons.gui.dialogs.GenericInsertDialog;
import edu.umassmed.omega.commons.gui.dialogs.GenericPickDialog;
import edu.umassmed.omega.commons.trajectoryTool.OmegaDataToolConstants;
import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksExporter;
import edu.umassmed.omega.commons.trajectoryTool.OmegaTracksImporter;
import edu.umassmed.omega.commons.utilities.OmegaIOUtility;

public class OmegaTracksToolDialog extends GenericDialog {
	
	private static final long serialVersionUID = -4689339679604912836L;
	
	public static String AUTOMATIC_FILENAME_LBL = "Use analysis run name(s)";
	
	private static String EXTENSIONS_TXT = "txt";
	private static String EXTENSIONS_XY = "xy";
	private static String EXTENSIONS_CSV = "csv";

	private JButton chooseFile_btt, action_btt, close_btt;
	private JButton addData_btt, addStandardData_btt, moveDataUp_btt,
			moveDataDown_btt, removeData_btt;
	private JTextField file_txt, fileIdentifier_txt, dataIdentifier_txt,
			particleIdentifier_txt, nonParticleIdentifier_txt,
			particleSeparatordentifier_txt;
	private JList<String> particleData_lst;
	private DefaultListModel<String> particleData_mdl;
	private JFileChooser fileChooser;
	private JCheckBox multipleFiles_ckb, startAtOne_ckb;
	private JCheckBox addImageName_ckb, createFolders_ckb;
	private GenericInsertDialog insertDialog;
	private GenericPickDialog pickDialog;
	private JComboBox<String> extension_cmb;
	private JRadioButton importTypeOmega_rbtt, importTypeParticles_rbtt,
			importTypeTracks_rbtt;
	
	private String selectedVal;
	
	private final OmegaIOUtility otio;
	
	private final boolean isImpExp, isImporter;
	
	private JPanel standardOptionsPanel, advancedOptionsPanel,
			advancedOptionsMainPanel;
	private JScrollPane standardOptionsSP, advancedOptionsSP;
	
	private GenericStatusPanel statusPanel;
	
	private String filename, namePrefix;

	private static int WIDTH = 800;
	private static int HEIGHT = OmegaTracksToolDialog.WIDTH / 2;
	private static int HELP_SIZE = OmegaTracksToolDialog.WIDTH / 2;
	
	public OmegaTracksToolDialog(final RootPaneContainer parentContainer,
			final boolean isImpExp, final boolean isImporter,
			final OmegaIOUtility otio) {
		super(parentContainer, "Omega Data Importer", false);
		if (!isImporter) {
			this.setTitle("Omega Data Exporter");
		}
		this.isImporter = isImporter;
		this.isImpExp = isImpExp;
		this.otio = otio;
		this.selectedVal = null;
		
		this.filename = null;
		this.namePrefix = null;
		
		this.adjustWidgets();
		
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		final Dimension size = new Dimension(OmegaTracksToolDialog.WIDTH,
				OmegaTracksToolDialog.HEIGHT);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		
		this.revalidate();
		this.repaint();
		this.pack();
	}
	
	private void adjustWidgets() {
		if (!this.isImporter) {
			this.standardOptionsPanel.setLayout(new GridLayout(7, 1));
			this.advancedOptionsPanel.setLayout(new GridLayout(5, 1));
		} else {
			this.standardOptionsPanel.setLayout(new GridLayout(6, 1));
			this.advancedOptionsPanel.setLayout(new GridLayout(6, 1));
		}
		
		if (!this.isImporter) {
			final JPanel extensionPanel = new JPanel();
			extensionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			final JLabel extension_lbl = new JLabel("File extension: ");
			extension_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
			extensionPanel.add(extension_lbl);
			this.extension_cmb = new JComboBox<String>();
			this.extension_cmb
					.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_TXT);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_XY);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_CSV);
			extensionPanel.add(this.extension_cmb);
			this.standardOptionsPanel.add(extensionPanel, 4);
			
			final JPanel createFoldersPanel = new JPanel();
			createFoldersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			final JLabel createFolders_lbl = new JLabel(
					"Create sub folders structure: ");
			createFolders_lbl
					.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
			createFoldersPanel.add(createFolders_lbl);
			this.createFolders_ckb = new JCheckBox();
			createFoldersPanel.add(this.createFolders_ckb);
			this.standardOptionsPanel.add(createFoldersPanel);

			this.addExporterListeners();
		} else {
			final JPanel importPanel = new JPanel();
			importPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			final JLabel import_lbl = new JLabel("Import type: ");
			import_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
			importPanel.add(import_lbl);
			this.importTypeOmega_rbtt = new JRadioButton("Omega Data");
			this.importTypeOmega_rbtt.setSelected(true);
			this.importTypeOmega_rbtt
					.setPreferredSize(OmegaGUIConstants.TEXT_SIZE);
			this.importTypeParticles_rbtt = new JRadioButton("Particles Data");
			this.importTypeParticles_rbtt
					.setPreferredSize(OmegaGUIConstants.TEXT_SIZE);
			this.importTypeTracks_rbtt = new JRadioButton("Tracks Data");
			this.importTypeTracks_rbtt
					.setPreferredSize(OmegaGUIConstants.TEXT_SIZE);
			final ButtonGroup bg = new ButtonGroup();
			bg.add(this.importTypeOmega_rbtt);
			bg.add(this.importTypeParticles_rbtt);
			bg.add(this.importTypeTracks_rbtt);
			importPanel.add(this.importTypeOmega_rbtt);
			importPanel.add(this.importTypeParticles_rbtt);
			importPanel.add(this.importTypeTracks_rbtt);
			this.standardOptionsPanel.add(importPanel, 0);

			this.dataIdentifier_txt.setEnabled(false);
			this.dataIdentifier_txt.setText("");
			
			final JPanel createFoldersPanel = new JPanel();
			createFoldersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			final JLabel createFolders_lbl = new JLabel(
					"Has sub folders structure: ");
			createFolders_lbl
					.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
			createFoldersPanel.add(createFolders_lbl);
			this.createFolders_ckb = new JCheckBox();
			createFoldersPanel.add(this.createFolders_ckb);
			this.standardOptionsPanel.add(createFoldersPanel);
			
			final JPanel particleDataPanel = new JPanel();
			particleDataPanel.setLayout(new BorderLayout(5, 5));
			particleDataPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			final JLabel particleDataPanel_lbl = new JLabel(
					"Particle entry names and order: ");
			particleDataPanel_lbl
					.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
			particleDataPanel_lbl.setHorizontalAlignment(SwingConstants.LEFT);
			particleDataPanel_lbl.setVerticalAlignment(SwingConstants.TOP);
			particleDataPanel.add(particleDataPanel_lbl, BorderLayout.WEST);
			this.particleData_mdl = new DefaultListModel<String>();
			this.particleData_lst = new JList<String>(this.particleData_mdl);
			this.particleData_mdl
					.addElement(OmegaDataToolConstants.PARTICLE_FRAMEINDEX);
			this.particleData_mdl
					.addElement(OmegaDataToolConstants.PARTICLE_XCOORD);
			this.particleData_mdl
					.addElement(OmegaDataToolConstants.PARTICLE_YCOORD);
			// this.particleData_mdl
			// .addElement(OmegaDataToolConstants.PARTICLE_CENT_INTENSITY);
			// this.particleData_mdl
			// .addElement(OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY);
			this.particleData_lst
					.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			final JScrollPane sp = new JScrollPane(this.particleData_lst);
			sp.setPreferredSize(new Dimension(
					OmegaGUIConstants.LARGE_TEXT_SIZE.width, 200));
			this.particleData_lst.setEnabled(false);
			particleDataPanel.add(sp, BorderLayout.CENTER);
			final JPanel particleDataButtonPanelMain = new JPanel();
			particleDataButtonPanelMain.setLayout(new BorderLayout());
			final JPanel particleDataButtonPanel = new JPanel();
			particleDataButtonPanel.setLayout(new GridLayout(5, 1));
			this.addData_btt = new JButton("Add");
			this.addData_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
			this.addData_btt.setEnabled(false);
			particleDataButtonPanel.add(this.addData_btt);
			this.addStandardData_btt = new JButton("Add standard");
			this.addStandardData_btt
					.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
			this.addStandardData_btt.setEnabled(false);
			particleDataButtonPanel.add(this.addStandardData_btt);
			this.moveDataUp_btt = new JButton("Move Up");
			this.moveDataUp_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
			this.moveDataUp_btt.setEnabled(false);
			particleDataButtonPanel.add(this.moveDataUp_btt);
			this.moveDataDown_btt = new JButton("Move Down");
			this.moveDataDown_btt
					.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
			this.moveDataDown_btt.setEnabled(false);
			particleDataButtonPanel.add(this.moveDataDown_btt);
			this.removeData_btt = new JButton("Remove");
			this.removeData_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
			this.removeData_btt.setEnabled(false);
			particleDataButtonPanel.add(this.removeData_btt);
			particleDataButtonPanelMain.add(particleDataButtonPanel,
					BorderLayout.NORTH);
			particleDataButtonPanelMain.add(new JLabel(), BorderLayout.CENTER);
			particleDataPanel.add(particleDataButtonPanelMain,
					BorderLayout.EAST);
			
			this.advancedOptionsMainPanel.add(particleDataPanel,
					BorderLayout.CENTER);
			
			this.addImporterListeners();
		}
		
		if (this.isImporter) {
			this.action_btt.setText("Import");
		} else {
			this.action_btt.setText("Export");
		}

		this.standardOptionsSP.setViewportView(this.standardOptionsPanel);
		this.advancedOptionsSP.setViewportView(this.advancedOptionsMainPanel);
	}
	
	@Override
	protected void createAndAddWidgets() {
		this.insertDialog = new GenericInsertDialog(this.getParentContainer(),
				"Insert particle data", true);
		this.pickDialog = new GenericPickDialog(this.getParentContainer(),
				"Select particle data", true);
		this.fileChooser = new JFileChooser(System.getProperty("user.dir"));
		this.fileChooser
				.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		// final JPanel mainPanel = new JPanel();
		// mainPanel.setLayout(new BorderLayout());
		
		// this.fieldsPanelMain = new JPanel();
		// this.fieldsPanelMain.setLayout(new BorderLayout());
		// this.fieldsPanel = new JPanel();
		
		final JTabbedPane pane = new JTabbedPane();
		
		this.standardOptionsPanel = new JPanel();
		this.advancedOptionsMainPanel = new JPanel();
		this.advancedOptionsMainPanel.setLayout(new BorderLayout());
		this.advancedOptionsPanel = new JPanel();
		this.advancedOptionsMainPanel.add(this.advancedOptionsPanel,
				BorderLayout.NORTH);

		this.standardOptionsSP = new JScrollPane();
		this.advancedOptionsSP = new JScrollPane();

		pane.add("Standard options", this.standardOptionsSP);
		pane.add("Advanced options", this.advancedOptionsSP);
		
		final JPanel filePanel = new JPanel();
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel folder_lbl = new JLabel("Select folder: ");
		folder_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		filePanel.add(folder_lbl);
		this.file_txt = new JTextField();
		this.file_txt.setText(this.fileChooser.getCurrentDirectory()
				.getAbsolutePath());
		this.file_txt.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		filePanel.add(this.file_txt);
		this.chooseFile_btt = new JButton("File browser");
		this.chooseFile_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
		filePanel.add(this.chooseFile_btt);
		this.standardOptionsPanel.add(filePanel);
		
		final JPanel multipleFilesPanel = new JPanel();
		multipleFilesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel multipleFiles_lbl = new JLabel("Multiple files: ");
		multipleFiles_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.MULTIFILE_TT + "</p></html>");
		multipleFiles_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		multipleFilesPanel.add(multipleFiles_lbl);
		this.multipleFiles_ckb = new JCheckBox();
		multipleFilesPanel.add(this.multipleFiles_ckb);
		this.standardOptionsPanel.add(multipleFilesPanel);
		
		final JPanel fileIdentifierPanel = new JPanel();
		fileIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel fileIdentifier_lbl = new JLabel("File indentifier: ");
		fileIdentifier_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.FILE_IDENTIFIER_TT + "</p></html>");
		fileIdentifier_lbl
				.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		fileIdentifierPanel.add(fileIdentifier_lbl);
		this.fileIdentifier_txt = new JTextField();
		this.fileIdentifier_txt
				.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		fileIdentifierPanel.add(this.fileIdentifier_txt);
		this.standardOptionsPanel.add(fileIdentifierPanel);
		
		final JPanel addImageNamePanel = new JPanel();
		addImageNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel addImageName_lbl = new JLabel("Add image name as prefix: ");
		addImageName_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		addImageNamePanel.add(addImageName_lbl);
		this.addImageName_ckb = new JCheckBox();
		addImageNamePanel.add(this.addImageName_ckb);
		this.standardOptionsPanel.add(addImageNamePanel);
		
		final JPanel trackIdentifierPanel = new JPanel();
		trackIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel trackIdentifier_lbl = new JLabel("Data type indentifier: ");
		fileIdentifier_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.DATA_TYPE_IDENTIFIER_TT
				+ "</p></html>");
		trackIdentifier_lbl
				.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		trackIdentifierPanel.add(trackIdentifier_lbl);
		this.dataIdentifier_txt = new JTextField();
		this.dataIdentifier_txt
				.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		trackIdentifierPanel.add(this.dataIdentifier_txt);
		this.advancedOptionsPanel.add(trackIdentifierPanel);
		
		final JPanel particleIdentifierPanel = new JPanel();
		particleIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel particleIdentifier_lbl = new JLabel(
				"Data line indentifier: ");
		particleIdentifier_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.DATA_LINE_IDENTIFIER_TT
				+ "</p></html>");
		particleIdentifier_lbl
				.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		particleIdentifierPanel.add(particleIdentifier_lbl);
		this.particleIdentifier_txt = new JTextField();
		this.particleIdentifier_txt
				.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		particleIdentifierPanel.add(this.particleIdentifier_txt);
		this.advancedOptionsPanel.add(particleIdentifierPanel);
		
		final JPanel nonParticleIdentifierPanel = new JPanel();
		nonParticleIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel nonParticleIdentifier_lbl = new JLabel(
				"Comment line identifier: ");
		nonParticleIdentifier_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.COMMENT_IDENTIFIER_TT + "</p></html>");
		nonParticleIdentifier_lbl
				.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		nonParticleIdentifierPanel.add(nonParticleIdentifier_lbl);
		this.nonParticleIdentifier_txt = new JTextField();
		this.nonParticleIdentifier_txt
				.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		this.nonParticleIdentifier_txt.setText("#");
		nonParticleIdentifierPanel.add(this.nonParticleIdentifier_txt);
		this.advancedOptionsPanel.add(nonParticleIdentifierPanel);
		
		final JPanel particleSeparatorIdentifierPanel = new JPanel();
		particleSeparatorIdentifierPanel.setLayout(new FlowLayout(
				FlowLayout.LEFT));
		final JLabel particleSeparatorIdentifier_lbl = new JLabel(
				"Column separator: ");
		particleSeparatorIdentifier_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.DATA_SEPARATOR_TT + "</p></html>");
		particleSeparatorIdentifier_lbl
				.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		particleSeparatorIdentifierPanel.add(particleSeparatorIdentifier_lbl);
		this.particleSeparatordentifier_txt = new JTextField();
		this.particleSeparatordentifier_txt
				.setPreferredSize(OmegaGUIConstants.LARGE_TEXT_SIZE);
		this.particleSeparatordentifier_txt.setText("TAB");
		particleSeparatorIdentifierPanel
				.add(this.particleSeparatordentifier_txt);
		this.advancedOptionsPanel.add(particleSeparatorIdentifierPanel);

		final JPanel particleStartPanel = new JPanel();
		particleStartPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel particleStart_lbl = new JLabel("Indexes start at 1: ");
		particleStart_lbl.setToolTipText("<html><p width=\""
				+ OmegaTracksToolDialog.HELP_SIZE + "\">"
				+ OmegaDataToolConstants.INDEX_START_1_TT + "</p></html>");
		particleStart_lbl.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE_LARGE);
		particleStartPanel.add(particleStart_lbl);
		this.startAtOne_ckb = new JCheckBox();
		particleStartPanel.add(this.startAtOne_ckb);
		this.advancedOptionsPanel.add(particleStartPanel);
		
		// this.fieldsPanelMain.add(this.fieldsPanel, BorderLayout.NORTH);
		
		// mainPanel.add(this.fieldsPanelMain, BorderLayout.NORTH);
		// mainPanel.add(new JLabel(), BorderLayout.CENTER);
		// this.add(mainPanel, BorderLayout.CENTER);

		this.add(pane, BorderLayout.CENTER);
		
		final JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		
		final JPanel buttPanel = new JPanel();
		buttPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		this.action_btt = new JButton();
		this.action_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
		buttPanel.add(this.action_btt);
		
		this.close_btt = new JButton(OmegaGUIConstants.MENU_FILE_CLOSE);
		this.close_btt.setPreferredSize(OmegaGUIConstants.BUTTON_SIZE);
		buttPanel.add(this.close_btt);
		
		bottomPanel.add(buttPanel, BorderLayout.NORTH);
		
		this.statusPanel = new GenericStatusPanel(1);
		this.updateStatus("Ready!");
		bottomPanel.add(this.statusPanel, BorderLayout.SOUTH);
		
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void addExporterListeners() {
		this.extension_cmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleExtensionSelection();
			}
		});
	}
	
	private void addImporterListeners() {
		this.particleData_lst
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(final ListSelectionEvent e) {
						OmegaTracksToolDialog.this.handleListSelection();
					}
				});
		this.addData_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleAddData();
			}
		});
		
		this.addStandardData_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleAddStandardData();
			}
		});
		this.moveDataUp_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleMoveDataUp();
			}
		});
		this.moveDataDown_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleMoveDataDown();
			}
		});
		this.removeData_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleRemoveData();
			}
		});
		this.importTypeOmega_rbtt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.importTypeChanged();
			}
		});
		this.importTypeParticles_rbtt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.importTypeChanged();
			}
		});
		this.importTypeTracks_rbtt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.importTypeChanged();
			}
		});
	}
	
	@Override
	protected void addListeners() {
		this.addImageName_ckb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.addImageName();
			}
		});
		this.chooseFile_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleChooseFile();
			}
		});
		this.multipleFiles_ckb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleMultipleFilesSelection();
			}
		});
		this.action_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleAction();
			}
		});
		this.close_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				OmegaTracksToolDialog.this.handleClose();
			};
		});
	}
	
	private void handleExtensionSelection() {
		final String ext = (String) this.extension_cmb.getSelectedItem();
		this.particleSeparatordentifier_txt.setText("TAB");
		this.particleSeparatordentifier_txt.setEnabled(true);
		if (ext.equals(OmegaTracksToolDialog.EXTENSIONS_CSV)) {
			this.particleSeparatordentifier_txt.setText(",");
			this.particleSeparatordentifier_txt.setEnabled(false);
		}
	}
	
	private void handleAddStandardData() {
		this.pickDialog.reset();
		final String[] data = { OmegaDataToolConstants.PARTICLE_CENT_INTENSITY,
				OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY,
				OmegaDataToolConstants.PARTICLE_TRACKINDEX,
				OmegaDataToolConstants.PARTICLE_ID };
		this.pickDialog.setContent(data);
		this.pickDialog.setVisible(true);
		if (!this.pickDialog.getConfirmation())
			return;
		final String toInsert = this.pickDialog.getSelection().toLowerCase();
		if (this.particleData_mdl.contains(toInsert)) {
			this.updateStatus(toInsert + " is already present!");
		} else if (toInsert.isEmpty()) {
			this.updateStatus("This is not a valid option to add!");
		} else {
			this.particleData_mdl.addElement(toInsert);
		}
	}
	
	private void handleAddData() {
		this.insertDialog.reset();
		this.insertDialog.setVisible(true);
		if (!this.insertDialog.getConfirmation())
			return;
		final String toInsert = this.insertDialog.getContent().toLowerCase();
		if (this.particleData_mdl.contains(toInsert)) {
			this.updateStatus(toInsert + " is already present!");
		} else if (toInsert.isEmpty()) {
			this.updateStatus("This is not a valid option to add!");
		} else {
			this.particleData_mdl.addElement(toInsert);
		}
	}
	
	private void handleMoveDataUp() {
		final int index = this.particleData_mdl.indexOf(this.selectedVal);
		this.particleData_mdl.insertElementAt(this.selectedVal, index - 1);
		this.particleData_mdl.remove(index + 1);
	}
	
	private void handleMoveDataDown() {
		final int index = this.particleData_mdl.indexOf(this.selectedVal);
		int newIndex = index + 2;
		if (newIndex > this.particleData_mdl.getSize()) {
			newIndex = this.particleData_mdl.getSize();
		}
		this.particleData_mdl.insertElementAt(this.selectedVal, newIndex);
		this.particleData_mdl.remove(index);
	}
	
	private void handleRemoveData() {
		this.particleData_mdl.removeElement(this.selectedVal);
	}
	
	private void handleListSelection() {
		this.moveDataUp_btt.setEnabled(false);
		this.moveDataDown_btt.setEnabled(false);
		this.removeData_btt.setEnabled(false);
		this.selectedVal = this.particleData_lst.getSelectedValue();
		if (this.selectedVal == null)
			return;
		// final int index = this.particleData_mdl.indexOf(this.selectedVal);
		if (this.selectedVal.equals(OmegaDataToolConstants.PARTICLE_FRAMEINDEX)
				|| this.selectedVal
						.equals(OmegaDataToolConstants.PARTICLE_XCOORD)
				|| this.selectedVal
						.equals(OmegaDataToolConstants.PARTICLE_YCOORD)
		/*
		 * && !this.selectedVal
		 * .equals(OmegaDataToolConstants.PARTICLE_CENT_INTENSITY) &&
		 * !this.selectedVal
		 * .equals(OmegaDataToolConstants.PARTICLE_PEAK_INTENSITY)
		 */) {
			this.moveDataUp_btt.setEnabled(true);
			this.moveDataDown_btt.setEnabled(true);
			// this.removeData_btt.setEnabled(true);
		} else {
			this.moveDataUp_btt.setEnabled(true);
			this.moveDataDown_btt.setEnabled(true);
			this.removeData_btt.setEnabled(true);
		}
		// if (index == 3 /* == 5 */) {
		// this.moveDataDown_btt.setEnabled(true);
		// this.removeData_btt.setEnabled(true);
		// } else {
		// this.moveDataUp_btt.setEnabled(true);
		// this.moveDataDown_btt.setEnabled(true);
		// this.removeData_btt.setEnabled(true);
		// }
		// }
	}
	
	private void handleMultipleFilesSelection() {
		// if (this.multipleFiles_ckb.isSelected()) {
		// this.fileIdentifier_txt.setEnabled(true);
		// } else {
		// this.fileIdentifier_txt.setEnabled(false);
		// }
	}
	
	private void importTypeChanged() {
		if (this.importTypeOmega_rbtt.isSelected()) {
			this.createFolders_ckb.setEnabled(true);
			this.particleData_lst.setEnabled(false);
			this.addData_btt.setEnabled(false);
			this.addStandardData_btt.setEnabled(false);
			this.moveDataUp_btt.setEnabled(false);
			this.moveDataDown_btt.setEnabled(false);
			this.removeData_btt.setEnabled(false);
			this.dataIdentifier_txt.setText("");
			this.dataIdentifier_txt.setEnabled(false);
		} else {
			this.createFolders_ckb.setSelected(false);
			this.createFolders_ckb.setEnabled(false);
			this.particleData_lst.setEnabled(true);
			this.addData_btt.setEnabled(true);
			this.addStandardData_btt.setEnabled(true);
			this.dataIdentifier_txt.setEnabled(true);
		}
	}
	
	private void addImageName() {
		if (!this.fileIdentifier_txt.isEditable())
			return;
		String name = "";
		if (this.addImageName_ckb.isSelected()) {
			name += this.namePrefix;
			name += "_";
			name += this.filename;
		} else {
			name += this.filename;
		}
		this.fileIdentifier_txt.setText(name);
	}
	
	private void handleChooseFile() {
		final int result = this.fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			final File dir = this.fileChooser.getSelectedFile();
			this.file_txt.setText(dir.getAbsolutePath());
		} else {
			this.file_txt.setText("");
		}
	}
	
	private void handleAction() {
		if (this.otio instanceof OmegaTracksImporter) {
			final OmegaTracksImporter oti = (OmegaTracksImporter) this.otio;
			oti.reset();
		}
		String fileIdentifier = null;
		File sourceFolder = null;
		String trackIdentifier = "";
		if (!this.dataIdentifier_txt.getText().isEmpty()) {
			trackIdentifier = this.dataIdentifier_txt.getText();
		} else {
			trackIdentifier = null;
		}
		String particleIdentifier = "";
		if (!this.particleIdentifier_txt.getText().isEmpty()) {
			particleIdentifier = this.particleIdentifier_txt.getText();
		} else {
			particleIdentifier = null;
		}
		String nonParticleIdentifier = "";
		if (!this.nonParticleIdentifier_txt.getText().isEmpty()) {
			nonParticleIdentifier = this.nonParticleIdentifier_txt.getText();
		} else {
			nonParticleIdentifier = null;
		}
		String particleSeparator = "";
		if (!this.particleSeparatordentifier_txt.getText().isEmpty()) {
			particleSeparator = this.particleSeparatordentifier_txt.getText();
		}
		String fName = null;
		if (!this.file_txt.getText().isEmpty()) {
			fName = this.file_txt.getText();
		}
		if (fName == null) {
			this.updateStatus("A directory is required to begin the process!");
			return;
		}
		
		final File f = new File(fName);
		if (f.isFile()) {
			this.updateStatus(f.getName()
					+ " is a file, and is not a valid option to begin the process!");
			return;
		}
		sourceFolder = f;
		if (!this.fileIdentifier_txt.getText().isEmpty()) {
			fileIdentifier = this.fileIdentifier_txt.getText();
		}
		if (this.otio instanceof OmegaTracksImporter) {
			if (!this.importTypeOmega_rbtt.isSelected()
					&& (fileIdentifier == null)) {
				this.updateStatus("A file identifier is required to begin the process!");
				return;
			}
		}
		// if (this.multipleFiles_ckb.isSelected()) {
		// } else {
		// if (f.isDirectory())
		// // TODO ERROR
		// return;
		// sourceFolder = f.getParentFile();
		// fileIdentifier = f.getName();
		// }
		final boolean multifile = this.multipleFiles_ckb.isSelected();
		final boolean startAtOne = this.startAtOne_ckb.isSelected();
		final boolean addImageName = this.addImageName_ckb.isSelected();
		final boolean createFolders = this.createFolders_ckb.isSelected();
		boolean completed = false;
		try {
			if (this.isImporter) {
				final List<String> dataOrder = new ArrayList<String>();
				for (int i = 0; i < this.particleData_mdl.getSize(); i++) {
					dataOrder.add(this.particleData_mdl.get(i));
				}
				final OmegaTracksImporter oti = (OmegaTracksImporter) this.otio;
				if (this.importTypeParticles_rbtt.isSelected()) {
					oti.setMode(OmegaTracksImporter.IMPORTER_MODE_PARTICLES);
				} else if (this.importTypeTracks_rbtt.isSelected()) {
					oti.setMode(OmegaTracksImporter.IMPORTER_MODE_TRACKS);
				} else {
					oti.setMode(OmegaTracksImporter.IMPORTER_MODE_OMEGA);
				}
				oti.importData(multifile, fileIdentifier, createFolders,
						trackIdentifier, particleIdentifier, startAtOne,
						nonParticleIdentifier, particleSeparator, dataOrder,
						sourceFolder);
			} else {
				final String extension = (String) this.extension_cmb
						.getSelectedItem();
				final OmegaTracksExporter ote = (OmegaTracksExporter) this.otio;
				ote.export(multifile, fileIdentifier, extension, addImageName,
						createFolders, trackIdentifier, particleIdentifier,
						startAtOne, nonParticleIdentifier, particleSeparator,
						sourceFolder);
			}
			completed = true;
		} catch (final IllegalArgumentException e) {
			// TODO manage error better
			this.updateStatus(e.getClass().getSimpleName() + " - "
					+ e.getMessage());
			completed = false;
			// e.printStackTrace();
		} catch (final IOException e) {
			// TODO manage error better
			this.updateStatus("There was a file problem in the process!");
			completed = false;
			// e.printStackTrace();
		} catch (final ParseException ex) {
			// TODO manage error better
			this.updateStatus("There was a parsing problem in the process!");
			completed = false;
		}
		if (completed) {
			if (this.otio instanceof OmegaTracksExporter) {
				final OmegaTracksExporter ote = (OmegaTracksExporter) this.otio;
				ote.resetAll();
			}
			this.setVisible(false);
		}
	}
	
	private void handleClose() {
		this.setVisible(false);
	}

	public void enableFileName() {
		this.fileIdentifier_txt.setEditable(true);
	}
	
	public void disableFileName() {
		this.fileIdentifier_txt
				.setText(OmegaTracksToolDialog.AUTOMATIC_FILENAME_LBL);
		this.fileIdentifier_txt.setEditable(false);
	}
	
	public void setPrefix(final String namePrefix) {
		this.namePrefix = namePrefix;
	}
	
	public void setFileName(final String filename) {
		this.filename = filename;
	}
	
	public void updateStatus(final String s) {
		try {
			this.statusPanel.updateStatus(0, s);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void setVisible(final boolean isVisible) {
		String name = "";
		if (isVisible) {
			if (this.fileIdentifier_txt.isEditable()) {
				if (this.addImageName_ckb.isSelected()) {
					name += this.namePrefix;
					name += "_";
				}
				if (this.filename != null) {
					name += this.filename;
				}
				this.fileIdentifier_txt.setText(name);
			}
		} else {
			this.fileIdentifier_txt.setText("");
		}
		super.setVisible(isVisible);
	}

	public void resetDialog() {
		this.updateStatus("Ready!");
	}
}
