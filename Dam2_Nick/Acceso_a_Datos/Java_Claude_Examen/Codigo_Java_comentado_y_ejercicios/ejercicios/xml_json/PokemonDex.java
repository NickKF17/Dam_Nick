// Un pokemon del fichero de texto. Se usa tanto para el XML como para el JSON (Gson lee sus atributos)
public class PokemonDex {
	private int codigo;
	private String nombre;
	private double peso;
	private double altura;
	// Los uno o dos tipos, en una sola cadena: "Bicho, Veneno"
	private String tipo;

	public PokemonDex(int codigo, String nombre, double peso, double altura, String tipo) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.peso = peso;
		this.altura = altura;
		this.tipo = tipo;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public double getPeso() {
		return this.peso;
	}

	public double getAltura() {
		return this.altura;
	}

	public String getTipo() {
		return this.tipo;
	}
}
