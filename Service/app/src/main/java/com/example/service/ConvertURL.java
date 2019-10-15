package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ConvertURL extends AppCompatActivity {

    TextView convertURL;
    Button copyURL;
    TextView titleTxt;
    String parse;
    private StringRequest mStringRequest;
    ClipboardManager clipboardManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_url);
        getSupportActionBar().setTitle("SS.GY");
        convertURL = findViewById(R.id.urlConvert);
        copyURL = findViewById(R.id.copyURL);
        titleTxt=findViewById(R.id.title);
        Intent intent = getIntent();
        String message = intent.getStringExtra("URL");
        convertURL(message);
        copyURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCalling();
            }
        });
    }
    public void convertURL(String URL) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlLink = "https://www.ss.gy/urlShortner/api/shortner/create.php?url=" + URL;

        mStringRequest = new StringRequest(Request.Method.GET, urlLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    parse = jsonObject.get("shortUrl").toString();
                     if(parse!=null)
                     {
                            convertURL.setText("http://www.ss.gy/" + parse);
                     }
                     else
                     {
                         Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                     }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(mStringRequest);
    }
    public void postCalling() {
        if(convertURL.getText().toString() != null) {
            clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setText(convertURL.getText().toString());

        }else{
            Toast.makeText(getApplicationContext(), "No Any Value Founded", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.setting)
        {
            Intent i=new Intent(this,Setting.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(startIntent);
        finish();
    }
}
