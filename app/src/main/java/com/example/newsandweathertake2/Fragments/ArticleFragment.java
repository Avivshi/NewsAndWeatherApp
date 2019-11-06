package com.example.newsandweathertake2.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsandweathertake2.Adapters.ArticleAdapter;
import com.example.newsandweathertake2.Model.Article;
import com.example.newsandweathertake2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleFragment extends Fragment implements ArticleAdapter.OnItemClickListener {
    public static final String EXTRA_URL = "image_url";
    public static final String EXTRA_AUTHOR = "author";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DETAILS = "details";
    public static final String EXTRA_TIME = "time";

    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private ArrayList<Article> articleList;
    private RequestQueue requestQueue;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.recycler_view_fragment,container,false);


        recyclerView =root.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        articleList =new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());
        parseJason();

        return root;

    }

    private void parseJason(){

        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=*api key number*";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("articles");

                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject hit = jsonArray.getJSONObject(i);

                                String title = hit.getString("title");
                                String urlToImage = hit.getString("urlToImage");
                                String description = hit.getString("description");
                                String url = hit.getString("url");

                                articleList.add(new Article(title,urlToImage,description,url));
                            }

                            adapter = new ArticleAdapter(getActivity(),articleList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(ArticleFragment.this);
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


    public void onItemClick(int position) {

        Article clickedItem = articleList.get(position);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(clickedItem.getUrl()));
        startActivity(intent);

    }
}