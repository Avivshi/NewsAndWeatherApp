package com.example.newsandweathertake2.Reciever;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsandweathertake2.Model.Article;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.core.app.NotificationCompat;
import static android.content.Context.NOTIFICATION_SERVICE;
public class NotificationReciever extends BroadcastReceiver {

    public static int NOTIF_ID_VALUE = 1;
    public static int index = 0;
    NotificationManager manager;
    private RequestQueue requestQueue;
    public static final Article[] article = {(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","","")),(new Article("","","",""))};
    private String stam;
    @Override
    public void onReceive(Context context, Intent intent) {

        requestQueue = Volley.newRequestQueue(context);
        String choice = intent.getStringExtra("catagory");
        parseJason(context,choice);
        manager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        String channelId = null;
        if(Build.VERSION.SDK_INT >= 26) {

            channelId =  "some_channel_id" ;
            CharSequence channelName =  "Breaking News" ;
            int  importance = NotificationManager. IMPORTANCE_LOW ;
            NotificationChannel notificationChannel =  new  NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration( true );
            notificationChannel.setVibrationPattern( new long []{ 100 ,  200 ,  300 ,  400 ,  500 ,  400 ,  300 ,  200 ,  400 });

            manager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);
        builder.setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(article[index].getTitle()).setColor(Color.RED);

        builder.setPriority(Notification.PRIORITY_MAX);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,new Intent(Intent.ACTION_VIEW, Uri.parse(article[index].getUrl())),PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);


        Notification notification = builder.build();

        notification.defaults = Notification.DEFAULT_VIBRATE;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify(NOTIF_ID_VALUE,notification);

            int seconds = intent.getIntExtra("seconds",30);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + seconds*1000,pendingIntent2);

            NOTIF_ID_VALUE++;

            if(index==article.length-1)
                index=0;
            else index++;

    }


    private void parseJason(final Context contex, String choice){

        String url = "https://newsapi.org/v2/top-headlines?country=us&category="+choice+"&apiKey=b0d2bc3b18fd439a96915fd36a6101c3";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");

                            for(int i=0;i<article.length;i++){
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String title = hit.getString("title");
                                String urlToImage = hit.getString("urlToImage");
                                String description = hit.getString("description");
                                String url = hit.getString("url");

                                article[i].setTitle(title);
                                article[i].setUrlToImage(urlToImage);
                                article[i].setDescription(description);
                                article[i].setUrl(url);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
        requestQueue.start();
    }

}
