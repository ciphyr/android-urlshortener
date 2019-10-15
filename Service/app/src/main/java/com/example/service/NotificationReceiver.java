package com.example.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import static android.content.Context.CLIPBOARD_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {


    private StringRequest mStringRequest, postStringRequest;
    String urlCopy, parse,title,body,imgurl;
    ClipboardManager clipboardManager;
    private Context mcontext;
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
     if(action.equals("Approve")) {
         String message = intent.getStringExtra("Yes");
         Toast.makeText(context, message, Toast.LENGTH_LONG).show();
         if (message != null) {
             convertURL(context, message);

         }
     }

    }



    public void convertURL(final Context context, String URL) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String urlLink = "https://www.ss.gy/urlShortner/api/shortner/create.php?url=" + URL;

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    parse = jsonObject.get("shortUrl").toString();
                    if(parse!=null)
                    {
                        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setText("ss.gy/" + parse);
                        Toast.makeText(context, "http://www.ss.gy/" + parse.toString(),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context,"Error"+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(mStringRequest);
    }
}
