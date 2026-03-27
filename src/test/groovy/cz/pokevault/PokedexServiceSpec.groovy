package cz.pokevault

import spock.lang.Specification

class PokedexServiceSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "adding a pokemon should make it appear in the pokedex"() {
        given:
        Pokemon pokemon = new Pokemon()
        pokemon.setName("bulbasaur")
        pokemon.setHeight(7)
        pokemon.setWeight(69)
        pokemon.setTypes("grass,poison")
        pokemon.setBaseExperience(64)

        when:
        Database.save(pokemon)
        List<Pokemon> result = Database.getAll("asc")

        then:
        result.any { it.getName() == "bulbasaur" }
    }

    def "removing a pokemon should remove it from the pokedex"() {
        given:
        Pokemon pokemon = new Pokemon()
        pokemon.setName("squirtle")
        pokemon.setHeight(5)
        pokemon.setWeight(90)
        pokemon.setTypes("water")
        pokemon.setBaseExperience(63)
        Database.save(pokemon)

        when:
        Database.remove("squirtle")
        List<Pokemon> result = Database.getAll("asc")

        then:
        !result.any { it.getName() == "squirtle" }
    }
}
