package p2;

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
	
	public void setKey(T newKey) {
		key = newKey;
	}
	
	public void setValue(U newVal) {
		value = newVal;
	}
	
	@Override
	public int compareTo(Pair<T, U> pair) {
		return key.compareTo(pair.key);
	}
	
	@Override
	public String toString() {
		return "<"+key.toString()+",	"+value.toString()+">\n";
	}
}
