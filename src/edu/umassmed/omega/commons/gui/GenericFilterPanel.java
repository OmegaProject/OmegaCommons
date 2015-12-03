package edu.umassmed.omega.commons.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.eventSystem.OmegaFilterEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaFilterEvent;

public class GenericFilterPanel extends GenericPanel {

	private static final long serialVersionUID = -7195237698210234650L;

	private final List<OmegaFilterEventListener> listeners = new ArrayList<OmegaFilterEventListener>();

	private JComboBox<String> cmb;
	private JTextField txt;
	private JButton searchBtt, resetBtt;
	private JCheckBox ckb;

	public GenericFilterPanel(final RootPaneContainer parent) {
		super(parent);

		this.createAndAddWidgets();

		this.addListeners();
	}

	private void createAndAddWidgets() {
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		final JLabel lbl1 = new JLabel("Search");
		mainPanel.add(lbl1);
		this.cmb = new JComboBox<String>();
		this.cmb.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.cmb.setMinimumSize(OmegaConstants.BUTTON_SIZE);
		mainPanel.add(this.cmb);
		final JLabel lbl2 = new JLabel("for");
		mainPanel.add(lbl2);
		this.txt = new JTextField();
		this.txt.setPreferredSize(OmegaConstants.BUTTON_SIZE_LARGE);
		this.txt.setMinimumSize(OmegaConstants.BUTTON_SIZE_LARGE);
		mainPanel.add(this.txt);
		final JLabel lbl3 = new JLabel("exact match?");
		mainPanel.add(lbl3);
		this.ckb = new JCheckBox();
		mainPanel.add(this.ckb);
		this.searchBtt = new JButton("Search");
		this.searchBtt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.searchBtt.setMinimumSize(OmegaConstants.BUTTON_SIZE);
		mainPanel.add(this.searchBtt);
		this.resetBtt = new JButton("Reset");
		this.resetBtt.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.resetBtt.setMinimumSize(OmegaConstants.BUTTON_SIZE);
		mainPanel.add(this.resetBtt);

		final JPanel spacePanel = new JPanel();
		spacePanel.setLayout(new BorderLayout());
		final JLabel lbl4 = new JLabel("");
		spacePanel.add(lbl4, BorderLayout.CENTER);

		this.add(mainPanel, BorderLayout.EAST);
		this.add(spacePanel, BorderLayout.CENTER);
	}

	private void addListeners() {
		this.searchBtt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				GenericFilterPanel.this.fireEvent();
			}
		});
		this.resetBtt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				GenericFilterPanel.this.fireResetEvent();
			}
		});
	}

	public void updateCombo(final List<String> columnNames) {
		this.cmb.removeAllItems();
		for (final String s : columnNames) {
			this.cmb.addItem(s);
		}
	}

	public synchronized void addOmegaFilterListener(
	        final OmegaFilterEventListener listener) {
		this.listeners.add(listener);
	}

	public synchronized void removeOmegaFilterEventListe(
			final OmegaFilterEventListener listener) {
		this.listeners.remove(listener);
	}

	public void fireResetEvent() {
		this.cmb.setSelectedIndex(0);
		this.txt.setText("");
		this.ckb.setSelected(false);
		this.fireEvent();
	}

	public synchronized void fireEvent() {
		final String key = (String) this.cmb.getSelectedItem();
		final String value = this.txt.getText();
		final boolean isExact = this.ckb.isSelected();
		final OmegaFilterEvent event = new OmegaFilterEvent(key, value, isExact);
		final Iterator<OmegaFilterEventListener> i = this.listeners.iterator();
		while (i.hasNext()) {
			i.next().handleFilterEvent(event);
		}
	}

	public synchronized void fireEvent(final OmegaFilterEvent event) {
		final Iterator<OmegaFilterEventListener> i = this.listeners.iterator();
		while (i.hasNext()) {
			i.next().handleFilterEvent(event);
		}
	}
}
