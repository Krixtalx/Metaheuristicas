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
	private int iteraciones;
	private int tenenciaTabu;
	private int itSinMejora;
	private int tamVecindario;

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
			iteraciones = Integer.parseInt(separeStrings[1]);
			if(iteraciones == -1) 
				iteraciones = Integer.MAX_VALUE;
			break;
			
		case "tenenciaTabu":
			tenenciaTabu = Integer.parseInt(separeStrings[1]);
			break;
			
		case "tamVecindario":
			tamVecindario = Integer.parseInt(separeStrings[1]);
			break;
			
		case "itSinMejora":
			itSinMejora = Integer.parseInt(separeStrings[1]);
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
	
	public boolean generateBool() {
		return randGenerator.nextBoolean();
	}

	public String getDataFile() {
		return dataFile;
	}
	
	public int getIteraciones() {
		return iteraciones;
	}
	
	public int getTenenciaTabu() {
		return tenenciaTabu;
	}
	
	public int getTamVecindario() {
		return tamVecindario;
	}
	
	public int getItSinMejora() {
		return itSinMejora;
	}
}
