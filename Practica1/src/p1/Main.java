package p1;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String fileName = "parameters";
		String extension = ".txt";
		Parameters params = new Parameters(fileName, "", extension);
		try {
			params.ReadFile();
		}catch (IOException e) {
			System.out.println("Could not read file: " + e.getMessage());
		}
	}
}
