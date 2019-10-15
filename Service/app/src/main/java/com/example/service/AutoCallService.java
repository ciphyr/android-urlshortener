package com.example.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class AutoCallService extends Service {
    ClipboardManager clipboardManager;
    public static boolean isServiceRunning = false;
    private StringRequest mStringRequest, postStringRequest;
    String urlCopy, parse,title,body,imgurl;

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        clipboardManager=(ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String newClip = clipboardManager.getText().toString();
                if (newClip.startsWith("http") || newClip.startsWith("https")) {
                    if (newClip.contains("ss.gy") == false) {
                        convertURL(newClip);
                    }
                }

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
        stopSelf();
        super.onDestroy();
        Log.i(TAG, "onCreate() , service stopped...");
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
                        Toast.makeText(getApplicationContext(),"Auto Call Service " + "http://www.ss.gy/" + parse.toString(),Toast.LENGTH_LONG).show();
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
