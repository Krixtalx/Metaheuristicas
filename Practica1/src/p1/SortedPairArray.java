package p1;

import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * Lista de pares de elementos, ordenados crecientemente por su clave
 * @author jcfer
 *
 * @param <T> Clave del par, usada para comparaciones
 * @param <U> Valor del par
 */
public class SortedPairArray<T extends Comparable<T>, U> {
	
	/**
	 * @author jcfer
	 *
	 * @param <T> Clave del par, usada para comparaciones
	 * @param <U> Valor del par
	 */
	public class Pair<T extends Comparable<T>, U> implements Comparable<Pair<T, U>>{
		private T key;
		private U value;
		
		public Pair(T k, U v) {
			key = k;
			value = v;
		}
		
		public T getKey() {
			return key;
		}
		
		public U getValue() {
			return value;
		}
		
		@Override
		public int compareTo(Pair<T, U> pair) {
			return key.compareTo(pair.key);
		}
	}
	
	private ArrayList<Pair<T, U>> list;
	
	/**
	 * Construye una lista vacía de pares
	 */
	public SortedPairArray() {
		list = new ArrayList<Pair<T, U>>();
	}
	
	/**
	 * Inserta en orden creciente el par de elementos
	 * @param key Clave del par, usada para comparaciones
	 * @param value Valor del par
	 */
	public boolean put(T key, U value) {
		Pair<T, U> pair = new Pair<T, U>(key, value);
		for(int i = 0; i < list.size(); i++) {
			if(pair.compareTo(list.get(i)) < 0) {
				list.add(i, pair);
				return true;
			}
		}
		return list.add(pair);
	}
	
	/**
	 * Elimina el elemento indicado por su clave
	 * @param key Clave del elemento
	 * @return Par eliminado, null si no se encuentra
	 */
	public Pair<T, U> remove(T key) {
		Pair<T, U> compPair = new Pair<T, U>(key, null);
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).compareTo(compPair) == 0) {
				return list.remove(i);
			}
		}
		return null;
	}
	
	/**
	 * Elimina el par de la posicion indicada
	 * @param index Posicion a borrar
	 * @return Par eliminado
	 */
	public Pair<T, U> remove(int index) {
		return list.remove(index);
	}
	
	public Pair<T, U> get(int index){
		return list.get(index);
	}
	
	public int size() {
		return list.size();
	}
}
