package p1;

import java.util.LinkedList;

/**
 * Lista circular
 * @author jcfer
 *
 * @param <T> Tipo de dato que almacena la lista
 */
public class CircularList<T> {
	private LinkedList<T> listaAux;
	private int contador;
	private int maximo;
	
	public CircularList(int tam) {
		maximo = tam;
		listaAux = new LinkedList<T>();
		for(int i = 0; i < tam; i++) {
			listaAux.add(null);
		}
	}
	
	/**
	 * Mete en la lista el elemento dado
	 * @param dato Lo que mete en la lista illo
	 */
	public void push(T dato) {
		listaAux.set(contador%maximo, dato);
		contador++;
	}
	
	/**
	 * @param c Posicion a obtener
	 * @return Objeto obtenido
	 */
	public T get(int c) {
		return listaAux.get(c);
	}
	
	
	public boolean contains(T dato) {
		return listaAux.contains(dato);
	}
	
	public void clear() {
		listaAux.clear();
		contador = 0;
		for(int i = 0; i < maximo; i++) {
			listaAux.add(null);
		}
	}
	
	@Override
	public String toString() {
		String aux = "[";
		for (int i = 0; i < listaAux.size(); i++) {
			aux+=listaAux.get(i);
			if(i<listaAux.size()-1)
				aux+=", ";
		}
		aux+="]";
		return aux;
	}
}
