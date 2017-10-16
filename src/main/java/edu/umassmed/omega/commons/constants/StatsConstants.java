package edu.umassmed.omega.commons.constants;

import javax.xml.bind.DatatypeConverter;

public class StatsConstants {
	
	public static final String DELTA_SYMBOL = new String(
			DatatypeConverter.parseHexBinary("ce94")).toLowerCase();
	
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
	public static final String TAB_GRAPH = "Plots";
	public static final String TAB_RESULTS = "Results";
	public static final String TAB_RESULTS_LOCAL = "Local results";
	public static final String TAB_RESULTS_GLOBAL = "Global results";
	public static final String TAB_RUN = "Run";
	
	// MOTION TYPE CLASSIFICATION
	public static final String GRAPH_MTC_LBL_COMPLETE = "Complete set";
	public static final String GRAPH_MTC_LBL_TRACK = "Trajectory plot";
	public static final String GRAPH_MTC_LBL_MSD = "Log-Log MSD vs. "
			+ StatsConstants.DELTA_SYMBOL + "t plot";
	public static final String GRAPH_MTC_LBL_MSS = "MSS plot";
	public static final String GRAPH_MTC_LBL_PHASE = "ODC2 vs. Slope MSS scatter plot";
	// TRACK
	public static final String GRAPH_MTC_NAME_TRACK = "Trajectory";
	public static final String GRAPH_MTC_LAB_TRACK_X = "X ["
			+ OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_MTC_LAB_TRACK_Y = "Y ["
			+ OmegaConstantsMathSymbols.MU + "m]";
	// MSD
	public static final String GRAPH_MTC_NAME_MSD = "MSD Plot";
	public static final String GRAPH_MTC_LAB_MSD_X = "Log("
			+ StatsConstants.DELTA_SYMBOL + "T) [log s]";
	public static final String GRAPH_MTC_LAB_MSD_Y = "Log(MSD) [log pixel^2 or"
			+ OmegaConstantsMathSymbols.MU + "m^2]";
	// MSS
	public static final String GRAPH_MTC_NAME_MSS = "MSS Plot";
	public static final String GRAPH_MTC_LAB_MSS_X = "Moment order";
	public static final String GRAPH_MTC_LAB_MSS_Y = "Gamma [a.u.]";
	// PHASE SPACE
	public static final String GRAPH_MTC_NAME_SMSS_D = "Phase Space";
	public static final String GRAPH_MTC_LAB_SMSS_D_X = "ODC2 ["// "D2 ["
			+ OmegaConstantsMathSymbols.MU + "m^2/s]";
	public static final String GRAPH_MTC_LAB_SMSS_D_Y = "Slope MSS [a.u]";
	
	// DIFFUSIVITY
	// GLOBAL
	public static final String GRAPH_NAME_UNCERT_D = "Uncertainty of OCD2 estimation";
	// "Diffusion Coefficent (D2) Uncertainty";
	public static final String GRAPH_NAME_UNCERT_SMSS = "Uncertainty of Slope MSS estimation";
	// "Slope MSS Uncertainty";
	public static final String GRAPH_NAME_DIFF = "Observed Diffusion Constant of Order 2 (Logaritmic Intercept)";
	// "Diffusion Coefficient (D2)";
	public static final String GRAPH_NAME_MSD = "Slope of Log-Log MSD vs. "
			+ StatsConstants.DELTA_SYMBOL + "t Plot";
	// "Slope of log-log MSD vs T Plot";
	public static final String GRAPH_NAME_MSS = "Slope of the Moment Scaling Spectrum";
	// "Slope MSS";
	// Y LABELS
	public static final String GRAPH_LAB_Y_UNCERT_D = "Uncertainty of OCD2 [a.u.]";
	// "D2 Uncertainty [a.u.]";
	public static final String GRAPH_LAB_Y_UNCERT_SMSS = "Uncertainty of Slope MSS [a.u]";
	// "Slope MSS Uncertainty [a.u.]";
	public static final String GRAPH_LAB_Y_DIFF = "Observed Diffusion Constant of Order 2 ["// "D2 ["
			+ OmegaConstantsMathSymbols.MU + "m^2/s]";
	public static final String GRAPH_LAB_Y_MSD = "Slope of Log-Log MSD vs. "
			+ StatsConstants.DELTA_SYMBOL + "t Plot [a.u.]";
	// "Slope of log(MSD) / log(deltaT) [a.u.]";
	public static final String GRAPH_LAB_Y_MSS = "Slope of the Moment Scaling Spectrum [a.u.]";
	// "Gamma [a.u.]";
	
