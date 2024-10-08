package p3;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {

		Reader params = new Reader(Constants.PARAM_NAME, "", Constants.DEFAULT_EXT);
		try {
			Param parametros = params.readParam();
			Reader dataReader = new Reader(parametros.dataFile, "", Constants.DEFAULT_EXT);
			Data problemData = dataReader.readData();
			parametros.calculaFeromonaInicial(problemData);
			Logger log = new Logger(parametros);
			System.out.println("Ejecucion comenzada");
			log.startTimer();
			AlgSCH_Clase04_Grupo2.ejecutar(problemData, parametros, log);
			log.endTimer();
			log.write("Tiempo empleado: " + log.getDuration().toMillis() + " milliseconds");
			log.close();
			System.out.println("Ejecucion" + " acabada correctamente en " + log.getDuration().toMillis());

		} catch (IOException e) {
			System.out.println("No se puede leer el fichero	: " + e.getMessage());
			System.out.println("Ejecucion finalizada");
		}
	}
}