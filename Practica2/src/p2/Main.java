package p2;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {

		//9 ficheros, 5 seed, 2 cruce, 2 elite
		String[] ficheros = {"GKD-c_1_n500_m50","GKD-c_2_n500_m50","GKD-c_3_n500_m50","MDG-a_21_n2000_m200","MDG-a_22_n2000_m200","MDG-a_23_n2000_m200","SOM-b_11_n300_m90","SOM-b_12_n300_m120","SOM-b_13_n400_m40"};
		int[] seed = {77690720, 7769072, 20776907, 72077690, 7207769};
		String[] cruce= {"MPX", "DosPuntos"};
		
		Reader params = new Reader(Constants.PARAM_NAME, "", Constants.DEFAULT_EXT);
		try {
			Param parametros = params.readParam();
			int cont = 0;
			for (int i = 0; i < ficheros.length; i++) {
				for (int j = 0; j < seed.length; j++) {
					for (int j2 = 0; j2 < cruce.length; j2++) {
						for (int k = 2; k <= 3; k++) {
							parametros.dataFile = ficheros[i];
							parametros.seed = seed[j];
							parametros.cruce = cruce[j2];
							parametros.elite = k;
							Reader dataReader = new Reader(parametros.dataFile, "", Constants.DEFAULT_EXT);
							Data problemData = dataReader.readData();
							Logger log = new Logger(parametros);
							System.out.println("Ha comenzado la ejecucion " + (cont++) + " " + ficheros[i] + " " + seed[j] + " " + cruce[j2] + " Elite:" + k);
							
							log.startTimer();
							
							AlgGN_Clase04_Grupo2.ejecutar(problemData, parametros, log);
							log.endTimer();
							log.write("Tiempo empleado: " + log.getDuration().toMillis() + " milliseconds");
							log.close();
							System.out.println("Ejecucion acabada correctamente en " + log.getDuration().toMillis());
						}
					}
				}
			}
			
			
		} catch (IOException e) {
			System.out.println("No se puede leer el fichero	: " + e.getMessage());
			System.out.println("Ejecucion acabada");
		}
	}
}