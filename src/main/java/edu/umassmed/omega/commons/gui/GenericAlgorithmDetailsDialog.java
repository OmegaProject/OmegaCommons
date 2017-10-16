package edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAlgorithmInformation;
import edu.umassmed.omega.commons.gui.dialogs.GenericDialog;
import edu.umassmed.omega.commons.utilities.OmegaStringUtilities;

public class GenericAlgorithmDetailsDialog extends GenericDialog {

	private static final long serialVersionUID = -678221971763691869L;
	private JPanel topIconPanel, topInfoPanel;

	private JScrollPane mainPanel;
	
	private JButton close_btt;

	private JLabel name_lbl, auth_lbl, date_lbl, version_lbl, ref_lbl;
	// ,desc_lbl;
	
	private JTextPane desc_ta;

	public GenericAlgorithmDetailsDialog(final RootPaneContainer parentContainer) {
		super(parentContainer, OmegaGUIConstants.ALGORITHM_INFORMATION, false);

		final Dimension dialogDim = new Dimension(600, 330);
		this.setPreferredSize(dialogDim);
		this.setSize(dialogDim);

		this.createPluginInformation();

		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.revalidate();
		this.repaint();
		// this.pack();
	}

	@Override
	protected void createAndAddWidgets() {
		final JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		this.topIconPanel = new JPanel();
		this.topIconPanel.setLayout(new FlowLayout());
		topPanel.add(this.topIconPanel, BorderLayout.WEST);

		this.topInfoPanel = new JPanel();
		// this.topInfoPanel.setLayout(new GridLayout(6, 1));
		this.topInfoPanel.setLayout(new BoxLayout(this.topInfoPanel,
				BoxLayout.Y_AXIS));
		topPanel.add(this.topInfoPanel, BorderLayout.CENTER);

		this.add(topPanel, BorderLayout.NORTH);

		this.mainPanel = new JScrollPane(new JLabel(""));
		// this.mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// this.mainPanel.getViewport().setLayout(new FlowLayout());
		this.add(this.mainPanel, BorderLayout.CENTER);

		final JPanel buttPanel = new JPanel();
		buttPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		this.close_btt = new JButton(OmegaGUIConstants.MENU_FILE_CLOSE);
		this.close_btt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		buttPanel.add(this.close_btt);

		this.add(buttPanel, BorderLayout.SOUTH);
	}

	private void createPluginInformation() {
		// if (this.plugin.getIcon() != null) {
		// final JLabel icon_lbl = new JLabel(this.plugin.getIcon());
		// this.topIconPanel.add(icon_lbl);
		// }

		this.name_lbl = new JLabel(OmegaGUIConstants.SIDEPANEL_INFO_NAME);
		this.auth_lbl = new JLabel(OmegaGUIConstants.AUTHOR);
		this.date_lbl = new JLabel(OmegaGUIConstants.RELEASED);
		this.version_lbl = new JLabel(OmegaGUIConstants.VERSION);
		this.ref_lbl = new JLabel(OmegaGUIConstants.REFERENCE);
		// final JLabel reference_lbl = new JLabel(OmegaGUIConstants.REFERENCE
		// + this.plugin.getAlgorithmReference());
		this.topInfoPanel.setBorder(new EmptyBorder(10, 0, 0, 10));
		this.topInfoPanel.add(this.name_lbl);
		this.topInfoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.topInfoPanel.add(this.auth_lbl);
		this.topInfoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.topInfoPanel.add(this.date_lbl);
		this.topInfoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.topInfoPanel.add(this.version_lbl);
		this.topInfoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.topInfoPanel.add(this.ref_lbl);
		this.topInfoPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		// this.topInfoPanel.add(this.ref_lbl);
		this.topInfoPanel.add(new JLabel(OmegaGUIConstants.DESCRIPTION));
		this.desc_ta = new JTextPane();
		this.desc_ta.setContentType("text/html");
		this.desc_ta.setBackground(this.name_lbl.getBackground());
		final Font font = this.name_lbl.getFont();
		this.desc_ta.setFont(font);
		// this.desc_lbl = new JLabel("");
		final Dimension dim = new Dimension(this.getWidth() - 30,
				(this.getHeight() - 60) / 3);
		final Dimension dim2 = new Dimension(this.getWidth() - 10,
				(this.getHeight() - 20) / 3);
		// this.desc_lbl.setPreferredSize(dim);
		// this.desc_lbl.setSize(dim);
		this.desc_ta.setPreferredSize(dim);
		this.desc_ta.setSize(dim);
		// final JPanel pan = new JPanel();
		// pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));
		// pan.add(this.desc_lbl);
		// pan.add(new JLabel(""));
		this.mainPanel.setViewportView(this.desc_ta);
		this.mainPanel.setPreferredSize(dim2);
		this.mainPanel.setSize(dim2);
		this.mainPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
	}

	@Override
	protected void addListeners() {
		this.close_btt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericAlgorithmDetailsDialog.this.setVisible(false);
			}
		});
	}

	public void updateAlgorithmInformation(
			final OmegaAlgorithmInformation algoInfo) {
		if (algoInfo != null) {
			this.name_lbl.setText(OmegaGUIConstants.SIDEPANEL_INFO_NAME
					+ algoInfo.getName());
			final String authors = OmegaStringUtilities.getHtmlString(
					OmegaGUIConstants.AUTHOR + algoInfo.getAuthors(), " ",
					SwingConstants.LEADING);
			this.auth_lbl.setText(authors);
			final DateFormat format = new SimpleDateFormat(
					OmegaConstants.OMEGA_DATE_FORMAT_LBL);
			this.date_lbl.setText(OmegaGUIConstants.RELEASED
					+ format.format(algoInfo.getPublicationData()));
			this.version_lbl.setText(OmegaGUIConstants.VERSION
					+ algoInfo.getVersion());
			final String ref = OmegaStringUtilities.getHtmlString(
					OmegaGUIConstants.REFERENCE + algoInfo.getReference(), " ",
					SwingConstants.LEADING);
			this.ref_lbl.setText(ref);
			final String desc = OmegaStringUtilities.getHtmlString(
					algoInfo.getDescription(), " ", SwingConstants.LEADING);
			// this.desc_lbl.setText(desc);
			this.desc_ta.setText(desc);
			this.mainPanel.setViewportView(this.desc_ta);
		} else {
			this.name_lbl.setText(OmegaGUIConstants.SIDEPANEL_INFO_NAME);
			this.auth_lbl.setText(OmegaGUIConstants.AUTHOR);
			this.date_lbl.setText(OmegaGUIConstants.RELEASED);
			this.version_lbl.setText(OmegaGUIConstants.VERSION);
			this.ref_lbl.setText(OmegaGUIConstants.REFERENCE);
			// this.desc_lbl.setText("");
			this.desc_ta.setText("");
			this.mainPanel.setViewportView(this.desc_ta);
		}
	}
}
