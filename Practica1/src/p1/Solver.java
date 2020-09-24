package p1;

import java.util.ArrayList;

/**
 * Implementa los distintos algoritmos para la resoluci�n
 * del problema detallado
 * @author jcfer
 *
 */
public class Solver {
	private Data problemData;
	private Param parameters;
	
	public Solver(Data data, Param param) {
		problemData = data;
		parameters = param;
	}
	
	/**
	 * Obtiene una soluci�n al problema mediante un algoritmo voraz, seleccionando los
	 * elementos que m�s aporten
	 * @return ArrayList con los elementos soluci�n
	 */
	public ArrayList<Integer> Greedy() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		result.add(parameters.generateInt(problemData.getSize()-1));//Restamos 1 ya que el �ltimo
																	//no tiene entrada en la matriz
		float distanciaM = 0, sumaMejor = -1, sumaTemp;
		float[][] matriz = problemData.getMatrix();
		int mejor = -1;
		while(result.size() < problemData.getSelection()) {
			sumaTemp = 0;
			
		}
		return result;
	}
}
