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

public class VenuesFragment extends Fragment {
    static ArrayList<Attraction> attr = new ArrayList<>();
    RecyclerView attractionRecyclerView;
    AttractionViewHolder.AttractionListener listener;
    Context mContext;

    public VenuesFragment() {
    }

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("VenuesFragment", "onResume running");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.v("VenuesFragment", "onAttach running");
        if (context instanceof AttractionViewHolder.AttractionListener) {
            listener = (AttractionViewHolder.AttractionListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("VenuesFragment", "onCreate running");
        attr = new Attraction().getVenuesArray(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Log.v("VenuesFragment", "onCreateView running");
        attractionRecyclerView = view.findViewById(R.id.recycler_list);
        attractionRecyclerView.setHasFixedSize(true);
        LinearLayoutManager myLinearLayoutManager = new LinearLayoutManager(getActivity());
        boolean isLandscapeMode = getResources().getBoolean(R.bool.is_in_landscape_mode);
        if (isLandscapeMode) {
            myLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        } else {
            myLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        if (attr.size() > 0 & attractionRecyclerView != null) {
            attractionRecyclerView.setAdapter(new AttractionRecyclerViewAdapter(attr, R.layout.vertical_one_column_item_list, getActivity(), listener));
        }
        attractionRecyclerView.setLayoutManager(myLinearLayoutManager);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("VenuesFrag", "VenuesFragment detached");
        listener = null;
    }
}