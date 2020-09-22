package p1;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String fileName = "GKD-c_1_n500_m50";
		String extension = ".txt";
		Reader params = new Reader(fileName, "", extension);
		float[][] matriz = new float[10][10];
		Integer size = new Integer(0);
		Integer numberElements = new Integer(0);
		try {
			matriz = params.txtToMatrix(size, numberElements);
		} catch (IOException e) {
			System.out.println("Could not read file: " + e.getMessage());
		}

		for (int i = 0; i < matriz.length; i++) {
			for (int j = i + 1; j < matriz.length; j++) {
				System.out.print(matriz[i][j] + " ");
			}
			System.out.println();
		}
	}
}
