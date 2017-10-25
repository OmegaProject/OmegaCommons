package edu.umassmed.omega.commons.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.umassmed.omega.commons.constants.OmegaConstants;
import edu.umassmed.omega.commons.constants.OmegaGUIConstants;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaAlgorithmInformation;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaParameter;
import edu.umassmed.omega.commons.data.analysisRunElements.OmegaRunDefinition;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaROI;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegment;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaSegmentationTypes;
import edu.umassmed.omega.commons.data.trajectoryElements.OmegaTrajectory;

public class OmegaAlgorithmsUtilities {

	public static OmegaRunDefinition DEFAULT_IMPORTER_SPEC;
	private static String DEFAULT_IMPORTER_ALGO_NAME = "OMEGA Data Importer";
	private static String DEFAULT_IMPORTER_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_IMPORTER_ALGO_DESC = "This is the Omega Default data importer tool";
	private static Date DEFAULT_IMPORTER_ALGO_DATE = new GregorianCalendar(
			2015, 5, 16).getTime();
	private static String DEFAULT_IMPORTER_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static List<OmegaParameter> DEFAULT_TRACKING_MEASURES_DIFF_PARAMS = new ArrayList<OmegaParameter>();
	public static List<OmegaParameter> DEFAULT_TRACKING_MEASURES_INTE_PARAMS = new ArrayList<OmegaParameter>();

