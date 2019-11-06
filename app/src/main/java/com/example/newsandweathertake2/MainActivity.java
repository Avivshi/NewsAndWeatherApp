package com.example.newsandweathertake2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.http.GET;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsandweathertake2.Adapters.WeatherAdapter;
import com.example.newsandweathertake2.Fragments.ArticleFragment;
import com.example.newsandweathertake2.Fragments.NotificationsFragment;
import com.example.newsandweathertake2.Fragments.WeatherFragment;
import com.example.newsandweathertake2.Model.Weather;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String ARTICLES_FRAGMENT_TAG = "article_fragment";
    final String WEATHER_FRAGMENT_TAG = "weather_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().add(R.id.news_layout,new ArticleFragment(),ARTICLES_FRAGMENT_TAG).addToBackStack(null).commit();
        getFragmentManager().beginTransaction().add(R.id.notif_layout,new NotificationsFragment(),ARTICLES_FRAGMENT_TAG).addToBackStack(null).commit();
        getFragmentManager().beginTransaction().add(R.id.weather_layout,new WeatherFragment(),WEATHER_FRAGMENT_TAG).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }

        else super.onBackPressed();
    }
}
