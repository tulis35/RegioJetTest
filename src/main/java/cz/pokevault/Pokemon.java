package cz.pokevault;

import java.sql.Connection;
import java.sql.DriverManager;

public class Pokemon {

    private int id ;
    private String name;
    private int height;
    private int weight;
    private String types;
    private int baseExperience;

    public Pokemon(){
        id = -1;
        name = "";
        height = 0;
        weight = 0;
        types = "";
        baseExperience = 0;
    }

    public Pokemon(int id, String name, int height, int weight, String types, int baseExperience){
        this.id = id;
        this.name = (name == null) ? "" : name.trim();
        this.height = (height < 0) ? 0 : height;
        this.weight = (weight < 0) ? 0 : weight;
        this.types = (types == null) ? "" : types.trim();
        this.baseExperience = (baseExperience < 0) ? 0 : baseExperience;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWeight(){
        return this.weight;
    }

    public String getTypes(){
        return this.types;
    }

    public int getBaseExperience(){
        return this.baseExperience;
    }

    public void setId(int value){
        this.id = value;
    }

    public void setName(String value){
        this.name = (value == null) ? "" : value;
    }

    public void setHeight(int value){
        this.height = (value < 0) ? 0 : value;
    }

    public void setWeight(int value){
        this.weight = (value < 0) ? 0 : value;
    }

    public void setTypes(String value){
        this.types = (value == null) ? "" : value;
    }

    public void setBaseExperience(int value){
        this.baseExperience = (value < 0) ? 0 : value;
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
