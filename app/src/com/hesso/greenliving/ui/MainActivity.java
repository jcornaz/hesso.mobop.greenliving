package com.hesso.greenliving.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.hesso.greenliving.R;

//Fragments swiping working !!! Add fragments in createFragments()

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	
	private List<AFragment> fragments = new ArrayList<AFragment>();
	private PagerAdapter pagerAdapter;
	private ActionBar actionBar;
	private ViewPager viewPager;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialization :
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager)findViewById(R.id.pager);

        createFragments();
        populateTabs();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        
        //select tab on swipe
        viewPager.setOnPageChangeListener(this);
    }
    
    private void createFragments() {
    	fragments.add(new FragmentBudget());
    	fragments.add(new FragmentTransactions());
    	//Add new Fragments here
    }
    
    private void populateTabs() {
    	for(AFragment fragment : fragments) {
    		//tabs with text OR icons
    		actionBar.addTab(actionBar.newTab().setText(fragment.getNameId()).setTabListener(this));
    		//actionBar.addTab(actionBar.newTab().setIcon(fragment.getIconId()).setTabListener(this));
    	}
    }

    //Options menu (todo)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //Manage tabs (ActionBar.TabListener)
	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
	}
	
	//ViewPager swiping (ViewPager.OnPageChangeListener)
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		actionBar.setSelectedNavigationItem(position);
	}
    
	
	private class PagerAdapter extends FragmentPagerAdapter {
		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			return fragments.get(index);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

}
