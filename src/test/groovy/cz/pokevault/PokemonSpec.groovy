package cz.pokevault

import spock.lang.Specification

class PokemonSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "adding a pokemon should not throw"() {
        given:
        Pokemon p = new Pokemon()
        p.name = "charmander"
        p.height = 6
        p.weight = 85
        p.types = "fire"
        p.baseExperience = 62

        when:
        Database.save(p)

        then:
        noExceptionThrown()
    }

    // def "adding a pokemon with null name should fail gracefully"() { ... }

    // def "adding a pokemon with empty name should fail gracefully"() { ... }

    // def "adding the same pokemon twice should not create duplicates"() { ... }

    // def "removing a pokemon that does not exist should not throw"() { ... }

    // def "SQL injection in pokemon name should not corrupt the database"() { ... }

    // def "formatForDisplay should handle null types gracefully"() { ... }
}
