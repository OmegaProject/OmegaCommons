package edu.umassmed.omega.commons.constants;

public class StatsConstants {

	public static final String GRAPH_RESULTSTYPE_LBL = "Select Results Type";

	public static final String GRAPH_X_LBL = "<html>Select Independent Variable<br>(x axis)</html>";
	public static final String GRAPH_Y_LBL = "<html>Select Dependent Variable<br>(y axis)</html>";
	public static final String GRAPH_TYPE = "Select Graph Type:";
	public static final String GRAPH_DRAW = "Draw";
	public static final String GRAPH_VAL_RANGE = "Max Value or Range";
	public static final String GRAPH_VAL_RANGE_TT = "Select a Maximum Value or a Range Interval for Y axis";

	public static final String GRAPH_TYPE_LINE = "Line";
	public static final String GRAPH_TYPE_BAR = "Bar";
	public static final String GRAPH_TYPE_HIST = "Frequency Distribution";
	public static final String GRAPH_TYPE_BOXP = "Box Plot";
	public static final String GRAPH_TYPE_SWARMP = "Swarm Plot";

	public static final String TAB_TRACK_BROWSER = "Browser";
	public static final String TAB_GRAPH = "Graphs";
	public static final String TAB_RESULTS = "Results";
	public static final String TAB_RESULTS_LOCAL = "Local results";
	public static final String TAB_RESULTS_GLOBAL = "Global results";
	public static final String TAB_RUN = "Run";

	// MOTION TYPE CLASSIFICATION
	public static final String GRAPH_MTC_NAME_TRACK = "Trajectory";
	public static final String GRAPH_MTC_LAB_TRACK_X = "X ["
			+ OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_MTC_LAB_TRACK_Y = "Y ["
			+ OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_MTC_NAME_MSD = "MSD Plot";
	public static final String GRAPH_MTC_LAB_MSD_X = "Log(deltaT) [log s]";
	public static final String GRAPH_MTC_LAB_MSD_Y = "Log(MSD) [log pixel^2 or"
			+ OmegaConstantsMathSymbols.MU + "m^2]";
	public static final String GRAPH_MTC_NAME_MSS = "MSS Plot";
	public static final String GRAPH_MTC_LAB_MSS_X = "Moment order";
	public static final String GRAPH_MTC_LAB_MSS_Y = "Gamma [a.u.]";
	public static final String GRAPH_MTC_NAME_SMSS_D = "Phase Space";
	public static final String GRAPH_MTC_LAB_SMSS_D_X = "D2 ["
			+ OmegaConstantsMathSymbols.MU + "m^2/s]";
	public static final String GRAPH_MTC_LAB_SMSS_D_Y = "Slope MSS [a.u]";

	// MOBILITY
	// GLOBAL
	public static final String GRAPH_NAME_MAX_DISP = "Max Displacement";
	public static final String GRAPH_LAB_Y_MAX_DISP = "Delta max [pixel or"
	        + OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_NAME_TOT_DISP = "Total Net Displacement";
	public static final String GRAPH_LAB_Y_TOT_DISP = "Delta net [pixel or"
	        + OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_NAME_TOT_DIST = "Total Distance Traveled";
	public static final String GRAPH_LAB_Y_TOT_DIST = "D tot [pixel or"
	        + OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_NAME_TOT_TIME = "Total Track Time";
	public static final String GRAPH_LAB_Y_TOT_TIME = "T tot [timepoint or s]";
	public static final String GRAPH_NAME_CONFRATIO = "Confinement Ratio";
	public static final String GRAPH_LAB_Y_CONFRATIO = "R con [a.u.]";
	// LOCAL
	public static final String GRAPH_NAME_ANGLES = "Instantaneous Angle";
	public static final String GRAPH_LAB_Y_ANGLES = "Angle alpha [rad]";
	public static final String GRAPH_NAME_ANGLES_LOCAL = "Direction change";
	public static final String GRAPH_LAB_Y_ANGLES_LOCAL = "Angle beta i [rad]";

	// VELOCITY
	public static final String GRAPH_NAME_SPEED_LOCAL = "Instantaneous Speed";
	public static final String GRAPH_LAB_Y_SPEED = "Speed [pixel or "
			+ OmegaConstantsMathSymbols.MU + "m /s]";
	public static final String GRAPH_NAME_VEL_LOCAL = "Instantaneous Velocity";
	public static final String GRAPH_LAB_Y_VEL = "Velocity [pixel or "
			+ OmegaConstantsMathSymbols.MU + "m /s]";
	public static final String GRAPH_NAME_SPEED = "Average Curvilinear Speed";
	public static final String GRAPH_NAME_VEL = "Average Straight-Line Velocity";

	// DIFFUSIVITY
	// GLOBAL
	public static final String GRAPH_NAME_UNCERT_D = "D2 Uncertainty";
	public static final String GRAPH_LAB_Y_UNCERT_D = "D2 Uncertainty [a.u.]";
	public static final String GRAPH_NAME_UNCERT_SMSS = "Slope MSS Uncertainty";
	public static final String GRAPH_LAB_Y_UNCERT_SMSS = "Slope MSS Uncertainty [a.u.]";
	public static final String GRAPH_NAME_DIFF = "Diffusion Coefficient";
	public static final String GRAPH_LAB_Y_DIFF = "D2 ["
			+ OmegaConstantsMathSymbols.MU + "m^2/s]";
	public static final String GRAPH_NAME_MSD = "Slope of log-log MSD Plot";
	public static final String GRAPH_LAB_Y_MSD = "Slope of log(MSD) / log(deltaT) [a.u.]";
	public static final String GRAPH_NAME_MSS = "Slope MSS";
	public static final String GRAPH_LAB_Y_MSS = "Gamma [a.u.]";

	// GENERAL
	public static final String GRAPH_LAB_Y_FREQ = "Frequency";
	public static final String GRAPH_LAB_X_TRACK = "Track";
	public static final String GRAPH_LAB_X_TPT = "Timepoint or Time";
	public static final String GRAPH_LAB_X_TIME = "Timepoint[a.u] or Time [s]";
}
