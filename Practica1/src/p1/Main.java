package p1;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {

		Reader params = new Reader(Constants.PARAM_NAME, "", Constants.DEFAULT_EXT);
		try {
			Param parametros = params.readParam();
			Reader dataReader = new Reader(parametros.getDataFile(), "", Constants.DEFAULT_EXT);
			Data problemData = dataReader.readData();

			switch (parametros.getAlgoritmo()) {
			case "Greedy":
				ArrayList<Integer> solucion = Solver.Greedy(problemData, parametros);
				for (int i = 0; i < solucion.size(); i++) {
					System.out.println("Solucion " + i + ": " + solucion.get(i));
				}
				break;
				
			case "Busqueda Local":
				ArrayList<Integer> solucion2 = Solver.busquedaLocal(problemData, parametros);
				for (int i = 0; i < solucion2.size(); i++) {
					System.out.println("Solucion " + i + ": " + solucion2.get(i));
				}
				break;
				
			}
			
		} catch (IOException e) {
			System.out.println("No se puedo leer el fichero	: " + e.getMessage());
		}
	}
}
