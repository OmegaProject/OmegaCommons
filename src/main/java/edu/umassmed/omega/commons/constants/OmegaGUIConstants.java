package edu.umassmed.omega.commons.constants;

import java.awt.Dimension;

public class OmegaGUIConstants {
	public static final String EVENT_PROPERTY_PLUGIN = "PluginSelected";
	public static final String EVENT_PROPERTY_TOGGLEWINDOW = "ToggleWindow";
	
	public static final String SIDEPANEL_SCALE_ONE = "Scale 1:1";
	public static final String SIDEPANEL_SCALE_FIT = "Scale To Fit";
	public static final String SIDEPANEL_ITEM_SCALE_FIT = "Scale To Fit";
	public static final String SIDEPANEL_ITEM_SELECTED = "Selected Element: ";
	public static final String SIDEPANEL_ITEM_ORPHANED = "Orphaned Container Selected";
	public static final String SIDEPANEL_NO_ITEM_LOADED = "No Element Loaded";
	public static final String SIDEPANEL_NO_ITEM_SELECTED = "No Element Selected";
	public static final String SIDEPANEL_PROJECT_DETAILS = "Project Details";
	public static final String SIDEPANEL_DATASET_DETAILS = "Dataset Details";
	public static final String SIDEPANEL_IMAGE_DETAILS = "Image Details";
	public static final String SIDEPANEL_PIXELS_DETAILS = "Pixels Details";
	public static final String SIDEPANEL_FRAME_DETAILS = "Frame Details";
	public static final String SIDEPANEL_NO_DETAILS = "No Details Available";
	public static final String SIDEPANEL_TABS_GENERAL = "General";
	public static final String SIDEPANEL_TABS_VIEWOPTIONS = "View Options";
	public static final String SIDEPANEL_TABS_TRACKS = "Trajectories";

	public static final String INFO_FILE_TYPE = "File type";
	public static final String INFO_FILE_TYPE_REGULAR = "regular";
	public static final String INFO_FILE_TYPE_LOCAL = "local";
	public static final String INFO_FILE_TYPE_GLOBAL = "global";
	public static final String INFO_FILE_TYPE_DM_DDT = "displacementVSDeltaT";
	public static final String INFO_FILE_TYPE_DM_MSS = "momentScalingSpectrum";
	public static final String INFO_FILE_TYPE_DM_PS = "phaseSpace";
	public static final String INFO_FILE_TYPE_SNR_IMAGE = "image";
	public static final String INFO_FILE_TYPE_SNR_PLANE = "plane";
	public static final String INFO_FILE_TYPE_SNR_ROI = "roi";
	
	public static final String INFO_COLUMN_SEP = ": ";
	public static final String INFO_ID = "ID";
	public static final String INFO_OWNER = "Owner";
	public static final String INFO_NAME = "Name";
	public static final String INFO_PARENTS = "Parents";
	public static final String INFO_ALGO = "Algorithm";
	public static final String INFO_SALGO = "Algorithm_short";
	public static final String INFO_ALGO_VERSION = "Version";
	public static final String INFO_ALGO_RELEASED = "Released";
	public static final String INFO_ALGO_AUTHOR = "Author(s)";
	public static final String INFO_ALGO_REF = "Reference";
	public static final String INFO_ALGO_DESC = "Description";
	public static final String INFO_PARAMS = "Parameters";
	public static final String INFO_EXECUTED = "Executed";
	
	public static final String INFO_NOT_NAMED = "Item Not Named";
	public static final String INFO_NUM_DATASET = "Number of Datasets";
	public static final String INFO_NUM_IMAGES = "Number of Images";
	public static final String INFO_NUM_ANALYSIS = "Times Analyzed";
	public static final String INFO_NUM_IMG_ANALYSIS = "Number of Images Analyzed";
	public static final String INFO_ACQUIRED = "Acquired";
	public static final String INFO_IMPORTED = "Imported";
	public static final String INFO_DIM_XY = "Dimensions (XY)";
	public static final String INFO_DIM_ZTC = "Dimensions (ZTC)";
	public static final String INFO_PIXELTYPE = "Pixel Type";
	public static final String INFO_PIXELSIZES = "Pixel Sizes (XY) "
			+ OmegaMathSymbolConstants.MU + "m";
	public static final String INFO_PIXELSIZES_Z = "Pixel Sizes (XYZ) "
			+ OmegaMathSymbolConstants.MU + "m";
	
