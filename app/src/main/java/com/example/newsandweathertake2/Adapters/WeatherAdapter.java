package com.example.newsandweathertake2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsandweathertake2.Model.Weather;
import com.example.newsandweathertake2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>{

    private Context context;
    private List<Weather> weatherList;

    public WeatherAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_view,parent,false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        Weather weather = weatherList.get(position);

        holder.firsTv.setText(weather.getTemp()+" °C");
        holder.secondTv.setText(weather.getDescription());
        holder.thirdTv.setText(weather.getCity_name()+", "+weather.getCountry_code());
        holder.dateTv.setText(weather.getDate());
        Picasso.get().load("https://www.weatherbit.io/static/img/icons/"+weather.getIcon()+".png").into(holder.iconImg);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        TextView firsTv,secondTv,thirdTv,dateTv;
        ImageView iconImg;


        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            firsTv = itemView.findViewById(R.id.textview_one);
            secondTv = itemView.findViewById(R.id.textview_two);
            thirdTv = itemView.findViewById(R.id.textview_three);
            iconImg = itemView.findViewById(R.id.weather_img);
            dateTv = itemView.findViewById(R.id.date_tv);

        }
    }
}
