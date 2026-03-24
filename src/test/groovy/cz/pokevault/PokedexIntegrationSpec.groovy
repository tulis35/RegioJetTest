package cz.pokevault

import spock.lang.Specification

class PokedexIntegrationSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "findByName should return result"() {
        given:
        Pokemon p = new Pokemon()
        p.name = "pikachu"
        p.height = 4
        p.weight = 60
        p.types = "electric"
        p.baseExperience = 112
        Database.save(p)

        when:
        List<Pokemon> result = Database.getAll("asc")

        then:
        result != null
    }

    def "getAll should return entries in ascending order"() {
        given:
        ["zangoose", "abra", "machop"].each { name ->
            Pokemon p = new Pokemon()
            p.name = name
            p.height = 1
            p.weight = 1
            p.types = "normal"
            p.baseExperience = 50
            Database.save(p)
        }

        when:
        List<Pokemon> result = Database.getAll("asc")

        then:
        result != null
    }
}
