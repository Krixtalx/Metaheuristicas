package p1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Reader {
	private final String DEFAULT_EXT = ".txt";
	private File paramFile;

	public Reader(String file, String path, String ext) {
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

	// En verdad se podria cambiar a un hashmap con pair<int,int> para la clave pero
	// tengo sueño.
	// También estaría bien usar un bufferedReader en vez del Scanner
	public float[][] txtToMatrix(Integer size, Integer numberElements) throws IOException {
		Scanner fileReader = new Scanner(paramFile);

		size = fileReader.nextInt();
		numberElements = fileReader.nextInt();
		System.out.println(numberElements);
		float[][] matrix = new float[size][size];
		fileReader.nextLine();

		while (fileReader.hasNextLine()) {
			String tempBufferString = fileReader.nextLine();
			String[] splitStrings = tempBufferString.split(" ");
			matrix[Integer.parseInt(splitStrings[0])][Integer.parseInt(splitStrings[1])] = Float
					.parseFloat(splitStrings[2]);
		}

		fileReader.close();
		return matrix;
	}
}
