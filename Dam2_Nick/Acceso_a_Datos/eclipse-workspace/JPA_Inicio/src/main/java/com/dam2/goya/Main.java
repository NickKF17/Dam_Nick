package com.dam2.goya;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Main {
// Atributo estático para deshabilitar el logger de hibernate
//private static final Logger LOG_HIBERNATE =Logger.getLogger("org.hibernate");
public static void main(String[] args) {
// Le decimos a hibernate que solo muestre los errores y nomensajes informativos
//LOG_HIBERNATE.setLevel(Level.SEVERE);
EntityManagerFactory emf =
Persistence.createEntityManagerFactory("clasePU");
EntityManager em = emf.createEntityManager();
em.getTransaction().begin();
Producto p = new Producto("Teclado", 25.5);
em.persist(p);
em.getTransaction().commit();
System.out.println("Producto guardado con id " + p.getId());
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