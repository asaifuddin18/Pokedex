package com.example.azizsaifuddin.pokedex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import java.io.InputStream;

public class Main3Activity extends AppCompatActivity {
    private static final String TAG = "Main3Activity";
    String url;
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        requestQueue = Volley.newRequestQueue(this);
        url = Main2Activity.getUrl();
        startAPICall();
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
                           TextView hp = findViewById(R.id.hp);
                           TextView attack = findViewById(R.id.attack);
                           TextView specialattack = findViewById(R.id.specialattack);
                           TextView defense = findViewById(R.id.defense);
                           TextView specialdefense = findViewById(R.id.specialdefense);
                           TextView speed = findViewById(R.id.speed);
                           PokemonSearch tosearch = new PokemonSearch(response);
                           hp.setText(tosearch.getStat("hp"));
                           attack.setText(tosearch.getStat("attack"));
                           specialattack.setText(tosearch.getStat("special-attack"));
                           defense.setText(tosearch.getStat("defense"));
                           specialdefense.setText(tosearch.getStat("special-defense"));
                           speed.setText(tosearch.getStat("speed"));
                           getEvo(response);
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
    public void getEvo(JSONObject input) {
        String url1;
        final String[] url = new String[3];
        try {
            url1 = input.getJSONObject("species").getString("url");
        } catch (Exception e) {
            return;
        }
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url1,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            String url2 = "error";
                            try {
                                url2 = response.getJSONObject("evolution_chain").getString("url");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                        Request.Method.GET,
                                        url2,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(final JSONObject response) {
                                                try {
                                                    JSONObject chain = response.getJSONObject("chain");
                                                    JSONArray evolvesto = chain.getJSONArray("evolves_to");
                                                    Log.w("TAG", "EVOLUTIONk" + evolvesto.length());
                                                    if (evolvesto == null || evolvesto.length() == 0) {
                                                        //Log.w(TAG, "EVOLUTION " + chain.getJSONObject("species").getString("name"));
                                                        new Main3Activity.DownloadImageTask((ImageView) findViewById(R.id.firstevo)) //pokemonimage
                                                                .execute(chain.getJSONObject("species").getString("url")); //gets name.
                                                        url[0] = chain.getJSONObject("species").getString("url");
                                                    }
                                                    if (evolvesto.length() == 1) {
                                                        String evostring1 = chain.getJSONObject("species").getString("name");
                                                        url[0] = "https://pokeapi.co/api/v2/pokemon/" + evostring1 + "/";
                                                        evochainJSONGETTER(url[0], 1);
                                                        Log.w(TAG, "EVOLUTION " + url[0]);
                                                        String evostring2 = evolvesto.getJSONObject(0).getJSONObject("species").getString("name");
                                                        url[1] = "https://pokeapi.co/api/v2/pokemon/" + evostring2 + "/";
                                                        evochainJSONGETTER(url[1], 2);
                                                        Log.w(TAG, "EVOLUTION " + url[1]);
                                                        String evostring3 = evolvesto.getJSONObject(0).getJSONArray("evolves_to").getJSONObject(0).getJSONObject("species").getString("name");
                                                        url[2] = "https://pokeapi.co/api/v2/pokemon/" + evostring3 + "/";
                                                        evochainJSONGETTER(url[2], 3);
                                                        Log.w(TAG, "EVOLUTION " + url[2]);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
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
    public void evochainJSONGETTER(String pokemonURL, final int number) {
        Log.w(TAG, "HELPER " + pokemonURL + number);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    pokemonURL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                PokemonSearch evol = new PokemonSearch(response);
                                int dexnumber = Integer.parseInt(evol.dex("number"));
                                if (number == 1) {
                                    Log.w(TAG, "HELPER EXECUTING 1");
                                    Log.w(TAG, "HELPER " + evol.dex("number"));
                                    if (dexnumber < 10) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.firstevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/00" + dexnumber +".png");
                                    } else if (dexnumber < 100) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.firstevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/0" + dexnumber + ".png");
                                        Log.w(TAG, dexnumber + "DEXNUMBER RIGHT HERE");
                                    } else {
                                        new DownloadImageTask((ImageView) findViewById(R.id.firstevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + dexnumber + ".png"); //DOES NOT WORK FOR GEN 6 & 7
                                    }
                                } else if (number == 2) {
                                    if (dexnumber < 10) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.secondevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/00" + dexnumber +".png");
                                    } else if (dexnumber < 100) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.secondevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/0" + dexnumber + ".png");
                                        Log.w(TAG, dexnumber + "DEXNUMBER RIGHT HERE");
                                    } else {
                                        new DownloadImageTask((ImageView) findViewById(R.id.secondevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + dexnumber + ".png"); //DOES NOT WORK FOR GEN 6 & 7
                                    }
                                } else {
                                    if (dexnumber < 10) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.thirdevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/00" + dexnumber +".png");
                                    } else if (dexnumber < 100) {
                                        new DownloadImageTask((ImageView) findViewById(R.id.thirdevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/0" + dexnumber + ".png");
                                        Log.w(TAG, dexnumber + "DEXNUMBER RIGHT HERE");
                                    } else {
                                        new DownloadImageTask((ImageView) findViewById(R.id.thirdevo)) //pokemonimage
                                                .execute("https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + dexnumber + ".png"); //DOES NOT WORK FOR GEN 6 & 7
                                    }
                                }
                            } catch (Exception e) {
                                Log.w(TAG, "HELPER " + e.toString());
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
}
