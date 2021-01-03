package p3;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;

public class AlgSCH_Clase04_Grupo2 {

	/**
	 * Ejecuta un SCH para la resolución del problema
	 * @param datos Datos del problema
	 * @param param Parámetros del problema
	 * @param log Clase para la escritura en ficheros
	 * @return Mejor hormiga
	 * @throws IOException Si no se puede escribir en fichero
	 */
	public static Pair<Double, ArrayList<Integer>> ejecutar(Data datos, Param param, Logger log) throws IOException {
		// INICIALIZACIÓN
		int it = 0;
		double[][] matrizHeuristica = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];
		double[][] matrizFeromonas = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];

		for (int i = 0; i < matrizHeuristica.length; i++) {
			for (int j = 0; j < matrizHeuristica[0].length; j++) {
				matrizHeuristica[i][j] = Data.valueMatrix[i][j];
				matrizFeromonas[i][j] = param.feromonaInicial;
			}
		}

		ArrayList<ArrayList<Integer>> colonia = new ArrayList<>();
		Pair<Double, ArrayList<Integer>> mejorHormigaLocal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE,
				null);
		Pair<Double, ArrayList<Integer>> mejorHormigaGlobal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE,
				null);

		Instant inicio = Instant.now();
		Instant fin = Instant.now();

		// BUCLE PRINCIPAL
		while (it < param.iteraciones && Duration.between(inicio, fin).toSeconds() < 5) {
			// Inicializar nueva población
			colonia.clear();
			ArrayList<Integer> hormiga;
			for (int i = 0; i < param.tamPoblacion; i++) {
				int candidatoAleatorio = param.generateInt(Data.size);
				hormiga = new ArrayList<>();
				hormiga.add(candidatoAleatorio);
				colonia.add(hormiga);
			}
			mejorHormigaLocal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE, null);

			// Mientras no se tengan soluciones completas
			for (int i = 1; i < Data.selection; i++) {
				// Añade un nuevo elemento a cada hormiga
				nuevoElementoHormiga(param, matrizHeuristica, matrizFeromonas, colonia, mejorHormigaLocal);

				// Actualiza la feromona local
				actualizarLocal(param, matrizFeromonas, colonia);
			}

			// Actualiza la feromona global y actualiza el mejor resultado global
			demonio(param, matrizFeromonas, mejorHormigaLocal, mejorHormigaGlobal);

			it++;
			System.out.println(it);
			log.write("It: " + it);
			log.write("Mejor hormiga local: " + mejorHormigaLocal);
			log.write("Mejor hormiga global: " + mejorHormigaGlobal);
			fin = Instant.now();
		}
		return mejorHormigaGlobal;
	}

	/**
	 * Actualiza la matriz de feromonas localmente
	 * @param param Parámetros del problema
	 * @param matrizFeromonas Matriz con los valores de feromona
	 * @param colonia Población actual de hormigas
	 */
	private static void actualizarLocal(Param param, double[][] matrizFeromonas,
			ArrayList<ArrayList<Integer>> colonia) {
		// Actualización local -> Diapositiva 68
		// Por cada hormiga
		for (int j = 0; j < param.tamPoblacion; j++) {
			// Por cada elemento
			for (int j2 = 0; j2 < colonia.get(j).size() - 1; j2++) {
				// τrs(t) = (1 − ϕ) ⋅ τrs(t − 1) + ϕ ⋅ τ0
				matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j)
						.get(colonia.get(j).size() - 1)] = (1 - param.actLocal)
								* matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j).get(colonia.get(j).size() - 1)]
								+ param.actLocal * param.feromonaInicial;
				matrizFeromonas[colonia.get(j).get(colonia.get(j).size() - 1)][colonia.get(j).get(
						j2)] = matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j).get(colonia.get(j).size() - 1)];

			}
		}
	}

	/**
	 * Añade un nuevo elemento a todas las hormigas de la población
	 * @param param Parámetros del problema
	 * @param matrizHeuristica Matriz de valores heurísticas
	 * @param matrizFeromonas Matriz con los valores de feromona
	 * @param colonia Población actual de hormigas
	 * @param mejorHormigaLocal Mejor hormiga de la población actual
	 */
	private static void nuevoElementoHormiga(Param param, double[][] matrizHeuristica, double[][] matrizFeromonas,
			ArrayList<ArrayList<Integer>> colonia, Pair<Double, ArrayList<Integer>> mejorHormigaLocal) {

		LinkedList<Integer> lrc;
		
		// Para cada hormiga
		for (int j = 0; j < param.tamPoblacion; j++) {
			// Obtener LRC
			lrc = LRC(colonia.get(j), param);
			double probDesp = param.generateDouble();
			double prob = 0;
			for (int k = 0; k < lrc.size(); k++) {
				int candidato = lrc.get(k);
				if (param.qcero > param.generateDouble()) { // P'k -> Diapositiva 66
					double suma = 0;
					// Sumatorio divisor
					for (int j2 = 0; j2 < lrc.size(); j2++) {
						double tau = sumFeromonas(colonia.get(j), lrc.get(j2), matrizFeromonas);
						double eta = Auxiliares.calcularDistanciaElemento(lrc.get(j2), colonia.get(j),
								matrizHeuristica);
						suma += Math.pow(tau, param.alpha) * Math.pow(eta, param.beta);
					}
					// Dividendo
					double tau = sumFeromonas(colonia.get(j), candidato, matrizFeromonas);
					double eta = Auxiliares.calcularDistanciaElemento(candidato, colonia.get(j), matrizHeuristica);

					prob += (Math.pow(tau, param.alpha) * Math.pow(eta, param.beta)) / suma; // Formula
																								// diapositiva
																								// 38
				} else { // arg max
					double maximo = -Double.MAX_VALUE;
					int mejor = -1;
					for (int j2 = 0; j2 < lrc.size(); j2++) {
						double tau = sumFeromonas(colonia.get(j), lrc.get(j2), matrizFeromonas);
						double eta = Auxiliares.calcularDistanciaElemento(lrc.get(j2), colonia.get(j),
								matrizHeuristica);
						double temp = Math.pow(tau, param.alpha) * Math.pow(eta, param.beta);
						if (temp > maximo) {
							maximo = temp;
							mejor = lrc.get(j2);
						}
					}
					// Ir al mejor
					candidato = mejor;
					prob = 1;
				}
				if (probDesp <= prob) {
					// Se desplaza
					colonia.get(j).add(candidato);
					double costeNueva = Auxiliares.calcularDistancia(colonia.get(j), Data.valueMatrix);
					if (costeNueva > mejorHormigaLocal.getKey()) {
						mejorHormigaLocal.setKey(costeNueva);
						mejorHormigaLocal.setValue(colonia.get(j));
					}

					// Pasa a la siguiente hormiga
					k = lrc.size();
				}
			}
		}
	}

	/**
	 * <pre>
			        _.---**""**-. 		
			._   .-'           /|`.     
			 \`.'             / |  `. 	
			  V              (  ;    \  
			  L       _.-  -. `'      \ 
			 / `-. _.'       \         ;
			:            __   ;    _   |
			:`-.___.+-*"': `  ;  .' `. |
			|`-/     `--*'   /  /  /`.\|
			: :              \    :`.| ;
			| |   .           ;/ .' ' / 
			: :  / `             :__.'  
			 \`._.-'       /     |      
			  : )         :      ;     
			  :----.._    |     /     
			 : .-.    `.       /       
			  \     `._       /       
			  /`-            /        
			 :             .'          
			  \ )       .-'           
			   `-----*"'
	 * </pre>
	 * Actualización global de feromona y del mejor encontrado
	 * @param param Parámetros del problema
	 * @param matrizFeromonas Matriz con los valores de feromona
	 * @param mejorHormigaLocal Mejor hormiga de la población actual
	 * @param mejorHormigaGlobal Mejor hormiga hasta el momento
	 */
	private static void demonio(Param param, double[][] matrizFeromonas,
			Pair<Double, ArrayList<Integer>> mejorHormigaLocal, Pair<Double, ArrayList<Integer>> mejorHormigaGlobal) {
		// En la mejor hormiga
		for (int i = 0; i < mejorHormigaLocal.getValue().size(); i++) {
			// Por cada elemento, actualiza su feromona
			for (int j = 0; j < i; j++) {
				matrizFeromonas[mejorHormigaLocal.getValue()
						.get(j)][mejorHormigaLocal
								.getValue().get(
										i)] = (1 - param.actFeronoma)
												* matrizFeromonas[mejorHormigaLocal.getValue().get(j)][mejorHormigaLocal
														.getValue().get(i)]
												+ param.actFeronoma * (mejorHormigaLocal.getKey());
				matrizFeromonas[mejorHormigaLocal.getValue().get(i)][mejorHormigaLocal.getValue().get(
						j)] = matrizFeromonas[mejorHormigaLocal.getValue().get(j)][mejorHormigaLocal.getValue().get(i)];
			}
		}

		// Actualiza mejor global
		if (mejorHormigaLocal.getKey() > mejorHormigaGlobal.getKey()) {
			mejorHormigaGlobal.setKey(mejorHormigaLocal.getKey());
			mejorHormigaGlobal.setValue(mejorHormigaLocal.getValue());
		}
	}

	/**
	 * Calcula la lista restringida de candidatos para la hormiga dada
	 * @param seleccionados Hormiga
	 * @param param Parámetros del problema
	 * @return Lista de candidatos
	 */
	private static LinkedList<Integer> LRC(ArrayList<Integer> seleccionados, Param param) {
		LinkedList<Integer> lista = new LinkedList<>();
		double dMin = distanciaMinima(seleccionados, Data.valueMatrix),
				dMax = distanciaMaxima(seleccionados, Data.valueMatrix);
		//Comprueba la condición por cada elemento posible del problema
		for (int i = 0; i < Data.size; i++) {
			double DISTANCIA = Auxiliares.calcularDistanciaElemento(i, seleccionados, Data.valueMatrix);
			double OTRA = (dMin + param.paramLRC * (dMax - dMin));

			if (!seleccionados.contains(i) && DISTANCIA >= OTRA) {
				lista.add(i);
			}
		}
		return lista;
	}

	/**
	 * Obtiene la distancia mínima de los arcos recorridos por la hormiga
	 * @param seleccionados Hormiga
	 * @param matriz Matriz de valores del problema
	 * @return Distancia mínima
	 */
	private static double distanciaMinima(ArrayList<Integer> seleccionados, double[][] matriz) {
		double actual = Double.MAX_VALUE;
		for (Integer integer : seleccionados) {
			double valor = Auxiliares.calcularDistanciaElemento(integer, seleccionados, matriz);
			if (valor < actual)
				actual = valor;
		}
		return actual;
	}

	/**
	 * Obtiene la distancia máxima de los arcos recorridos por la hormiga
	 * @param seleccionados Hormiga
	 * @param matriz Matriz de valores del problema
	 * @return Distancia máxima
	 */
	private static double distanciaMaxima(ArrayList<Integer> seleccionados, double[][] matriz) {
		double actual = -Double.MAX_VALUE;
		for (Integer integer : seleccionados) {
			double valor = Auxiliares.calcularDistanciaElemento(integer, seleccionados, matriz);
			if (valor > actual)
				actual = valor;
		}
		return actual;
	}

	/**
	 * Calcula la sumatoria de feromona de la hormiga respecto otro elemento
	 * @param r Hormiga
	 * @param s Elemento
	 * @param mFeromonas Matriz con los valores de feromona
	 * @return Sumatoria de feromona
	 */
	private static double sumFeromonas(ArrayList<Integer> r, int s, double[][] mFeromonas) {
		double suma = 0;
		for (Integer elem : r) {
			if (elem < s)
				suma += mFeromonas[elem][s];
			else
				suma += mFeromonas[s][elem];
		}
		return suma;
	}
}
