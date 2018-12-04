package com.example.azizsaifuddin.pokedex;

import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class PokemonSearch {
    private JSONObject json;
    private String name;
    public PokemonSearch(JSONObject JSONOBJECT) {
        json = JSONOBJECT;
    }
    public String getPokemonName() {
     return name;
    }
    public String hiddenPassive() {
       try {
           JSONArray abilities = json.getJSONArray("abilities");
           for (int i = 0; i < abilities.length(); i++) {
               JSONObject hidden = abilities.getJSONObject(i);
               if (hidden.getString("is_hidden").equals("true")) {
                   return "Hidden Ability: " + hidden.getJSONObject("ability").getString("name");

               }
           }
       } catch (Exception e) {
           return e.toString();
       }
       return "big error";
    }
    public String passive() {
        try {
            JSONArray abilities = json.getJSONArray("abilities");
            for (int i = 0; i < abilities.length(); i++) {
                JSONObject hidden = abilities.getJSONObject(i);
                if (hidden.getString("is_hidden").equals("false")) {
                    return "Ability: " + hidden.getJSONObject("ability").getString("name");
                }
            }
        } catch (Exception e) {
            return e.toString();
        }
        return "big error";
    }
    public String weight() {
        try {
            int hecto = Integer.parseInt(json.getString("weight"));
            double lbs = hecto * 0.2204622622;
            DecimalFormat df = new DecimalFormat("#.##");
            return "Weight: " + df.format(lbs) + " lbs";
        } catch (Exception e) {
            return e.toString();
        }
    }
    public String height() {
        try {
            int hecto = Integer.parseInt(json.getString("height"));
            double height = 0.328084 * hecto;
            int feet = 0;
            for (; height > 1; height--) {
                feet++;
            }
            double inches = height * 12;
            DecimalFormat df = new DecimalFormat("#");
            String inch = df.format(inches);
            char quot = (char) 34;
            return "Height: " + feet + "'" + " " + inch + quot;
        } catch (Exception e) {
            return e.toString();
        }
    }
    public String dex() {
        try {
            JSONArray indecies = json.getJSONArray("game_indices");
            String number = indecies.getJSONObject(0).getString("game_index");
            return "National Dex: #" + number;
        } catch (Exception e) {
            return e.toString();
        }
    }
}