	// MOBILITY
	// GLOBAL
	public static final String GRAPH_NAME_DIST_GLO = "Total Curvilinar Distance Traveled";
	public static final String GRAPH_NAME_MAX_DISP_GLO = "Max Cumulative Straigh-Line Distance Traveled";
	public static final String GRAPH_NAME_DISP_GLO = "Total Net Straigh-Line Distance Traveled";
	public static final String GRAPH_NAME_TIME_GLO = "Total Time Traveled";
	public static final String GRAPH_NAME_CONFRATIO_GLO = "Confinement Ratio";
	// LOCAL
	public static final String GRAPH_NAME_DIST_P2P_LOC = "Distance Traveled";
	public static final String GRAPH_NAME_TIME_LOC = "Cumulative Time Traveled";
	public static final String GRAPH_NAME_DIST_LOC = "Cumulative Curvilinear Distance Traveled ";
	public static final String GRAPH_NAME_DISP_LOC = "Cumulative Straigh-Line Distance Traveled ";
	public static final String GRAPH_NAME_CONFRATIO_LOC = "Cumulative "
			+ StatsConstants.GRAPH_NAME_CONFRATIO_GLO;
	public static final String GRAPH_NAME_ANGLES_LOC = "Instantaneous Angle";
	public static final String GRAPH_NAME_DIRCHANGE_LOC = "Directional change";
	// Y LABELS
	public static final String GRAPH_LAB_Y_DISP = "Displacement [pixel or"
			+ OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_LAB_Y_DIST = "Distance [pixel or"
			+ OmegaConstantsMathSymbols.MU + "m]";
	public static final String GRAPH_LAB_Y_TOT_TIME = "Time traveled [timepoint or s]";
	public static final String GRAPH_LAB_Y_CONFRATIO = "Confinement ratio [a.u.]";
	public static final String GRAPH_LAB_Y_ANGLES = "Angle [rad]";
	public static final String GRAPH_LAB_Y_DIRCHANGE = "Directional change [rad]";
	
	// VELOCITY
	// LOCAL
	public static final String GRAPH_NAME_SPEED_P2P_LOC = "Instantaneous Speed";
	public static final String GRAPH_NAME_SPEED_LOC = "Instantanous Cumulative Curvilinear Speed";
	public static final String GRAPH_NAME_VEL_LOC = "Instantaneous Cumulative Straigh-Line Speed";
	// GLOBAL
	public static final String GRAPH_NAME_SPEED_GLO = "Average Curvilinear Speed";
	public static final String GRAPH_NAME_VEL_GLO = "Average Straight-Line Speed";
	public static final String GRAPH_NAME_FORPROLIN_GLO = "Forward Progression Linearity";
	// Y LABELS
	public static final String GRAPH_LAB_Y_VEL = "Velocity [pixel or "
			+ OmegaConstantsMathSymbols.MU + "m /s]";
	public static final String GRAPH_LAB_Y_SPEED = "Speed [pixel or "
			+ OmegaConstantsMathSymbols.MU + "m /s]";
	
