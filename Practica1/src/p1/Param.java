package p1;

/**
 * 
 * @author Jose Antonio
 * @implNote Clase para almacenar los parametros del programa
 */
public class Param {
	private String algoritmo;
	private String VALUE_SEPARATOR = "=";

	public void parseParam(String linea) throws IllegalArgumentException {
		String[] separeStrings = linea.split(VALUE_SEPARATOR);

		switch (separeStrings[0]) {

		case "algoritmo":
			algoritmo = separeStrings[1];
			break;

		default:
			throw new IllegalArgumentException(separeStrings[0] + " no es un parametro válido");

		}
	}

	public String getAlgoritmo() {
		return algoritmo;
	}

}
