package p1;

import java.util.ArrayList;
import java.util.Map.Entry;

public class SortedPairArray<T extends Comparable<T>, U> {
	
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
	
	public SortedPairArray() {
		list = new ArrayList<Pair<T, U>>();
	}
	
	public void put(T key, U value) {
		Pair<T, U> pair = new Pair<T, U>(key, value);
		for(int i = 0; i < list.size(); i++) {
			if(pair.compareTo(list.get(i)) < 0) {
				list.add(i, pair);
				return;
			}
		}
		list.add(pair);
	}
	
	public Pair<T, U> remove(T key) {
		Pair<T, U> compPair = new Pair<T, U>(key, null);
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).compareTo(compPair) == 0) {
				return list.remove(i);
			}
		}
		return null;
	}
	
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