	public static final String SIDEPANEL_RENDERING_Z = "Z-Section";
	public static final String SIDEPANEL_RENDERING_T = "Time Point (Time)";
	public static final String SIDEPANEL_RENDERING_OPTIONS = "Options";
	public static final String SIDEPANEL_RENDERING_IMAGE_COMPRESSION = "Image Compression";
	public static final String SIDEPANEL_RENDERING_C = "Channel";
	
	public static final String SIDEPANEL_TRACKS_SHOWATT = "Show Trajectories Starting At Current Timepoint";
	public static final String SIDEPANEL_TRACKS_SHOWUPT = "Show Trajectories Up To Current Timepoint";
	public static final String SIDEPANEL_TRACKS_ACTIVEONLY = "Show Only Active Trajectories";
	public static final String SIDEPANEL_TRACKS_OVERLAY = "Select Overlay To Display";
	public static final String SIDEPANEL_TRACKS_OVERLAY_PARTICLES = "Particles";
	public static final String SIDEPANEL_TRACKS_OVERLAY_TRACKS = "Trajectories";
	public static final String SIDEPANEL_TRACKS_OVERLAY_ADJ = "Edited Trajectories";
	public static final String SIDEPANEL_TRACKS_OVERLAY_SEGM = "Segmented Trajectories";
	
	public static final String NONE = "None";
	
	public static final String ZOOM_IN = "Zoom In";
	public static final String ZOOM_OUT = "Zoom Out";
	public static final String RANDOM_COLORS = "Assign Random Colors";
	public static final String CHOSE_COLOR = "Chose Color";
	
	public static final String TOPPANEL_PLUGINMENU_IMAGE_BROWSER = "Image Browser";
	public static final String TOPPANEL_PLUGINMENU_DATA_BROWSER = "Data Browser";
	public static final String TOPPANEL_PLUGINMENU_PARTICLE_TRACKER = "Particle Tracking";
	public static final String TOPPANEL_PLUGINMENU_TRACK_MANAGER = "Trajectory Manager";
	public static final String TOPPANEL_PLUGINMENU_TRACK_MEASURES = "Tracking Measures";
	public static final String TOPPANEL_PLUGINMENU_SNR_ESTIMATOR = "SNR Estimation";
	
	public static final String MENU_FILE = "File";
	public static final String MENU_FILE_IMPORT_TRACKS = "Import tracks";
	public static final String MENU_FILE_IMPORT_TRACKS_TT = "Import tracks results from file";
	public static final String MENU_FILE_EXPORT_TRACKS = "Export tracks";
	public static final String MENU_FILE_EXPORT_TRACKS_TT = "Export tracks results to file";
	public static final String MENU_FILE_EXPORT_DIFF = "Export diffusivity results";
	public static final String MENU_FILE_EXPORT_DIFF_TT = "Export diffusivity results to file";
	public static final String MENU_FILE_EXPORT_DATA = "Export all data";
	public static final String MENU_FILE_EXPORT_DATA_TT = "Export all data results to file";
	public static final String MENU_FILE_LOAD = "Load";
	public static final String MENU_FILE_LOAD_ORPHANED = "Load Orphaned";
	public static final String MENU_FILE_LOAD_TT = "Load analysis results from database";
	public static final String MENU_FILE_LOAD_ORPHANED_TT = "Load orphaned analysis results from database";
	public static final String MENU_FILE_SAVE = "Save";
	public static final String MENU_FILE_SAVE_TT = "Save analysis results to database";
	public static final String MENU_FILE_ABOUT = "About";
	public static final String MENU_FILE_QUIT = "Quit";
	public static final String MENU_FILE_CLOSE = "Close";
	public static final String MENU_EDIT = "Edit";
	public static final String MENU_EDIT_PREF = "General Preferences";
	public static final String MENU_EDIT_DB_PREF = "Database Preferences";
	public static final String MENU_VIEW = "View";
	public static final String MENU_VIEW_SEPARATE = "Separate Windows";
	public static final String MENU_VIEW_UNIFY = "Join Windows";
	public static final String MENU_WORKSPACE = "Workspace";
	public static final String MENU_WORKSPACE_DOCK = "Dock Plugin Windows";
	public static final String MENU_WORKSPACE_UNDOCK = "Undock Plugin Windows";
	public static final String MENU_WORKSPACE_DOCK_SINGLE = "Dock Plugin";
	public static final String MENU_WORKSPACE_UNDOCK_SINGLE = "Undock Plugin";
	public static final String MENU_VIEW_HIDE_DATA_SELECTION = "Hide data selection";
	public static final String MENU_VIEW_SHOW_DATA_SELECTION = "Show data selection";
	
