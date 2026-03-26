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

    public String formatForDisplay() {
        return String.format("%-16s | Height: %5d | Weight: %5d | Types: %-20s | XP: %d",
                name, height, weight, types, baseExperience);
    }

    @Override
    public String toString() {
        return id + "|" + name + "|" + height + "|" + weight + "|" + types + "|" + baseExperience;
    }
}