	// INTENSITY
	// INTENSITY
	public static final String MAX_PEAK_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.GRAPH_NAME_INT_PEAK;
	public static final String AVG_PEAK_INTENSITY_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.GRAPH_NAME_INT_PEAK;
	public static final String MIN_PEAK_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.GRAPH_NAME_INT_PEAK;
	public static final String MAX_CENTROID_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.GRAPH_NAME_INT_CENT;
	public static final String AVG_CENTROID_INTENSITY_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.GRAPH_NAME_INT_CENT;
	public static final String MIN_CENTROID_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.GRAPH_NAME_INT_CENT;
	public static final String MAX_MEAN_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.GRAPH_NAME_INT_MEAN;
	public static final String AVG_MEAN_INTENSITY_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.GRAPH_NAME_INT_MEAN;
	public static final String MIN_MEAN_INTENSITY_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.GRAPH_NAME_INT_MEAN;
	public static final String MAX_BACKGROUND_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.BACKGROUND_LOC;
	public static final String AVG_BACKGROUND_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.BACKGROUND_LOC;
	public static final String MIN_BACKGROUND_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.BACKGROUND_LOC;
	public static final String MAX_NOISE_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.NOISE_LOC;
	public static final String AVG_NOISE_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.NOISE_LOC;
	public static final String MIN_NOISE_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.NOISE_LOC;
	public static final String MAX_AREA_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.GRAPH_NAME_AREA;
	public static final String AVG_AREA_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.GRAPH_NAME_AREA;
	public static final String MIN_AREA_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.GRAPH_NAME_AREA;
	public static final String MAX_SNR_GLO = StatsConstants.GRAPH_NAME_MAX
			+ " " + StatsConstants.SNR_LOC;
	public static final String AVG_SNR_GLO = StatsConstants.GRAPH_NAME_AVG
			+ " " + StatsConstants.SNR_LOC;
	public static final String MIN_SNR_GLO = StatsConstants.GRAPH_NAME_MIN
			+ " " + StatsConstants.SNR_LOC;
	
	public static final String PEAK_INTENSITY_LOC = StatsConstants.GRAPH_NAME_INT_PEAK;
	public static final String CENTROID_INTENSITY_LOC = StatsConstants.GRAPH_NAME_INT_CENT;
	public static final String MEAN_INTENSITY_LOC = StatsConstants.GRAPH_NAME_INT_MEAN;
	public static final String AREA_LOC = StatsConstants.GRAPH_NAME_AREA;
	public static final String BACKGROUND_LOC = "Local "
			+ StatsConstants.GRAPH_NAME_BACKGROUND;
	public static final String NOISE_LOC = "Local "
			+ StatsConstants.GRAPH_NAME_NOISE;
	public static final String SNR_LOC = "Local "
			+ StatsConstants.GRAPH_NAME_SNR;
	
	public static final String GRAPH_NAME_INT_PEAK = "Peak Intensity";
	public static final String GRAPH_NAME_INT_CENT = "Centroid Intensity";
	public static final String GRAPH_NAME_INT_MEAN = "Mean Intensity";
	public static final String GRAPH_NAME_AREA = "Particle Area";
	public static final String GRAPH_NAME_BACKGROUND = "Background";
	public static final String GRAPH_NAME_NOISE = "Noise";
	public static final String GRAPH_NAME_SNR = "SNR";
	public static final String GRAPH_NAME_PROB = "Identification Probability";
	public static final String GRAPH_NAME_RADIUS = "Radius";
	public static final String GRAPH_LAB_Y_INT = "Flourescence Intensity [a.u.]";
	// LABELS
	public static final String GRAPH_NAME_MAX = "Maximum ";
	public static final String GRAPH_NAME_MIN = "Minimum ";
	public static final String GRAPH_NAME_AVG = "Average ";
	
	// GENERAL
	public static final String GRAPH_LAB_Y_FREQ = "Frequency";
	public static final String GRAPH_LAB_X_TRACK = "Trajectory";
	public static final String GRAPH_LAB_X_TPT = "Time Point or Time";
	public static final String GRAPH_LAB_X_TIME = "Time Point [a.u] or Time [s]";
}
