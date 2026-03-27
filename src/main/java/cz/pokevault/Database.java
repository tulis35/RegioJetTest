package cz.pokevault;

import groovy.lang.GString;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static void init() {
        try {
            Connection conn = getConnection();
            if(conn == null)
                return;

            String sql = "CREATE TABLE IF NOT EXISTS pokedex (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "height INTEGER NOT NULL, " +
                    "weight INTEGER NOT NULL, " +
                    "types TEXT NOT NULL, " +
                    "base_experience INTEGER NOT NULL)";
            conn.createStatement().execute(sql);
            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
        }
    }

    public static void save(Pokemon p) {
        if(p == null || p.getName() == null || p.getName().isEmpty())
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
            PreparedStatement stmt = null;
            if(findByName(p.getName()) != null) {
                sql = "UPDATE pokedex SET height = ?, weight = ?, types = ?, base_experience = ? WHERE name = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, p.getHeight());
                stmt.setInt(2, p.getWeight());
                stmt.setString(3, p.getTypes());
                stmt.setInt(4, p.getBaseExperience());
                stmt.setString(5, p.getName());
            }else {
                sql = "INSERT INTO pokedex (name, height, weight, types, base_experience) VALUES (?,?,?,?,?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, p.getName());
                stmt.setInt(2, p.getHeight());
                stmt.setInt(3, p.getWeight());
                stmt.setString(4, p.getTypes());
                stmt.setInt(5, p.getBaseExperience());
            }
            stmt.execute();
            System.out.println("Added " + p.getName() + " to your Pokedex!");
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
            if(findByName(name) == null){
                System.out.println("Pokemon not found in database!");
                return;
            }

            conn = getConnection();
            if(conn == null)
                return;

            String sql = "DELETE FROM pokedex WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.execute();
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
        if(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc")) {
            Connection conn = null;
            try {
                conn = getConnection();
                if (conn == null)
                    return list;

                String sql = "SELECT * FROM pokedex ORDER BY name " + sortOrder.toUpperCase();
                ResultSet rs = conn.createStatement().executeQuery(sql);
                while (rs.next()) {
                    Pokemon p = new Pokemon();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setHeight(rs.getInt("height"));
                    p.setWeight(rs.getInt("weight"));
                    p.setTypes(rs.getString("types"));
                    p.setBaseExperience(rs.getInt("base_experience"));
                    list.add(p);
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("Failed to get Pokemons from database: " + e.getMessage());
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (SQLException e) {
                    System.out.println("Error when closing SQL connection: " + e.getMessage());
                }
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

            String sql = "SELECT * FROM pokedex WHERE name LIKE ? LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pokemon p = new Pokemon();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setHeight(rs.getInt("height"));
                p.setWeight(rs.getInt("weight"));
                p.setTypes(rs.getString("types"));
                p.setBaseExperience(rs.getInt("base_experience"));
                rs.close();
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
