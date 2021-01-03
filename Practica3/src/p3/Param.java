package p3;

import java.io.IOException;
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
	public int iteraciones;
	public int tamPoblacion;
	public int alpha;
	public int beta;
	public double qcero;
	public double actFeronoma;
	public double actLocal;
	public double paramLRC;
	public double feromonaInicial;

	public void parseParam(String linea) throws IllegalArgumentException {
		String[] separeStrings = linea.split(Constants.PARAM_SEPARATOR);
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

		case "iteraciones":
			iteraciones = Integer.parseInt(separeStrings[1]);
			if (iteraciones == -1)
				iteraciones = Integer.MAX_VALUE;
			break;
			
		case "tamPoblacion":
			tamPoblacion = Integer.parseInt(separeStrings[1]);
			break;
			
		case "alpha":
			alpha = Integer.parseInt(separeStrings[1]);
			break;
			
		case "beta":
			beta = Integer.parseInt(separeStrings[1]);
			break;
			
		case "qcero":
			qcero = Double.parseDouble(separeStrings[1]);
			break;
			
		case "actFeronoma":
			actFeronoma = Double.parseDouble(separeStrings[1]);
			break;
			
		case "actLocal":
			actLocal = Double.parseDouble(separeStrings[1]);
			break;
			
		case "paramLRC":
			paramLRC = Double.parseDouble(separeStrings[1]);
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

	public void calculaFeromonaInicial(Data d) throws IOException {
		feromonaInicial = 1/(tamPoblacion*AlgGR_Clase04_Grupo2.ejecutar(d, this, null));
	}
}
