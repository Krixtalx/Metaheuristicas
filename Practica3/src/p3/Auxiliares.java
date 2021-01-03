package p3;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementa los distintos algoritmos para la resolucion del problema detallado
 * 
 * @author jcfer
 *
 */
public class Auxiliares {



	// -----------------------------------------------------------------------------
	// METODOS AUXILIARES
	// -----------------------------------------------------------------------------

	/**
	 * Calcula la solucion con los valores que menos veces han aparecido en una
	 * solucion
	 * 
	 * @param memoria Memoria de cada elemento del problema
	 * @param nElem   Numero de elementos que forman una solucion
	 * @return Lista de enteros correspondiente a los elementos de menos
	 *         repeticiones
	 */
	public static ArrayList<Integer> menosElegidos(int[] memoria, int nElem) {
		ArrayList<Integer> sol = new ArrayList<>();
		while (sol.size() < nElem) {
			int peor = -1;
			int peorValor = Integer.MAX_VALUE;
			for (int i = 0; i < memoria.length; i++) {
				if (memoria[i] < peorValor && !sol.contains(i)) {
					peorValor = memoria[i];
					peor = i;
				}
			}
			sol.add(peor);
		}
		return sol;
	}

	/**
	 * Calcula la solucion con los valores que mas veces han aparecido en una
	 * solucion
	 * 
	 * @param memoria Memoria de cada elemento del problema
	 * @param nElem   Numero de elementos que forman una solucion
	 * @return Lista de enteros correspondiente a los elementos de mas repeticiones
	 */
	public static ArrayList<Integer> masElegidos(int[] memoria, int nElem) {
		ArrayList<Integer> sol = new ArrayList<>();
		while (sol.size() < nElem) {
			int mejor = -1;
			int mejorValor = Integer.MIN_VALUE;
			for (int i = 0; i < memoria.length; i++) {
				if (memoria[i] > mejorValor && !sol.contains(i)) {
					mejorValor = memoria[i];
					mejor = i;
				}
			}
			sol.add(mejor);
		}
		return sol;
	}

	/**
	 * Calcula la diferencia del aporte entre un elemento seleccionado y otro no
	 * seleccionado
	 * 
	 * @param seleccion Elementos seleccionados
	 * @param matriz    Matriz de distancias
	 * @param elemento  Elemento seleccionado a intercambiar
	 * @param candidato Elemento no seleccionado a intercambiar
	 * @return Diferencia entre la distancia del seleccionado y el no seleccionado
	 */
	public static double difIntercambio(List<Integer> seleccion, double[][] matriz, int elemento, int candidato) {
		double diferencia = 0;
		for (int i = 0; i < seleccion.size(); i++) {
			if (seleccion.get(i) != elemento) {
				diferencia += valorMatriz(matriz, seleccion.get(i), candidato)
						- valorMatriz(matriz, seleccion.get(i), elemento);
			}
		}
		return diferencia;
	}

	/**
	 * Calcula la distancia de cada elemento de la seleccion dada
	 * 
	 * @param seleccion Conjunto de elementos
	 * @param matriz    Matriz de distancias
	 * @return SortedPairArray con el valor de cada elemento
	 */
	public static SortedPairArray<Double, Integer> ordenarSeleccion(List<Integer> seleccion, double[][] matriz) {
		SortedPairArray<Double, Integer> res = new SortedPairArray<Double, Integer>();
		for (int i = 0; i < seleccion.size(); i++) {
			res.put(calcularDistanciaElemento(seleccion.get(i), seleccion, matriz), seleccion.get(i));
		}
		return res;
	}

	/**
	 * Calcula la distancia del conjunto de elementos seleccionados
	 * 
	 * @param seleccion Conjunto de elementos
	 * @param matriz    Matriz de distancias
	 * @return Distancia del conjuto
	 */
	public static double calcularDistancia(List<Integer> seleccion, double[][] matriz) {
		double suma = 0;
		int tam = seleccion.size();
		for (int i = 0; i < tam; i++) {
			for (int j = i + 1; j < tam; j++) {
				suma += valorMatriz(matriz, seleccion.get(i), seleccion.get(j));
			}
		}
		return suma;
	}

	/**
	 * Calcula la distancia de un elemento respecto una selección
	 * 
	 * @param elemento  Elemento del que calcular la distancia
	 * @param seleccion Conjunto de elementos
	 * @param matriz    Matriz de distancias
	 * @return Distancia del elemento
	 */
	public static double calcularDistanciaElemento(int elemento, List<Integer> seleccion, double[][] matriz) {
		float suma = 0;
		for (int elementoSeleccion: seleccion) {
			if (elementoSeleccion != elemento) {
				suma += valorMatriz(matriz, elementoSeleccion, elemento);
			}
		}
		return suma;
	}

	/**
	 * Devuelve el valor de la matriz correspondiente a la fila f y a la columna c
	 * 
	 * @param m
	 * @param f
	 * @param c
	 * @return float
	 */
	public static double valorMatriz(double[][] m, int f, int c) {
		if (f < c) {
			return m[f][c];
		} else {
			return m[c][f];
		}
	}
}
