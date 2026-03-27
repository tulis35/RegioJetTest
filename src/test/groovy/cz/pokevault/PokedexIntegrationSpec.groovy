package cz.pokevault

import spock.lang.Specification

class PokedexIntegrationSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "findByName should return result"() {
        given:
        Pokemon p = new Pokemon()
        p.setName("pikachu")
        p.setHeight(4)
        p.setWeight(60)
        p.setTypes("electric")
        p.setBaseExperience(112)
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
            p.setName(name)
            p.setHeight(1)
            p.setWeight(1)
            p.setTypes("normal")
            p.setBaseExperience(50)
            Database.save(p)
        }

        when:
        List<Pokemon> result = Database.getAll("asc")

        then:
        result != null
    }
}
