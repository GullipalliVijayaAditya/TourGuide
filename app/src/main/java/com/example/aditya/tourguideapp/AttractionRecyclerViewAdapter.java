package com.example.aditya.tourguideapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AttractionRecyclerViewAdapter extends RecyclerView.Adapter<AttractionViewHolder> {
    private int resourceLayoutID;
    private Context context;
    private ArrayList<Attraction> attrsList;
    private AttractionViewHolder.AttractionListener attrListener;

    public AttractionRecyclerViewAdapter(ArrayList<Attraction> attrData, int resource, Context vContext, AttractionViewHolder.AttractionListener attrListener) {
        attrsList = attrData;
        context = vContext;
        resourceLayoutID = resource;
        this.attrListener = attrListener;
        Log.v("RVAdaptor", "AttractionRecyclerViewAdapter constructor was called");
    }

    @Override
    public void onBindViewHolder(final AttractionViewHolder holder, int position) {
        AttractionViewHolder viewHolder = (AttractionViewHolder) holder;
        viewHolder.setItem(attrsList.get(position));
        Log.v("RVAdaptor", "OnBindViewHolder was called");
    }

    @Override
    public AttractionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceLayoutID, parent, false);
        Log.v("RVAdaptor", "OnCreateViewHolder was called");
        return new AttractionViewHolder(view, context, attrListener, this);
    }

    @Override
    public int getItemCount() {
        return attrsList.size();
    }

    public void updateAttractionList(int position) {
        notifyItemChanged(position);
    }
}