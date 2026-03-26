package cz.pokevault;

import java.sql.Connection;
import java.sql.DriverManager;

public class Pokemon {

    public int id;
    public String name;
    public int height;
    public int weight;
    public String types;
    public int baseExperience;

    public void saveToDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:pokedex.db");
            String sql = "INSERT INTO pokedex (name, height, weight, types, base_experience) VALUES ('"
                    + name + "', " + height + ", " + weight + ", '" + types + "', " + baseExperience + ")";
            conn.createStatement().execute(sql);
            System.out.println("Saved " + name + " to database");
        } catch (Exception e) {
        }
    }

    public String formatForDisplay() {
        return String.format("%-16s | Height: %5d | Weight: %5d | Types: %-20s | XP: %d",
                name, height, weight, types, baseExperience);
    }

    @Override
    public String toString() {
        return id + "|" + name + "|" + height + "|" + weight + "|" + types + "|" + baseExperience;
    }
}