	public static final String ALGORITHM_INFORMATION = "Algorithm Information";
	
	public static final String PLUGIN_RUN_QUEUE = "Run Queue";
	public static final String PLUGIN_LOADED_DATA = "Loaded Data";
	public static final String PLUGIN_ORPHANED_ANALYSES = "Orphaned analyses";
	public static final String PLUGIN_RUN_DEFINITION = "Run Definition";
	
	public static final String PLUGIN_INPUT_INFORMATION = "Input information";
	public static final String PLUGIN_PARAMETERS_DETECTION = "Particle Detection Parameters";
	public static final String PLUGIN_PARAMETERS_LINKING = "Particle Linking Parameters";
	public static final String PLUGIN_PARAMETERS_LINKING_ADVANCED = "Particle Linking Advanced Options";
	public static final String PLUGIN_PARAMETERS_SELECTION = "Selection Parameters";
	public static final String PLUGIN_PARAMETERS_SNR = "SNR Estimation Parameters";
	public static final String PLUGIN_PARAMETERS_TMD = "Diffusivity Estimation Parameters";
	public static final String PLUGIN_PARAMETERS_TMDE = "Error Estimation Parameters";
	
	public static final String TRACK_BROWSER_SHOW_SPOT_THUMB = "Show Spots Thumbnail";
	public static final String TRACK_BROWSER_HIDE_SPOT_THUMB = "Hide Spots Thumbnail";
	
	public static final String TRACK_CHOSE_COLOR_CONFIRM = "Chose Color Confirmation";
	public static final String TRACK_CHOSE_COLOR_CONFIRM_MSG = "Do you want to change the color of track: ";
	public static final String TRACK_CHOSE_COLOR_DIALOG_MSG = "Chose color for track: ";
	
	public static final String TRACK_RANDOM_COLOR_CONFIRM = "Random Colors Confirmation";
	public static final String TRACK_RANDOM_COLOR_CONFIRM_MSG = "Do you want to change the color of all tracks?";
	
	public static final String SAVE = "Save";
	public static final String UNDO = "Undo";
	public static final String REDO = "Redo";
	public static final String UNDO_ALL = "Undo all";
	
	public static final String SAVE_NAME = "Save Name";
	public static final String EDIT_NOTES = "Edit Notes";
	
	public static final String SELECT_IMAGE = "Select Image";
	public static final String SELECT_TRACKS_SPOT = "Select Detection Run";
	public static final String SELECT_TRACKS_LINKING = "Select Linking Run";
	public static final String SELECT_TRACKS_ADJ = "Select Editing Run";
	public static final String SELECT_TRACKS_SEGM = "Select Segmentation Run";
	public static final String SELECT_TRACK_MEASURES = "Select Tracking Measures Run";
	
	public static final String RESULTS_IMAGE_ID = "Image ID";
	public static final String RESULTS_PLANE_ID = "Plane ID";
	public static final String RESULTS_PLANE_INDEX = "Plane Index";
	public static final String RESULTS_PARTICLE_ID = "Particle ID";
	public static final String RESULTS_FRAME = "Frame";
	public static final String RESULTS_X = "X";
	public static final String RESULTS_Y = "Y";
	public static final String RESULTS_C = "C";
	public static final String RESULTS_Z = "Z";
	public static final String RESULTS_INDEX = "Index";
	public static final String RESULTS_TRACK_ID = "Trajectory ID";
	public static final String RESULTS_TRACK_NAME = "Trajectory Name";
	public static final String RESULTS_TRACK_LENGTH = "Trajectory Length";
	public static final String RESULTS_SEGM_ID = "Segment ID";
	public static final String RESULTS_SEGM_NAME = "Segment Name";
	public static final String RESULTS_SEGM_TYPE = "Motion Type";
	public static final String RESULTS_SEGM_LENGTH = "Segment Length";

	public static final String RESULTS_AVERAGE = "Avg";
	public static final String RESULTS_MIN = "Min";
	public static final String RESULTS_MAX = "Max";

