package cz.pokevault;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void init() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "CREATE TABLE IF NOT EXISTS pokedex (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "height INTEGER, " +
                    "weight INTEGER, " +
                    "types TEXT, " +
                    "base_experience INTEGER)";
            conn.createStatement().execute(sql);
            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println("Failed to init database: " + e.getMessage());
        }
    }

    public static void save(Pokemon p) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "INSERT INTO pokedex (name, height, weight, types, base_experience) VALUES ('"
                    + p.name + "', " + p.height + ", " + p.weight + ", '" + p.types + "', " + p.baseExperience + ")";
            conn.createStatement().execute(sql);
            System.out.println("Added " + p.name + " to your Pokedex!");
        } catch (Exception e) {
        }
    }

    public static void remove(String name) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "DELETE FROM pokedex WHERE name = '" + name + "'";
            conn.createStatement().execute(sql);
            System.out.println("Removed " + name + " from your Pokedex.");
        } catch (Exception e) {
        }
    }

    public static List<Pokemon> getAll(String sortOrder) {
        List<Pokemon> list = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "SELECT * FROM pokedex ORDER BY name " + sortOrder;
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Pokemon p = new Pokemon();
                p.id = rs.getInt("id");
                p.name = rs.getString("name");
                p.height = rs.getInt("height");
                p.weight = rs.getInt("weight");
                p.types = rs.getString("types");
                p.baseExperience = rs.getInt("base_experience");
                list.add(p);
            }
        } catch (Exception e) {
        }
        return list;
    }

    public static Pokemon findByName(String name) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "SELECT * FROM pokedex WHERE name LIKE '" + name + "'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            if (rs.next()) {
                Pokemon p = new Pokemon();
                p.id = rs.getInt("id");
                p.name = rs.getString("name");
                p.height = rs.getInt("height");
                p.weight = rs.getInt("weight");
                p.types = rs.getString("types");
                p.baseExperience = rs.getInt("base_experience");
                return p;
            }
        } catch (Exception e) {
        }
        return null;
    }
}