	public static OmegaRunDefinition DEFAULT_TRACKING_MEASURES_DIFF_SPEC;
	private static String DEFAULT_TRACKING_MEASURES_DIFF_ALGO_NAME = "OMEGA Diffusivity Measures";
	private static String DEFAULT_TRACKING_MEASURES_DIFF_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_TRACKING_MEASURES_DIFF_ALGO_DESC = "This algorithm implements diffusivity measurements as described by Ewers et al. (in: Ewers, H., A.E. Smith, I.F. Sbalzarini, H. Lilie, P. Koumoutsakos, and A. Helenius. 2005. Single-particle tracking of murine polyoma virus-like particles on live cells and artificial membranes. Proc Natl Acad Sci USA. 102:15110–15115. doi:10.1073/pnas.0504407102). In addition, it provides an easy to use graphical user interface to plot such measures for each individual trajectory. Finally it utilizes a scatter plot to summarize the data and display the observed diffusion constant (ODC2) and the slope of the moment scaling spectrum (SMSS) of all trajectories in a dataset in a single plot. Individual trajectories are represented as single points in this ODC2 vs. SMSS phase space, allowing to globally evaluate the behavior of 'clouds' of trajectories and compare the effect of different experimental conditions on their shapes.";
	private static Date DEFAULT_TRACKING_MEASURES_DIFF_ALGO_DATE = new GregorianCalendar(
			2014, 29, 1).getTime();
	private static String DEFAULT_TRACKING_MEASURES_DIFF_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static OmegaRunDefinition DEFAULT_TRACKING_MEASURES_MOBI_SPEC;
	private static String DEFAULT_TRACKING_MEASURES_MOBI_ALGO_NAME = "OMEGA Mobility Measures";
	private static String DEFAULT_TRACKING_MEASURES_MOBI_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_TRACKING_MEASURES_MOBI_ALGO_DESC = "This algorithm implements calculation of intensity measures as described by Meijering et al. (in: E. Meijering, O. Dzyubachyk, and I. Smal.  2012. Methods for cell and particle tracking. Meth. Enzymol. 504:183–200. doi:10.1016/B978-0-12-391857-4.00009-4). In addition, it provides an easy to use graphical user interface to plot such measures in function of time and or trajectory number.";
	private static Date DEFAULT_TRACKING_MEASURES_MOBI_ALGO_DATE = new GregorianCalendar(
			2014, 29, 1).getTime();
	private static String DEFAULT_TRACKING_MEASURES_MOBI_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static OmegaRunDefinition DEFAULT_TRACKING_MEASURES_VELO_SPEC;
	private static String DEFAULT_TRACKING_MEASURES_VELO_ALGO_NAME = "OMEGA Velocity Measures";
	private static String DEFAULT_TRACKING_MEASURES_VELO_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_TRACKING_MEASURES_VELO_ALGO_DESC = "This algorithm implements calculation of velocity measures as described by Meijering et al. (in: E. Meijering, O. Dzyubachyk, and I. Smal.  2012. Methods for cell and particle tracking. Meth. Enzymol. 504:183–200. doi:10.1016/B978-0-12-391857-4.00009-4). In addition, it provides an easy to use graphical user interface to plot such measures in function of time and or trajectory number.";
	private static Date DEFAULT_TRACKING_MEASURES_VELO_ALGO_DATE = new GregorianCalendar(
			2014, 29, 1).getTime();
	private static String DEFAULT_TRACKING_MEASURES_VELO_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static OmegaRunDefinition DEFAULT_TRACKING_MEASURES_INTE_SPEC;
	private static String DEFAULT_TRACKING_MEASURES_INTE_ALGO_NAME = "OMEGA Intensity Measures";
	private static String DEFAULT_TRACKING_MEASURES_INTE_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_TRACKING_MEASURES_INTE_ALGO_DESC = "This algorithm implements calculation of intensity measures as described by Meijering et al. (in: E. Meijering, O. Dzyubachyk, and I. Smal.  2012. Methods for cell and particle tracking. Meth. Enzymol. 504:183–200. doi:10.1016/B978-0-12-391857-4.00009-4). In addition, it provides an easy to use graphical user interface to plot such measures in function of time and or trajectory number.";
	private static Date DEFAULT_TRACKING_MEASURES_INTE_ALGO_DATE = new GregorianCalendar(
			2014, 29, 1).getTime();
	private static String DEFAULT_TRACKING_MEASURES_INTE_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static OmegaRunDefinition DEFAULT_SEGMENTATIONS_SPEC;
	private static String DEFAULT_SEGMENTATION_ALGO_NAME = "OMEGA Trajectory Segmentation";
	private static String DEFAULT_SEGMENTATION_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_SEGMENTATION_ALGO_DESC = "This is a the default trajectory segmentation algorithm designed to subdivide individual trajectories into segments characterized by uniform motion properties. The subdivision of individual trajectories into segments is subjectively decided by the user and is therefore arbitrary. ";
	private static Date DEFAULT_SEGMENTATION_ALGO_DATE = new GregorianCalendar(
			2014, 12, 1).getTime();
	private static String DEFAULT_SEGMENTATION_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static OmegaRunDefinition DEFAULT_RELINKING_SPEC;
	private static String DEFAULT_RELINKING_ALGO_NAME = "OMEGA Trajectory Editing";
	private static String DEFAULT_RELINKING_ALGO_VERSION = OmegaGUIConstants.NOT_ASSIGNED;
	private static String DEFAULT_RELINKING_ALGO_DESC = "This is a the default trajectory re-linking algorithm designed to edit individual particle-particle links in cases in which linking errors are introduced by the Single Particle Tracking algorithm. Such errors are common and typically result from particle blinking, temporary focal plane shifts or the crossing of trajectories with temporary particle fusion.";
	private static Date DEFAULT_RELINKING_ALGO_DATE = new GregorianCalendar(
			2014, 12, 1).getTime();
	private static String DEFAULT_RELINKING_ALGO_REFERENCE = OmegaGUIConstants.NOT_ASSIGNED;

	public static String DEFAULT_DEVELOPER = "Alex Rigano";

