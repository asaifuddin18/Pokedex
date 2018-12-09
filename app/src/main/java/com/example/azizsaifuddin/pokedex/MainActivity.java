package com.example.azizsaifuddin.pokedex;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.Scanner;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
    private static String input;
    private String url;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        Button searchForPokemon = findViewById(R.id.button);
        final EditText search = findViewById(R.id.editText);
        searchForPokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                input = search.getText().toString();
                url = "https://pokeapi.co/api/v2/pokemon/" + input + "/";
                startAPICall();
            }
        });
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
                            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    AlertDialog.Builder onerror = new AlertDialog.Builder(MainActivity.this);
                    onerror.setMessage("Error: Pokemon Not Found")
                            .create();
                    onerror.show();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            AlertDialog.Builder onerror = new AlertDialog.Builder(MainActivity.this);
            onerror.setMessage("Error: Pokemon Not Found")
                    .create();
            onerror.show();
            e.printStackTrace();
        }
    }
    public static String getInput() {
        return input;
    }

}
