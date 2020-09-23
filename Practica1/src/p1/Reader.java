package p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Reader {
	private final String DEFAULT_EXT = ".txt";
	private final String VALUE_SEPARATOR = " ";
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

	public Data txtToMatrixBR() throws IOException {

		FileReader r = new FileReader(paramFile);
		BufferedReader reader = new BufferedReader(r);

		String lineData = reader.readLine();
		if (lineData == null) {
			reader.close();
			throw new IOException("Formato del fichero erroneo");
		}
		String[] lineValues = lineData.split(VALUE_SEPARATOR);
		if (lineValues.length != 2) {
			reader.close();
			throw new IOException("Formato del fichero erroneo");
		}
		int size = Integer.parseInt(lineValues[0]);
		int numElements = Integer.parseInt(lineValues[1]);
		if (size <= 0 || numElements <= 0) {
			reader.close();
			throw new IOException("Valores iniciales no permitidos");
		}

		lineData = reader.readLine();
		Data inputValues = new Data(size, numElements);
		int mRow, mCol;
		try {
			while (lineData != null) {

				lineValues = lineData.split(VALUE_SEPARATOR);
				mRow = Integer.parseInt(lineValues[0]);
				mCol = Integer.parseInt(lineValues[1]);
				inputValues.addValue(mRow, mCol, Float.parseFloat(lineValues[2]));
				lineData = reader.readLine();
			}
		} catch (Exception e) {
			reader.close();
			throw new IOException("Valores de matriz no permitidos");
		}

		reader.close();
		return inputValues;
	}
}
