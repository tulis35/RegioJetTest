package cz.pokevault

import spock.lang.Specification

class DatabaseSpec extends Specification {

    def setup() {
        Database.init()
        Pokemon p = new Pokemon()
        p.setName("bulbasaur")
        p.setHeight(7)
        p.setWeight(69)
        p.setTypes("grass,poison")
        p.setBaseExperience(64)
        Database.save(p)
    }

    def "findByName should return correct pokemon"() {
        when:
        Pokemon result = Database.findByName("bulbasaur")

        then:
        result != null
        result.getName() == "bulbasaur"
    }
}
