package com.example.aditya.tourguideapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AttractionViewHolder.AttractionListener, DetailsFragment.OnFragmentInteractionListener, CategoryPagerAdapter.MyOnBackStackChangedListener {
    public static final String MY_PREFERENCES = "mypref";
    private static final int HOME = 0;
    private static final int DINING = 1;
    private static final int SHOPPING = 2;
    private static final int VENUES = 3;
    private static final int NIGHTLIFE = 4;
    private static final int DETAIL_SCREEN = 5;
    SharedPreferences sharedPreferences;
    TextView sloganTextView;
    LinearLayout tabsLinearLayoutView;
    FrameLayout detailsFrameLayoutView;
    ViewPager viewPager;
    Toolbar mainToolbar;
    SharedPreferences.Editor sharedPrefEditor;
    Attraction currentAttractionItem;
    RelativeLayout topHalfView;
    CategoryPagerAdapter adapter;
    int currentAdapterPosition = 0;
    AttractionRecyclerViewAdapter attrRecyclerViewAdapter;
    boolean wasLikeButtonPressedInDetailsFragment = false;
    int detailsFragmentParent = HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        topHalfView = findViewById(R.id.top_half_tab_layout);
        tabsLinearLayoutView = findViewById(R.id.main_tabs_layout);
        detailsFrameLayoutView = findViewById(R.id.attr_details_fragment_container);
        sloganTextView = findViewById(R.id.category_slogan);
        sloganTextView.setText(R.string.home_slogan);
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mainToolbar.setLogo(R.drawable.app_title);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CategoryPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.v("MainActivity", "Page " + position + "was scrolled");
            }

            @Override
            public void onPageSelected(int position) {
                Log.v("MainActivity", "Page is" + position);
                switch (position) {
                    case HOME:
                        sloganTextView.setText(R.string.home_slogan);
                        break;
                    case DINING:
                        sloganTextView.setVisibility(View.VISIBLE);
                        sloganTextView.setText(R.string.dine_slogan);
                        break;
                    case SHOPPING:
                        sloganTextView.setText(R.string.shop_slogan);
                        break;
                    case VENUES:
                        sloganTextView.setText(R.string.venues_slogan);
                        break;
                    case NIGHTLIFE:
                        sloganTextView.setText(R.string.nightlife_slogan);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.v("MainActivity", "Page state is " + state);
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onLikeButtonClicked(Attraction attrItem, int position, boolean wasLiked) {
        String favorite = attrItem.getAttractionName();
        sharedPrefEditor = sharedPreferences.edit();

        if (wasLiked) {
            sharedPrefEditor.putBoolean(favorite, true);
            sharedPrefEditor.apply();
        } else {
            sharedPrefEditor.remove(favorite).commit();
        }
        if (sharedPreferences.contains(favorite)) {
        }
    }

    @Override
    public void onAttractionClicked(Attraction attrItem, int position, AttractionRecyclerViewAdapter listAdapter) {
        attrRecyclerViewAdapter = listAdapter;
        currentAttractionItem = attrItem;
        String attrName = attrItem.getAttractionName();
        boolean wasAttractionLiked = false;
        if (sharedPreferences.contains(attrName)) {
            wasAttractionLiked = true;
        }
        detailsFragmentParent = viewPager.getCurrentItem();
        Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        DetailsFragment detailsFragment = DetailsFragment.newInstance(detailsFragmentParent, position, wasAttractionLiked);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.attr_details_fragment_container, detailsFragment, "details").addToBackStack(null).commit();
        categoryViewSetup(DETAIL_SCREEN);
    }

    @Override
    public void detachDetailScreen() {
        Log.v("MainActivity", "Detail Screen detached");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mainToolbar.setLogo(R.drawable.app_title);
        mainToolbar.setBackgroundColor(Color.TRANSPARENT);
        detailsFrameLayoutView.setVisibility(View.GONE);
        tabsLinearLayoutView.setVisibility(View.VISIBLE);
        sloganTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mainToolbar.requestLayout();
        mainToolbar.getLayoutParams().height = 147;
        mainToolbar.requestLayout();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    public void onClickDetailsLikeButton(int category, int position, boolean isFavorite) {
        wasLikeButtonPressedInDetailsFragment = true;
        currentAdapterPosition = position;
        Attraction attrItem = new Attraction();
        if (detailsFragmentParent == HOME) {
            attrItem = attrItem.getDetailAttraction(this, HOME, position);
            Log.v("MainActivity", "The parent fragment was HOME");
        } else {
            attrItem = attrItem.getDetailAttraction(this, category, position);
            Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        }
        String name = attrItem.getAttractionName();
        Log.v("MainActivity", "clicked item was " + name);
        sharedPrefEditor = sharedPreferences.edit();
        if (isFavorite) {
            Toast.makeText(this, name + " was added to Favorites!", Toast.LENGTH_SHORT).show();
            sharedPrefEditor.putBoolean(name, true);
            sharedPrefEditor.apply();
        } else {
            Toast.makeText(this, name + " was removed from " + "Favorites!", Toast.LENGTH_SHORT).show();
            Log.v("MainActivity", "Removing Key name " + name + " from favorites.");
            sharedPrefEditor.remove(name).commit();
        }
    }

    public void onClickDetailsCallButton(int category, int position, String phone) {

        Attraction attrItem = new Attraction();
        if (detailsFragmentParent == HOME) {
            attrItem = attrItem.getDetailAttraction(this, HOME, position);
            Log.v("MainActivity", "The parent fragment was HOME");
        } else {
            attrItem = attrItem.getDetailAttraction(this, category, position);
            Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        }
        String number = attrItem.getAttractionPhoneNumberLink();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onClickDetailsWebButton(int category, int position, String website) {
        Attraction attrItem = new Attraction();
        if (detailsFragmentParent == HOME) {
            attrItem = attrItem.getDetailAttraction(this, HOME, position);
            Log.v("MainActivity", "The parent fragment was HOME");
        } else {
            attrItem = attrItem.getDetailAttraction(this, category, position);
            Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        }
        String url = getString(R.string.url_prefix);
        url = url + attrItem.getAttractionWebsite();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onClickDetailsMapButton(int category, int position, String coords) {
        Attraction attrItem = new Attraction();
        if (detailsFragmentParent == HOME) {
            attrItem = attrItem.getDetailAttraction(this, HOME, position);
            Log.v("MainActivity", "The parent fragment was HOME");
        } else {
            attrItem = attrItem.getDetailAttraction(this, category, position);
            Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        }
        String location = attrItem.getAttractionCoordinates();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(location));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onClickDetailsShareButton(int category, int position, String attrInfo) {
        Attraction attrItem = new Attraction();
        if (detailsFragmentParent == HOME) {
            attrItem = attrItem.getDetailAttraction(this, HOME, position);
            Log.v("MainActivity", "The parent fragment was HOME");
        } else {
            attrItem = attrItem.getDetailAttraction(this, category, position);
            Log.v("MainActivity", "The parent fragment was " + detailsFragmentParent);
        }
        String informationToShare = attrItem.toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, informationToShare);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share_info)));
    }

    public void categoryViewSetup(int category) {
        if (category == DETAIL_SCREEN) {
            mainToolbar.requestLayout();
            mainToolbar.getLayoutParams().height = 180;
            mainToolbar.requestLayout();
            getSupportActionBar().setDisplayUseLogoEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mainToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.primaryTextColor), PorterDuff.Mode.SRC_ATOP);
            mainToolbar.setBackgroundResource(R.drawable.scrim);
            mainToolbar.setTitleTextColor(Color.WHITE);
            mainToolbar.setTitle(currentAttractionItem.getAttractionName());
            sloganTextView.setVisibility(View.GONE);
            tabsLinearLayoutView.setVisibility(View.GONE);
            detailsFrameLayoutView.setVisibility(View.VISIBLE);

        } else {
            if (category == HOME) {
                Log.v("MainActivity", "Update to HOME themed views");
            } else if (category == DINING) {
                Log.v("MainActivity", "Update to DINING themed views");
            } else if (category == SHOPPING) {
                Log.v("MainActivity", "Update to SHOPPING themed views");
            } else if (category == VENUES) {
                Log.v("MainActivity", "Update to VENUES themed views");
            } else {
                Log.v("MainActivity", "Update to NIGHTLIFE themed views");
            }
        }
    }

    @Override
    public void fragmentChanged() {
        if (wasLikeButtonPressedInDetailsFragment) {
            attrRecyclerViewAdapter.updateAttractionList(currentAdapterPosition);
            wasLikeButtonPressedInDetailsFragment = false;
        }
    }

    public void resetFavorites() {
        sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.clear();
        sharedPrefEditor.apply();
    }
}