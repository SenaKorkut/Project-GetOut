package Adapter;

/*
 * Adapter class for make fragments functional on main page
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bilkent.subfly.getout.FragmentCategories;
import com.bilkent.subfly.getout.FragmentCreate;
import com.bilkent.subfly.getout.FragmentMain;

import java.util.ArrayList;

public class SectionStatePagerAdapter extends FragmentPagerAdapter {

    //Variables
    private ArrayList<Fragment> pages;

    public SectionStatePagerAdapter(FragmentManager fm) {
        //Initialization
        super(fm);
        pages = new ArrayList<>();
        FragmentMain main = new FragmentMain();
        pages.add(main.newInstance());
        FragmentCategories categories = new FragmentCategories();
        pages.add(categories.newInstance());
        FragmentCreate create = new FragmentCreate();
        pages.add(create.newInstance());
    }

    /**
     * Method to make fragments change on swipes
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return pages.get(2);
            case 1:
                return pages.get(0);
            case 2:
                return pages.get(1);
            default:
                return pages.get(0);
        }
    }

    /**
     * Method that returns page number on main menu
     * @return
     */
    @Override
    public int getCount(){
       return 3;
    }
}
