package main.java.edu.umassmed.omega.commons.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class CSVFileLoader {

	private final File f;
	private final Map<Integer, String[]> fileContent;

	public CSVFileLoader(final String path) throws IllegalArgumentException {
		this(new File(path));
	}

	public CSVFileLoader(final File file) throws IllegalArgumentException {
		if (!file.exists())
			throw new IllegalArgumentException("File " + file.getName()
					+ " does not exists. " + file.getAbsolutePath());
		this.f = file;
		this.fileContent = new LinkedHashMap<Integer, String[]>();
	}

	public void load() throws IOException {
		final FileReader fr = new FileReader(this.f);
		final BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		int counter = 0;
		while (line != null) {
			final String[] splitted = line.split(";");
			this.fileContent.put(counter, splitted);
			counter++;
			line = br.readLine();
		}
		br.close();
		fr.close();
	}

	public Map<Integer, String[]> getFileContent() {
		return this.fileContent;
	}
}
