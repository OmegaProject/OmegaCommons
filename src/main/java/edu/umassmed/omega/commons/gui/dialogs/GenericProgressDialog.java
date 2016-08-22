/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School
 * Alessandro Rigano (Program in Molecular Medicine)
 * Caterina Strambio De Castillia (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts:
 * Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.RootPaneContainer;

import edu.umassmed.omega.commons.constants.OmegaConstants;

public class GenericProgressDialog extends GenericDialog {

	private static final long serialVersionUID = -9143470688994233600L;

	private JLabel projectLbl, datasetLbl, imageLbl, analysisLbl, totalLbl;
	private JButton close;
	// private boolean confirmation;
	private JProgressBar projectProgress, datasetProgress, imageProgress,
	analysisProgress, totalProgress;

	public GenericProgressDialog(final RootPaneContainer parentContainer,
			final String title, final String label, final boolean modal) {
		super(parentContainer, title, modal);

		// this.confirmation = false;
		this.totalLbl.setText(label);
		this.totalProgress.setMinimum(0);
		this.totalProgress.setMaximum(1);
		this.analysisProgress.setMinimum(0);
		this.analysisProgress.setMaximum(1);
		this.imageProgress.setMinimum(0);
		this.imageProgress.setMaximum(1);
		this.datasetProgress.setMinimum(0);
		this.datasetProgress.setMaximum(1);
		this.projectProgress.setMinimum(0);
		this.projectProgress.setMaximum(1);
		this.revalidate();
		this.repaint();
		final Dimension dim = new Dimension(600, 300);
		this.setSize(dim);
		this.setPreferredSize(dim);
		// this.pack();
	}

	@Override
	protected void createAndAddWidgets() {
		final JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new GridLayout(2, 1));
		this.projectLbl = new JLabel("Test");
		projectPanel.add(this.projectLbl);
		this.projectProgress = new JProgressBar();
		this.projectProgress.setStringPainted(true);
		projectPanel.add(this.projectProgress);

		final JPanel datasetPanel = new JPanel();
		datasetPanel.setLayout(new GridLayout(2, 1));
		this.datasetLbl = new JLabel("Test");
		datasetPanel.add(this.datasetLbl);
		this.datasetProgress = new JProgressBar();
		this.datasetProgress.setStringPainted(true);
		datasetPanel.add(this.datasetProgress);

		final JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(2, 1));
		this.imageLbl = new JLabel("Test");
		imagePanel.add(this.imageLbl);
		this.imageProgress = new JProgressBar();
		this.imageProgress.setStringPainted(true);
		imagePanel.add(this.imageProgress);

		final JPanel analysisPanel = new JPanel();
		analysisPanel.setLayout(new GridLayout(2, 1));
		this.analysisLbl = new JLabel("Test");
		analysisPanel.add(this.analysisLbl);
		this.analysisProgress = new JProgressBar();
		this.analysisProgress.setStringPainted(true);
		analysisPanel.add(this.analysisProgress);

		// final JPanel subPanel4 = new JPanel();
		// subPanel4.setLayout(new BorderLayout());
		final JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new GridLayout(2, 1));
		this.totalLbl = new JLabel("Test");
		totalPanel.add(this.totalLbl);
		this.totalProgress = new JProgressBar();
		this.totalProgress.setStringPainted(true);
		totalPanel.add(this.totalProgress);
		// subPanel4.add(totalPanel, BorderLayout.NORTH);

		final JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new GridLayout(5, 1));
		progressPanel.add(projectPanel);
		progressPanel.add(datasetPanel);
		progressPanel.add(imagePanel);
		progressPanel.add(analysisPanel);
		progressPanel.add(totalPanel);

		this.add(progressPanel, BorderLayout.CENTER);

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		this.close = new JButton("Close");
		this.close.setEnabled(false);
		this.close.setPreferredSize(OmegaConstants.BUTTON_SIZE);
		this.close.setSize(OmegaConstants.BUTTON_SIZE);
		buttonPanel.add(this.close);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void addListeners() {
		this.close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent evt) {
				// GenericProgressDialog.this.confirmation = true;
				GenericProgressDialog.this.setVisible(false);
			}
		});
	}

	public void enableClose() {
		this.close.setEnabled(true);
	}

	public void setTotalProgressMax(final int max) {
		this.totalProgress.setMaximum(max);
		this.revalidate();
		this.repaint();
	}

	public void setTotalProgressCurrent(final int current) {
		this.totalProgress.setValue(current);
		this.revalidate();
		this.repaint();
	}

	public void setAnalysisProgressMax(final int max) {
		this.analysisProgress.setMaximum(max);
		this.revalidate();
		this.repaint();
	}

	public void setAnalysisProgressCurrent(final int current) {
		this.analysisProgress.setValue(current);
		this.revalidate();
		this.repaint();
	}

	public void setImageProgressMax(final int max) {
		this.imageProgress.setMaximum(max);
		this.revalidate();
		this.repaint();
	}

	public void setImageProgressCurrent(final int current) {
		this.imageProgress.setValue(current);
		this.revalidate();
		this.repaint();
	}

	public void setDatasetProgressMax(final int max) {
		this.datasetProgress.setMaximum(max);
		this.revalidate();
		this.repaint();
	}

	public void setDatasetProgressCurrent(final int current) {
		this.datasetProgress.setValue(current);
		this.revalidate();
		this.repaint();
	}

	public void setProjectProgressMax(final int max) {
		this.projectProgress.setMaximum(max);
		this.revalidate();
		this.repaint();
	}

	public void setProjectProgressCurrent(final int current) {
		this.projectProgress.setValue(current);
		this.revalidate();
		this.repaint();
	}

	public void updateProjectMessage(final String msg) {
		this.projectLbl.setText(msg);
		this.revalidate();
		this.repaint();
	}

	public void updateDatasetMessage(final String msg) {
		this.datasetLbl.setText(msg);
		this.revalidate();
		this.repaint();
	}

	public void updateImageMessage(final String msg) {
		this.imageLbl.setText(msg);
		this.revalidate();
		this.repaint();
	}

	public void updateAnalysisMessage(final String msg) {
		this.analysisLbl.setText(msg);
		this.revalidate();
		this.repaint();
	}

	public void updateTotalMessage(final String msg) {
		this.totalLbl.setText(msg);
		this.revalidate();
		this.repaint();
	}
}
