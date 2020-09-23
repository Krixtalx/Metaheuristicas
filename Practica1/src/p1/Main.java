package p1;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String fileName = "GKD-c_1_n500_m50";
		String extension = ".txt";
		Reader params = new Reader(fileName, "", extension);
		try {
			Data input = params.txtToMatrixBR();
			System.out.println("size=" + input.getSize() + "\nselection=" + input.getSelection());
			input.printRow(1);
		} catch (IOException e) {
			System.out.println("Could not read file: " + e.getMessage());
		}
	}
}
