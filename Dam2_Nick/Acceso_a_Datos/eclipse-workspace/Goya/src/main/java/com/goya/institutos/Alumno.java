package com.goya.institutos;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "Alumnos")
public class Alumno {
@Id //Le dice que es clave primaria
@GeneratedValue(strategy = GenerationType.IDENTITY) //Hace el id autoincrementable
private Long id;
private String DNI;
//@Column(name = "producto", length = 100, nullable = false, unique = true)
private String nombre;
private String apellidos;
private int edad;
private double matricula;
// JPA exige siempre un constructor sin argumentos
public Alumno() {
}
public Alumno(String DNI,String nombre,String apellidos,int edad,double matricula) {
this.DNI = DNI;
this.nombre=nombre;
this.apellidos=apellidos;
this.edad=edad;
this.matricula=matricula;
}
public Long getId() {
return id;
}
public String getDNI() {
	return DNI;
}
public String getNombre() {
return nombre;
}
public String getApellidos() {
	return apellidos;
}
public int getEdad() {
	return edad;
}
public double getMatricula() {
	return matricula;
}
@Override
public String toString() {
	String salida="";
	salida="("+id+") Nombre: "+nombre+" "+apellidos+"\nEdad: "+edad+" Pago de Matricula: "+matricula;
	return salida;
}
}