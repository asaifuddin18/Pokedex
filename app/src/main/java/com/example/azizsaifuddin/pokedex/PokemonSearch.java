package com.example.azizsaifuddin.pokedex;

public class PokemonSearch {
    private String rawJSON;
    private String name;
    public PokemonSearch(String inputJSON, String inputName) {
        rawJSON = inputJSON;
        name = inputName;

    }
    public String getPokemonName() {
     return name;
    }
    public String hiddenPassive() {
        String toReturn = "No hidden ability";
        for (int i = 15; i < rawJSON.length(); i++) {
            String isHidden = "\"is_hidden\":true";
            if (rawJSON.substring(i - 15, i + 1).equals(isHidden)) {
                String abilityname = "\"name\":";
                for (int j = i; j > 5; j--) {
                    if (rawJSON.substring(j - 6, j + 1).equals(abilityname)) {
                        int endindex = rawJSON.indexOf('"', j + 2);
                        toReturn = rawJSON.substring(j + 2, endindex);
                    }
                }
            }
        }
        System.out.println(toReturn);
        return toReturn;
    }
    public String passive() {
        String toReturn = "No ability";
        for (int i = 16; i < rawJSON.length(); i++ ) {
            String notHidden = "\"is_hidden\":false";
            if (rawJSON.substring(i - 16, i + 1).equals(notHidden)) {
                String abilityname = "\"name\":";
                for (int j = i; j > 5; j++) {
                    if (rawJSON.substring(j - 6, j + 1).equals(abilityname)) {
                        int endindex = rawJSON.indexOf('"', j + 2);
                        toReturn = rawJSON.substring(j + 2, endindex);
                    }
                }
            }
        }
        return toReturn;
    }
}
