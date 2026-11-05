package com.goya.institutos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Main {
// Atributo estático para deshabilitar el logger de hibernate
private static final Logger LOG_HIBERNATE =Logger.getLogger("org.hibernate");
public static void main(String[] args) {
// Le decimos a hibernate que solo muestre los errores y nomensajes informativos
LOG_HIBERNATE.setLevel(Level.SEVERE);
EntityManagerFactory emf =
Persistence.createEntityManagerFactory("clasePU");
EntityManager em = emf.createEntityManager();
try {

String jpql="From Alumno";
	List<Alumno> lista =em.createQuery(jpql,Alumno.class).getResultList();
	for(Alumno p:lista)
		System.out.println(p);
	
	System.out.println("Total de Alumnos: "+lista.size());

}catch(Exception e){
	System.out.println("Error: " + e.getMessage());
	em.getTransaction().rollback();
}
em.close();
emf.close();

}


}