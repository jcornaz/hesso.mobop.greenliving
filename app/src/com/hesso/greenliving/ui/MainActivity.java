package com.hesso.greenliving.ui;

import java.text.DecimalFormat;
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
import android.util.Log;
import android.view.MenuItem;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.test.TestManager;

//Fragments swiping working !!! Add fragments in createFragments()

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

	static final DecimalFormat DEC_FORMAT = new DecimalFormat( "#0.00" );

	private static boolean createModel = true;

	private class PagerAdapter extends FragmentPagerAdapter {
		public PagerAdapter( FragmentManager fm ) {
			super( fm );
		}

		@Override
		public Fragment getItem( int index ) {
			return MainActivity.this.fragments.get( index );
		}

		@Override
		public int getCount() {
			return MainActivity.this.fragments.size();
		}
	}

	private List<AbstractFragment> fragments = new ArrayList<AbstractFragment>();
	private PagerAdapter pagerAdapter;
	private ActionBar actionBar;
	private ViewPager viewPager;
	private TransactionsFragment transactionFragment;

	public MainActivity() {
		super();
	}

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		Log.d( "debug", "MainActivity#onCreate" );

		this.setContentView( R.layout.activity_main );
		// Initialization :
		this.actionBar = this.getActionBar();
		this.actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
		/*this.actionBar.setDisplayShowHomeEnabled(false);
		this.actionBar.setDisplayShowTitleEnabled(false);*/
		this.viewPager = (ViewPager) this.findViewById( R.id.pager );

		this.createFragments();
		this.populateTabs();
		this.pagerAdapter = new PagerAdapter( this.getSupportFragmentManager() );
		this.viewPager.setAdapter( this.pagerAdapter );

		// select tab on swipe
		this.viewPager.setOnPageChangeListener( this );

		// Create a fake model
		if( createModel ) {
			TestManager.createFakeModel();
			createModel = false;
		}
	}

	private void createFragments() {
		this.transactionFragment = new TransactionsFragment( this );

		this.fragments.add( new AccountFragment( this ) );
		this.fragments.add( this.transactionFragment );
		// Add new Fragments here
	}

	private void populateTabs() {
		for( AbstractFragment fragment : this.fragments ) {
			// tabs with text OR icons
			this.actionBar.addTab( this.actionBar.newTab().setText( fragment.getNameId() ).setTabListener( this ) );
			// actionBar.addTab(actionBar.newTab().setIcon(fragment.getIconId()).setTabListener(this));
		}
	}

	// Options menu (todo)
	/*@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if( id == R.id.action_settings ) {
			return true;
		}
		return super.onOptionsItemSelected( item );
	}

	// Manage tabs (ActionBar.TabListener)
	@Override
	public void onTabSelected( Tab tab, android.app.FragmentTransaction ft ) {
		this.viewPager.setCurrentItem( tab.getPosition() );
	}

	@Override
	public void onTabUnselected( Tab tab, android.app.FragmentTransaction ft ) {
	}

	@Override
	public void onTabReselected( Tab tab, android.app.FragmentTransaction ft ) {
	}

	// ViewPager swiping (ViewPager.OnPageChangeListener)
	@Override
	public void onPageScrollStateChanged( int arg0 ) {
	}

	@Override
	public void onPageScrolled( int arg0, float arg1, int arg2 ) {
	}

	@Override
	public void onPageSelected( int position ) {
		this.actionBar.setSelectedNavigationItem( position );
		Log.i( "position", String.valueOf( position ) );
		if( position != this.fragments.indexOf( this.transactionFragment ) ) {
			this.transactionFragment.setBudgetEntry( null );
		}
	}

	public void openTransactions( Account budgetEntry ) {
		this.viewPager.setCurrentItem( this.fragments.indexOf( this.transactionFragment ) );
		this.transactionFragment.setBudgetEntry( budgetEntry );
	}
}
