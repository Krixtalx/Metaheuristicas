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
	private int tamInicialVecindario;
	private int tamMinimoVecindario;
	private double probIntensificar;
	private double penalizacionMemoria;

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

		case "iteraciones":
			iteraciones = Integer.parseInt(separeStrings[1]);
			if (iteraciones == -1)
				iteraciones = Integer.MAX_VALUE;
			break;

		case "tenenciaTabu":
			tenenciaTabu = Integer.parseInt(separeStrings[1]);
			break;

		case "tamInicialVecindario":
			tamInicialVecindario = Integer.parseInt(separeStrings[1]);
			break;
			
		case "tamMinimoVecindario":
			tamMinimoVecindario = Integer.parseInt(separeStrings[1]);
			break;

		case "itSinMejora":
			itSinMejora = Integer.parseInt(separeStrings[1]);
			break;

		case "probIntensificar":
			probIntensificar = Double.parseDouble(separeStrings[1]);
			break;

		case "penalizacionMemoria":
			penalizacionMemoria = Double.parseDouble(separeStrings[1]);
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

	public double generateDouble() {
		return randGenerator.nextDouble();
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

	public int getItSinMejora() {
		return itSinMejora;
	}

	public int getTamVecindario(int it) {
		double temp = it/iteraciones;
		temp*=-temp;
		temp+=1;
		int tempTam = (int) (tamInicialVecindario*temp);
		if(tempTam < tamMinimoVecindario) {
			return tamMinimoVecindario;
		}
		return tempTam;
	}
	
	public double getProbIntens() {
		return probIntensificar;
	}
	
	public double getPenalizacionMemoria() {
		return penalizacionMemoria;
	}
}
