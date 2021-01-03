package p3;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlgSCH_Clase04_Grupo2 {

	/**
	 * Ejecuta un SCH para la resolución del problema
	 * 
	 * @param datos Datos del problema
	 * @param param Parámetros del problema
	 * @param log   Clase para la escritura en ficheros
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
		ArrayList<ArrayList<Integer>> noSeleccion = new ArrayList<>();
		ArrayList<Integer> noSeleccionLleno = new ArrayList<>();
		for (int j = 0; j < Data.size; j++) {
			noSeleccionLleno.add(j);
		}
		for (int i = 0; i < param.tamPoblacion; i++) {
			noSeleccion.add(new ArrayList<>());		
		}
		Instant inicio = Instant.now();
		Instant fin = Instant.now();

		// BUCLE PRINCIPAL
		while (it < param.iteraciones && Duration.between(inicio, fin).toSeconds() < 600) {
			// Inicializar nueva población
			colonia.clear();
			ArrayList<Integer> hormiga;
			for (int i = 0; i < param.tamPoblacion; i++) {
				int candidatoAleatorio = param.generateInt(Data.size);
				hormiga = new ArrayList<>();
				hormiga.add(candidatoAleatorio);
				colonia.add(hormiga);
				noSeleccion.set(i, new ArrayList<>(noSeleccionLleno));
				noSeleccion.get(i).remove(candidatoAleatorio);
			}

			mejorHormigaLocal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE, null);

			// Mientras no se tengan soluciones completas
			for (int i = 1; i < Data.selection; i++) {
				// Añade un nuevo elemento a cada hormiga
				nuevoElementoHormiga(param, matrizHeuristica, matrizFeromonas, colonia, mejorHormigaLocal, noSeleccion);

				// Actualiza la feromona local
				actualizarLocal(param, matrizFeromonas, colonia);
			}

			// Calcula la mejor local
			for (ArrayList<Integer> hormiga2: colonia) {
				double costeNueva = Auxiliares.calcularDistancia(hormiga2, Data.valueMatrix);
				if (costeNueva > mejorHormigaLocal.getKey()) {
					mejorHormigaLocal.setKey(costeNueva);
					mejorHormigaLocal.setValue(hormiga2);
				}
			}

			// Actualiza la feromona global y actualiza el mejor resultado global
			demonio(param, matrizFeromonas, mejorHormigaLocal, mejorHormigaGlobal);

			it++;
			// System.out.println(it);
			log.write("It: " + it);
			log.write("Mejor hormiga local: " + mejorHormigaLocal);
			log.write("Mejor hormiga global: " + mejorHormigaGlobal);
//			System.out.println("It: " + it);
//			System.out.print(mejorHormigaLocal);
//			System.out.println("Tam: " + mejorHormigaLocal.getValue().size());
//			System.out.print(mejorHormigaGlobal);
//			System.out.println("Tam: " + mejorHormigaGlobal.getValue().size());
			fin = Instant.now();
		}
		return mejorHormigaGlobal;
	}

	/**
	 * Actualiza la matriz de feromonas localmente
	 * 
	 * @param param           Parámetros del problema
	 * @param matrizFeromonas Matriz con los valores de feromona
	 * @param colonia         Población actual de hormigas
	 */
	private static void actualizarLocal(Param param, double[][] matrizFeromonas,
			ArrayList<ArrayList<Integer>> colonia) {
		// Actualización local -> Diapositiva 68
		// Por cada hormiga
		for (int j = 0; j < param.tamPoblacion; j++) {
			// Por cada elemento
			for (int j2 = 0; j2 < colonia.get(j).size() - 1; j2++) {
				// τrs(t) = (1 − ϕ) ⋅ τrs(t − 1) + ϕ ⋅ τ0
				int primero = colonia.get(j).get(j2);
				int segundo = colonia.get(j).get(colonia.get(j).size() - 1);
				matrizFeromonas[primero][segundo] = (1 - param.actLocal) * matrizFeromonas[primero][segundo]
						+ param.actLocal * param.feromonaInicial;
				matrizFeromonas[segundo][primero] = matrizFeromonas[primero][segundo];

			}
		}
	}

	/**
	 * Añade un nuevo elemento a todas las hormigas de la población
	 * 
	 * @param param             Parámetros del problema
	 * @param matrizHeuristica  Matriz de valores heurísticas
	 * @param matrizFeromonas   Matriz con los valores de feromona
	 * @param colonia           Población actual de hormigas
	 * @param mejorHormigaLocal Mejor hormiga de la población actual
	 */
	private static void nuevoElementoHormiga(Param param, double[][] matrizHeuristica, double[][] matrizFeromonas,
			ArrayList<ArrayList<Integer>> colonia, Pair<Double, ArrayList<Integer>> mejorHormigaLocal,
			ArrayList<ArrayList<Integer>> noSelecc) {

		LinkedList<Integer> lrc;
		ArrayList<Integer> hormiga;
		// Para cada hormiga
		for (int j = 0; j < param.tamPoblacion; j++) {
			// Obtener LRC
			hormiga = colonia.get(j);
			lrc = LRC(hormiga, param.paramLRC, noSelecc.get(j));
			double probDesp = param.generateDouble();
			double prob = 0d;
			int tamLRC = lrc.size();
			for (int k = 0; k < tamLRC; k++) {
				int candidato = lrc.get(k);
				if (param.qcero > param.generateDouble()) { // P'k -> Diapositiva 66
					double suma = 0;
					// Sumatorio divisor
					for (int elemLRC : lrc) {
						double tau = sumFeromonas(hormiga, elemLRC, matrizFeromonas);
						double eta = Auxiliares.calcularDistanciaElemento(elemLRC, hormiga, matrizHeuristica);
						suma += Math.pow(tau, param.alpha) * Math.pow(eta, param.beta);
					}
					// Dividendo
					double tau = sumFeromonas(hormiga, candidato, matrizFeromonas);
					double eta = Auxiliares.calcularDistanciaElemento(candidato, hormiga, matrizHeuristica);

					prob += (Math.pow(tau, param.alpha) * Math.pow(eta, param.beta)) / suma; // Formula
																								// diapositiva
																								// 38
				} else { // arg max
					double maximo = -Double.MAX_VALUE;
					int mejor = -1;
					for (int elemLRC : lrc) {
						double tau = sumFeromonas(hormiga, elemLRC, matrizFeromonas);
						double eta = Auxiliares.calcularDistanciaElemento(elemLRC, hormiga, matrizHeuristica);
						double temp = Math.pow(tau, param.alpha) * Math.pow(eta, param.beta);
						if (temp > maximo) {
							maximo = temp;
							mejor = elemLRC;
						}
					}
					// Ir al mejor
					candidato = mejor;
					prob = 1;
				}
				if (probDesp <= prob) {
					// Se desplaza
					hormiga.add(candidato);
					noSelecc.get(j).remove(noSelecc.get(j).indexOf(candidato));
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
	 * 
	 * Actualización global de feromona y del mejor encontrado
	 * 
	 * @param param              Parámetros del problema
	 * @param matrizFeromonas    Matriz con los valores de feromona
	 * @param mejorHormigaLocal  Mejor hormiga de la población actual
	 * @param mejorHormigaGlobal Mejor hormiga hasta el momento
	 */
	private static void demonio(Param param, double[][] matrizFeromonas,
			Pair<Double, ArrayList<Integer>> mejorHormigaLocal, Pair<Double, ArrayList<Integer>> mejorHormigaGlobal) {
		// En toda la matriz de feromona, evapora
		int primero, segundo;
		for (int i = 0; i < matrizFeromonas.length; i++) {
			for (int j = 0; j < matrizFeromonas[i].length; j++) {
				matrizFeromonas[i][j] = (1 - param.actFeronoma) * matrizFeromonas[i][j];
			}
		}

		// En la mejor hormiga
		for (int i = 0; i < mejorHormigaLocal.getValue().size(); i++) {
			// Por cada elemento, actualiza su feromona
			for (int j = 0; j < i; j++) {
				primero = mejorHormigaLocal.getValue().get(j);
				segundo = mejorHormigaLocal.getValue().get(i);
				matrizFeromonas[primero][segundo] += param.actFeronoma * (mejorHormigaLocal.getKey());
				matrizFeromonas[segundo][primero] = matrizFeromonas[primero][segundo];
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
	 * 
	 * @param seleccionados Hormiga
	 * @param param         Parámetros del problema
	 * @return Lista de candidatos
	 */
	private static LinkedList<Integer> LRC(ArrayList<Integer> seleccionados, Double delta,
			ArrayList<Integer> noSeleccionados) {
		LinkedList<Integer> lista = new LinkedList<>();

		double dMin = distanciaMinima(noSeleccionados, seleccionados, Data.valueMatrix),
				dMax = distanciaMaxima(noSeleccionados, seleccionados, Data.valueMatrix);
		double limite = dMin + delta * (dMax - dMin);
		double distancia;

		// Comprueba la condición por cada elemento posible del problema
		for (int i : noSeleccionados) {
			distancia = Auxiliares.calcularDistanciaElemento(i, seleccionados, Data.valueMatrix);
			if (distancia >= limite) {
				lista.add(i);
			}
		}
		return lista;
	}

	/**
	 * Obtiene la distancia mínima de los arcos recorridos por la hormiga
	 * 
	 * @param seleccionados Hormiga
	 * @param matriz        Matriz de valores del problema
	 * @return Distancia mínima
	 */
	private static double distanciaMinima(List<Integer> noSeleccionados, ArrayList<Integer> seleccionados,
			double[][] matriz) {
		double actual = Double.MAX_VALUE;
		double valor;
		for (int noseleccionado : noSeleccionados) {
			valor = Auxiliares.calcularDistanciaElemento(noseleccionado, seleccionados, matriz);
			if (valor < actual)
				actual = valor;

		}
		return actual;
	}

	/**
	 * Obtiene la distancia máxima de los arcos recorridos por la hormiga
	 * 
	 * @param seleccionados Hormiga
	 * @param matriz        Matriz de valores del problema
	 * @return Distancia máxima
	 */
	private static double distanciaMaxima(List<Integer> noSeleccionados, ArrayList<Integer> seleccionados,
			double[][] matriz) {
		double actual = -Double.MAX_VALUE;
		for (int noseleccionado : noSeleccionados) {
			double valor = Auxiliares.calcularDistanciaElemento(noseleccionado, seleccionados, matriz);
			if (valor > actual)
				actual = valor;
		}
		return actual;
	}

	/**
	 * Calcula la sumatoria de feromona de la hormiga respecto otro elemento
	 * 
	 * @param r          Hormiga
	 * @param s          Elemento
	 * @param mFeromonas Matriz con los valores de feromona
	 * @return Sumatoria de feromona
	 */
	private static double sumFeromonas(ArrayList<Integer> r, int s, double[][] mFeromonas) {
		double suma = 0;
		for (Integer elem : r) {
			suma += Auxiliares.valorMatriz(mFeromonas, elem, s);
		}
		return suma;
	}
}
