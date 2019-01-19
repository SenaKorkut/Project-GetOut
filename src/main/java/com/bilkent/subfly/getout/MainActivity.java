package com.bilkent.subfly.getout;

/*
 * Main activity that runs the program
 */

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import Adapter.SectionStatePagerAdapter;

public class MainActivity extends AppCompatActivity {

    // Tag for alert on terminal
    private static final String TAG = "Main Activity";

    //Variables
    private ViewPager mViewPager;
    private SectionStatePagerAdapter mSectionStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialization
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Printing terminal dialog
        Log.d(TAG, "onCreate: Started...");

        //Avoiding keybord pushing the instances on the screen when oppened
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Toolbar support
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //View pager setup
        mViewPager = (ViewPager) findViewById(R.id.container);
        mSectionStatePagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionStatePagerAdapter);
        mViewPager.setCurrentItem(1);
    }

    /**
     * Sets up fragments
     * @return main fragment that needs to be shown
     */
    public ViewPager getmViewPager(){
        return mViewPager;
    }

    public void startFragment(int i){
        mViewPager.setCurrentItem(0);
    }
}
