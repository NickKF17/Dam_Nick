package Ficheros;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Ejercicio_Coordenadas {
	final static String ruta ="coordenadas.dat";
	public static void main(String[] args) {
		final int TAMANYO_REGISTRO=20;
		try(DataInputStream fichero= new DataInputStream(new FileInputStream(ruta))){
			File ficheroFisico=new File(ruta);
			final int NUM_REGISTROS=(int)ficheroFisico.length()/TAMANYO_REGISTRO;
			System.out.println("Satelites y coordenadas");
			for(int i=0;i<NUM_REGISTROS;i++) {
				int id=fichero.readInt();
			
					float latitud =fichero.readFloat();
					float longitud=fichero.readFloat();
					String estado="";
					for(int j=0;j<4;j++)
						estado+=fichero.readChar();
					System.out.printf("Satelite ID: %d | Posicion: (%.4f,%.4f) | Estado: %s\n",id,latitud,longitud,estado);
			}
			
		}catch(Exception e){
			
		}
}
}
