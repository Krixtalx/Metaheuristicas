package p1;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String fileName = "parameters";
		String extension = ".txt";
		Reader params = new Reader(fileName, "", extension);
		try {
			Param input = params.readParam();
			System.out.println(input.getAlgoritmo());
		} catch (IOException e) {
			System.out.println("No se puedo leer el fichero	: " + e.getMessage());
		}
	}
}
