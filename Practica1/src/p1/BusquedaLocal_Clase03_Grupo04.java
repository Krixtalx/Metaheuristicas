package p1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class BusquedaLocal_Clase03_Grupo04 {

	/**
	 * Obtiene una solucion al problema mediante un algoritmo de busqueda local,
	 * explorando el vecindario de una solucion inicial aleatoria
	 * 
	 * @return ArrayList con los elementos solucion
	 * @throws IOException
	 */
	public static ArrayList<Integer> ejecutar(Data data, Param param, Logger log) throws IOException {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		ArrayList<Integer> vecino = new ArrayList<>();
		SortedPairArray<Double, Integer> ordenCambio;

		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		for (int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size() - 1));
			seleccionados.add(elegido); // Seleccionamos aleatoriamente entre los candidatos
			vecino.add(elegido);
		}

		System.out.println("Random inicio: " + seleccionados.toString());
		double[][] matrizCoste = data.getMatrix();
		int it = 0, siguiente = 0, elemCambio;
		boolean vecinoEncontrado = false;

		ordenCambio = Auxiliares.ordenarSeleccion(seleccionados, matrizCoste);
		log.write("Solucion inicial: " + seleccionados + "\nCoste: " + Auxiliares.calcularDistancia(seleccionados, matrizCoste));
		log.nextIteration();

		// Itera un numero determinado de veces, o hasta recorrer todo el vecindario
		while (it < param.getIteraciones() && siguiente < ordenCambio.size()) {
			elemCambio = ordenCambio.get(siguiente).getValue();
			// Para cada intercambio, obtiene la distancia del nuevo elemento
			for (int i = 0; i < candidatos.size(); i++) {
				// Si el vecino es mejor, lo intercambia
				it++;

				double diferencia = Auxiliares.difIntercambio(seleccionados, matrizCoste, elemCambio, candidatos.get(i));
				if (diferencia > 0) {
					// Elimina el candidato de la lista, lo a�ade en la posicion correspondiente, y
					// devuelve el elemento previo a los candidatos
					candidatos.add(seleccionados.set(seleccionados.indexOf(elemCambio), candidatos.remove(i)));
					vecinoEncontrado = true;
					i = candidatos.size();
					log.write("Solucion actual: " + seleccionados + "\nCoste: "
							+ Auxiliares.calcularDistancia(seleccionados, matrizCoste) + "\nDiferencia de costes: " + diferencia);
					log.nextIteration();
				}
			}
			if (vecinoEncontrado) { // Si se ha encontrado un vecino mejor, reinicia la busqueda
				siguiente = 0;
				ordenCambio = Auxiliares.ordenarSeleccion(seleccionados, matrizCoste);
				vecinoEncontrado = false;
			} else { // Si no, vuelve a intentarlo con el siguiente elemento de la solucion
				siguiente++;
			}
		}
		log.write("Solucion final: " + seleccionados);
		log.write("Distancia M: " + Auxiliares.calcularDistancia(seleccionados, matrizCoste));
		log.write("Numero iteraciones: " + it);
		return seleccionados;
	}
}
