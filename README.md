# PokeVault CLI

A command-line Pokemon catalog and personal Pokedex manager. Browse Pokemon from the [PokeAPI](https://pokeapi.co/), search by name, and maintain your own Pokedex stored locally.

---

## Requirements

- Java 21+
- Maven 3.8+

---

## Build

```bash
mvn package -DskipTests
```

This produces `target/pokevault-1.0-SNAPSHOT.jar`.

---

## Run

```bash
java -jar target/pokevault-1.0-SNAPSHOT.jar <command> [options]
```

Or via Maven:

```bash
mvn exec:java -Dexec.args="<command> [options]"
```

---

## Commands

| Command | Description |
|---|---|
| `list [--page N]` | List Pokemon from PokeAPI |
| `search <name>` | Search Pokemon by name |
| `add <name>` | Add a Pokemon to your personal Pokedex |
| `remove <name>` | Remove a Pokemon from your Pokedex |
| `pokedex [--sort asc\|desc]` | Show your Pokedex (default sort: asc) |
| `info <name>` | Show full details for a Pokemon |

### Examples

```bash
# List the first page of Pokemon
java -jar target/pokevault-1.0-SNAPSHOT.jar list

# Search for fire-type Pokemon
java -jar target/pokevault-1.0-SNAPSHOT.jar search char

# Add Pikachu to your Pokedex
java -jar target/pokevault-1.0-SNAPSHOT.jar add pikachu

# View your Pokedex sorted descending
java -jar target/pokevault-1.0-SNAPSHOT.jar pokedex --sort desc

# Get details on a specific Pokemon
java -jar target/pokevault-1.0-SNAPSHOT.jar info mewtwo

# Remove a Pokemon
java -jar target/pokevault-1.0-SNAPSHOT.jar remove pikachu
```

---

## Persistence

Your Pokedex is stored in a local SQLite database file: `pokedex.db` (created automatically on first run in the current working directory).

---

## Tests

```bash
mvn test
```

Tests are written in [Spock Framework](https://spockframework.org/) (Groovy). Test files are in `src/test/groovy/cz/pokevault/`.

---

## Project Structure

```
src/
├── main/java/cz/pokevault/
│   ├── Main.java           # CLI entry point and command handling
│   ├── Database.java       # SQLite persistence
│   ├── PokeApiClient.java  # HTTP client for PokeAPI
│   └── Pokemon.java        # Pokemon domain model
└── test/groovy/cz/pokevault/
    ├── PokedexServiceSpec.groovy
    ├── DatabaseSpec.groovy
    ├── PokeApiClientSpec.groovy
    ├── PokedexIntegrationSpec.groovy
    └── PokemonSpec.groovy
```
