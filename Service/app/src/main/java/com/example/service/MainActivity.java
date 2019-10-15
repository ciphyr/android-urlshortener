package com.example.service;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private final String C = "com.example.service.ServiceChecker";
    Button startBtn;
    EditText txtURL;
    String urlText;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("SS.GY");

        setContentView(R.layout.activity_main);
        startBtn=findViewById(R.id.button);
        txtURL=findViewById(R.id.urlLink);
        txtURL.setText("");
        startBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
            urlText=txtURL.getText().toString();
            Intent startIntent = new Intent(getApplicationContext(), ConvertURL.class);
            startIntent.putExtra("URL",urlText);
            startIntent.putExtra("URL",(urlText.startsWith("https://") || urlText.startsWith("http://") ? urlText : "http://" + urlText));
            startActivity(startIntent);
            finish();

            }
        });
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
            i.putExtra("preURL","null");
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
