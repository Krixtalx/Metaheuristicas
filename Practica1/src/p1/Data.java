package p1;

public class Data {
	private float[][] valueMatrix;
	private int size;
	private int selection;
	
	public Data(int _size, int _selection) {
		size = _size;
		selection = _selection;
		valueMatrix = new float[_size][_size];
	}
	
	public void addValue(int f, int c, float v) {
		valueMatrix[f][c] = v;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getSelection() {
		return selection;
	}
	
	public float[][] getMatrix(){
		return valueMatrix;
	}
	
	public void printRow(int r) {
		for(int i = 0; i < valueMatrix[r].length; i++) {
			System.out.println(valueMatrix[r][i] + " ");
		}
	}
	
	public void printMatrix() {
		for(int i = 0; i < valueMatrix.length; i++) {
			printRow(i);
			System.out.println();
		}
	}
}
