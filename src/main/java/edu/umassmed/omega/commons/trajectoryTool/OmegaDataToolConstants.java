package edu.umassmed.omega.commons.trajectoryTool;

public class OmegaDataToolConstants {

	public static boolean COMPLETE_CHAIN_AFTER_IMPORT = true;

	public static final int PARENT_IMAGE = 0;
	public static final int PARENT_DETECTION = 1;
	public static final int PARENT_LINKING = 2;
	public static final int PARENT_RELINKING = 3;
	public static final int PARENT_SEGMENTATION = 4;
	public static final int PARENT_SNR = 5;

	public static final String PARTICLE_TRACKINDEX = "track index";
	public static final String PARTICLE_FRAMEINDEX = "identifier";
	public static final String PARTICLE_XCOORD = "x";
	public static final String PARTICLE_YCOORD = "y";
	public static final String PARTICLE_CENT_INTENSITY = "centroid intensity";
	public static final String PARTICLE_PEAK_INTENSITY = "peak intensity";
	public static final String PARTICLE_SEPARATOR = "separator";
	public static final String PARTICLE_ID = "id";
	public static final String PARTICLE_C = "channel";
	public static final String PARTICLE_Z = "z-plane";
	
	public static final String FILE_IDENTIFIER_TT = "Enter a single file name or in case of multiple files to import, enter a regular expression (RegEx) capturing the file name pattern being used. For example for the following file name: SMSS_MIXED_1.txt. The correct RegEx is: SMSS_MIXED_[\\d].txt";
	public static final String DATA_TYPE_IDENTIFIER_TT = "In case all of your data is contained in a single file, enter the term describing the type of the data you wish to import. Examples are: Particle, Link, Trajectory, Edited Trajectory, Segment etc.";
	public static final String DATA_LINE_IDENTIFIER_TT = "If available, enter the character that in your file identifies lines containing data";
	public static final String COMMENT_IDENTIFIER_TT = "Enter the character which in your file identifies lines containing comments or other non data. These lines will be ignored by the importer. Typical examples include: %, >, /# etc.";
	public static final String DATA_SEPARATOR_TT = "Enter the character which in your file separates the column of the line containing data. TAB if the characters is the tabulator ('\t'), SPACE if the character is an empty space (' '). Typical examples include: 'space', 'tab', ',' etc.";
	public static final String INDEX_START_1_TT = "Activate this option in case your particles / trajectories / segments are indexed from 1 up instead that from 0 up.";
	public static final String MULTIFILE_TT = "Check this box if your data is subdivided in multiple files that contain for example one trajectory per file";
}
