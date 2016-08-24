/*******************************************************************************
 * Copyright (C) 2014 University of Massachusetts Medical School
 * Alessandro Rigano (Program in Molecular Medicine)
 * Caterina Strambio De Castillia (Program in Molecular Medicine)
 *
 * Created by the Open Microscopy Environment inteGrated Analysis (OMEGA) team:
 * Alex Rigano, Caterina Strambio De Castillia, Jasmine Clark, Vanni Galli,
 * Raffaello Giulietti, Loris Grossi, Eric Hunter, Tiziano Leidi, Jeremy Luban,
 * Ivo Sbalzarini and Mario Valle.
 *
 * Key contacts:
 * Caterina Strambio De Castillia: caterina.strambio@umassmed.edu
 * Alex Rigano: alex.rigano@umassmed.edu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package edu.umassmed.omega.commons.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.umassmed.omega.commons.OmegaLogFileManager;

public class OmegaClassLoaderUtilities {

	// public static void addLibraryPath(final String pathToAdd)
	// throws NoSuchFieldException, SecurityException,
	// IllegalArgumentException, IllegalAccessException {
	// final Field usrPathsField = ClassLoader.class
	// .getDeclaredField("usr_paths");
	// usrPathsField.setAccessible(true);
	//
	// // get array of paths
	// final String[] paths = (String[]) usrPathsField.get(null);
	//
	// // check if the path to add is already present
	// for (final String path : paths) {
	// if (path.equals(pathToAdd))
	// return;
	// }
	//
	// // add the new path
	// final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	// newPaths[newPaths.length - 1] = pathToAdd;
	// usrPathsField.set(null, newPaths);
	// }
	public static void addLibraryPath(final String pathToAdd) {
		final String paths = System.getProperty("java.library.path");
		final String newPaths = pathToAdd + ";" + paths;
		System.setProperty("java.library.path", newPaths);
		OmegaLogFileManager.appendToCoreLog(newPaths);
	}

	public static void loadLibrary(final String library) {
		try {
			System.load(OmegaClassLoaderUtilities.saveLibrary(library));
		} catch (final IOException e) {
			// LOG.warn("Could not find library " + library +
			// " as resource, trying fallback lookup through System.loadLibrary");
			System.loadLibrary(library);
		}
	}

	private static String getOSSpecificLibraryName(final String library,
	        final boolean includePath) {
		final String osArch = System.getProperty("os.arch");
		final String osName = System.getProperty("os.name").toLowerCase();
		String name;
		String path;

		if (osName.startsWith("win")) {
			if (osArch.equalsIgnoreCase("x86")) {
				name = library + ".dll";
				path = "win-x86/";
			} else
				throw new UnsupportedOperationException("Platform " + osName
				        + ":" + osArch + " not supported");
		} else if (osName.startsWith("linux")) {
			if (osArch.equalsIgnoreCase("amd64")) {
				name = "lib" + library + ".so";
				path = "linux-x86_64/";
			} else if (osArch.equalsIgnoreCase("ia64")) {
				name = "lib" + library + ".so";
				path = "linux-ia64/";
			} else if (osArch.equalsIgnoreCase("i386")) {
				name = "lib" + library + ".so";
				path = "linux-x86/";
			} else
				throw new UnsupportedOperationException("Platform " + osName
				        + ":" + osArch + " not supported");
		} else
			throw new UnsupportedOperationException("Platform " + osName + ":"
			        + osArch + " not supported");

		return includePath ? path + name : name;
	}

	private static String saveLibrary(final String library) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		final ClassLoader cl = OmegaClassLoaderUtilities.class.getClassLoader();
		try {
			final String libraryName = OmegaClassLoaderUtilities
			        .getOSSpecificLibraryName(library, true);
			in = cl.getResourceAsStream("lib/" + libraryName);
			final String tmpDirName = System.getProperty("java.io.tmpdir");
			final File tmpDir = new File(tmpDirName);
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			final File file = File
			        .createTempFile(library + "-", ".tmp", tmpDir);
			// Clean up the file when exiting
			file.deleteOnExit();
			out = new FileOutputStream(file);

			int cnt;
			final byte buf[] = new byte[16 * 1024];
			// copy until done.
			while ((cnt = in.read(buf)) >= 1) {
				out.write(buf, 0, cnt);
			}
			// LOG.info("Saved libfile: " + file.getAbsoluteFile());
			return file.getAbsolutePath();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ignore) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException ignore) {
				}
			}
		}
	}
}