	static {
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_PARAMS
				.add(new OmegaParameter(
						OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW,
						OmegaConstants.PARAMETER_DIFFUSIVITY_WINDOW_3));
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_PARAMS
				.add(new OmegaParameter(
						OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION,
						OmegaConstants.PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_AND_LINEAR));
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_PARAMS
				.add(new OmegaParameter(OmegaConstants.PARAMETER_ERROR_OPTION,
						OmegaConstants.PARAMETER_ERROR_OPTION_DISABLED));

		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_PARAMS
				.add(new OmegaParameter(OmegaConstants.PARAMETER_SNR_USE,
						String.valueOf(false)));

		// OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER = new OmegaPerson("Alex",
		// "Rigano");
		final OmegaAlgorithmInformation algoInfoRelinking = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_RELINKING_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_RELINKING_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_RELINKING_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_RELINKING_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_RELINKING_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_RELINKING_SPEC = new OmegaRunDefinition(
				algoInfoRelinking);
		final OmegaAlgorithmInformation algoInfoSegmentation = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATION_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATION_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATION_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATION_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATION_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATIONS_SPEC = new OmegaRunDefinition(
				algoInfoSegmentation);
		final OmegaAlgorithmInformation algoInfoTrackingMeasuresDiff = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_SPEC = new OmegaRunDefinition(
				algoInfoTrackingMeasuresDiff);
		final OmegaAlgorithmInformation algoInfoTrackingMeasuresMobi = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_SPEC = new OmegaRunDefinition(
				algoInfoTrackingMeasuresMobi);
		final OmegaAlgorithmInformation algoInfoTrackingMeasuresVelo = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_SPEC = new OmegaRunDefinition(
				algoInfoTrackingMeasuresVelo);
		final OmegaAlgorithmInformation algoInfoTrackingMeasuresInte = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_SPEC = new OmegaRunDefinition(
				algoInfoTrackingMeasuresInte);
		final OmegaAlgorithmInformation algoInfoTracksImporter = new OmegaAlgorithmInformation(
				OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_ALGO_NAME,
				OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_ALGO_VERSION,
				OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_ALGO_DESC,
				OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER,
				OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_ALGO_DATE,
				OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_ALGO_REFERENCE);
		OmegaAlgorithmsUtilities.DEFAULT_IMPORTER_SPEC = new OmegaRunDefinition(
				algoInfoTracksImporter);
	}

	public static OmegaRunDefinition getDefaultRelinkingAlgorithmSpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_RELINKING_SPEC;
	}

	public static OmegaRunDefinition getDefaultSegmentationAlgorithmSpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_SEGMENTATIONS_SPEC;
	}

	public static OmegaRunDefinition getDefaultTrackingMeasuresDiffusivitySpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_DIFF_SPEC;
	}

	public static OmegaRunDefinition getDefaultTrackingMeasuresMobilitySpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_MOBI_SPEC;
	}

	public static OmegaRunDefinition getDefaultTrackingMeasuresVelocitySpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_VELO_SPEC;
	}

	public static OmegaRunDefinition getDefaultTrackingMeasuresIntensitySpecification() {
		return OmegaAlgorithmsUtilities.DEFAULT_TRACKING_MEASURES_INTE_SPEC;
	}

	public static String getDefaultDeveloper() {
		return OmegaAlgorithmsUtilities.DEFAULT_DEVELOPER;
	}

	public static Map<OmegaTrajectory, List<OmegaSegment>> createDefaultSegmentation(
			final List<OmegaTrajectory> trajectories) {
		final Map<OmegaTrajectory, List<OmegaSegment>> segmentsMap = new LinkedHashMap<>();
		for (final OmegaTrajectory trajectory : trajectories) {
			final List<OmegaSegment> segments = OmegaAlgorithmsUtilities
					.createDefaultSegmentation(trajectory);
			segmentsMap.put(trajectory, segments);
		}
		return segmentsMap;
	}

	public static List<OmegaSegment> createDefaultSegmentation(
			final OmegaTrajectory trajectory) {
		final List<OmegaSegment> segments = new ArrayList<OmegaSegment>();
		final OmegaROI startingPoint = trajectory.getROIs().get(0);
		final OmegaROI endingPoint = trajectory.getROIs().get(
				trajectory.getLength() - 1);
		final String name = trajectory.getName() + "_"
				+ OmegaSegment.DEFAULT_SEGM_NAME + "_"
				+ startingPoint.getFrameIndex() + "-"
				+ endingPoint.getFrameIndex();
		final OmegaSegment edge = new OmegaSegment(startingPoint, endingPoint,
				name);
		edge.setSegmentationType(OmegaSegmentationTypes.NOT_ASSIGNED_VAL);
		segments.add(edge);
		return segments;
	}
}
