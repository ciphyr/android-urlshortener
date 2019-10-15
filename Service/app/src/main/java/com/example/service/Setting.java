package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Setting extends AppCompatActivity {

    private final String C = "com.example.service.ServiceChecker";
    Switch notify, service;
    boolean check=false;
    int setCheckValue = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        notify = findViewById(R.id.notification);
        service = findViewById(R.id.foregroundService);
        int i = ServiceChecker.serviceCheck;
        boolean reuslt2=isMyServiceRunning(AutoCallService.class,getApplicationContext());


        if(i==1)
        {
            notify.setChecked(true);
            setCheckValue = 1;
        }

        if(i==2)
        {
            service.setChecked(true);
            setCheckValue = 2;
        }

        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (notify.isChecked()) {
                    if(service.isChecked())
                    {
//                        Intent stopIntent=new Intent(Setting.this,AutoCallService.class);
//                        stopService(stopIntent);
//                        service.setChecked(false);
//                        Toast.makeText(Setting.this,"stopped Auto Call Service When Initiated Notify Service",Toast.LENGTH_SHORT).show();

                        Intent stopIntent=new Intent(Setting.this, ServiceChecker.class);
                        stopService(stopIntent);
                        service.setChecked(false);
                    }

                    Intent startIntent=new Intent(Setting.this, ServiceChecker.class);
                    startIntent.putExtra("ServiceCheck",1);
                    startService(startIntent);



                }
                else {
                    Intent startIntent=new Intent(Setting.this, ServiceChecker.class);
                    stopService(startIntent);
                }

            }
        });



        service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (service.isChecked()) {
                    if(notify.isChecked())
                    {
//                        Intent stopIntent=new Intent(Setting.this,AutoCallService.class);
//                        stopService(stopIntent);
//                        service.setChecked(false);
//                        Toast.makeText(Setting.this,"stopped Auto Call Service When Initiated Notify Service",Toast.LENGTH_SHORT).show();

                        Intent stopIntent=new Intent(Setting.this, ServiceChecker.class);
                        stopService(stopIntent);
                        notify.setChecked(false);
                    }
                    Intent startIntent=new Intent(Setting.this, ServiceChecker.class);
                    startIntent.putExtra("ServiceCheck",2);
                    startService(startIntent);



                }
                else {
                    Intent startIntent=new Intent(Setting.this, ServiceChecker.class);
                    stopService(startIntent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.back) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
     finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager)context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
            }
        }
        return false;
    }





    }