	public static final String RESULTS_INTENSITY_CENTROID = "Centroid Intensity";
	public static final String RESULTS_INTENSITY_PEAK = "Peak Intensity";
	public static final String RESULTS_INTENSITY_MEAN = "Mean Intensity";
	public static final String RESULTS_INTENSITY_BACKGROUND = "Local Background";
	public static final String RESULTS_INTENSITY_NOISE = "Local Noise";
	public static final String RESULTS_INTENSITY_SNR = "Local SNR";

	public static final String RESULTS_INTENSITY_CENTROID_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_CENTROID;
	public static final String RESULTS_INTENSITY_CENTROID_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_CENTROID;
	public static final String RESULTS_INTENSITY_CENTROID_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_CENTROID;
	public static final String RESULTS_INTENSITY_PEAK_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_PEAK;
	public static final String RESULTS_INTENSITY_PEAK_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_PEAK;
	public static final String RESULTS_INTENSITY_PEAK_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_PEAK;
	public static final String RESULTS_INTENSITY_MEAN_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_MEAN;
	public static final String RESULTS_INTENSITY_MEAN_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_MEAN;
	public static final String RESULTS_INTENSITY_MEAN_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_MEAN;
	public static final String RESULTS_INTENSITY_BACKGROUND_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND;
	public static final String RESULTS_INTENSITY_BACKGROUND_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND;
	public static final String RESULTS_INTENSITY_BACKGROUND_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_BACKGROUND;
	public static final String RESULTS_INTENSITY_NOISE_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_NOISE;
	public static final String RESULTS_INTENSITY_NOISE_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_NOISE;
	public static final String RESULTS_INTENSITY_NOISE_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_NOISE;
	public static final String RESULTS_INTENSITY_SNR_AVG = OmegaGUIConstants.RESULTS_AVERAGE
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_SNR;
	public static final String RESULTS_INTENSITY_SNR_MIN = OmegaGUIConstants.RESULTS_MIN
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_SNR;
	public static final String RESULTS_INTENSITY_SNR_MAX = OmegaGUIConstants.RESULTS_MAX
			+ " " + OmegaGUIConstants.RESULTS_INTENSITY_SNR;

	public static final String RESULTS_MOBILITY_DIST = "Distance Traveled";
	public static final String RESULTS_MOBILITY_CUM_CURV_DIST = "Cumulative Curvilinear Distance Traveled";
	public static final String RESULTS_MOBILITY_CUM_STR_DIST = "Cumulative Straight Distance Traveled";
	public static final String RESULTS_MOBILITY_CUM_CONF_RATIO = "Cumulative Confinement Ratio";
	public static final String RESULTS_MOBILITY_CUM_INSTA_ANGLE = "Instantaneous Angle";
	public static final String RESULTS_MOBILITY_DIR_CHANGE = "Directional Change";
	public static final String RESULTS_MOBILITY_CUM_TIME = "Cumulative Time Traveled";

	public static final String RESULTS_MOBILITY_TOT_CURV_DIST = "Total Curvilinear Distance Traveled";
	public static final String RESULTS_MOBILITY_MAX_STR_DIST = "Max Cumulative Straight Distance Traveled";
	public static final String RESULTS_MOBILITY_TOT_STR_DIST = "Total Net Straight Distance Traveled";
	public static final String RESULTS_MOBILITY_CONF_RATIO = "Confinement Ratio";
	public static final String RESULTS_MOBILITY_TOT_TIME = "Total Time Traveled";
	
	public static final String RESULTS_VELOCITY_SPEED = "Instantaneous Speed";
	public static final String RESULTS_VELOCITY_CUM_SPEED = "Instantaneous Cumulative Curvilinear Speed";
	public static final String RESULTS_VELOCITY_CUM_VELO = "Instantaneous Cumulative Straight Speed";
	
	public static final String RESULTS_VELOCITY_AVG_SPEED = "Average Curvilinear Speed";
	public static final String RESULTS_VELOCITY_AVG_VELO = "Average Straight Speed";
	public static final String RESULTS_VELOCITY_PROG = "Forward Progression Linearity";

	// DIFF LOCAL
	public static final String RESULTS_DIFFUSIVITY_WIN_SIZE = "Max Computation Window Size";
	public static final String RESULTS_DIFFUSIVITY_MOMENT_ORDER = "Moment's Order (Nu)";
	public static final String RESULTS_DIFFUSIVITY_DELTA = "Delta T";
	public static final String RESULTS_DIFFUSIVITY_MU = "Moment of Displacement (Mu)";
	public static final String RESULTS_DIFFUSIVITY_DELTA_LOG = "Log Delta T";
	public static final String RESULTS_DIFFUSIVITY_MU_LOG = "Log Moment of Displacement";

