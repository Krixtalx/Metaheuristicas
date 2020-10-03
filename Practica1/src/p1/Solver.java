package p1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementa los distintos algoritmos para la resolucion del problema detallado
 * 
 * @author jcfer
 *
 */
public class Solver {

	/**
	 * Obtiene una solucion al problema mediante un algoritmo voraz, seleccionando
	 * los elementos que mas aporten
	 * 
	 * @return ArrayList con los elementos solucion
	 */
	public static ArrayList<Integer> Greedy(Data data, Param param) {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		seleccionados.add(param.generateInt(data.getSize() - 1)); // Restamos 1 ya que el ultimo
																	// no tiene entrada en la matriz
		candidatos.remove(seleccionados.get(0));
		float sumaMejor, sumaTemp, distanciaTotal = 0;
		float[][] matriz = data.getMatrix();
		int mejor;
		while (seleccionados.size() < data.getSelection()) {
			mejor = -1;
			sumaMejor = -1;
			for (int i = 0; i < candidatos.size(); i++) { // Bucle de candidatos
				sumaTemp = 0;
				for (int j = 0; j < seleccionados.size(); j++) { // Bucle de seleccionados
					sumaTemp += valorMatriz(matriz, candidatos.get(i), seleccionados.get(j));
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
		}
		System.out.println("Distancia M: " + distanciaTotal);
		return seleccionados;
	}
	
	public static ArrayList<Integer> BusquedaLocal(Data data, Param param) {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		ArrayList<Integer> vecino = new ArrayList<>();
		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		
		for(int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size()-1));
			seleccionados.add(elegido); //Seleccionamos aleatoriamente entre los candidatos
			vecino.add(elegido);
		}
	
		float[][] matriz = data.getMatrix();
		float sumaActual = calcularDistancia(seleccionados, matriz);
		float sumaTemp;
		int posCambio, elemCambio, maximoIteraciones;
		
		if(param.getIteraciones() < candidatos.size()) {
			maximoIteraciones = param.getIteraciones();
		}else {
			maximoIteraciones = candidatos.size();
		}
		for (int j = 0; j < seleccionados.size(); j++) {
			posCambio = menorDistancia(seleccionados, matriz); //Seleccionado que va a cambiarse
			elemCambio = seleccionados.get(posCambio);
			for(int i = 0; i < maximoIteraciones; i++) {
				
				vecino.set(posCambio, candidatos.get(i));
				sumaTemp = sumaActual - calcularDistanciaElemento(elemCambio, seleccionados, matriz) + calcularDistanciaElemento(candidatos.get(i), vecino, matriz);
				
				//Si el vecino es mejor, desplazar
				if(sumaActual < sumaTemp) {
					sumaActual = sumaTemp;
					seleccionados.set(posCambio, candidatos.remove(i));
					candidatos.add(elemCambio);
				}
			}
		}
		System.out.println("Distancia M: " + sumaActual);
		return seleccionados;
	}
	
	public static ArrayList<Integer> NuevaBusquedaLocal(Data data, Param param) {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		ArrayList<Integer> vecino = new ArrayList<>();
		SortedPairArray<Float, Integer> ordenCambio;
		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		for(int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size()-1));
			seleccionados.add(elegido); //Seleccionamos aleatoriamente entre los candidatos
			vecino.add(elegido);
		}
		
		System.out.println("Random inicio:\n" + seleccionados.toString());
		float[][] matriz = data.getMatrix();
		int it = 0, siguiente = 0, posCambio;
		float distCambio, distVecino;
		boolean vecinoEncontrado = false;
		
		ordenCambio = distancias(seleccionados, matriz);
		//Itera un numero determinado de veces, o hasta recorrer todo el vecindario
		while(it < param.getIteraciones() && siguiente < ordenCambio.size()) {
			distCambio = ordenCambio.get(siguiente).getKey();
			posCambio = ordenCambio.get(siguiente).getValue();
			//Para cada cambio, obtiene la distancia del nuevo elemento
			for (int i = 0; i < candidatos.size(); i++) {
				it++;
				vecino.set(posCambio, candidatos.get(i));
				distVecino = calcularDistanciaElemento(candidatos.get(i), vecino, matriz);
//				System.out.println("probando: " + candidatos.get(i) + "-"+ distVecino + " >= " + ordenCambio.get(siguiente).getValue() + "-" + distCambio);
				if(distVecino >= distCambio) { //Si el vecino es mejor, lo intercambia
					candidatos.add(seleccionados.set(posCambio, candidatos.remove(i)));
					vecinoEncontrado = true;
//					System.out.println("nuevo: " + seleccionados);
					i = candidatos.size();
				}else { //Si no, reestablece el elemento original y continua explorando
					vecino.set(posCambio, seleccionados.get(posCambio));
				}
			}
			if(vecinoEncontrado) { //Si se ha encontrado un vecino mejor, reinicia la busqueda
				siguiente = 0;
				ordenCambio = distancias(seleccionados, matriz);
				vecinoEncontrado = false;
			}else { //Si no, vuelve a intentarlo con el siguiente elemento de la solucion
				siguiente++;
			}
		}
		
		System.out.println("dis=" + calcularDistancia(seleccionados, matriz));
		return seleccionados;
	}
	
	private static SortedPairArray<Float, Integer> distancias(List<Integer> seleccion, float[][] matriz){
		SortedPairArray<Float, Integer> res = new SortedPairArray<Float, Integer>();
		for (int i = 0; i < seleccion.size(); i++) {
			res.put(calcularDistanciaElemento(seleccion.get(i), seleccion, matriz), i);
		}
		return res;
	}
	
	private static int menorDistancia(List<Integer> seleccion, float[][] matriz) {
		int sel = -1;
		float suma, menorSuma = Integer.MAX_VALUE;
		for(int i = 0; i < seleccion.size(); i++) {
			suma = 0;
			for(int j = 0; j < seleccion.size(); j++) {
				if(i != j) {
					suma += valorMatriz(matriz, seleccion.get(i), seleccion.get(j));
				}
			}
			if(suma < menorSuma) {
				menorSuma = suma;
				sel = i;
			}
		}
		
		return sel;
	}

	private static float calcularDistancia(List<Integer> seleccion, float[][] matriz) {
		float suma = 0;
		for(int i = 0; i < seleccion.size(); i++) {
			for(int j = i+1; j < seleccion.size(); j++) {
				suma += valorMatriz(matriz, seleccion.get(i), seleccion.get(j));
			}
		}
		return suma;
	}
	
	private static float calcularDistanciaElemento(int elemento, List<Integer> seleccion, float[][] matriz) {
		float suma = 0;
		
		for (int i = 0; i < seleccion.size(); i++) {
			if(seleccion.get(i) != elemento) {
				suma += valorMatriz(matriz, seleccion.get(i), elemento);
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
	public static float valorMatriz(float[][] m, int f, int c) {
		if (f < c) {
			return m[f][c];
		} else {
			return m[c][f];
		}
	}
}
