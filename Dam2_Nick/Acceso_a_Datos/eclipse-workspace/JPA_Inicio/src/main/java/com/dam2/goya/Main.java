package com.dam2.goya;
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
//em.getTransaction().begin();//Este y commit solo si hacemos cambios dentro de la base de datos
String jpql="From Producto p Where p.precio > :precioMax";
	List<Producto> lista =em.createQuery(jpql,Producto.class).setParameter("precioMax", 30).getResultList();
	for(Producto p:lista)
		System.out.println(p);
	
	System.out.println("Total de productos de la tabla: "+lista.size());
/*
	Producto p = new Producto("Teclado", 25.5);
em.persist(p);
p = new Producto("Cascos", 35.5);
em.persist(p);
p = new Producto("Mouse", 20.5);
em.persist(p);
em.persist(new Producto("Alfomobrilla", 30.5)); //Asi tambien se puede
*/
//em.getTransaction().commit();
//System.out.println("Producto guardado con id " + p.getId());
}catch(Exception e){
	System.out.println("Error: " + e.getMessage());
	em.getTransaction().rollback();
}
em.close();
emf.close();
/*
//Para insertar un registro ya lo hemos visto antes:
Producto p2 = new Producto("Teclado", 25.5);
em.persist(p2);
//Para modificar un producto (update). En el siguiente ejemplo primero lo buscamos por clave primaria y luego lo modificamos:
Producto p3 = em.find(Producto.class, 1L);
if (p3 != null)
p3.setPrecio(29.9);
//Para eliminar un producto, igualmente primero lo buscamos:
Producto p4 = em.find(Producto.class, 1L);
if (p4 != null)
em.remove(p4);
/*Después de cualquiera de estos cambios hay que hacer commit, no lo olvides, pero fíjate que sólo
hace falta ejecutar persist() con los objetos nuevos. Los objetos ya existentes que encontramos con
find() están ya “gestionados” por hibernate y cuando hacemos commit se transmiten los cambios a
las tablas, ya sean updates o deletes
/*
*/
}


}