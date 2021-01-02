package p3;

import java.util.ArrayList;
import java.util.LinkedList;

public class AlgSCH_Clase04_Grupo2 {

	public static void ejecutar(Data datos, Param param, Logger log) {
		int it = 0;
		double[][] matrizHeuristica = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];
		double[][] matrizFeromonas = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];

		// Inicializamos la matriz heuristica
		for (int i = 0; i < matrizHeuristica.length; i++) {
			for (int j = 0; j < matrizHeuristica[0].length; j++) {
				matrizHeuristica[i][j] = Data.valueMatrix[i][j];
				matrizFeromonas[i][j] = param.feromonaInicial;
			}
		}

		ArrayList<ArrayList<Integer>> colonia = new ArrayList<>();
		LinkedList<Integer> lrc;
		Pair<Double, ArrayList<Integer>> mejorHormigaLocal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE,
				null);
		Pair<Double, ArrayList<Integer>> mejorHormigaGlobal = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE,
				null);

		while (it < param.iteraciones) {
			System.out.println(it);
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
				// Para cada hormiga
				double prob = 0;
				for (int j = 0; j < param.tamPoblacion; j++) {
					// Obtener LRC
					lrc = LRC(colonia.get(j), param);
					double probDesp = param.generateDouble();
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
							double eta = Auxiliares.calcularDistanciaElemento(candidato, colonia.get(j),
									matrizHeuristica);

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

					// System.out.println("TAM HORMIGA: " + colonia.get(j).size());
				}
				// Actualización local -> Diapositiva 68
				// Por cada hormiga
				for (int j = 0; j < param.tamPoblacion; j++) {
					// Por cada elemento
					for (int j2 = 0; j2 < colonia.get(j).size() - 1; j2++) {
						// τrs(t) = (1 − ϕ) ⋅ τrs(t − 1) + ϕ ⋅ τ0
						matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j)
								.get(colonia.get(j).size() - 1)] = (1 - param.actLocal)
										* matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j)
												.get(colonia.get(j).size() - 1)]
										+ param.actLocal * param.feromonaInicial;
						matrizFeromonas[colonia.get(j).get(colonia.get(j).size() - 1)][colonia.get(j)
								.get(j2)] = matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j)
										.get(colonia.get(j).size() - 1)];
						
					}
				}

			}
			/*
			EL
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
					   
			 */
			
			// En la mejor hormiga
			for (int i = 0; i < mejorHormigaLocal.getValue().size(); i++) {
				// Por cada elemento, actualiza su feromona
				for (int j = 0; j < i; j++) {
					matrizFeromonas[mejorHormigaLocal.getValue().get(j)][mejorHormigaLocal
							.getValue().get(
									i)] = (1 - param.actFeronoma)
											* matrizFeromonas[mejorHormigaLocal.getValue().get(j)][mejorHormigaLocal
													.getValue().get(i)]
											+ param.actFeronoma * (mejorHormigaLocal.getKey());
					matrizFeromonas[mejorHormigaLocal.getValue().get(i)][mejorHormigaLocal.getValue()
							.get(j)] = matrizFeromonas[mejorHormigaLocal.getValue().get(j)][mejorHormigaLocal.getValue()
									.get(i)];
				}
			}

			// Actualiza mejor global
			if (mejorHormigaLocal.getKey() > mejorHormigaGlobal.getKey()) {
				mejorHormigaGlobal = mejorHormigaLocal;
			}

			it++;
			System.out.print(mejorHormigaLocal);
			System.out.println("Tam: " + mejorHormigaLocal.getValue().size());
			System.out.print(mejorHormigaGlobal);
			System.out.println("Tam: " + mejorHormigaGlobal.getValue().size());
		}
	}

	private static LinkedList<Integer> LRC(ArrayList<Integer> seleccionados, Param param) {
		LinkedList<Integer> lista = new LinkedList<>();
		double dMin = distanciaMinima(seleccionados, Data.valueMatrix),
				dMax = distanciaMaxima(seleccionados, Data.valueMatrix);
		for (int i = 0; i < Data.size; i++) {
			double DISTANCIA = Auxiliares.calcularDistanciaElemento(i, seleccionados, Data.valueMatrix);
			double OTRA = (dMin + param.paramLRC * (dMax - dMin));

			if (!seleccionados.contains(i) && DISTANCIA >= OTRA) {
				lista.add(i);
			}
		}
		return lista;
	}

	private static double distanciaMinima(ArrayList<Integer> seleccionados, double[][] matriz) {
		double actual = Double.MAX_VALUE;
		for (Integer integer : seleccionados) {
			double valor = Auxiliares.calcularDistanciaElemento(integer, seleccionados, matriz);
			if (valor < actual)
				actual = valor;
		}
		return actual;
	}

	private static double distanciaMaxima(ArrayList<Integer> seleccionados, double[][] matriz) {
		double actual = -Double.MAX_VALUE;
		for (Integer integer : seleccionados) {
			double valor = Auxiliares.calcularDistanciaElemento(integer, seleccionados, matriz);
			if (valor > actual)
				actual = valor;
		}
		return actual;
	}

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
