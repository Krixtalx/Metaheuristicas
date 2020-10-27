package p1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Implementa los distintos algoritmos para la resolucion del problema detallado
 * 
 * @author jcfer
 *
 */
public class Solver {

	//-----------------------------------------------------------------------------
	//								ALGORITMOS
	//-----------------------------------------------------------------------------
	
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
		double sumaMejor, sumaTemp, distanciaTotal = 0;
		double[][] matrizCoste = data.getMatrix();
		int mejor;
		while (seleccionados.size() < data.getSelection()) {
			mejor = -1;
			sumaMejor = -1;
			for (int i = 0; i < candidatos.size(); i++) { // Bucle de candidatos
				sumaTemp = 0;
				for (int j = 0; j < seleccionados.size(); j++) { // Bucle de seleccionados
					sumaTemp += valorMatriz(matrizCoste, candidatos.get(i), seleccionados.get(j));
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
	
	/**
	 * Obtiene una solucion al problema mediante un algoritmo de busqueda local,
	 * explorando el vecindario de una solucion inicial aleatoria
	 * 
	 * @return ArrayList con los elementos solucion
	 * @throws IOException 
	 */
	public static ArrayList<Integer> busquedaLocal(Data data, Param param) throws IOException {
		ArrayList<Integer> seleccionados = new ArrayList<>(); // Seleccionados
		LinkedList<Integer> candidatos = new LinkedList<>(); // Candidatos
		ArrayList<Integer> vecino = new ArrayList<>();
		SortedPairArray<Double, Integer> ordenCambio;
		
		for (int i = 0; i < data.getSize(); i++) {
			candidatos.add(i);
		}
		for(int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size()-1));
			seleccionados.add(elegido); //Seleccionamos aleatoriamente entre los candidatos
			vecino.add(elegido);
		}
		
		System.out.println("Random inicio: " + seleccionados.toString());
		double[][] matrizCoste = data.getMatrix();
		int it = 0, siguiente = 0, elemCambio;
		boolean vecinoEncontrado = false;
		
		ordenCambio = distancias(seleccionados, matrizCoste);
		
		FileWriter wrt = new FileWriter("logPruebas.txt");
		//Itera un numero determinado de veces, o hasta recorrer todo el vecindario
		while(it < param.getIteraciones() && siguiente < ordenCambio.size()) {
			elemCambio = ordenCambio.get(siguiente).getValue();
			//Para cada intercambio, obtiene la distancia del nuevo elemento
			for (int i = 0; i < candidatos.size(); i++) {
				//Si el vecino es mejor, lo intercambia
				it++;
				
				wrt.write("Prueba: " + elemCambio + " vs " + candidatos.get(i));
					
				if(difIntercambio(seleccionados, matrizCoste, elemCambio, candidatos.get(i)) > 0) {
					//Elimina el candidato de la lista, lo a�ade en la posici�n correspondiente, y devuelve
						//el elemento previo a los candidatos
					candidatos.add(seleccionados.set(seleccionados.indexOf(elemCambio), candidatos.remove(i)));
					vecinoEncontrado = true;
					i = candidatos.size();
					wrt.write(" mejora\n");
				}else {
					wrt.write(" sigue\n");
				}
			}
			if(vecinoEncontrado) { //Si se ha encontrado un vecino mejor, reinicia la busqueda
				siguiente = 0;
				ordenCambio = distancias(seleccionados, matrizCoste);
				vecinoEncontrado = false;
			}else { //Si no, vuelve a intentarlo con el siguiente elemento de la solucion
				siguiente++;
			}
		}
		wrt.close();
		System.out.println("Distancia M: " + calcularDistancia(seleccionados, matrizCoste) + " en " + it + " evaluaciones");
		return seleccionados;
	}
	
	public static ArrayList<Integer> busquedaTabu(Data data, Param param) throws IOException{
		//Inicializamos los parametros que usaremos en la funcion
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
		for(int i = 0; i < data.getSelection(); i++) {
			int elegido = candidatos.remove(param.generateInt(candidatos.size()-1));
			seleccionados.add(elegido); //Seleccionamos aleatoriamente entre los candidatos
			mejorSolucion.add(elegido);
		}
		int it = 0;
		
		valorMejorSolucion = calcularDistancia(mejorSolucion, matrizCostes);
		
		SortedPairArray<Double, Integer> ordenCambio;
		int elemCambio, siguiente = 0;

		FileWriter wrt = new FileWriter("logPruebas.txt");
		ArrayList<Integer> randUsedList = new ArrayList<>();
		while(it < param.getIteraciones()) {
			int iteracionesSinMejora=0;
			
			while(iteracionesSinMejora < param.getItSinMejora() && it < param.getIteraciones()) {
				ordenCambio = distancias(seleccionados, matrizCostes); //Calculamos los elementos que menos aportan a la solucion
				
				//Generar vecindario
				elemCambio = ordenCambio.get(siguiente).getValue();
				double mejorCostoVecino = Integer.MIN_VALUE;
				int mejorVecino=-1;
				String temp = "i-> ";
				//TODO: limitar % vecindario. GL
				for (int i = 0; i < param.getTamVecindario(); i++) {
					//Genera un vecino aleatoriamente
					int randPosition = param.generateInt(candidatos.size());
					int candidatoCambio = candidatos.get(randPosition);
					if(!listaTabu.contains(candidatoCambio)) { //Si no esta restringido, entra al vecindario
						//Comprueba si es el mejor
						double diff = difIntercambio(seleccionados, matrizCostes, elemCambio, candidatoCambio);
						if(mejorCostoVecino < diff) {
							mejorCostoVecino = diff;
							mejorVecino = candidatoCambio;
						}
					}else {
						i--;
					}
					//Quitamos de los candidatos el recien probado
					randUsedList.add(candidatos.remove(randPosition));
					temp += i + ", ";
				}
				wrt.write(temp+"\n");
				//Intercambiamos el nuevo elemento con el antiguo
				candidatos.add(seleccionados.remove(seleccionados.indexOf(elemCambio)));
				seleccionados.add(randUsedList.remove(randUsedList.indexOf(mejorVecino)));
				//Actualiza la memoria a largo plazo
				for (int i = 0; i < seleccionados.size(); i++) {
					memoria[seleccionados.get(i)]++;
				}
				
				//Actualiza lista tabu
				listaTabu.push(elemCambio);
				
				//Incrementamos el numero de iteracciones
				it++;
				
				if(mejorCostoVecino<=0) //Si no mejora nuestra solucion, aumentamos el numero de iteraciones sin mejora.
					iteracionesSinMejora++;
				else { //Si es mejor, comprueba si es mejor que la mejor solucion global y la guarda
					iteracionesSinMejora = 0;
					double aux = calcularDistancia(seleccionados, matrizCostes);
					if(valorMejorSolucion < aux) {
						valorMejorSolucion = aux;
						mejorSolucion = new ArrayList<>(seleccionados);
						//Borramos la memoria a largo plazo y a corto plazo
						for (int i = 0; i < memoria.length; i++) {
							memoria[i]=0;
						}
						listaTabu.clear();
					}
				}
				
				//Reestablece los candidatos
				candidatos.addAll(randUsedList);
				randUsedList.clear();

			}
			//Reiniciar
			if(param.generateBool()) {//Intensificar
				seleccionados = masElegidos(memoria, data.getSelection());
				System.out.println("INTENSIFICANDO MEMORIA=" + Arrays.toString(memoria));
				System.out.println("INTENSIFICADO="+seleccionados);
			}else {//Diversificar
				seleccionados = menosElegidos(memoria, data.getSelection());
				System.out.println("DIVERSIFICANDO MEMORIA=" + Arrays.toString(memoria));
				System.out.println("DIVERSIFICADO="+seleccionados);
			}

			//Borramos la memoria a largo plazo y a corto plazo
			for (int i = 0; i < memoria.length; i++) {
				memoria[i]=0;
			}
			listaTabu.clear();
			
		}
		System.out.println("actualcoste:" + calcularDistancia(seleccionados, matrizCostes));
		System.out.println("mejorcoste: " + calcularDistancia(mejorSolucion, matrizCostes));
		wrt.close();
		return mejorSolucion;
	}
	

	//-----------------------------------------------------------------------------
	//								METODOS PRIVADOS
	//-----------------------------------------------------------------------------

	/**
	 * Calcula la solucion con los valores que menos veces han aparecido en una solucion
	 * @param memoria Memoria de cada elemento del problema
	 * @param nElem Numero de elementos que forman una solucion
	 * @return Lista de enteros correspondiente a los elementos de menos repeticiones
	 */
	private static ArrayList<Integer> menosElegidos(int[] memoria, int nElem) {
		ArrayList<Integer> sol = new ArrayList<>();
		while(sol.size() < nElem) {
			int peor = -1;
			int peorValor = Integer.MAX_VALUE;
			for (int i = 0; i < memoria.length; i++) {
				if(memoria[i] < peorValor && !sol.contains(i)) {
					peorValor = memoria[i];
					peor = i;
				}
			}
			sol.add(peor);
		}
		return sol;
	}
	
	/**
	 * Calcula la solucion con los valores que mas veces han aparecido en una solucion
	 * @param memoria Memoria de cada elemento del problema
	 * @param nElem Numero de elementos que forman una solucion
	 * @return Lista de enteros correspondiente a los elementos de mas repeticiones
	 */
	private static ArrayList<Integer> masElegidos(int[] memoria, int nElem) {
		ArrayList<Integer> sol = new ArrayList<>();
		while(sol.size() < nElem) {
			int mejor= -1;
			int mejorValor = Integer.MIN_VALUE;
			for (int i = 0; i < memoria.length; i++) {
				if(memoria[i] > mejorValor && !sol.contains(i)) {
					mejorValor = memoria[i];
					mejor = i;
				}
			}
			sol.add(mejor);
		}
		return sol;
	}
	
	/**
	 * Calcula la diferencia del aporte entre un elemento seleccionado y otro no seleccionado
	 * 
	 * @param seleccion Elementos seleccionados
	 * @param matriz Matriz de distancias
	 * @param elemento Elemento seleccionado a intercambiar
	 * @param candidato Elemento no seleccionado a intercambiar
	 * @return Diferencia entre la distancia del seleccionado y el no seleccionado
	 */
	private static double difIntercambio(List<Integer> seleccion, double[][] matriz, int elemento, int candidato) {
		double diferencia = 0;
		for(int i = 0; i < seleccion.size(); i++) {
			if(seleccion.get(i) != elemento) {
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
	 * @param matriz Matriz de distancias
	 * @return SortedPairArray con el valor de cada elemento
	 */
	private static SortedPairArray<Double, Integer> distancias(List<Integer> seleccion, double[][] matriz){
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
	 * @param matriz Matriz de distancias
	 * @return Distancia del conjuto
	 */
	private static double calcularDistancia(List<Integer> seleccion, double[][] matriz) {
		double suma = 0;
		for(int i = 0; i < seleccion.size(); i++) {
			for(int j = i+1; j < seleccion.size(); j++) {
				suma += valorMatriz(matriz, seleccion.get(i), seleccion.get(j));
			}
		}
		return suma;
	}
	
	/**
	 * Calcula la distancia de un elemento respecto una selección
	 * 
	 * @param elemento Elemento del que calcular la distancia
	 * @param seleccion Conjunto de elementos
	 * @param matriz Matriz de distancias
	 * @return Distancia del elemento
	 */
	private static double calcularDistanciaElemento(int elemento, List<Integer> seleccion, double[][] matriz) {
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
	private static double valorMatriz(double[][] m, int f, int c) {
		if (f < c) {
			return m[f][c];
		} else {
			return m[c][f];
		}
	}
}
