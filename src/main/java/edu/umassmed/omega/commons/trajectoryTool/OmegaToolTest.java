package edu.umassmed.omega.commons.trajectoryTool;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;
import edu.umassmed.omega.commons.eventSystem.OmegaTrajectoryIOEventListener;
import edu.umassmed.omega.commons.eventSystem.events.OmegaTrajectoryIOEvent;

public class OmegaToolTest implements OmegaTrajectoryIOEventListener {

	private final JFrame frame;
	private JButton b1, b2, b3;

	private final List<OmegaTrajectory> tracks;

	public OmegaToolTest() {
		this.frame = new JFrame();

		this.createAndAddWidgets();

		this.addListeners();

		this.frame.pack();

		this.tracks = new ArrayList<OmegaTrajectory>();
	}

	public void show() {
		this.frame.setVisible(true);
	}

	private void createAndAddWidgets() {
		final Container panel = this.frame.getContentPane();
		panel.setLayout(new GridLayout(1, 3));

		this.b1 = new JButton("Importer");
		this.b2 = new JButton("Exporter");
		this.b3 = new JButton("Calculate");

		panel.add(this.b1);
		panel.add(this.b2);
		panel.add(this.b3);
	}

	private void addListeners() {
		this.b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final OmegaTracksExporter oti = new OmegaTracksExporter();
				oti.showDialog(OmegaToolTest.this.frame);
			}
		});

		this.b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final OmegaTracksImporter ote = new OmegaTracksImporter();
				ote.showDialog(OmegaToolTest.this.frame);
			}
		});

		this.b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static void main(final String[] args) {
		final OmegaToolTest ott = new OmegaToolTest();
		ott.show();
	}

	@Override
	public void handleIOEvent(final OmegaTrajectoryIOEvent event) {
		if (event.getEventType() == OmegaTrajectoryIOEvent.INPUT) {
			final OmegaTracksImporter oti = (OmegaTracksImporter) event
					.getSource();
			System.out.println(oti.getTracks().size());
		} else {

		}
	}
}
