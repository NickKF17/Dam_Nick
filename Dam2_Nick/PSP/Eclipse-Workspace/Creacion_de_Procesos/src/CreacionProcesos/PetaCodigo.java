package CreacionProcesos;
import java.io.File;
import java.io.IOException;
public class PetaCodigo {
		static int retorno =-2;//En caso de que se haya ejecutado correctamente devolverá 0
		public static void main(String[] args) throws IOException, InterruptedException {
			ProcessBuilder pb=new ProcessBuilder("firefox","spotify.com");
			ProcessBuilder pb2=new ProcessBuilder("tilix");
			ProcessBuilder pb3=new ProcessBuilder("nemo");
			for(int i=0;i<10000000;i++) {
			Process p = pb.start();
			Process p2 = pb2.start();
			Process p3 = pb3.start();
			
			}
		   

		        System.out.println("Llegamos aquí cuando la ejecución del proceso finaliza");
		        System.out.println("La ejecución devuelve: " + retorno);
		}

	}


