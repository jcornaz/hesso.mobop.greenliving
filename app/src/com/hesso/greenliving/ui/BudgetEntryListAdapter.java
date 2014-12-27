package com.hesso.greenliving.ui;

import android.content.Context;
import android.view.ViewGroup;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.BudgetEntry;

public class BudgetEntryListAdapter extends EntityListAdapter<BudgetEntry, BudgetEntryView> {

    public BudgetEntryListAdapter( MainActivity mainActivity, Context context ) {
	super( mainActivity, context, R.layout.item_budget );
    }

    @Override
    protected BudgetEntryView inflateItem( ViewGroup parent ) {
	return BudgetEntryView.inflate( parent );
    }
}
