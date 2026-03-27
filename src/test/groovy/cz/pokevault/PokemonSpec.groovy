package cz.pokevault

import spock.lang.Specification

class PokemonSpec extends Specification {

    def setup() {
        Database.init()
    }

    def "adding a pokemon should not throw"() {
        given:
        Pokemon p = new Pokemon()
        p.setName("charmander")
        p.setHeight(6)
        p.setWeight(85)
        p.setTypes("fire")
        p.setBaseExperience(62)

        when:
        Database.save(p)

        then:
        noExceptionThrown()
    }

    def "adding a pokemon with null name should not save"(){
        given:
        Pokemon p = new Pokemon(-1, null, 10, 10, "fire", 100)
        Database.save(p)

        when:
        List<Pokemon> result = Database.getAll("ASC")

        then:
        !result.any { it.getName() == null }
    }

    // def "adding a pokemon with empty name should fail gracefully"() { ... }
    def "adding a pokemon with empty name should not save"(){
        given:
        Pokemon p = new Pokemon(-1, "", 10, 10, "fire", 100)
        Database.save(p)

        when:
        List<Pokemon> result = Database.getAll("ASC")

        then:
        !result.any { it.getName() == "" }
    }

    def "adding the same pokemon twice should not create duplicates"(){
        given:
        Database.remove("abra")
        ["zangoose", "abra", "abra"].each { name ->
            Pokemon p = new Pokemon(-1, name, 1, 1, "normal", 50)
            Database.save(p)
        }

        when:
        List<Pokemon> result = Database.getAll("asc")

        then:
        result.findAll {p -> p.getName() == "abra"}.size() < 2
    }

    def "removing a pokemon that does not exist should not throw"(){
        given:
        Pokemon p = new Pokemon(-1, "pikachu", 4, 60, "electric", 112)
        Database.save(p)

        when:
        Database.remove("bulbasour")

        then:
        noExceptionThrown()
    }

    // def "SQL injection in pokemon name should not corrupt the database"() { ... }
    def "SQL injection in pokemon name should not corrupt the database"(){
        given:
        ["zangoose", "abra", "pikachu"].each { name ->
            Pokemon p = new Pokemon(-1, name, 1, 1, "normal", 60)
            Database.save(p)
        }

        when:
        Database.remove("abra and 1=1")
        List<Pokemon> result = Database.getAll("asc")

        then:
        result.any()
    }

    def "formatForDisplay should handle null types gracefully"(){
        when:
        String s = new Pokemon(-1, null, 10, 10, "fire", 100).formatForDisplay()

        then:
        noExceptionThrown()
        !s.isEmpty()
    }
}
