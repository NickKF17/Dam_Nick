import java.io.BufferedReader;
import java.io.FileReader;

public class Ejercicio05 {

	public static void main(String[] args) {
		String fichero = "datos/estadisticas.txt"; // Ruta fácil de cambiar
		int hombres = 0;
		int mujeres = 0;
		double sumaAlturas = 0;
		int personas = 0;
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String sexo;
			// Cada persona ocupa dos líneas: primero el sexo y luego la altura.
			// Leemos el sexo en la condición del while; si es null, se acabó el fichero
			while ((sexo = lector.readLine()) != null) {
				String altura = lector.readLine(); // Segunda línea del par
				if (sexo.equals("Hombre"))
					hombres++;
				else
					mujeres++; // El formato es correcto, así que lo que no es Hombre es Mujer
				sumaAlturas += Double.parseDouble(altura); // Convertimos el texto a número
				personas++;
			}
			System.out.println("Hombres: " + hombres + ".");
			System.out.println("Mujeres: " + mujeres + ".");
			if (personas > 0) { // Evitamos dividir entre cero con un fichero vacío
				double media = sumaAlturas / personas;
				// Redondeo a 2 decimales: multiplicar por 100, redondear y volver a dividir
				media = Math.round(media * 100) / 100.0;
				System.out.println("Estatura media: " + media);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
