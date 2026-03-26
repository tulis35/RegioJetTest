package cz.pokevault;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Pokemon> cache = new ArrayList<>();
    private static PokeApiClient client = new PokeApiClient();

    public static void main(String[] args) {
        System.out.println("Welcome to PokeAPI CLI!");

        if (args.length == 0) {
            printHelp();
            return;
        }
        if(args.length >= 4){
            System.out.println(("Too many arguments!"));
            return;
        }

        String command = args[0];

        if (command.equalsIgnoreCase("list")) {
            int page = 1;
            if (args.length > 2 && args[1].equalsIgnoreCase("--page")) {
                try {
                    page = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid page number");
                    System.exit(1);
                }
            }
            handleList(page);

        } else if (command.equalsIgnoreCase("search")) {
            if (args.length < 2) {
                System.out.println("Usage: search <name>");
                System.exit(1);
            }
            handleSearch(args[1]);

        } else if (command.equalsIgnoreCase("add")) {
            if (args.length < 2) {
                System.out.println("Usage: add <name>");
                System.exit(1);
            }
            handleAdd(args[1]);

        } else if (command.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                System.out.println("Usage: remove <name>");
                System.exit(1);
            }
            handleRemove(args[1]);

        } else if (command.equalsIgnoreCase("pokedex")) {
            String sort = "asc";
            if (args.length > 2 && args[1].equalsIgnoreCase("--sort"))
                if(args[2].equalsIgnoreCase("asc") || args[2].equalsIgnoreCase("desc"))
                    sort = args[2];

            handlePokedex(sort);

        } else if (command.equalsIgnoreCase("info")) {
            if (args.length < 2) {
                System.out.println("Usage: info <name>");
                System.exit(1);
            }
            handleInfo(args[1]);

        } else {
            System.out.println("Unknown command: " + command);
            printHelp();
            System.exit(1);
        }
    }

    private static void handleList(int page) {
        System.out.println("Fetching Pokemon list (page " + page + ")...");
        List<Pokemon> pokemons = client.listPokemon(page);

        cache = pokemons;

        if (pokemons.isEmpty()) {
            System.out.println("No Pokemon found.");
            return;
        }

        System.out.println("\n--- Pokemon List ---");
        for (Pokemon p : pokemons) {
            System.out.println(p.formatForDisplay());
        }
        System.out.println("Total: " + pokemons.size());
    }

    private static void handleSearch(String query) {
        query = query.trim();
        System.out.println("Searching for: " + query);
        List<Pokemon> results = client.searchPokemon(query);

        if (results.isEmpty()) {
            System.out.println("No Pokemon found matching '" + query + "'");
            return;
        }

        System.out.println("\n--- Search Results ---");
        for (Pokemon p : results) {
            System.out.println(p.formatForDisplay());
        }
    }

    private static void handleAdd(String name) {
        System.out.println("Looking up " + name + "...");
        Pokemon p = client.fetchPokemon(name);

        if (p == null) {
            System.out.println("Pokemon '" + name + "' not found.");
            return;
        }
        Database.init();
        Database.save(p);
    }

    private static void handleRemove(String name) {
        Database.remove(name);
    }

    private static void handlePokedex(String sort) {
        Database.init();
        List<Pokemon> pokedex = Database.getAll(sort);

        if (pokedex.isEmpty()) {
            System.out.println("Your Pokedex is empty. Use 'add <name>' to add Pokemon.");
            return;
        }

        System.out.println("\n--- Your Pokedex ---");
        for (Pokemon p : pokedex) {
            System.out.println(p.formatForDisplay());
        }
        System.out.println("Total: " + pokedex.size() + " Pokemon");
    }

    private static void handleInfo(String name) {
        System.out.println("Fetching info for: " + name);
        Pokemon p = client.fetchPokemon(name);

        if (p == null) {
            System.out.println("Pokemon '" + name + "' not found.");
            return;
        }

        System.out.println("\n--- " + p.name.toUpperCase() + " ---");
        System.out.println("Height : " + p.height);
        System.out.println("Weight : " + p.weight);
        System.out.println("Types  : " + p.types);
        System.out.println("Base XP: " + p.baseExperience);
        System.out.println("Raw    : " + p.toString());
    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("  list [--page N]           List Pokemon");
        System.out.println("  search <name>             Search by name");
        System.out.println("  add <name>                Add to your Pokedex");
        System.out.println("  remove <name>             Remove from Pokedex");
        System.out.println("  pokedex [--sort asc|desc] Show your Pokedex");
        System.out.println("  info <name>               Show Pokemon details");
    }

    private static void dumpCache() {
        System.out.println("Cache dump:");
        for (Pokemon p : cache) {
            System.out.println(p);
        }
    }

    /*
     * TODO: implement interactive mode
     *
     * public static void runInteractive() {
     *     Scanner scanner = new Scanner(System.in);
     *     while (true) {
     *         System.out.print("> ");
     *         String line = scanner.nextLine();
     *         if (line.equals("exit")) break;
     *         // parse and dispatch...
     *     }
     * }
     */
}
