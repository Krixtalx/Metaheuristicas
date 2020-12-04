package p3;

/**
 * 
 * @author jcfer
 * @implNote Clase utilizada para almacenar los datos que se leen de los
 *           archivos de datos.
 */
public class Data {
	public static double[][] valueMatrix;
	public static int size;
	public static int selection;

	public Data(int _size, int _selection) {
		size = _size;
		selection = _selection;
		valueMatrix = new double[_size][_size];
	}

	/**
	 * A�ade un valor a la matriz de valores
	 * @param f Fila
	 * @param c Columna
	 * @param v Valor
	 */
	public void addValue(int f, int c, double v) {
		valueMatrix[f][c] = v;
	}

	public int getSize() {
		return size;
	}

	public int getSelection() {
		return selection;
	}

	public double[][] getMatrix() {
		return valueMatrix;
	}

	/**
	 * Muestra una linea de la matriz por la salida estandar
	 * 
	 * @param r Linea a mostrar
	 */
	public void printRow(int r) {
		for (int i = 0; i < valueMatrix[r].length; i++) {
			System.out.println(valueMatrix[r][i] + " ");
		}
	}

	/*
	 * Muestra la matriz por la salida estandar
	 */
	public void printMatrix() {
		for (int i = 0; i < valueMatrix.length; i++) {
			printRow(i);
			System.out.println();
		}
	}
}
