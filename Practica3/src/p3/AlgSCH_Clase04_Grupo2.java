package p3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AlgSCH_Clase04_Grupo2 {

	public static void ejecutar(Data datos, Param param, Logger log) {
		int it = 0;
		double[][] matrizHeuristica = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];
		double[][] matrizFeromonas = new double[Data.valueMatrix.length][Data.valueMatrix[0].length];

		// Inicializamos la matriz heuristica
		for (int i = 0; i < matrizHeuristica.length; i++) {
			for (int j = 0; j < matrizHeuristica[0].length; j++) {
				matrizHeuristica[i][j] = 1 / Data.valueMatrix[i][j];
				matrizFeromonas[i][j] = param.feromonaInicial;
			}
		}

		ArrayList<ArrayList<Integer>> colonia = new ArrayList<>();
		LinkedList<Integer> lrc;
		Pair<Double, ArrayList<Integer>> mejorHormiga = new Pair<Double, ArrayList<Integer>>(Double.MIN_VALUE, null);

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

			// Mientras no se tengan soluciones completas
			for (int i = 1; i < Data.selection; i++) {
				// Para cada hormiga
				for (int j = 0; j < param.tamPoblacion; j++) {
					// Obtener LRC
					lrc = LRC(colonia.get(j), param);
					double probDesp = param.generateDouble();
					for (int k = 0; k < lrc.size(); k++){
						
						int candidato = lrc.get(k);
						double prob = 0;
						if (param.generateDouble() < param.qcero) { // P'k
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

							prob = (Math.pow(tau, param.alpha) * Math.pow(eta, param.beta)) / suma; // Formula
																									// diapositiva 38
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
//							for (Integer origen : colonia.get(j)) {
//								//τrs(t) = (1 − ϕ) ⋅ τrs(t − 1) + ϕ ⋅ τ0
//								matrizFeromonas[origen][candidato] = (1 - param.actLocal)
//										* matrizFeromonas[origen][candidato] + param.actLocal * param.feromonaInicial;
//							}
							colonia.get(j).add(candidato);
							double costeNueva = Auxiliares.calcularDistancia(colonia.get(j), Data.valueMatrix);
							if(costeNueva > mejorHormiga.getKey()) {
								mejorHormiga.setKey(costeNueva);
								mejorHormiga.setValue(colonia.get(j));
							}
							break;
						}
					}
				}
				//Actualización local
				for (int j = 0; j < param.tamPoblacion; j++) {
					for (int j2 = 0; j2 < colonia.get(j).size()-1; j2++) {
						//τrs(t) = (1 − ϕ) ⋅ τrs(t − 1) + ϕ ⋅ τ0
						matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j).get(colonia.get(j).size()-1)] = (1 - param.actLocal)
								* matrizFeromonas[colonia.get(j).get(j2)][colonia.get(j).get(colonia.get(j).size()-1)] + param.actLocal * param.feromonaInicial;
					}
				}
			}

			//EL DEMOÑO
//			for (int i = 0; i < matrizFeromonas.length; i++) {
//				for (int k = 0; k < matrizFeromonas[i].length; k++) {
//					matrizFeromonas[i][k] = (1-param.actFeronoma)*matrizFeromonas[i][k];
//					if(mejorHormiga.getValue().contains(i) || mejorHormiga.getValue().contains(k))
//						matrizFeromonas[i][k]+=param.actFeronoma*Auxiliares.calcularDistancia(mejorHormiga.getValue(), Data.valueMatrix);
//				}
//			}
			
			for (int i = 0; i < mejorHormiga.getValue().size(); i++) {
				for (int j = 0; j < i; j++) {
					matrizFeromonas[mejorHormiga.getValue().get(j)][mejorHormiga.getValue().get(i)] = (1 - param.actFeronoma)
							* matrizFeromonas[mejorHormiga.getValue().get(j)][mejorHormiga.getValue().get(i)] + param.actFeronoma * (1/mejorHormiga.getKey());
				}
			}
			it++;
			System.out.print(mejorHormiga);
			System.out.println("Tam: " + mejorHormiga.getValue().size());
		}
	}

	private static LinkedList<Integer> LRC(ArrayList<Integer> seleccionados, Param param) {
		LinkedList<Integer> lista = new LinkedList<>();
		double dMin = distanciaMinima(seleccionados, Data.valueMatrix), dMax = distanciaMaxima(seleccionados, Data.valueMatrix);
		for (int i = 0; i < Data.size; i++) {
			double DISTANCIA = Auxiliares.calcularDistanciaElemento(i, seleccionados,
					Data.valueMatrix);
			double OTRA = (dMin + param.paramLRC * (dMax - dMin));
			if (!seleccionados.contains(i) && Auxiliares.calcularDistanciaElemento(i, seleccionados,
					Data.valueMatrix) >= (dMin + param.paramLRC * (dMax - dMin))) {
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
			suma += mFeromonas[elem][s];
		}
		return suma;
	}
}
