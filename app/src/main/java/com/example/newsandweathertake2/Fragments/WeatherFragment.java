package com.example.newsandweathertake2.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsandweathertake2.Adapters.WeatherAdapter;
import com.example.newsandweathertake2.MainActivity;
import com.example.newsandweathertake2.Model.Weather;
import com.example.newsandweathertake2.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherFragment extends Fragment {
    private RecyclerView recyclerView;
    private WeatherAdapter adapter;
    private ArrayList<Weather> weatherList;
    private RequestQueue requestQueue;

    FusedLocationProviderClient client;
    double latitude,longitude;

    final String WHEATHER_SERVICE_LINK  = "https://api.weatherbit.io/v2.0/forecast/daily?";//lat=38.123&lon=-78.543&key=API_KEY
    final String API_KEY = "*api key number*";
    final int LOCATION_PERMISSION_REQUEST = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.recycler_view_fragment,container,false);
        if(Build.VERSION.SDK_INT>=23) {
            int hasLocationPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if(hasLocationPermission!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST);
            }
            else getLocation();
        }
        else getLocation();

        weatherList = new ArrayList<>();
        recyclerView =root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
        requestQueue = Volley.newRequestQueue(getActivity());

        return root;
    }

    private void getLocation() {

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        final LocationRequest request = LocationRequest.create();
        request.setInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_LOW_POWER);

        LocationCallback callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();

                //Web service
                latitude = location.getLatitude();
                longitude = location.getLongitude();


                final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WHEATHER_SERVICE_LINK +
                        "lat=" + latitude + "&lon=" + longitude + "&key=" + API_KEY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("city_name");
                            String country = response.getString("country_code");

                            JSONArray jsonArray = response.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String description = object.getJSONObject("weather").getString("description");
                                String icon = object.getJSONObject("weather").getString("icon");
                                String date = object.getString("datetime");
                                double temp = object.getDouble("temp");

                                weatherList.add(new Weather(name,country,icon,description,date,temp));
                            }

                            adapter = new WeatherAdapter(getActivity(),weatherList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(request);
                requestQueue.start();
            }
        };

        if(Build.VERSION.SDK_INT>=23 && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            client.requestLocationUpdates(request,callback,null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_PERMISSION_REQUEST) {
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Attention!").setMessage("You must give location permission to the app if you want to knbow the wheather")
                        .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:"+getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        }).show();
            }
            else getLocation();
        }
    }
}