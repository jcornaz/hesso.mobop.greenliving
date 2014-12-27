package com.hesso.greenliving.ui;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;

public class FragmentTransactions extends AbstractFragment implements Observer {

    private TransactionListAdapter adapter;

    @Override
    public int getNameId() {
	return R.string.fragment_transactions_name;
    }

    @Override
    public int getIconId() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	ListView res = (ListView) inflater.inflate( R.layout.frag_transactions, container, false );
	this.adapter = new TransactionListAdapter( container.getContext() );
	res.setAdapter( this.adapter );
	
	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );
	
	return res;
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.adapter.setList( Budget.getInstance().getTransactions() );
    }
}
