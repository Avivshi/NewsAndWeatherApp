package com.example.newsandweathertake2.Fragments;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsandweathertake2.Model.Article;
import com.example.newsandweathertake2.R;
import com.example.newsandweathertake2.Reciever.NotificationReciever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class NotificationsFragment extends Fragment {
    ArrayList<Article> articles;
    private RequestQueue requestQueue;
    AlarmManager alarmManager;
    String catagoryChoice = "general";
    RadioGroup radioGroup;
    RadioButton oneMinRbtn, fifteenMinsRbtn, thirtyMinRbtn, oneHourRbtn;
    RadioButton generalRbtn, sportsRBtn, technologyRbtn;
    String url = "https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=*api key number*";
    Button cancelBtn;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.notification_set,container,false);

        articles =new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());
        //parseJason();
        final Button updateBtn = root.findViewById(R.id.updating_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View dialogView = factory.inflate(R.layout.dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                radioGroup = dialogView.findViewById(R.id.time_group);
                oneMinRbtn = dialogView.findViewById(R.id.one_min);
                fifteenMinsRbtn = dialogView.findViewById(R.id.fifteen_mins);
                thirtyMinRbtn = dialogView.findViewById(R.id.thirty_mins);
                oneHourRbtn = dialogView.findViewById(R.id.one_hour);

                generalRbtn = dialogView.findViewById(R.id.general);
                sportsRBtn = dialogView.findViewById(R.id.sports);
                technologyRbtn = dialogView.findViewById(R.id.technology);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                dialogView.findViewById(R.id.done_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(technologyRbtn.isChecked()){
                            catagoryChoice = "technology";
                            Toast.makeText(getActivity(), "Notification Set to default: Technology", Toast.LENGTH_LONG).show();
                        } else if(sportsRBtn.isChecked()){
                            catagoryChoice = "sports";
                            Toast.makeText(getActivity(), "Notification Set to default: Sports", Toast.LENGTH_LONG).show();
                        } else Toast.makeText(getActivity(), "Notification Set to default: General", Toast.LENGTH_LONG).show();

                        if(oneMinRbtn.isChecked()) {
                            scheduleNotification(60);
                        } else if(fifteenMinsRbtn.isChecked()) {
                            scheduleNotification(15 * 60);
                        } else if(thirtyMinRbtn.isChecked()){
                            scheduleNotification(30*60);
                        } else if(oneHourRbtn.isChecked()){
                            scheduleNotification(60*60);
                        } else {
                            Toast.makeText(getActivity(), "You must choose an option", Toast.LENGTH_SHORT).show();
                        }

                        alertDialog.dismiss();
                        cancelBtn.setVisibility(View.VISIBLE);
                        updateBtn.setVisibility(View.GONE);
                    }
                });

                dialogView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        cancelBtn = root.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),NotificationReciever.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);

                cancelBtn.setVisibility(View.GONE);
                updateBtn.setVisibility(View.VISIBLE);            }
        });

        return root;
    }

    private void scheduleNotification(int delay) {

        Intent intent = new Intent(getActivity(), NotificationReciever.class);

        intent.putExtra("seconds",delay);
        intent.putExtra("catagory",catagoryChoice);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay*1000;
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }
}
