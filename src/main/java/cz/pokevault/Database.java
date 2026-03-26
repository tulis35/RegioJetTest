package cz.pokevault;

import groovy.lang.GString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void init() {
        try {
            Connection conn = getConnection();
            if(conn == null)
                return;

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
        if(p == null || p.name == null || p.name.isEmpty())
        {
            System.out.println("Invalid Pokemon parameters!");
            return;
        }
        Connection conn = null;
        try {
            conn = getConnection();
            if(conn == null)
                return;

            String sql;
            if(findByName(p.name) != null)
                sql = String.format("UPDATE pokedex SET height = %d, weight = %d, types = '%s', base_experience = %d WHERE name = '%s'",
                        p.height, p.weight, p.types, p.baseExperience, p.name);
            else
                sql = "INSERT INTO pokedex (name, height, weight, types, base_experience) VALUES ('"
                        + p.name + "', " + p.height + ", " + p.weight + ", '" + p.types + "', " + p.baseExperience + ")";
            conn.createStatement().execute(sql);
            System.out.println("Added " + p.name + " to your Pokedex!");
        } catch (Exception e) {
            System.out.println("Failed to save Pokemon to database: " + e.getMessage());
        }
        finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error when closing SQL connection: " + e.getMessage());
            }
        }
    }

    public static void remove(String name) {
        Connection conn = null;
        try {
            conn = getConnection();
            if(conn == null)
                return;

            String sql = "DELETE FROM pokedex WHERE name = '" + name + "'";
            conn.createStatement().execute(sql);
            System.out.println("Removed " + name + " from your Pokedex.");
        } catch (Exception e) {
            System.out.println("Failed to remove Pokemon from database: " + e.getMessage());
        }
        finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error when closing SQL connection: " + e.getMessage());
            }
        }
    }

    public static List<Pokemon> getAll(String sortOrder) {
        List<Pokemon> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = getConnection();
            if(conn == null)
                return list;

            String sql = "SELECT * FROM pokedex ORDER BY name " + sortOrder.toUpperCase();
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
            System.out.println("Failed to get Pokemons from database: " + e.getMessage());
        }
        finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error when closing SQL connection: " + e.getMessage());
            }
        }
        return list;
    }

    public static Pokemon findByName(String name){
        Connection conn = null;
        try {
            conn = getConnection();
            if(conn == null)
                return null;

            String sql = "SELECT * FROM pokedex WHERE name LIKE '" + name + "'" + " LIMIT 1";
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
            System.out.println("Failed to find Pokemon from database: " + e.getMessage());
        }finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                System.out.println("Error when closing SQL connection: " + e.getMessage());
            }
        }
        return null;
    }

    private static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:sqlite:pokedex.db");
        }catch (Exception e){
            System.out.println("Failed to initialize SQL connection: " + e.getMessage());
            return null;
        }
    }
}
