package app.food.note.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import app.food.note.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static String[] TAB_TITLES;
    private static String[] TAB_ZONE = {"ice","dry","seasoning","normal"};

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        TAB_TITLES = new String[]{context.getString(R.string.tab_ice_box),
                context.getString(R.string.tab_dry_box),
                context.getString(R.string.tab_seasoning_box),
                context.getString(R.string.tab_normal_zone)};
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(TAB_ZONE[position]);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}