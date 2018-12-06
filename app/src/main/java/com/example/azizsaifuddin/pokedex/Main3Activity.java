package com.example.azizsaifuddin.pokedex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
}
