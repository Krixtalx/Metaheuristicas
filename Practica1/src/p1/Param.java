package p1;

import java.util.Random;

/**
 * 
 * @author Jose Antonio
 * @implNote Clase para almacenar los parametros del programa
 */
public class Param {
	private String algoritmo;
	private String dataFile;
	private int seed;
	private Random randGenerator;
	private int iteracciones;

	public void parseParam(String linea) throws IllegalArgumentException {
		String[] separeStrings = linea.split(Constants.PARAM_SEPARATOR);

		switch (separeStrings[0]) {

		case "algoritmo":
			algoritmo = separeStrings[1];
			break;

		case "seed":
			try {
				seed = Integer.parseInt(separeStrings[1]);
				randGenerator = new Random(seed);
				break;
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(e.getMessage());
			}

		case "dataFile":
			dataFile = separeStrings[1];
			break;
			
		case "iteracciones":
			iteracciones = Integer.parseInt(separeStrings[1]);
			if(iteracciones == -1) 
				iteracciones = Integer.MAX_VALUE;
			break;

		default:
			throw new IllegalArgumentException(separeStrings[0] + " no es un parametro válido");

		}
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

	public int getSeed() {
		return seed;
	}

	public int generateInt(int max) {
		return randGenerator.nextInt(max);
	}

	public String getDataFile() {
		return dataFile;
	}
	
	public int getIteraciones() {
		return iteracciones;
	}
}
