package cz.pokevault

import spock.lang.Specification

class DatabaseSpec extends Specification {

    def setup() {
        Database.init()
        Pokemon p = new Pokemon()
        p.name = "bulbasaur"
        p.height = 7
        p.weight = 69
        p.types = "grass,poison"
        p.baseExperience = 64
        Database.save(p)
    }

    def "findById should return correct pokemon"() {
        when:
        Pokemon result = Database.findById(1)

        then:
        result != null
        result.name == "bulbasaur"
    }
}
