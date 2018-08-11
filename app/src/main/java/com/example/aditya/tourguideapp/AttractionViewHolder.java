package com.example.aditya.tourguideapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AttractionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public static final String MY_PREFERENCES = "mypref";
    public TextView attrDescriptionTextView;
    public ImageView attrImageView;
    public ImageView likeImageView;
    public TextView attrNameTextView;
    public Attraction attrItem;
    public Context context;
    AttractionRecyclerViewAdapter attrRecyclerViewAdapter;
    AttractionListener listener;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;

    public AttractionViewHolder(View v, Context vContext, final AttractionListener listener, AttractionRecyclerViewAdapter adapter) {
        super(v);
        attrRecyclerViewAdapter = adapter;
        context = vContext;
        this.listener = listener;
        attrDescriptionTextView = v.findViewById(R.id.attr_snippet);
        attrDescriptionTextView.setOnClickListener(this);
        attrImageView = v.findViewById(R.id.attr_image);
        attrImageView.setOnClickListener(this);
        attrNameTextView = v.findViewById(R.id.attr_name);
        attrNameTextView.setOnClickListener(this);
        likeImageView = v.findViewById(R.id.like_button);
        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                int id = (int) likeImageView.getTag();
                if (id == R.drawable.like) {
                    likeImageView.setTag(R.drawable.liked);
                    likeImageView.setImageResource(R.drawable.liked);
                    DrawableCompat.setTint(likeImageView.getDrawable(), ContextCompat.getColor(context, R.color.secondaryLightColor));
                    Toast.makeText(context, attrNameTextView.getText() + " added to favorites", Toast.LENGTH_SHORT).show();
                    if (attrItem != null) {
                        listener.onLikeButtonClicked(attrItem, position, true);
                    }
                } else {
                    likeImageView.clearColorFilter();
                    likeImageView.setTag(R.drawable.like);
                    likeImageView.setImageResource(R.drawable.like);
                    Toast.makeText(context, attrNameTextView.getText() + " removed from " + "favorites", Toast.LENGTH_SHORT).show();
                    if (attrItem != null) {
                        listener.onLikeButtonClicked(attrItem, position, false);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (attrItem == null) {
        } else {
            listener.onAttractionClicked(attrItem, position, attrRecyclerViewAdapter);
        }
    }

    public void setItem(Attraction item) {
        this.attrItem = item;
        attrNameTextView.setText(attrItem.getAttractionName());
        String blurbText = attrItem.getAttractionBlurb();
        attrDescriptionTextView.setText(blurbText);
        attrImageView.setImageResource(attrItem.getAttractionImageId());
        attrImageView.setTag(attrItem.getAttractionImageId());
        String favorite = attrItem.getAttractionName();
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, android.content.Context.MODE_PRIVATE);
        if (sharedPreferences.contains(favorite)) {
            boolean wasAttractionLiked = sharedPreferences.getBoolean(favorite, false);
            if (wasAttractionLiked) {
                likeImageView.setImageResource(R.drawable.liked);
                likeImageView.setTag(R.drawable.liked);
                DrawableCompat.setTint(likeImageView.getDrawable(), ContextCompat.getColor(context, R.color.secondaryLightColor));
            }
        } else {
            likeImageView.setTag(R.drawable.like);
            likeImageView.setImageResource(R.drawable.like);
        }
    }

    public interface AttractionListener {
        void onLikeButtonClicked(Attraction attrItem, int position, boolean wasLiked);

        void onAttractionClicked(Attraction attrItem, int position, AttractionRecyclerViewAdapter adapter);
    }


}