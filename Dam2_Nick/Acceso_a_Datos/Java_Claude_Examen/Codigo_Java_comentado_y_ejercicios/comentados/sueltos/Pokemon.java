package tercerTrimestre;

// Serializable: interfaz 'marcadora' (sin métodos) que autoriza a guardar el objeto en binario.
import java.io.Serializable;

// Para poder grabar los objetos en un fichero y darles persistencia tienen que ser Serializables
// Para poder comparar tiene que ser Comparable
// Pokemon es Serializable (para ObjectOutputStream) y Comparable<Pokemon> (para poder ordenarlos).
public class Pokemon implements Serializable,  Comparable<Pokemon> {
	// Número de la Pokedex.
	private int codigo;
	// Nombre.
	private String nombre;
	// Array de 2 posiciones: tipo principal y secundario (puede quedar null).
	private String[] tipo = new String[2];
	// Puntos de vida. protected: visible también en las clases hijas (PokemonLegendario).
	protected int pv;
	// Referencia a otro Pokemon (su evolución). Al serializar, Java guarda también ese objeto.
	private Pokemon evolucion = null;
	
	// Los datos estáticos, si los hubiera, no se guardan
	// Si quisiéramos que algún atributo no se guardara (por tener información sensible, por ejemplo)
	// lo definimos como transient:
	
	// private transient String password;

	// Constructor para pokemon de UN tipo. El tipo[1] queda null.
	public Pokemon(int c, String n, String t) {
		// this.atributo = parámetro: this distingue el atributo del parámetro.
		this.codigo = c;
		this.nombre = n;
		this.tipo[0] = t;
		// Math.random() da un decimal en [0,1). *51 -> [0,51), +50 -> [50,101), el cast a int trunca: PV entre 50 y 100.
		this.pv = (int) ((Math.random() * 51) + 50);
	}

	// Constructor sobrecargado para pokemon de DOS tipos (mismo nombre, distintos parámetros).
	public Pokemon(int c, String n, String t1, String t2) {
		this.codigo = c;
		this.nombre = n;
		this.tipo[0] = t1;
		this.tipo[1] = t2;
		this.pv = (int) ((Math.random() * 51) + 50);
	}

	// Muestra la ficha completa por consola.
	public void mostrar() {
		System.out.println("------------------");
		System.out.println(this.codigo + " - " + this.nombre);
		// Si no hay segundo tipo, se imprime en singular.
		if (this.tipo[1] == null)
			System.out.println("Tipo: " + this.tipo[0]);
		else
			System.out.println("Tipos: " + this.tipo[0] + ", " + this.tipo[1]);
		// Solo se muestra la evolución si existe.
		if (this.evolucion != null)
			System.out.println("Evoluciona en: " + this.evolucion.nombre);
		System.out.println("PV: " + this.pv);
		System.out.println("------------------");
	}
	
	// El decorador Override no es obligatorio pero si muy recomendado
	 // @Override indica que sobrescribimos un método heredado de Object; el compilador avisa si nos equivocamos de nombre.
	 @Override
	 // toString() se llama automáticamente al imprimir el objeto (System.out.println(pokemon)).
	 public String toString() {
		 // Construimos el texto concatenando.
		 String texto = "("+ this.codigo + ") " + this.nombre + "\n";
		 if(this.tipo[1]== null)
			 texto += "Tipo: " + this.tipo[0];
		 else
			 texto += "Tipos: " + this.tipo[0] + " / " + this.tipo[1];
		 texto +="\n-----------";
		 return texto;
	 }
	 
	 @Override
	 // equals define cuándo dos pokemon son 'iguales'.
	 public boolean equals(Object otro) {
		 // Por defecto, distintos.
		 boolean iguales = false;
		 // NOTA del mentor: este cast lanza ClassCastException si 'otro' no es Pokemon, y NullPointerException si es null. Un equals robusto comprobaría antes con instanceof.
		 Pokemon comparado = (Pokemon)otro;
		 // NOTA del mentor: con Strings hay que comparar con .equals(), no con ==. El == compara referencias y solo funciona por casualidad cuando ambos apuntan al mismo String.
		 if(this.nombre == comparado.nombre)
			 iguales = true;
		 return iguales;
	 }
	 
	 @Override
	 // compareTo define el orden natural: devuelve positivo si this va después, negativo si va antes, 0 si empatan.
	 public int compareTo(Pokemon otro) {
		 // Empate por defecto.
		 int devolver = 0;
		 // Ordenamos por número de Pokedex.
		 if(this.codigo > otro.codigo)
		 	devolver = 1;
		 else if(this.codigo < otro.codigo)
		 	devolver = -1;
		 return devolver;
	 }

	// Setter: asigna la evolución.
	public void setEvolucion(Pokemon p) {
		this.evolucion = p;
	}

	// Devuelve la evolución, o él mismo si no puede evolucionar.
	public Pokemon evoluciona() {
		// this = el objeto actual.
		Pokemon pokemon = this;
		if (this.evolucion == null)
			System.out.println("Este pokemon no sabe evolucionar");
		else
			pokemon = this.evolucion;
		return pokemon;
	}

	// Simula un combate por turnos. Devuelve true si gana el atacante (this).
	public boolean combateContra(Pokemon atacado) {
		// Bandera de fin de combate.
		boolean combateTerminado;
		// Resultado por defecto.
		boolean vencedor = true;
		// Un pokemon debilitado no puede pelear.
		if (this.pv <= 0 || atacado.pv <= 0)
			System.out.println("Un pokemon sin PV no puede combatir");
		else {
			// Se repiten turnos hasta que alguien se quede sin PV.
			do {
				// Daño aleatorio entre 25 y 75.
				int danyo = (int) ((Math.random() * 51) + 25);
				// Primero ataca el atacante; restamos PV al atacado.
				atacado.pv -= danyo;
				// Si sigue vivo, contraataca.
				if (atacado.pv > 0) {
					danyo = (int) ((Math.random() * 51) + 25);
					this.pv -= danyo;
					if (this.pv > 0)
						combateTerminado = false;
					else {
						vencedor = false;
						System.out.println(this.nombre + " ha sido derrotado");
						combateTerminado = true;
					}
				} else {
					vencedor = true;
					System.out.println(atacado.nombre + " ha sido derrotado");
					combateTerminado = true;
				}
			// Mientras no haya terminado, otro turno.
			} while (combateTerminado == false);
		}
		// True si gana el atacante, false si gana el atacado		
		// Resultado final.
		return vencedor;
	}

}

// En las clases hijas no hace falta poner que es serializable. Si la clase Padre lo es, también lo son todas sus hijas
// Herencia: PokemonLegendario es un Pokemon con PV mayores. Es Serializable automáticamente por heredar.
class PokemonLegendario extends Pokemon{
	// Constructor de la hija.
	public PokemonLegendario(int c, String n, String t) {
		// super(...) llama al constructor del padre (debe ser la primera línea).
		super(c, n, t);
		// Los legendarios tienen entre 100 y 300 PV; sobrescribe el valor que puso el padre.
		this.pv = (int) ((Math.random() * 201) + 100);
	}
	
	// Sobrescribe mostrar() del padre.
	public void mostrar() {
		System.out.println("------------------");
		System.out.println("*** LEGENDARIO ***");
		// Reutiliza el mostrar() del padre en lugar de repetir código.
		super.mostrar();
	}
}
