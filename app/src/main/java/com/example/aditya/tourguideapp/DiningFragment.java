package com.example.aditya.tourguideapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DiningFragment extends Fragment {
    static ArrayList<Attraction> attractions = new ArrayList<>();
    RecyclerView attractionRecyclerView;
    AttractionViewHolder.AttractionListener listener;
    Context mContext;

    public DiningFragment() {
    }

    public static DiningFragment newInstance() {
        DiningFragment fragment = new DiningFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("DiningFragment", "onResume running");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.v("DiningFragment", "onAttach running");
        if (context instanceof AttractionViewHolder.AttractionListener) {
            listener = (AttractionViewHolder.AttractionListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("DiningFragment", "onCreate running");
        attractions = new Attraction().getDiningArray(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Log.v("DiningFragment", "onCreateView running");
        attractionRecyclerView = view.findViewById(R.id.recycler_list);
        attractionRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(getActivity());
        boolean isLandscapeMode = getResources().getBoolean(R.bool.is_in_landscape_mode);
        if (isLandscapeMode) {
            myLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        } else {
            myLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        if (attractions.size() > 0 & attractionRecyclerView != null) {
            attractionRecyclerView.setAdapter(new AttractionRecyclerViewAdapter(attractions, R.layout.vertical_one_column_item_list, getActivity(), listener));
        }
        attractionRecyclerView.setLayoutManager(myLinearLayoutManager);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("DiningFrag", "DiningFragment detached");
        listener = null;
    }
}