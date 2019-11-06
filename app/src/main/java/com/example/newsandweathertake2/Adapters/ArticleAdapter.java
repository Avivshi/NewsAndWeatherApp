package com.example.newsandweathertake2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newsandweathertake2.Model.Article;
import com.example.newsandweathertake2.R;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Target;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private Context context;
    private List<Article> articles;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_view,parent,false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(view);
        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        Article article = articles.get(position);

        holder.titleTv.setText(article.getTitle());
        holder.descriptionTv.setText(article.getDescription());

        Glide.with(context).load(article.getUrlToImage()).apply(new RequestOptions().override(100,100)).into(holder.articleIv);

        //Glide
        //    .with(context)
        //    .load(path)
        //    .apply(new RequestOptions().override(600, 200))
        //    .into(imageViewResizeCenterCrop);
       // Picasso.get().load(article.getUrlToImage()).into(holder.articleIv);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        TextView titleTv;
        TextView descriptionTv;
        ImageView articleIv;


        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.article_title);
            descriptionTv = itemView.findViewById(R.id.article_details);
            articleIv = itemView.findViewById(R.id.article_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null){
                        int position = getAdapterPosition();

                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}