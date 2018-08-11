package com.example.aditya.tourguideapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class DetailsFragment extends Fragment {


    SharedPreferences sharedPreferences;
    public static final String MY_PREFERENCES = "mypref";
    private static final String ATTRACTION_CATEGORY_KEY = "Home";
    private static final String ITEM_POSITION_KEY = "item_position_key";
    private static final String ATTRACTION_LIKED_KEY = "attr_liked_key";
    private static final int HOME = 0;
    private static final int DINING = 1;
    private static final int SHOPPING = 2;
    private static final int VENUES = 3;
    private static final int NIGHTLIFE = 4;
    private int mCategory = HOME;
    private int mPosition = 0;
    private Attraction currentAttraction;
    private boolean viewMoreInfo;
    private boolean isFavorite = false;
    private Context mContext;
    TextView attrBlurb, attrDescription, attrName, attrAddress,
            attrPhone, attrWebsite, moreInfoTextView;
    LinearLayout moreInfoTable;
    TextView callButton, webButton, mapsButton, shareButton, likeButton;
    ImageView attrImage;
    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        void onClickDetailsLikeButton(int category, int position, boolean isFavorite);
        void onClickDetailsCallButton(int category, int position, String phoneLink);
        void onClickDetailsWebButton(int category, int position, String website);
        void onClickDetailsShareButton(int category, int position, String attrInfo);
        void onClickDetailsMapButton(int category, int position, String geolocation);
        void detachDetailScreen();
    }

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(int category, int position, boolean isFavorited) {

        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ATTRACTION_CATEGORY_KEY, category);
        args.putInt(ITEM_POSITION_KEY, position);
        args.putBoolean(ATTRACTION_LIKED_KEY, isFavorited);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getInt(ATTRACTION_CATEGORY_KEY);
            mPosition = getArguments().getInt(ITEM_POSITION_KEY);
            isFavorite = getArguments().getBoolean(ATTRACTION_LIKED_KEY);
        }

        currentAttraction = new Attraction().getDetailAttraction(mContext, mCategory, mPosition);
        if (currentAttraction == null) {
            Log.v("DetailsFragment", "currentAttraction is null");
        } else {
            Log.v("DetailsFragment", "currentAttraction is " + currentAttraction.getAttractionName());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attraction_details_fragment, container, false);
        webButton = view.findViewById(R.id.web_button);
        mapsButton = view.findViewById(R.id.map_button);
        callButton = view.findViewById(R.id.call_button);
        shareButton = view.findViewById(R.id.share_button);
        moreInfoTextView = view.findViewById(R.id.more_info);
        moreInfoTable = view.findViewById(R.id.more_info_table);
        attrName = view.findViewById(R.id.attr_name);
        attrImage = view.findViewById(R.id.attr_image);
        attrPhone = view.findViewById(R.id.attr_phone);
        attrAddress = view.findViewById(R.id.attr_address);
        attrBlurb = view.findViewById(R.id.attr_snippet);
        attrDescription = view.findViewById(R.id.attr_description);
        attrWebsite = view.findViewById(R.id.attr_website_text);
        viewMoreInfo = false;
        moreInfoTable.setVisibility(View.GONE);
        attrImage.setImageResource(currentAttraction.getAttractionImageId());
        attrAddress.setText(currentAttraction.getAttractionAddress());
        attrPhone.setText(currentAttraction.getAttractionPhoneNumberText());
        attrName.setText(currentAttraction.getAttractionName());
        attrBlurb.setText(currentAttraction.getAttractionBlurb());
        attrDescription.setText(currentAttraction.getAttractionDescription());
        attrWebsite.setText(currentAttraction.getAttractionWebsite());
        likeButton = view.findViewById(R.id.like_button);
        sharedPreferences = mContext.getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        if (isFavorite) {

            Log.v("DetailsFrag", currentAttraction.getAttractionName() + " is a favorite");
            likeButton.setText(R.string.fav_saved);
            Drawable heart = getResources().getDrawable(R.drawable.liked);
            heart.setColorFilter(getResources().getColor(R.color.secondaryLightColor), PorterDuff.Mode
                    .SRC_ATOP);
            likeButton.setCompoundDrawablesWithIntrinsicBounds(null, heart, null, null);

        } else {
            likeButton.setText(R.string.fav_save);
            Log.v("DetailsFrag", currentAttraction.getAttractionName() + " is NOT a favorite");
            Drawable heart = getResources().getDrawable(R.drawable.like);
            heart.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
            likeButton.setCompoundDrawablesWithIntrinsicBounds(null, heart, null, null);
        }

        moreInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewMoreInfo) {
                    moreInfoTable.setVisibility(View.VISIBLE);
                    moreInfoTextView.setText(R.string.less_info_link);
                    viewMoreInfo = true;
                } else {
                    moreInfoTable.setVisibility(View.GONE);
                    moreInfoTextView.setText(R.string.more_info_link);
                    viewMoreInfo = false;
                }
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDetailsCallButton(mCategory, mPosition, currentAttraction.getAttractionPhoneNumberLink());
            }
        });

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDetailsWebButton(mCategory, mPosition, currentAttraction.getAttractionWebsite());
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDetailsMapButton(mCategory, mPosition, currentAttraction.getAttractionCoordinates());
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickDetailsShareButton(mCategory, mPosition, currentAttraction.toString());
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFavorite) {

                    isFavorite = false;

                    Drawable heart = getResources().getDrawable(R.drawable.like);
                    heart.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(null, heart, null, null);

                    likeButton.setText(R.string.fav_save);
                } else {

                    isFavorite = true;
                    Drawable heart = getResources().getDrawable(R.drawable.liked);
                    heart.setColorFilter(getResources().getColor(R.color.secondaryLightColor), PorterDuff.Mode
                            .SRC_ATOP);
                    likeButton.setCompoundDrawablesWithIntrinsicBounds(null, heart, null, null);
                    likeButton.setText(R.string.fav_saved);
                }
                mListener.onClickDetailsLikeButton(mCategory, mPosition, isFavorite);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            Log.v("detailsfragment", "details screen onAttach has run");
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.detachDetailScreen();
        mListener = null;
    }
}