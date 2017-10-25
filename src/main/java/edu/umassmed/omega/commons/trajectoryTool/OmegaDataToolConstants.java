package edu.umassmed.omega.commons.trajectoryTool;

public class OmegaDataToolConstants {
	
	public static String RUN_NAME = "Run name";
	public static String RUN_ON = "Run on";
	public static String RUN_BY = "Owner";
	public static String ALGORITHM = "Algorithm";
	public static String AUTHOR = "Author";
	public static String VERSION = "Version";
	public static String PUBLISHED = "Published";
	public static String REFERENCE = "Reference";
	public static String DESCRIPTION = "Description";
	public static String PARAMETERS = "Parameters";
	public static String PARAM = "Param";

	public static final String PARTICLE_TRACKINDEX = "track index";
	public static final String PARTICLE_FRAMEINDEX = "identifier";
	public static final String PARTICLE_XCOORD = "x";
	public static final String PARTICLE_YCOORD = "y";
	public static final String PARTICLE_CENT_INTENSITY = "centroid intensity";
	public static final String PARTICLE_PEAK_INTENSITY = "peak intensity";
	public static final String PARTICLE_SEPARATOR = "separator";
	
	public static final String FILE_IDENTIFIER_TT = "Enter a single file name or in case of multiple files to import, enter a regular expression (RegEx) capturing the file name pattern being used. For example for the following file name: SMSS_MIXED_1.txt. The correct RegEx is: SMSS_MIXED_[\\d].txt";
	public static final String DATA_IDENTIFIER_TT = "In case all of your data is contained in a single file, enter the term describing the type of the data you wish to import. Examples are: Particle, Link, Trajectory, Edited Trajectory, Segment etc.";
	public static final String PARTICLE_IDENTIFIER_TT = "If available, enter the character that in your file identifies lines containing particle data";
	public static final String NON_PARTICLE_IDENTIFIER_TT = "Enter the character which in your file identifies lines containing comments or other non-particle data. These lines will be ignored by the importer. Typical examples include: %, >, /# etc.";
	public static final String MULTIFILE_TT = "Check this box if your data is subdivided in multiple files that contain for example one trajectory per file";
}
