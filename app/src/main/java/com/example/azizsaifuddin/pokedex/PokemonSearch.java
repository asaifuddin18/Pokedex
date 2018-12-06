package com.example.azizsaifuddin.pokedex;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class PokemonSearch {
    private JSONObject json;
    private String name;
    private static final String TAG = "PokemonSearch";
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
    public String dex(String input) {
        try {
            JSONArray indecies = json.getJSONArray("game_indices");
            String number = indecies.getJSONObject(0).getString("game_index");
            if (input.equals("number")) {
                return number;
            }
            return "National Dex: #" + number;
        } catch (Exception e) {
            return e.toString();
        }
    }
    public String type() {
        String typelist = "No types found";
        try {
            JSONArray types = json.getJSONArray("types");
            for (int i = 0; i < types.length(); i++) {
                if (i == 0) {
                    typelist = types.getJSONObject(0).getJSONObject("type").getString("name");
                    typelist = typelist.substring(0, 1).toUpperCase() + typelist.substring(1);
                } else {
                    String raw = types.getJSONObject(i).getJSONObject("type").getString("name");
                    raw = raw.substring(0, 1).toUpperCase() + raw.substring(1);
                    typelist = typelist + ", " + raw;
                }
                if (types.length() == 1) {
                    typelist = "Type: " + typelist;
                }
                if (types.length() != 1 && i == types.length() - 1) {
                    typelist = "Types: " + typelist;
                }
            }
        } catch (Exception e) {
            return e.toString();
        }
        return typelist;
    }
    public String getStat(String stat) {
        String proper = stat.substring(0,1).toUpperCase() + stat.substring(1);//BIG ERROR
        for (int i = 0; i < proper.length(); i++) {
            if (proper.charAt(i) == '-') {
                proper = proper.substring(0, i) + " " + proper.substring(i + 1);
                proper = proper.substring(0, i + 1) + proper.substring(i + 1, i + 2) + proper.substring(i + 2) + ": ";
                break;
            } else  if (i == proper.length() - 1) {
                proper = proper + ": ";
            }
        }
        String toReturn = "error could not find stat";
        try {
            JSONArray stats = json.getJSONArray("stats");
            for (int i = 0; i < stats.length(); i++) {
                if (stats.getJSONObject(i).getJSONObject("stat").getString("name").equals(stat)) {
                     toReturn = proper + stats.getJSONObject(i).getString("base_stat");
                }
            }
        } catch (Exception e) {
            return e.toString();
        }
        return toReturn;
    }
}
