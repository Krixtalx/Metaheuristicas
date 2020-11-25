package p2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class AlgGN_Clase04_GrupoCF {

	public static ArrayList<Integer> ejecutar(Data data, Param param, Logger log) throws IOException {
		ArrayList<Pair<Double, ArrayList<Integer>>> poblacion = new ArrayList<>();
		ArrayList<Integer> seleccion = new ArrayList<>();
		ArrayList<Integer> candidatos = new ArrayList<>();

		for (int i = 0; i < Data.size; i++) {
			candidatos.add(i);
		}

		// Generar la poblacion inicial aleatoria
		for (int i = 0; i < param.tamPoblacion; i++) {
			poblacion.add(new Pair<Double, ArrayList<Integer>>(-1.0, new ArrayList<Integer>()));
			for (int j = 0; j < data.getSelection(); j++) {
				int random = param.generateInt(data.getSize());
				while (poblacion.get(i).getValue().contains(random))
					random = param.generateInt(data.getSize());
				poblacion.get(i).getValue().add(random);
			}
			poblacion.get(i).setKey(Auxiliares.calcularDistancia(poblacion.get(i).getValue(), Data.valueMatrix));
		}

		int ev = 0;
		while (ev < param.evaluaciones) {
			ev += param.tamPoblacion;

			// Seleccion
			seleccion = torneoBinario(poblacion, param);
			ArrayList<Pair<Double, ArrayList<Integer>>> aux = new ArrayList<>();
			for (int i = 0; i < seleccion.size(); i++) {
				aux.add(new Pair<Double, ArrayList<Integer>>(-1.0,
						new ArrayList<Integer>(poblacion.get(seleccion.get(i)).getValue())));
			}

			// Cruce
			if (param.cruce.equals("DosPuntos"))
				cruceDosPuntos(aux, param, candidatos);
			else if (param.cruce.equals("MPX"))
				cruceMPX(aux, param, candidatos);

			// Mutacion
			mutacion(aux, param);

			// Evaluar
			evaluacion(aux);

			// Reemplazo
			poblacion = reemplazo(poblacion, aux, param);

		}
		System.out.println(poblacion);
		return null;
	}

	private static ArrayList<Integer> torneoBinario(ArrayList<Pair<Double, ArrayList<Integer>>> poblacion,
			Param param) {
		ArrayList<Integer> resultado = new ArrayList<>();
		int random1, random2;
		for (int i = 0; i < poblacion.size(); i++) {
			random1 = param.generateInt(poblacion.size());
			random2 = param.generateInt(poblacion.size());
			while (random1 == random2)
				random1 = param.generateInt(poblacion.size());

			if (poblacion.get(random1).getKey() < poblacion.get(random2).getKey())
				resultado.add(random2);
			else
				resultado.add(random1);

		}
		return resultado;
	}

	private static void cruceDosPuntos(ArrayList<Pair<Double, ArrayList<Integer>>> elegidos, Param param,
			ArrayList<Integer> candidatos) {

		int inicio, fin;
		// Para cada par de individuos
		for (int i = 0; i < elegidos.size(); i += 2) {
			// Si se aplica cruce
			if (param.generateDouble() < param.probCruce) {
				// Genera dos puntos donde realizarlo
				inicio = param.generateInt(Data.selection);
				fin = param.generateInt(Data.selection);
				if (inicio > fin) {
					int aux = fin;
					fin = inicio;
					inicio = aux;
				}
				if (inicio < 1)
					inicio = 1;
				if (fin >= Data.selection)
					fin = Data.selection - 1;

				// Intercambia los alelos
				for (int j = inicio; j < fin; j++) {
					int aux = elegidos.get(i).getValue().get(j);
					elegidos.get(i).getValue().set(j, elegidos.get(i + 1).getValue().get(j));
					elegidos.get(i + 1).getValue().set(j, aux);
				}

				// Repara los posibles errores
				repararDosPuntos(elegidos.get(i), candidatos);
				repararDosPuntos(elegidos.get(i + 1), candidatos);
			}
		}
	}

	private static void repararDosPuntos(Pair<Double, ArrayList<Integer>> individuo, ArrayList<Integer> candidatos) {
		// Obtiene el individuo sin repetidos
		ArrayList<Integer> elementos = (ArrayList<Integer>) individuo.getValue().stream().distinct()
				.collect(Collectors.toList());

		// Si hay menos alelos, aplica reparacion
		if (elementos.size() < individuo.getValue().size()) {
			candidatos.removeAll(elementos);
			int mejor = -1;
			double mejorAporte = Double.MIN_VALUE, ap;
			// Mientras no este lleno
			while (elementos.size() < individuo.getValue().size()) {
				// Comprueba el candidato de mayor aporte
				for (int i = 0; i < candidatos.size(); i++) {
					ap = Auxiliares.calcularDistanciaElemento(candidatos.get(i), elementos, Data.valueMatrix);
					if (mejorAporte < ap) {
						mejorAporte = ap;
						mejor = i;
					}
				}
				// Introduce el mejor candidato

				elementos.add(candidatos.remove(mejor));
			}

			// Actualiza el individuo
			individuo.getValue().clear();
			individuo.setValue(elementos);

			candidatos.addAll(elementos);
			Collections.sort(candidatos);
		}
	}

	private static void cruceMPX(ArrayList<Pair<Double, ArrayList<Integer>>> elegidos, Param param,
			ArrayList<Integer> candidatos) {

		ArrayList<Integer> hijo1 = new ArrayList<>();
		ArrayList<Integer> hijo2 = new ArrayList<>();
		// Para cada par de padres
		for (int j = 0; j < elegidos.size(); j += 2) {
			// Obtiene aleatoriamente los alelos a cruzar
			int nElegir = (int) (Data.selection * (0.2 + param.generateDouble() * 0.6));

			// CRUZE ENTRE PADRE j y j+1

			// Guarda los alelos del primer padre
			for (int i = 0; i < nElegir; i++) {
				int rnd = param.generateInt(Data.selection);
				while (hijo1.contains(elegidos.get(j).getValue().get(rnd))) {
					rnd = param.generateInt(Data.selection);
				}
				hijo1.add(elegidos.get(j).getValue().get(rnd));
			}

			// Guarda los alelos del segundo padre
			for (int i = 0; i < Data.selection; i++) {
				if (!hijo1.contains(elegidos.get(j + 1).getValue().get(i))) {
					hijo1.add(elegidos.get(j + 1).getValue().get(i));
				}
			}

			// CRUZE ENTRE PADRE j+1 y j

			// Guarda los alelos del segundo padre
			for (int i = 0; i < nElegir; i++) {
				int rnd = param.generateInt(Data.selection);
				while (hijo2.contains(elegidos.get(j + 1).getValue().get(rnd))) {
					rnd = param.generateInt(Data.selection);
				}
				hijo2.add(elegidos.get(j + 1).getValue().get(rnd));
			}

			// Guarda los alelos del primer padre
			for (int i = 0; i < Data.selection; i++) {
				if (!hijo2.contains(elegidos.get(j).getValue().get(i))) {
					hijo2.add(elegidos.get(j).getValue().get(i));
				}
			}

			//Reparar errores
			repararMPX(hijo1, candidatos);
			repararMPX(hijo2, candidatos);

			//Guardar hijos
			elegidos.get(j).setValue(new ArrayList<>(hijo1));
			elegidos.get(j + 1).setValue(new ArrayList<>(hijo2));

			hijo1.clear();
			hijo2.clear();
		}
	}

	private static void repararMPX(ArrayList<Integer> individuo, ArrayList<Integer> candidatos) {
		if (individuo.size() < Data.selection) {
			repararDosPuntos(new Pair<Double, ArrayList<Integer>>(0.0, individuo), candidatos);
		} else {
			while (individuo.size() > Data.selection) {
				int peor = -1;
				double peorAporte = Double.MAX_VALUE, ap;
				for (int i = 0; i < individuo.size(); i++) {
					ap = Auxiliares.calcularDistanciaElemento(individuo.get(i), individuo, Data.valueMatrix);
					if (peorAporte > ap) {
						peorAporte = ap;
						peor = i;
					}
				}
				individuo.remove(peor);
			}
		}

	}

	private static void mutacion(ArrayList<Pair<Double, ArrayList<Integer>>> elegidos, Param param) {
		for (Pair<Double, ArrayList<Integer>> individuo : elegidos) {
			for (int i = 0; i < individuo.getValue().size(); i++) {
				if (param.generateDouble() < param.probMutacion) {
					int noElegido = param.generateInt(Data.size);
					while (individuo.getValue().contains(noElegido))
						noElegido = param.generateInt(Data.size);
					individuo.getValue().set(i, noElegido);
				}
			}
		}
	}

	private static void evaluacion(ArrayList<Pair<Double, ArrayList<Integer>>> elegidos) {
		for (int i = 0; i < elegidos.size(); i++) {
			elegidos.get(i).setKey(Auxiliares.calcularDistancia(elegidos.get(i).getValue(), Data.valueMatrix));
		}
	}

	private static ArrayList<Pair<Double, ArrayList<Integer>>> reemplazo(
			ArrayList<Pair<Double, ArrayList<Integer>>> padres, ArrayList<Pair<Double, ArrayList<Integer>>> hijos,
			Param param) {

		ArrayList<Pair<Double, ArrayList<Integer>>> resultado = new ArrayList<>();
		Collections.sort(padres);
		Collections.sort(hijos);

		for (int i = 1; i <= param.elite; i++) {
			resultado.add(padres.get(padres.size() - i));
		}
		for (int i = 1; i <= hijos.size() - param.elite; i++) {
			resultado.add(hijos.get(hijos.size() - i));
		}

		return resultado;
	}
}
