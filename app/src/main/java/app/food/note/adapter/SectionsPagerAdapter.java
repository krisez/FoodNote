package app.food.note.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import app.food.note.R;
import app.food.note.ui.fragment.PlaceholderFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static String[] TAB_TITLES;
    private PlaceholderFragment[] mFragments = new PlaceholderFragment[4];

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        TAB_TITLES = new String[]{context.getString(R.string.tab_cold_storage),
                context.getString(R.string.tab_soft_freeze),
                context.getString(R.string.tab_freezing)};
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        PlaceholderFragment fragment = PlaceholderFragment.newInstance(TAB_TITLES[position]);
        mFragments[position] = fragment;
        return fragment;
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

    public void refreshData(String area){
        for (int i = 0; i < TAB_TITLES.length; i++) {
            if(TAB_TITLES[i].equals(area)){
                if(mFragments[i]!=null){
                    mFragments[i].refreshData();
                }
                break;
            }
        }
    }
}