package cz.pokevault

import spock.lang.Specification

class PokedexServiceSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "adding a pokemon should make it appear in the pokedex"() {
        given:
        Pokemon pokemon = new Pokemon()
        pokemon.name = "bulbasaur"
        pokemon.height = 7
        pokemon.weight = 69
        pokemon.types = "grass,poison"
        pokemon.baseExperience = 64

        when:
        Database.save(pokemon)
        List<Pokemon> result = Database.getAll("asc")

        then:
        result.any { it.name == "bulbasaur" }
    }

    def "removing a pokemon should remove it from the pokedex"() {
        given:
        Pokemon pokemon = new Pokemon()
        pokemon.name = "squirtle"
        pokemon.height = 5
        pokemon.weight = 90
        pokemon.types = "water"
        pokemon.baseExperience = 63
        Database.save(pokemon)

        when:
        Database.remove("squirtle")
        List<Pokemon> result = Database.getAll("asc")

        then:
        !result.any { it.name == "squirtle" }
    }
}
