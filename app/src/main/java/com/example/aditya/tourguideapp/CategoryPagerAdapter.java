package com.example.aditya.tourguideapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class CategoryPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private CategoryPagerAdapter categoryPagerAdapter;

    private MyOnBackStackChangedListener listener;

    public CategoryPagerAdapter(Context context, FragmentManager fragmentmanager) {
        super(fragmentmanager);
        mContext = context;
        if (context instanceof MyOnBackStackChangedListener) {
            listener = (MyOnBackStackChangedListener) context;
        }
        fragmentmanager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.v("CategoryPagerAdapter", "OnBackStackListener changed");
                listener.fragmentChanged();
            }
        });
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new DiningFragment();
        } else if (position == 2) {
            return new ShoppingFragment();
        } else if (position == 3) {
            return new VenuesFragment();
        } else {
            return new NightlifeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.home_fragment);
        } else if (position == 1) {
            return mContext.getString(R.string.venues_fragment);
        } else if (position == 2) {
            return mContext.getString(R.string.nightlife_fragment);
        } else if (position == 3) {
            return mContext.getString(R.string.dine_fragment);
        } else {
            return mContext.getString(R.string.shop_fragment);
        }
    }

    public interface MyOnBackStackChangedListener {
        void fragmentChanged();
    }
}