	// DIFF GENERIC GLOBAL
	public static final String RESULTS_DIFFUSIVITY_GAMMA = "Slope (Gamma)";
	public static final String RESULTS_DIFFUSIVITY_Y0 = "Intercept (y0)";
	public static final String RESULTS_DIFFUSIVITY_FIT = "Goodness of Fit";

	public static final String RESULTS_DIFFUSIVITY_GAMMA_LOG = "Slope (Gamma) (Log)";
	public static final String RESULTS_DIFFUSIVITY_Y0_LOG = "Intercept (y0) (Log)";
	public static final String RESULTS_DIFFUSIVITY_FIT_LOG = "Goodness of Fit (Log)";
	public static final String RESULTS_DIFFUSIVITY_ODC_LOG = "ODC2 (Log)";
	
	// DIFF SPECIFIC GLOBAL
	public static final String RESULTS_DIFFUSIVITY_MSD = "Slope Log-Log MSD";
	public static final String RESULTS_DIFFUSIVITY_Y02 = "Intercept (y02)";
	public static final String RESULTS_DIFFUSIVITY_ODC = "ODC2";
	public static final String RESULTS_DIFFUSIVITY_SMSS = "Slope MSS";
	public static final String RESULTS_DIFFUSIVITY_ODC_ERR = "Uncertainty ODC2";
	public static final String RESULTS_DIFFUSIVITY_SMSS_ERR = "Uncertainty SMSS";
	
	public static final String RESULTS_DIFFISIVITY_MIN_DET_ODC = "Minimum Detectable ODC";

	public static final String RESULTS_SNR_AREA = "Particle Area";
	public static final String RESULTS_SNR_BACKGROUND = "Background";
	public static final String RESULTS_SNR_NOISE = "Noise";
	public static final String RESULTS_SNR_SNR_AVG = "Avg SNR";
	public static final String RESULTS_SNR_SNR_MIN = "Min SNR";
	public static final String RESULTS_SNR_SNR_MAX = "Max SNR";

	public static final String RESULTS_SNR_SNR_INDEX = "Index SNR";
	
	public static final String RESULTS_SNR_SNR_INDEX_AVG = "Avg Index SNR";
	public static final String RESULTS_SNR_SNR_INDEX_MIN = "Min Index SNR";
	public static final String RESULTS_SNR_SNR_INDEX_MAX = "Max Index SNR";

	// TAB LABELS
	public static final String TAB_TRACK_TRACK_BROWSER = "Trajectory Browser";
	public static final String TAB_TRACK_SEGM_BROWSER = "Segment Browser";
	public static final String TAB_GRAPH = "Plots";
	public static final String TAB_RESULTS = "Results";
	public static final String TAB_RESULTS_LOCAL = "Local results";
	public static final String TAB_RESULTS_GLOBAL = "Global results";
	public static final String TAB_RUN = "Run";
	
	public static final String PREFERENCES = "Preferences";
	
	public static final String EDIT_DETAILS = "Edit Details";
	
	public static final String NOT_ASSIGNED = "NA";
	public static final String NOT_A_NUMBER = "NaN";
	public static final String NULL = "null";
	
	// SIZES
	public final static int THUMBNAIL_SIZE = 100;
	public final static int DRAWING_POINTSIZE = 4;
	public final static Dimension BUTTON_SIZE_SMALL = new Dimension(60, 20);
	public final static Dimension BUTTON_SIZE = new Dimension(120, 20);
	public final static Dimension BUTTON_SIZE_LARGE = new Dimension(180, 20);
	public final static Dimension BUTTON_SIZE_LARGE_DOUBLE_HEIGHT = new Dimension(
			180, 40);
	public final static Dimension TEXT_SIZE = new Dimension(200, 20);
	public final static Dimension LARGE_TEXT_SIZE = new Dimension(300, 20);
	public final static Dimension LARGE_LIST_SIZE = new Dimension(300, 100);

	// COMBO BOX
	public final static int COMBOBOX_MAX_OPTIONS = 5;
	
	// RELINKING
	public final static String RELINKING_CURRENT = "Unsaved Editing";
	
	// SEGMENTATION
	public final static String SEGMENTATION_CURRENT = "Unsaved Segmentation";
}
