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
				System.out.println("Solucion: " + solucion);
				break;
				
			case "Busqueda Local":
				ArrayList<Integer> solucion2 = Solver.busquedaLocal(problemData, parametros);
				System.out.println("Solucion: " + solucion2);
				break;
				
			case "Busqueda Tabu":
				ArrayList<Integer> solucion3 = Solver.busquedaTabu(problemData, parametros);
				System.out.println("Solucion: " + solucion3);
				break;
			}
			
		} catch (IOException e) {
			System.out.println("No se puede leer el fichero	: " + e.getMessage());
		}
	}
}
