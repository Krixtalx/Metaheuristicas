package p1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class BusquedaTabu_Clase05_Grupo04 {
	/**
	 * Obtiene una soluci�n al problema mediante un algoritmo de busqueda tabu
	 * @param data Datos del problema
	 * @param param Parametros para la ejecucion
	 * @param log Logger para la escritura en fichero de la ejecucion
	 * @return Lista con los elementos de la solucion obtenida
	 * @throws IOException
	 */
	public static ArrayList<Integer> ejecutar(Data data, Param param, Logger log) throws IOException {
		if(param.getTamVecindario(0) == -1) {
			param.setTamIniVecindario(data.getSize(), data.getSelection());
		}
		
		// Inicializamos los parametros que usaremos en la funcion
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		ArrayList<Integer> mejorSolucion = new ArrayList<>(); // Mejor Solucion hasta ahora

		double valorMejorSolucion = 0;
		double[][] matrizCostes = data.getMatrix();

		CircularList<Integer> listaTabu = new CircularList<>(param.getTenenciaTabu());
		int[] memoria = new int[data.getSize()];

		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
			memoria[i] = 0;
		}
		for (int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size() - 1));
			seleccionados.add(elegido); // Seleccionamos aleatoriamente entre los candidatos
			mejorSolucion.add(elegido);
		}
		int it = 0;

		valorMejorSolucion = Auxiliares.calcularDistancia(mejorSolucion, matrizCostes);

		SortedPairArray<Double, Integer> ordenCambio;
		int elemCambio = -1;

		log.write("Solucion inicial: " + seleccionados + "\nCoste: " + valorMejorSolucion);
		log.nextIteration();
		ArrayList<Integer> randUsedList = new ArrayList<>();
		while (it < param.getIteraciones()) {
			int iteracionesSinMejora = 0;

			while (iteracionesSinMejora < param.getItSinMejora() && it < param.getIteraciones()) {
				ordenCambio = Auxiliares.ordenarSeleccion(seleccionados, matrizCostes); 	// Calculamos los elementos que menos
																				// aportan a la solucion
				// Generar vecindario
				double mejoraVecino = Integer.MIN_VALUE;
				int mejorVecino = -1, mejorElemCambio = -1;
				int vecinosGenerados = 0;
				for (int i = 0; i < ordenCambio.size() && vecinosGenerados < param.getTamVecindario(it); i++) {
					elemCambio = ordenCambio.get(i).getValue();
					while (candidatos.size() > 0 && vecinosGenerados < param.getTamVecindario(it)) {
						vecinosGenerados++;
						// Genera un vecino aleatoriamente
						int randPosition = param.generateInt(candidatos.size());
						int candidatoCambio = candidatos.get(randPosition);
						if (!listaTabu.contains(candidatoCambio)) { // Si no esta restringido, entra al vecindario
							// Comprueba si es el mejor
							double diff = Auxiliares.difIntercambio(seleccionados, matrizCostes, elemCambio, candidatoCambio);
							if (mejoraVecino < diff) {
								mejoraVecino = diff;
								mejorVecino = candidatoCambio;
								mejorElemCambio = elemCambio;
							}
						} else {
							vecinosGenerados--;
						}
						// Quitamos de los candidatos el recien probado
						randUsedList.add(candidatos.remove(randPosition));
					}

					// Reestablece los candidatos
					candidatos.addAll(randUsedList);
					randUsedList.clear();
				}
				// Intercambiamos el nuevo elemento con el antiguo
				candidatos.add(seleccionados.remove(seleccionados.indexOf(mejorElemCambio)));
				seleccionados.add(candidatos.remove(candidatos.indexOf(mejorVecino)));
				
				double costeSeleccion = Auxiliares.calcularDistancia(seleccionados, matrizCostes);
				log.write("Solucion actual: " + seleccionados + "\nCoste: " + costeSeleccion + "\nLista tabu: "
						+ listaTabu + "\nMemoria a largo plazo: " + Arrays.toString(memoria));
				log.nextIteration();
				// Actualiza la memoria a largo plazo
				for (int i = 0; i < seleccionados.size(); i++) {
					memoria[seleccionados.get(i)]++;
				}

				// Actualiza lista tabu
				listaTabu.push(mejorElemCambio);

				// Incrementamos el numero de iteracciones
				it++;
				iteracionesSinMejora++;
				
				//Si la solucion actual es mejor, la guarda
				if (valorMejorSolucion < costeSeleccion) {
					valorMejorSolucion = costeSeleccion;
					mejorSolucion = new ArrayList<>(seleccionados);
					// Borramos la memoria a largo plazo y a corto plazo
					for (int i = 0; i < memoria.length; i++) {
						memoria[i] *= param.getPenalizacionMemoria();
					}
					listaTabu.clear();
					iteracionesSinMejora = 0;
				}

			}
			// Reiniciar
			candidatos.addAll(seleccionados);
			if (param.generateDouble() < (param.getProbIntens() * it / param.getIteraciones())) {// Intensificar
				seleccionados = Auxiliares.masElegidos(memoria, data.getSelection());
				log.write("Reiniciando busqueda tabu intensificando");
				log.write("Nueva seleccion: " + seleccionados);
			} else {// Diversificar
				seleccionados = Auxiliares.menosElegidos(memoria, data.getSelection());
				log.write("Reiniciando busqueda tabu diversificando");
				log.write("Nueva seleccion: " + seleccionados);
			}
			candidatos.removeAll(seleccionados);

			// Borramos la memoria a largo plazo y a corto plazo
			for (int i = 0; i < memoria.length; i++) {
				memoria[i] *= param.getPenalizacionMemoria();
			}
			listaTabu.clear();

		}
		log.nextIteration();
		log.write("Mejor solucion obtenida: " + mejorSolucion);
		log.write("Coste: " + valorMejorSolucion);
		log.write("Numero iteraciones: " + it);
		return mejorSolucion;
	}
}
