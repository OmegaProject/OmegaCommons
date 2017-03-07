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
import java.awt.Dimension;
import java.io.File;

import javax.swing.UIDefaults;

import edu.umassmed.omega.commons.data.coreElements.OmegaExperimenter;

public class OmegaConstants {

	public final static OmegaExperimenter OMEGA_DEFAULT_EXPERIMENTER = new OmegaExperimenter(
			"Default", "Experimenter");

	// ***BUILD AND INFO
	public final static String OMEGA_TITLE = "OMEGA pre beta release";
	public final static String OMEGA_BUILD = "build 0.02";
	public final static String OMEGA_WEBSITE = "http://omega.umassmed.edu/";
	public final static String OMEGA_AUTHOR = "UMass Medical School";
	public final static String OMEGA_DESCRIPTION = "<html>Open Microscopy Environment inteGrated Analysis</html>";

	// ***PATHS AND FILENAMES
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
	public final static String OMEGA_ERROR_INTERPOLATION_D_FILE = "D_interpolation_data_old.csv";
	public final static String OMEGA_ERROR_INTERPOLATION_SMSS_FILE = "SMSS_interpolation_data_old.csv";

	// ***LOGS***
	public final static String LOG_TRAIN_CALLED = "DLL train method called";
	public final static String LOG_SEGMENT_CALLED = "DLL segment method called";
	public final static String LOG_SET_INI_FAILED = "Cannot set the INI file";

	// ***SIZES***
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

	// ***DATE FORMATTING***
	public final static String OMEGA_DATE_FORMAT = "yyyy-MM-dd_hh-mm-ss";
	public final static String OMEGA_DATE_FORMAT_LBL = "yyyy";

	// ***RELINKING***
	public final static String OMEGA_RELINKING_CURRENT = "Unsaved relinking";

	// ***SEGMENTATION***
	public final static String OMEGA_SEGMENTATION_CURRENT = "Unsaved segmentation";

	// ***DIFFUSIVITY***
	public static final String PARAMETER_DIFFUSIVITY_WINDOW = "Window size";
	public static final String PARAMETER_DIFFUSIVITY_LOG_OPTION = "Log option";
	public static final String PARAMETER_ERROR_OPTION = "Error option";
	public static final String PARAMETER_ERROR_SNR = "Select SNR estimation";

	public static final String PARAMETER_DIFFUSIVITY_WINDOW_3 = "3";
	public static final String PARAMETER_DIFFUSIVITY_WINDOW_5 = "5";
	public static final String PARAMETER_DIFFUSIVITY_WINDOW_10 = "10";

	public static final String PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_ONLY = "Log only";
	public static final String PARAMETER_DIFFUSIVITY_LOG_OPTION_LINEAR_ONLY = "Linear only";
	public static final String PARAMETER_DIFFUSIVITY_LOG_OPTION_LOG_AND_LINEAR = "Log and linear";

	public static final String PARAMETER_ERROR_OPTION_ENABLED = "Error enabled";
	public static final String PARAMETER_ERROR_OPTION_DISABLED = "Error disabled";
	public static final String PARAMETER_ERROR_OPTION_ONLY = "Error only";

	// ***COLORS***
	/**
	 *
	 * @return
	 */
	public final static Color getDefaultSelectionBackgroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.selectionBackground");
	}

	/**
	 *
	 * @return
	 */
	public final static Color getDefaultSelectionForegroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.selectionForeground");
	}

	public static Color getDefaultBackgroundColor() {
		final UIDefaults defaults = javax.swing.UIManager.getDefaults();
		return defaults.getColor("List.background");
	}
}
