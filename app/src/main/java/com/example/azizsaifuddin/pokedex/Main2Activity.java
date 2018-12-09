package com.example.azizsaifuddin.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {
    private static RequestQueue requestQueue;
    private static final String TAG = "Main2Activity";
    private static String url;
    private PokemonSearch tosearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main2);
        url = "https://pokeapi.co/api/v2/pokemon/" + MainActivity.getInput() + "/";
        Button stats = findViewById(R.id.statsevolution);
        //pokemon image
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                startActivity(intent);
            }
        });
        startAPICall();
    }
    public void showToast(View view) {
        String test = tosearch.hiddenPassiveURL();
        Log.w(TAG, "TOASTERINO " + test);
        hiddenabilityToast(tosearch.hiddenPassiveURL());
    }
    public void abilityShowToast(View view) {
        String test = tosearch.passiveURL();
        Log.w(TAG, "ABILITYTOASY" + test);
        abilityToast(test);
    }
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            TextView ability = findViewById(R.id.ability);
                            TextView hiddenability = findViewById(R.id.hiddenability);
                            TextView weight = findViewById(R.id.weight);
                            TextView height = findViewById(R.id.height);
                            TextView dex = findViewById(R.id.nationaldex);
                            TextView pokemonname = findViewById(R.id.pokemonname);
                            TextView species = findViewById(R.id.species);
                            TextView type = findViewById(R.id.type);
                            tosearch = new PokemonSearch(response);
                            int dexnumber = Integer.parseInt(tosearch.dex("number")); //a source of error
                            Log.w(TAG, "GEN6DEBUG  TRIGGERAFTER");
                            if (dexnumber < 10) {
                                new DownloadImageTask((ImageView) findViewById(R.id.pokemonImage)) //pokemonimage
                                        .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/00" + dexnumber +".png");
                            } else if (dexnumber < 100) {
                                new DownloadImageTask((ImageView) findViewById(R.id.pokemonImage)) //pokemonimage
                                        .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/0" + dexnumber + ".png");
                                Log.w(TAG, dexnumber + "DEXNUMBER RIGHT HERE");
                            } else {
                                new DownloadImageTask((ImageView) findViewById(R.id.pokemonImage)) //pokemonimage
                                        .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + dexnumber + ".png"); //DOES NOT WORK FOR GEN 6 & 7
                            }
                            String[] urls = tosearch.getSprite();
                            Log.w(TAG, "SPRITES URL" + urls[0]);
                            new DownloadImageTask((ImageView) findViewById(R.id.front_default)) //pokemonimage
                                    .execute(urls[0]);
                            new DownloadImageTask((ImageView) findViewById(R.id.front_shiny)) //pokemonimage
                                    .execute(urls[1]);
                            new DownloadImageTask((ImageView) findViewById(R.id.back_default)) //pokemonimage
                                    .execute(urls[2]);
                            new DownloadImageTask((ImageView) findViewById(R.id.back_shiny)) //pokemonimage
                                    .execute(urls[3]);
                            ability.setText(tosearch.passive());
                            hiddenability.setText(tosearch.hiddenPassive());
                            weight.setText(tosearch.weight());
                            height.setText(tosearch.height());
                            dex.setText(tosearch.dex("formatted"));
                            species(response);
                            flavortext(response);
                            pokemonname.setText(MainActivity.getInput());
                            pokemonname.setText(tosearch.getPokemonName());
                            type.setText(tosearch.type());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            //progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            //display error in text box (pokemon not found).
        }
    }
    public void hiddenabilityToast(String inputurl) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    inputurl,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                JSONArray flavortest = response.getJSONArray("flavor_text_entries");
                                for (int i = 0; i < flavortest.length(); i++) {
                                    if (flavortest.getJSONObject(i).getJSONObject("language").getString("name").equals("en")) {
                                        Toast.makeText(Main2Activity.this, flavortest.getJSONObject(i).getString("flavor_text"), Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                //do nothing
                            }
                        }
                        public void initialize() {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            //progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //display error in text box (pokemon not found).
        }
    }
    public void abilityToast(String inputurl) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    inputurl,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                JSONArray flavortest = response.getJSONArray("flavor_text_entries");
                                for (int i = 0; i < flavortest.length(); i++) {
                                    if (flavortest.getJSONObject(i).getJSONObject("language").getString("name").equals("en")) {
                                        Toast.makeText(Main2Activity.this, flavortest.getJSONObject(i).getString("flavor_text"), Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                //do nothing
                            }
                        }
                        public void initialize() {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            //progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //display error in text box (pokemon not found).
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) { //initialize
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public void species(JSONObject json) {
        try {
            String url = json.getJSONObject("species").getString("url");
            Log.w(TAG, url + " THISISATEST");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        private String thereturn;
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                JSONArray genera = response.getJSONArray("genera");
                                for (int i = 0; i < genera.length(); i++) {
                                    JSONObject genus = genera.getJSONObject(i);
                                    JSONObject language = genus.getJSONObject("language");
                                    Log.w(TAG, "worked");
                                    Log.w(TAG, language.getString("name") + "LANGUAGE");
                                    if (language.getString("name").equals("en")) {
                                        TextView species = findViewById(R.id.species);
                                        species.setText("The " + genus.getString("genus"));
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                //do nothing
                            }
                        }
                        public void initialize() {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            //progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //display error in text box (pokemon not found).
        }
    }
    public void flavortext(JSONObject json) {
        try {
            String url = json.getJSONObject("species").getString("url");
            Log.w(TAG, url + " THISISATEST");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        private String thereturn;
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                JSONArray flavorarray = response.getJSONArray("flavor_text_entries");
                                for (int i = 0; i < flavorarray.length(); i++) {
                                    JSONObject flavor = flavorarray.getJSONObject(i);
                                    JSONObject language = flavor.getJSONObject("language");
                                    Log.w(TAG, "worked");
                                    Log.w(TAG, language.getString("name") + "LANGUAGE");
                                    if (language.getString("name").equals("en")) {
                                        TextView description = findViewById(R.id.description);
                                        description.setText(flavor.getString("flavor_text"));
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                //do nothing
                            }
                        }
                        public void initialize() {
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            //progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //display error in text box (pokemon not found).
        }
    }
    public static String getUrl() {
        return url;
    }

}
