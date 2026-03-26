package cz.pokevault;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class PokeApiClient {
    private static String POKEMON_API_URL = "https://pokeapi.co/api/v2/pokemon";

    public List<Pokemon> listPokemon(int page) {
        List<Pokemon> pokemonList = new ArrayList<>();
        try {
            int offset = (page == 1) ? 0 : page * 10;
            URL url = new URL(POKEMON_API_URL + "?limit=20&offset=" + offset);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Something went wrong. HTTP code: " + conn.getResponseCode());
                return null;
            }

            JsonNode root = getJSONFromRequest(conn.getInputStream());
            if(root != null){
            JsonNode results = root.get("results");

            for (JsonNode node : results) {
                String name = node.get("name").asText();
                Pokemon p = fetchPokemon(name);
                if (p != null) {
                    pokemonList.add(p);
                }
            }
            }
        } catch (Exception e) {
            System.out.println("Failed to get Pokemons: " + e.getMessage());
        }
        return pokemonList;
    }

    public Pokemon fetchPokemon(String pokemonName) {
        try {
            URL url = new URL(POKEMON_API_URL + "/" + pokemonName.toLowerCase());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Something went wrong. HTTP code: " + conn.getResponseCode());
                return null;
            }

            JsonNode root = getJSONFromRequest(conn.getInputStream());
            if(root != null) {

                Pokemon p = new Pokemon();
                p.name = root.get("name").asText();
                p.height = root.get("height").asInt();
                p.weight = root.get("weight").asInt();
                p.baseExperience = root.get("base_experience").asInt();

                List<String> typeList = new ArrayList<>();
                for (JsonNode typeNode : root.get("types")) {
                    typeList.add(typeNode.get("type").get("name").asText());
                }
                p.types = String.join(",", typeList);

                return p;
            }else
                return null;
        } catch (Exception e) {
            System.out.println("Failed to get Pokemon: " + e.getMessage());
            return null;
        }
    }

    public List<Pokemon> searchPokemon(String query) {
        if(query == null || query.isBlank())
            return new ArrayList<>();

        List<Pokemon> results = new ArrayList<>();
        try {
            URL url = new URL(POKEMON_API_URL + "?limit=10000&offset=0");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Something went wrong. HTTP code: " + conn.getResponseCode());
                return null;
            }

            JsonNode root = getJSONFromRequest(conn.getInputStream());

            if(root != null) {
                ArrayNode node = (ArrayNode)root.get("results");
                List<String> result = StreamSupport.stream(node.spliterator(), false)
                        .filter(p -> p.get("name").asText().contains(query.toLowerCase()))
                        .map(p -> p.get("name").asText())
                        .toList();

                for(String name : result){
                    Pokemon p = fetchPokemon(name);
                    if (p != null) {
                        results.add(p);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to find Pokemon: " + e.getMessage());
        }
        return results;
    }

    private JsonNode getJSONFromRequest(InputStream input){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder data = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null) {
                data.append(temp);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(data.toString());
        } catch (Exception e) {
            System.out.println("Failed to manage request: " + e.getMessage());
            return  null;
        }
    }
}
