package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class ServiceChecker extends Service {


    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private final String C = "com.example.service.ServiceChecker";
    private final String C1 = "com.example.service.MainActivity";
    static final int NOTIFICATION_ID = 543;
    public static boolean isServiceRunning = false;
    ClipboardManager clipboardManager;
    IBinder mBinder;
    int mStartMode;
    int counter = 0;
    private Handler h;
    private Runnable r;
    String compare = "", current_URL = null, prev_URL = null, data;
    public static int serviceCheck = 0;
    private StringRequest mStringRequest, postStringRequest;
    String urlCopy, parse,title,body,imgurl;


    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            try {
                Bundle _intent =intent.getExtras();
                int valueToCheck = _intent.getInt("ServiceCheck");
                serviceCheck = valueToCheck;
            }catch (Exception e)
            {

            }

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String newClip = clipboardManager.getText().toString();
                if (newClip.startsWith("http") || newClip.startsWith("https")) {
                    if (newClip.contains("ss.gy") == false) {
                        if(serviceCheck == 1) {
                            notificationDialog(newClip);
                        }else if( serviceCheck == 2){
                            convertURL(newClip);
                        }

                    }
                }
            }

            private boolean isNotificationServiceEnabled() {
                String pkgName = getPackageName();
                final String allNames = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
                if (allNames != null && !allNames.isEmpty()) {
                    for (String name : allNames.split(":")) {
                        if (getPackageName().equals(ComponentName.unflattenFromString(name).getPackageName())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }


    @Override
    public void onDestroy() {
        isServiceRunning = false;
        super.onDestroy();
        Log.i(TAG, "onCreate() , service stopped...");
    }


    private void notificationDialog(String url) {

        String NOTIFICATION_CHANNEL_ID = String.valueOf(100);
        Intent yesNotification = new Intent(this, NotificationReceiver.class);

        yesNotification.putExtra("Yes", url);
        yesNotification.setAction("Approve");
        PendingIntent yesIntent = PendingIntent.getBroadcast(this, 0, yesNotification, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        StatusBarNotification[] notifications = new StatusBarNotification[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notifications = notificationManager.getActiveNotifications();
        }
        if (notifications != null) {
                for (StatusBarNotification notification : notifications) {
                    if (notification.getId() == 0) {
                        notificationManager.cancelAll();
                    }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
                // Configure the notification channel.
                notificationChannel.setDescription("Sample Channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

            notificationBuilder
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.link)
                    .setTicker("URL Shortener")
                    .setContentTitle("Shorten Copied URL ?")
                    .setContentText(url)
                    .setOnlyAlertOnce(true)
                    .setContentInfo("Information")
                    .addAction(R.mipmap.ic_launcher_round, "SHORTEN", yesIntent)

            ;
            notificationManager.notify(0, notificationBuilder.build());


        }

    }




    public void convertURL(String URL) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlLink = "https://www.ss.gy/urlShortner/api/shortner/create.php?url=" + URL;

        mStringRequest = new StringRequest(Request.Method.GET, urlLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    parse = jsonObject.get("shortUrl").toString();
                    if(parse!=null)
                    {
                        clipboardManager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        clipboardManager.setText("http://www.ss.gy/" + parse);
                        Toast.makeText(getApplicationContext(),"http://www.ss.gy/" + parse.toString(),Toast.LENGTH_LONG).show();
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
}
