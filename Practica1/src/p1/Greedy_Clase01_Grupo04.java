package p1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Greedy_Clase01_Grupo04 {

	/**
	 * Obtiene una solucion al problema mediante un algoritmo voraz, seleccionando
	 * los elementos que mas aporten
	 * 
	 * @return ArrayList con los elementos solucion
	 */
	public static ArrayList<Integer> ejecutar(Data data, Param param, Logger log) throws IOException {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		seleccionados.add(param.generateInt(data.getSize() - 1)); // Restamos 1 ya que el ultimo
																	// no tiene entrada en la matriz
		log.write("Seleccion inicial: " + seleccionados.get(0));
		candidatos.remove(seleccionados.get(0));

		double sumaMejor, sumaTemp, distanciaTotal = 0;
		double[][] matrizCoste = data.getMatrix();
		int mejor;
		while (seleccionados.size() < data.getSelection()) {
			mejor = -1;
			sumaMejor = -1;
			for (int i = 0; i < candidatos.size(); i++) { // Bucle de candidatos
				sumaTemp = 0;
				for (int j = 0; j < seleccionados.size(); j++) { // Bucle de seleccionados
					sumaTemp += Auxiliares.valorMatriz(matrizCoste, candidatos.get(i), seleccionados.get(j));
				}
				if (sumaMejor < sumaTemp) { // Almacena el mejor candidato
					sumaMejor = sumaTemp;
					mejor = i;
				}
			}
			if (mejor == -1) {
				throw new RuntimeException("Error al buscar mejor candidato");
			}
			distanciaTotal += sumaMejor;
			seleccionados.add(candidatos.remove(mejor));
			log.nextIteration();
			log.write("Nueva seleccion: " + seleccionados.get(seleccionados.size() - 1) + " con un aporte de "
					+ sumaMejor);
			log.write("Distancia M actual: " + distanciaTotal);
		}
		log.write("Solucion: " + seleccionados);
		log.write("Distancia M: " + distanciaTotal);
		return seleccionados;
	}
}
