package p2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

/**
 * Clase para la generaci�n de ficheros de historial de ejecuci�n
 * @author Jose Antonio
 *
 */
public class Logger {
	String fileName;
	BufferedWriter escritor;
	Instant inicio;
	Instant fin;
	/**
	 * Activa o desactiva la escritura
	 */
	boolean on = true;

	public Logger(Param parametros) {
		fileName = "log-"+ parametros.seed + "-" + parametros.cruce + "-" + parametros.dataFile + ".txt";
		File fichero = new File(fileName);
		FileWriter out = null;
		try {
			out = new FileWriter(fichero);
		} catch (IOException e) {
			e.printStackTrace();
		}
		escritor = new BufferedWriter(out);
	}

	public void write(String text) throws IOException {
		if (on) {
			escritor.write(text);
			escritor.write('\n');
		}
	}

	public void nextIteration() throws IOException {
		if (on) {
			escritor.write('\n');
			escritor.write("========================================================");
			escritor.write('\n');
		}
	}

	public void close() throws IOException {
		escritor.close();
	}

	public void startTimer() {
		inicio = Instant.now();
	}

	public void endTimer() {
		fin = Instant.now();
	}

	public Duration getDuration() {
		return Duration.between(inicio, fin);
	}
}
