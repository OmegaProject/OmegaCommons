/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School Alessandro
 * Rigano (Program in Molecular Medicine) Caterina Strambio De Castillia
 * (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts: Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.constants;

import java.awt.Color;
import java.io.File;

import javax.swing.UIDefaults;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;

public class OmegaGenericConstants {
	
	public final static OmegaExperimenter OMEGA_DEFAULT_EXPERIMENTER = new OmegaExperimenter(
			"Default", "Experimenter");
	
	// BUILD AND INFO
	public final static String OMEGA_TITLE = "OMEGA pre beta release";
	public final static String OMEGA_BUILD = "build 0.39";
	public final static String OMEGA_WEBSITE = "http://omega.umassmed.edu/";
	public final static String OMEGA_AUTHOR = "UMass Medical School";
	public final static String OMEGA_DESCRIPTION = "<html>Open Microscopy Environment inteGrated Analysis</html>";
	
	public static String PREF_GRAPH_LINE_SIZE = "Graphics Line Size";
	public static Integer PREF_GRAPH_LINE_SIZE_VAL = 2;
	public static String PREF_GRAPH_SHAPE_SIZE = "Graphics Shape Size";
	public static Integer PREF_GRAPH_SHAPE_SIZE_VAL = 2;
	public static String PREF_TRACK_LINE_SIZE = "Trajectory Line Size";
	public static Integer PREF_TRACK_LINE_SIZE_VAL = 2;
	
	// PATHS AND FILENAMES
	public final static String OMEGA_IMGS_FOLDER_2 = "." + File.separator
			+ "src" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "images";
	public final static String OMEGA_IMGS_FOLDER = "images";
	public final static String OMEGA_SPT_FOLDER_2 = "." + File.separator
			+ "src" + File.separator + "main" + File.separator + "resources"
			+ File.separator + "sptWinPlugin";
	public final static String OMEGA_SPT_FOLDER = "sptWinPlugin";
	public final static String OMEGA_SPT_DLL = "omega-spt-stats";
	public final static String OMEGA_ERROR_INTERPOLATION_FOLDER_2 = "."
			+ File.separator + "src" + File.separator + "main" + File.separator
			+ "resources" + File.separator + "errorInterpolation";
	
	public final static String OMEGA_ERROR_INTERPOLATION_FOLDER = "errorInterpolation";
	public final static String OMEGA_ERROR_INTERPOLATION_DATA = "2018-05-10";
	public final static String OMEGA_ERROR_INTERPOLATION_D_FILE = "D_interpolation_data_old.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_D_FILE_L3 = "D_interpolation_data_L3.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_D_FILE_L5 = "D_interpolation_data_L5.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_D_FILE_L10 = "D_interpolation_data_L10.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_SMSS_FILE = "SMSS_interpolation_data_old.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_SMSS_FILE_L3 = "SMSS_interpolation_data_L3.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_SMSS_FILE_L5 = "SMSS_interpolation_data_L5.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_SMSS_FILE_L10 = "SMSS_interpolation_data_L10.csv";
	
	// LOGS
	public final static String LOG_TRAIN_CALLED = "DLL train method called";
	public final static String LOG_SEGMENT_CALLED = "DLL segment method called";
	public final static String LOG_SET_INI_FAILED = "Cannot set the INI file";
	
	// DATE FORMATTING
	public final static String OMEGA_DATE_FORMAT = "yyyy-MM-dd_hh-mm-ss";
	public final static String OMEGA_DATE_FORMAT_LBL = "yyyy";
	
	// COLORS
	public final static Color getDefaultSelectionBackgroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.selectionBackground");
	}

	public final static Color getDefaultSelectionForegroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.selectionForeground");
	}
	
	public static Color getDefaultBackgroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.background");
	}
}
