package com.facebook.kshia.nytsearch.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.kshia.nytsearch.Article;
import com.facebook.kshia.nytsearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kshia on 6/20/16.
 */
public class ArticleRecyclerAdapter extends
        RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {//implements Target {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivThumbnail;
        public TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivThumbnail = (ImageView)itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }

    }

    // Store a member variable for the contacts
    private List<Article> articles;

    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public ArticleRecyclerAdapter(Context context, List<Article> articleList) {
        mContext = context;
        articles = articleList;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ArticleRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleRecyclerAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Article article = articles.get(position);

        // Set item views based on the data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline());

        ImageView ivThumbnail = viewHolder.ivThumbnail;
        ivThumbnail.setImageResource(0);

        String thumbnailUrl = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnailUrl)) {
            Picasso.with(ivThumbnail.getContext()).load(thumbnailUrl).into(ivThumbnail);
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return articles.size();
    }
}