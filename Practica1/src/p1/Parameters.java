package p1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Parameters {
	private final String DEFAULT_EXT = ".txt";
	private File paramFile;

	public Parameters(String file, String path, String ext) {
		if (!ext.contains(".")) {
//			System.out.println("Invalid extension, using default: " + DEFAULT_EXT);
			paramFile = new File(path + file + DEFAULT_EXT);
		} else {
			paramFile = new File(path + file + ext);
		}
	}

	public void ReadFile() throws IOException {
		Scanner fileReader = new Scanner(paramFile);
		while (fileReader.hasNextLine()) {
			String lineData = fileReader.nextLine();
			System.out.println(lineData);
		}
		fileReader.close();
	}
}
