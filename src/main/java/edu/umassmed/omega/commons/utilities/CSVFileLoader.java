package edu.umassmed.omega.commons.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVFileLoader {

	private final InputStream is;
	private final Map<Integer, String[]> fileContent;

	public CSVFileLoader(final String path) throws IllegalArgumentException,
	FileNotFoundException {
		this(new FileInputStream(path));
	}

	public CSVFileLoader(final InputStream is) throws IllegalArgumentException {
		// if (!is.)
		// throw new IllegalArgumentException("File " + file.getName()
		// + " does not exists. " + file.getAbsolutePath());
		this.is = is;
		this.fileContent = new LinkedHashMap<Integer, String[]>();
	}

	public void load() throws IOException {
		// final FileReader fr = new FileReader(is);
		// final BufferedReader br = new BufferedReader(is);
		final InputStreamReader isr = new InputStreamReader(this.is);
		final BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		int counter = 0;
		while (line != null) {
			final String[] splitted = line.split(";");
			this.fileContent.put(counter, splitted);
			counter++;
			line = br.readLine();
		}
		br.close();
		isr.close();
	}

	public Map<Integer, String[]> getFileContent() {
		return this.fileContent;
	}
}
