package com.hesso.greenliving.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.hesso.greenliving.R;
import com.hesso.greenliving.dao.PersistenceManager;
import com.hesso.greenliving.model.Account;

//Fragments swiping working !!! Add fragments in createFragments()

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

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

    public static final DecimalFormat DEC_FORMAT = new DecimalFormat( "#0.00" );

    private static MainActivity instance;

    public static MainActivity getInstance() {
	return instance;
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
	// Lock portrait
	this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
	instance = this;

	Log.d( "debug", "MainActivity#onCreate" );

	this.setContentView( R.layout.activity_main );

	// Initialization :
	this.actionBar = this.getActionBar();
	this.actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );
	this.viewPager = (ViewPager) this.findViewById( R.id.pager );

	this.createFragments();
	this.populateTabs();
	this.pagerAdapter = new PagerAdapter( this.getSupportFragmentManager() );
	this.viewPager.setAdapter( this.pagerAdapter );

	// select tab on swipe
	this.viewPager.setOnPageChangeListener( this );

	PersistenceManager.start( this );

	// TODO à supprimer une fois l'app terminée
	// TestManager.createFakeModelIfNecessary();

	// this.startActivity( new Intent( this, DialogCreditExpense.class ) );

    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
    }

    private void createFragments() {
	this.transactionFragment = new TransactionsFragment();
	this.fragments.add( new AccountFragment() );
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
	if( position != this.fragments.indexOf( this.transactionFragment ) ) {
	    this.transactionFragment.setAccount( null );
	}
    }

    public void openTransactions( Account budgetEntry ) {
	this.viewPager.setCurrentItem( this.fragments.indexOf( this.transactionFragment ) );
	this.transactionFragment.setAccount( budgetEntry );
    }
}
