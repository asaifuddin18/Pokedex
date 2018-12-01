package com.example.azizsaifuddin.pokedex;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Scanner;

import com.android.volley.Request;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
public class MainActivity extends AppCompatActivity {

    private static RequestQueue requestQueue;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up a queue for our Volley requests
        requestQueue = Volley.newRequestQueue(this);

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);

        // Attach the handler to our UI button
        final Button searchForPokemon = findViewById(R.id.search);
        searchForPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Log.d(TAG, "Start API button clicked");
                startAPICall();  //this method does the main things.
                TextView test = findViewById(R.id.viewInfo);
                //test.setText("HELLO WORLD!");
            }
        });

        // Make sure that our progress bar isn't spinning and style it a bit
        //ProgressBar progressBar = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.INVISIBLE);
    }
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://pokeapi.co/api/v2/pokemon/chimchar/",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            //display pokemon name in text box.
                            String raw = response.toString();
                            TextView toview = findViewById(R.id.viewInfo);
                            PokemonSearch attempt = new PokemonSearch(raw, "Dialga");
                            toview.setText(attempt.hiddenPassive());
                            //toview.setText(raw);
                            Log.w(TAG, "Success");
                            ProgressBar stop = findViewById(R.id.progressBar);
                            stop.setVisibility(View.INVISIBLE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    TextView toview = findViewById(R.id.viewInfo);
                    //toview.setText("BIG ERROR");
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
