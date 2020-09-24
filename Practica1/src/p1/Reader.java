package p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Jose Antonio
 * @implNote Clase encargada de leer los ficheros.
 */
public class Reader {
	private File paramFile;

	public Reader(String file, String path, String ext) {
		if (!ext.contains(".")) {
//			System.out.println("Invalid extension, using default: " + DEFAULT_EXT);
			paramFile = new File(path + file + Constants.DEFAULT_EXT);
		} else {
			paramFile = new File(path + file + ext);
		}
	}

	public Param readParam() throws IOException {
		FileReader r = new FileReader(paramFile);
		BufferedReader reader = new BufferedReader(r);
		String bufferString = reader.readLine();
		Param auxParam = new Param();

		if (!bufferString.equals("[Param]")) {
			reader.close();
			System.out.println(bufferString);
			throw new IOException("El fichero especificado no es un fichero de parametros");
		} else {
			bufferString = reader.readLine();
			try {
				while (bufferString != null) { // Leemos lineas y la incluimos en la matriz de Data.
					auxParam.parseParam(bufferString);
					bufferString = reader.readLine();
				}
			} catch (Exception e) {
				reader.close();
				System.err.println(e.getMessage());
			}
		}
		return auxParam;

	}

	/**
	 * 
	 * @return Data con todos los datos leidos.
	 * @throws IOException: En caso de error de lectura.
	 */
	public Data readData() throws IOException {

		FileReader r = new FileReader(paramFile);
		BufferedReader reader = new BufferedReader(r); // BufferedReader para optimizar el rendimiento de lectura.

		String lineData = reader.readLine(); // Leemos la primera linea.
		if (lineData == null) {
			reader.close();
			throw new IOException("Formato del fichero erroneo");
		}

		String[] lineValues = lineData.split(Constants.DATA_SEPARATOR); // Separamos el string "Linea" en varios string separados
																// por VALUE_SEPARATOR.
		if (lineValues.length != 2) {
			reader.close();
			throw new IOException("Formato del fichero erroneo");
		}

		int size = Integer.parseInt(lineValues[0]);
		int numElements = Integer.parseInt(lineValues[1]); // Convertimos de String a Int.
		if (size <= 0 || numElements <= 0) {
			reader.close();
			throw new IOException("Valores iniciales no permitidos");
		}

		lineData = reader.readLine();
		Data inputValues = new Data(size, numElements); // Creamos un "Data" con el tamaño y el numElementos leidos de
														// la primera linea.
		int mRow, mCol;
		try {
			while (lineData != null) { // Leemos lineas y la incluimos en la matriz de Data.

				lineValues = lineData.split(Constants.DATA_SEPARATOR);
				mRow = Integer.parseInt(lineValues[0]);
				mCol = Integer.parseInt(lineValues[1]);
				inputValues.addValue(mRow, mCol, Float.parseFloat(lineValues[2]));
				lineData = reader.readLine();
			}
		} catch (Exception e) {
			reader.close();
			throw new IOException("Valores de matriz no permitidos");
		}

		reader.close(); // Cerramos el lector.
		return inputValues; // Devolvemos la variable Data necesaria.
	}
}
