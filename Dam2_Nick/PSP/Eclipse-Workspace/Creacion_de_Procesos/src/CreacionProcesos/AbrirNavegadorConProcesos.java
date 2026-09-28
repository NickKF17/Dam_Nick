package CreacionProcesos;

import java.io.File;
import java.io.IOException;

public class AbrirNavegadorConProcesos {
	static int retorno =-2;//En caso de que se haya ejecutado correctamente devolverá 0
	public static void main(String[] args) throws IOException, InterruptedException {
		ProcessBuilder pb=new ProcessBuilder("firefox","spotify.com");

		Process p = pb.start();
	
		retorno = p.waitFor();

	        System.out.println("Llegamos aquí cuando la ejecución del proceso finaliza");
	        System.out.println("La ejecución devuelve: " + retorno);
	}

}
