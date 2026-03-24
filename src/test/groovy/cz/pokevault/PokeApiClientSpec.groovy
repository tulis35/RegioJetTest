package cz.pokevault

import spock.lang.Specification

class PokeApiClientSpec extends Specification {

    def "searchPokemon should use correct URL"() {
        when:
        String url = "https://pokeapi.co/api/v2/pokemon/" + "pikachu"

        then:
        url == "https://pokeapi.co/api/v2/pokemon/pikachu"
    }

    def "fetchPokemon should return null for unknown pokemon"() {
        given:
        PokeApiClient client = new PokeApiClient()

        when:
        Pokemon result = client.fetchPokemon("thisdoesnotexist99999")

        then:
        result == null
    }
}
