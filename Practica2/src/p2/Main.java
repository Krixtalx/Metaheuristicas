package p2;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {

		Reader params = new Reader(Constants.PARAM_NAME, "", Constants.DEFAULT_EXT);
		try {
			Param parametros = params.readParam();
			Reader dataReader = new Reader(parametros.getDataFile(), "", Constants.DEFAULT_EXT);
			Data problemData = dataReader.readData();
			Logger log = new Logger(parametros);
			
			System.out.println("Ha comenzado la ejecucion");
			
			log.startTimer();
			
			switch (parametros.getAlgoritmo()) {
				
			}
			log.endTimer();
			log.write("Tiempo empleado: " + log.getDuration().toMillis() + " milliseconds");
			log.close();
			System.out.println("Ejecucion acabada correctamente");
			
		} catch (IOException e) {
			System.out.println("No se puede leer el fichero	: " + e.getMessage());
			System.out.println("Ejecucion acabada");
		}
	}
}
