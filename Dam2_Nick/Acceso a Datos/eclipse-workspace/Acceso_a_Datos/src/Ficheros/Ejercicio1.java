package Ficheros;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ejercicio1 {

    public static void main(String[] args) {
        // 1. Leer el fichero de animes y crear el diccionario (ID -> Título)
        HashMap<Integer, String> animes = leerFicheroAnimes("animes.txt");
        
        // Lista para acumular los personajes que no tienen un anime asociado
        ArrayList<String> personajesSinAnime = new ArrayList<>();

        // 2. Por cada entrada del diccionario de animes, buscar los personajes con su mismo ID
        for (Map.Entry<Integer, String> entry : animes.entrySet()) {
            int idAnime = entry.getKey();
            String tituloAnime = entry.getValue();

            // Mostramos el título del anime
            System.out.println(tituloAnime);

            // Obtenemos la lista de personajes para este ID específico usando una función modular
            ArrayList<String> personajesDelAnime = buscarPersonajesPorId(idAnime, "personajes.txt");

            if (!personajesDelAnime.isEmpty()) {
                for (String personaje : personajesDelAnime) {
                    System.out.println("-"+ personaje);
                }
                System.out.println();
            } else {
                System.out.println("No hay personajes");
            }
            System.out.println();
        }

        // 3. Obtener los personajes que no tienen anime (códigos que no están en el fichero de animes)
        personajesSinAnime = buscarPersonajesSinAnime(animes, "personajes.txt");
        
        if (!personajesSinAnime.isEmpty()) {
            System.out.println("Personajes sin anime");
            for (String personaje : personajesSinAnime) {
                System.out.println(personaje);
            }
        }
    }

    // Función para leer el fichero de animes y devolver un HashMap<Integer, String>
    public static HashMap<Integer, String> leerFicheroAnimes(String fichero) {
        HashMap<Integer, String> mapaAnimes = new HashMap<>();
        try {
            List<String> lineas = Files.readAllLines(Path.of(fichero));
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                int espacio = linea.indexOf(" ");
                if (espacio != -1) {
                    int id = Integer.parseInt(linea.substring(0, espacio));
                    String titulo = linea.substring(espacio + 1).trim();
                    mapaAnimes.put(id, titulo);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de animes: " + e.getMessage());
        }
        return mapaAnimes;
    }

    // Función modular que recibe un código y el nombre del fichero, y devuelve la lista de personajes asociados
    public static ArrayList<String> buscarPersonajesPorId(int idBuscado, String fichero) {
        ArrayList<String> lista = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Path.of(fichero));
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                int espacio = linea.indexOf(" ");
                if (espacio != -1) {
                    int id = Integer.parseInt(linea.substring(0, espacio));
                    String nombrePersonaje = linea.substring(espacio + 1).trim();
                    
                    if (id == idBuscado) {
                        lista.add(nombrePersonaje);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el archivo de personajes: " + e.getMessage());
        }
        return lista;
    }

    // Función para encontrar personajes cuyo código no tenga una entrada en el diccionario de animes
    public static ArrayList<String> buscarPersonajesSinAnime(HashMap<Integer, String> animes, String fichero) {
        ArrayList<String> sinAnime = new ArrayList<>();
        try {
            List<String> lineas = Files.readAllLines(Path.of(fichero));
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                int espacio = linea.indexOf(" ");
                if (espacio != -1) {
                    int id = Integer.parseInt(linea.substring(0, espacio));
                    String nombrePersonaje = linea.substring(espacio + 1).trim();
                    
                    // Si el ID del personaje NO está en el mapa de animes, es un personaje sin anime
                    if (!animes.containsKey(id)) {
                        sinAnime.add(nombrePersonaje);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar personajes sin anime: " + e.getMessage());
        }
        return sinAnime;
    }
}