package edu.umassmed.omega.commons.trajectoryTool.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.umassmed.omega.commons.constants.OmegaConstants;
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
	private GenericInsertDialog insertDialog;
	private GenericPickDialog pickDialog;
	private JComboBox<String> extension_cmb;

	private String selectedVal;

	private final OmegaIOUtility otio;

	private final boolean isImpExp, isImporter;

	private JPanel fieldsPanel;

	private GenericStatusPanel statusPanel;
	
	public OmegaTracksToolDialog(final RootPaneContainer parentContainer,
			final boolean isImpExp, final boolean isImporter,
			final OmegaIOUtility otio) {
		super(parentContainer, "Omega Particle/Trajectory Data Importer", false);
		if (!isImporter) {
			this.setTitle("Omega Data Exporter");
		}
		this.isImporter = isImporter;
		this.isImpExp = isImpExp;
		this.otio = otio;
		this.selectedVal = null;
		
		this.adjustWidgets();

		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.revalidate();
		this.repaint();
		this.pack();
	}

	private void adjustWidgets() {
		if (!this.isImporter) {
			this.fieldsPanel.setLayout(new GridLayout(9, 1));
		} else {
			this.fieldsPanel.setLayout(new GridLayout(8, 1));
		}

		if (!this.isImporter) {
			final JPanel extensionPanel = new JPanel();
			extensionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			final JLabel extension_lbl = new JLabel("File extension: ");
			extension_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
			extensionPanel.add(extension_lbl);
			this.extension_cmb = new JComboBox<String>();
			this.extension_cmb.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_TXT);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_XY);
			this.extension_cmb.addItem(OmegaTracksToolDialog.EXTENSIONS_CSV);
			this.extension_cmb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					OmegaTracksToolDialog.this.handleExtensionSelection();
				}
			});
			extensionPanel.add(this.extension_cmb);
			this.fieldsPanel.add(extensionPanel, 4);
		}

		if (this.isImporter) {
			this.action_btt.setText("Import");
		} else {
			this.action_btt.setText("Export");
		}
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

		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		final JPanel fieldsPanelMain = new JPanel();
		fieldsPanelMain.setLayout(new BorderLayout());
		this.fieldsPanel = new JPanel();

		final JPanel filePanel = new JPanel();
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel folder_lbl = new JLabel("Select folder: ");
		folder_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		filePanel.add(folder_lbl);
		this.file_txt = new JTextField();
		this.file_txt.setText(this.fileChooser.getCurrentDirectory()
				.getAbsolutePath());
		this.file_txt.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		filePanel.add(this.file_txt);
		this.chooseFile_btt = new JButton("File browser");
		this.chooseFile_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		filePanel.add(this.chooseFile_btt);
		this.fieldsPanel.add(filePanel);

		final JPanel multipleFilesPanel = new JPanel();
		multipleFilesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel multipleFiles_lbl = new JLabel("Multiple files: ");
		multipleFiles_lbl.setToolTipText("<html><p width=\"500\">"
				+ OmegaDataToolConstants.MULTIFILE_TT + "</p></html>");
		multipleFiles_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		multipleFilesPanel.add(multipleFiles_lbl);
		this.multipleFiles_ckb = new JCheckBox();
		multipleFilesPanel.add(this.multipleFiles_ckb);
		this.fieldsPanel.add(multipleFilesPanel);

		final JPanel fileIdentifierPanel = new JPanel();
		fileIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel fileIdentifier_lbl = new JLabel("File indentifier: ");
		fileIdentifier_lbl.setToolTipText("<html><p width=\"500\">"
				+ OmegaDataToolConstants.FILE_IDENTIFIER_TT + "</p></html>");
		fileIdentifier_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		fileIdentifierPanel.add(fileIdentifier_lbl);
		this.fileIdentifier_txt = new JTextField();
		this.fileIdentifier_txt
				.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		fileIdentifierPanel.add(this.fileIdentifier_txt);
		this.fieldsPanel.add(fileIdentifierPanel);

		final JPanel trackIdentifierPanel = new JPanel();
		trackIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel trackIdentifier_lbl = new JLabel("Data type indentifier: ");
		fileIdentifier_lbl.setToolTipText("<html><p width=\"500\">"
				+ OmegaDataToolConstants.DATA_IDENTIFIER_TT + "</p></html>");
		trackIdentifier_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		trackIdentifierPanel.add(trackIdentifier_lbl);
		this.dataIdentifier_txt = new JTextField();
		this.dataIdentifier_txt
				.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		trackIdentifierPanel.add(this.dataIdentifier_txt);
		this.fieldsPanel.add(trackIdentifierPanel);

		final JPanel particleIdentifierPanel = new JPanel();
		particleIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel particleIdentifier_lbl = new JLabel("Line indentifier: ");
		particleIdentifier_lbl
				.setToolTipText("<html><p width=\"500\">"
						+ OmegaDataToolConstants.PARTICLE_IDENTIFIER_TT
						+ "</p></html>");
		particleIdentifier_lbl
				.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		particleIdentifierPanel.add(particleIdentifier_lbl);
		this.particleIdentifier_txt = new JTextField();
		this.particleIdentifier_txt
				.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		particleIdentifierPanel.add(this.particleIdentifier_txt);
		this.fieldsPanel.add(particleIdentifierPanel);

		final JPanel particleStartPanel = new JPanel();
		particleStartPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel particleStart_lbl = new JLabel("Indexes start at 1: ");
		particleStart_lbl.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		particleStartPanel.add(particleStart_lbl);
		this.startAtOne_ckb = new JCheckBox();
		particleStartPanel.add(this.startAtOne_ckb);
		this.fieldsPanel.add(particleStartPanel);

		final JPanel nonParticleIdentifierPanel = new JPanel();
		nonParticleIdentifierPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JLabel nonParticleIdentifier_lbl = new JLabel(
				"Comment line identifier: ");
		nonParticleIdentifier_lbl.setToolTipText("<html><p width=\"500\">"
				+ OmegaDataToolConstants.NON_PARTICLE_IDENTIFIER_TT
				+ "</p></html>");
		nonParticleIdentifier_lbl
				.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		nonParticleIdentifierPanel.add(nonParticleIdentifier_lbl);
		this.nonParticleIdentifier_txt = new JTextField();
		this.nonParticleIdentifier_txt
				.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		nonParticleIdentifierPanel.add(this.nonParticleIdentifier_txt);
		this.fieldsPanel.add(nonParticleIdentifierPanel);

		final JPanel particleSeparatorIdentifierPanel = new JPanel();
		particleSeparatorIdentifierPanel.setLayout(new FlowLayout(
				FlowLayout.LEFT));
		final JLabel particleSeparatorIdentifier_lbl = new JLabel(
				"Column separator: ");
		particleSeparatorIdentifier_lbl
				.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		particleSeparatorIdentifierPanel.add(particleSeparatorIdentifier_lbl);
		this.particleSeparatordentifier_txt = new JTextField();
		this.particleSeparatordentifier_txt
				.setPreferredSize(OmegaConstants.LARGE_TEXT_SIZE);
		particleSeparatorIdentifierPanel
				.add(this.particleSeparatordentifier_txt);
		this.fieldsPanel.add(particleSeparatorIdentifierPanel);

		final JPanel particleDataPanel = new JPanel();
		particleDataPanel.setLayout(new BorderLayout(5, 5));
		particleDataPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		final JLabel particleDataPanel_lbl = new JLabel(
				"Particle entry names and order: ");
		particleDataPanel_lbl
				.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
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
		sp.setPreferredSize(new Dimension(OmegaConstants.LARGE_TEXT_SIZE.width,
				200));
		particleDataPanel.add(sp, BorderLayout.CENTER);
		final JPanel particleDataButtonPanelMain = new JPanel();
		particleDataButtonPanelMain.setLayout(new BorderLayout());
		final JPanel particleDataButtonPanel = new JPanel();
		particleDataButtonPanel.setLayout(new GridLayout(5, 1));
		this.addData_btt = new JButton("Add");
		this.addData_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		particleDataButtonPanel.add(this.addData_btt);
		this.addStandardData_btt = new JButton("Add standard");
		this.addStandardData_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		particleDataButtonPanel.add(this.addStandardData_btt);
		this.moveDataUp_btt = new JButton("Move Up");
		this.moveDataUp_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.moveDataUp_btt.setEnabled(false);
		particleDataButtonPanel.add(this.moveDataUp_btt);
		this.moveDataDown_btt = new JButton("Move Down");
		this.moveDataDown_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.moveDataDown_btt.setEnabled(false);
		particleDataButtonPanel.add(this.moveDataDown_btt);
		this.removeData_btt = new JButton("Remove");
		this.removeData_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.removeData_btt.setEnabled(false);
		particleDataButtonPanel.add(this.removeData_btt);
		particleDataButtonPanelMain.add(particleDataButtonPanel,
				BorderLayout.NORTH);
		particleDataButtonPanelMain.add(new JLabel(), BorderLayout.CENTER);
		particleDataPanel.add(particleDataButtonPanelMain, BorderLayout.EAST);

		fieldsPanelMain.add(this.fieldsPanel, BorderLayout.NORTH);
		fieldsPanelMain.add(particleDataPanel, BorderLayout.CENTER);

		mainPanel.add(fieldsPanelMain, BorderLayout.NORTH);
		mainPanel.add(new JLabel(), BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.CENTER);

		final JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		final JPanel buttPanel = new JPanel();
		buttPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		this.action_btt = new JButton();
		this.action_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		buttPanel.add(this.action_btt);

		this.close_btt = new JButton(OmegaGUIConstants.MENU_FILE_CLOSE);
		this.close_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		buttPanel.add(this.close_btt);

		bottomPanel.add(buttPanel, BorderLayout.NORTH);

		this.statusPanel = new GenericStatusPanel(1);
		this.updateStatus("Ready!");
		bottomPanel.add(this.statusPanel, BorderLayout.SOUTH);

		this.add(bottomPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void addListeners() {
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
				OmegaDataToolConstants.PARTICLE_TRACKINDEX };
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
		String particleSeparator = "\t";
		if (!this.particleSeparatordentifier_txt.getText().isEmpty()) {
			particleSeparator = this.particleSeparatordentifier_txt.getText();
		}
		final List<String> dataOrder = new ArrayList<String>();
		for (int i = 0; i < this.particleData_mdl.getSize(); i++) {
			dataOrder.add(this.particleData_mdl.get(i));
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
		if (fileIdentifier == null) {
			this.updateStatus(f.getName()
					+ "A file identifier and is required to begin the process!");
			return;
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
		boolean completed = false;
		try {
			if (this.isImporter) {
				final OmegaTracksImporter oti = (OmegaTracksImporter) this.otio;
				oti.setMode(OmegaTracksImporter.IMPORTER_MODE_TRACKS);
				oti.importData(multifile, fileIdentifier, trackIdentifier,
						particleIdentifier, startAtOne, nonParticleIdentifier,
						particleSeparator, dataOrder, sourceFolder);
			} else {
				final String extension = (String) this.extension_cmb
						.getSelectedItem();
				final OmegaTracksExporter ote = (OmegaTracksExporter) this.otio;
				ote.export(multifile, fileIdentifier, extension,
						trackIdentifier, particleIdentifier, startAtOne,
						nonParticleIdentifier, particleSeparator, dataOrder,
						sourceFolder);
			}
			completed = true;
		} catch (final IllegalArgumentException e) {
			// TODO manage error better
			this.updateStatus(e.getMessage());
			completed = false;
			// e.printStackTrace();
		} catch (final IOException e) {
			// TODO manage error better
			this.updateStatus("There was a file problem in the process!");
			completed = false;
			// e.printStackTrace();
		}
		if (this.otio instanceof OmegaTracksExporter) {
			final OmegaTracksExporter ote = (OmegaTracksExporter) this.otio;
			ote.resetAll();
		}
		if (completed) {
			this.setVisible(false);
		}
	}

	private void handleClose() {
		this.setVisible(false);
	}

	public void disableFileName() {
		this.fileIdentifier_txt.setEditable(false);
	}
	
	public void setFileName(final String targetAnalysisName) {
		this.fileIdentifier_txt.setText(targetAnalysisName);
	}

	public void updateStatus(final String s) {
		try {
			this.statusPanel.updateStatus(0, s);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}
}
