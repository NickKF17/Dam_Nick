import java.util.List;
import com.google.gson.annotations.SerializedName;

// Raíz del JSON de pokemons (equivalente a Agenda en la agenda)
public class Pokedex {
	@SerializedName("pokedex")
	private List<PokemonDex> pokemons;

	public List<PokemonDex> getPokemons() {
		return pokemons;
	}

	public void setPokemons(List<PokemonDex> pokemons) {
		this.pokemons = pokemons;
	}
}
