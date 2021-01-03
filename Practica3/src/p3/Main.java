package p3;

import java.io.IOException;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		/*
		 * String[] ficheros =
		 * {"GKD-c_1_n500_m50","GKD-c_2_n500_m50","GKD-c_3_n500_m50",
		 * "MDG-a_21_n2000_m200","MDG-a_22_n2000_m200","MDG-a_23_n2000_m200",
		 * "SOM-b_11_n300_m90","SOM-b_12_n300_m120","SOM-b_13_n400_m40"}; int[] seed =
		 * {77690720, 7769072, 20776907, 72077690, 7207769};
		 */
		Reader params = new Reader(Constants.PARAM_NAME, "", Constants.DEFAULT_EXT);
		try {
			Param parametros = params.readParam();
			String[] ficheros = { "GKD-c_1_n500_m50", "GKD-c_2_n500_m50", "GKD-c_3_n500_m50" };
			int[] seed = { 77690720, 7769072, 20776907, 72077690, 7207769 };
			int[] alpha = { 1, 1, 2 };
			int[] beta = { 1, 2, 1 };
			for (int i = 0; i < ficheros.length; i++) {
				for (int j = 0; j < seed.length; j++) {
					for (int j2 = 0; j2 < alpha.length; j2++) {
						Reader dataReader = new Reader(parametros.dataFile, "", Constants.DEFAULT_EXT);
						parametros.dataFile = ficheros[i];
						parametros.seed = seed[j];
						parametros.randGenerator = new Random(seed[j]);
						parametros.alpha = alpha[j2];
						parametros.beta = beta[j2];
						Data problemData = dataReader.readData();
						parametros.calculaFeromonaInicial(problemData);
						Logger log = new Logger(parametros);
						log.startTimer();
						AlgSCH_Clase04_Grupo2.ejecutar(problemData, parametros, log);
						log.endTimer();
						log.write("Tiempo empleado: " + log.getDuration().toMillis() + " milliseconds");
						log.close();
						System.out.println("Ejecucion " + (i + j + j2) + " acabada correctamente en "
								+ log.getDuration().toMillis());
					}
				}
			}

		} catch (IOException e) {
			System.out.println("No se puede leer el fichero	: " + e.getMessage());
			System.out.println("Ejecucion finalizada");
		}
	}
}