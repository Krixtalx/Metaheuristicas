package p2;

import java.util.Random;

/**
 * 
 * @author Jose Antonio
 * @implNote Clase para almacenar los parametros del programa
 */
public class Param {
	public String dataFile;
	public int seed;
	public Random randGenerator;
	public int evaluaciones;
	public int elite;
	public String cruce;
	public int tamPoblacion;
	public double probCruce;
	public double probMutacion;

	public void parseParam(String linea) throws IllegalArgumentException {
		String[] separeStrings = linea.split(Constants.PARAM_SEPARATOR);
//		System.out.println("LEYENDO: " + separeStrings[0]);
		switch (separeStrings[0]) {

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

		case "evaluaciones":
			evaluaciones = Integer.parseInt(separeStrings[1]);
			if (evaluaciones == -1)
				evaluaciones = Integer.MAX_VALUE;
			break;

		case "elite":
			elite = Integer.parseInt(separeStrings[1]);
			break;

		case "cruce":
			cruce = separeStrings[1];
			break;
			
		case "tamPoblacion":
			tamPoblacion = Integer.parseInt(separeStrings[1]);
			break;

		case "probCruce":
			probCruce = Double.parseDouble(separeStrings[1]);
			break;

		case "probMutacion":
			probMutacion = Double.parseDouble(separeStrings[1]);
			break;

		default:
			throw new IllegalArgumentException(separeStrings[0] + " no es un parametro válido");

		}
	}

	public int generateInt(int max) {
		return randGenerator.nextInt(max);
	}

	public boolean generateBool() {
		return randGenerator.nextBoolean();
	}

	public double generateDouble() {
		return randGenerator.nextDouble();
	}

	
}
