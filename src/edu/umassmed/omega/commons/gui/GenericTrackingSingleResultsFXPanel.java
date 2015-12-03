package edu.umassmed.omega.commons.gui;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.coreElements.OmegaElement;
import edu.umassmed.omega.commons.data.coreElements.OmegaFrame;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class GenericTrackingSingleResultsFXPanel extends TitledPane {

	private final GridPane mainGridPane;
	private final TableView<String> table;

	public GenericTrackingSingleResultsFXPanel(final OmegaElement element,
			final List<OmegaROI> rois) {
		if (element instanceof OmegaTrajectory) {
			final OmegaTrajectory t = (OmegaTrajectory) element;
			this.setText(t.getName());
		} else if (element instanceof OmegaFrame) {
			final OmegaFrame f = (OmegaFrame) element;
			this.setText("Frame " + f.getIndex());
		}

		this.mainGridPane = new GridPane();
		this.mainGridPane.setPadding(new Insets(OmegaGUIConstants.PADDING));
		this.mainGridPane.setVgap(OmegaGUIConstants.GAP);
		this.mainGridPane.setHgap(OmegaGUIConstants.GAP);

		// Insert generic info

		this.table = new TableView<String>();

	}
}
