package AgendaJson;


public class Contacto {
private String nombre;
private String dni;
private String telefono;
public Contacto(String n ,String dni,String tel){
	this.nombre=n;
	this.dni=dni;
	this.telefono=tel;
}
public  void mostrar() {
	System.out.println("Nombre: "+ this.nombre);
	System.out.println("Telefono: "+this.telefono);
	System.out.println("DNI: "+this.dni);
	System.out.println();
	
}

}
