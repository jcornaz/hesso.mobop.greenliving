package com.hesso.greenliving.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hesso.greenliving.R;

public class FragmentBudget extends AbstractFragment {

    @Override
    public int getNameId() {
	return R.string.fragment_budget_name;
    }

    @Override
    public int getIconId() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	return inflater.inflate( R.layout.frag_budget_entries, container, false );
    }
}
