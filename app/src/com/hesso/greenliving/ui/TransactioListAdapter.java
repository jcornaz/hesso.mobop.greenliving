package com.hesso.greenliving.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesso.greenliving.model.Transaction;

public class TransactioListAdapter extends ArrayAdapter<Transaction> {

	public TransactioListAdapter(Context context, int resource, int textViewResourceId, List<Transaction> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public TransactioListAdapter(Context context, int resource, int textViewResourceId, Transaction[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public TransactioListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public TransactioListAdapter(Context context, int resource, List<Transaction> objects) {
		super(context, resource, objects);
	}

	public TransactioListAdapter(Context context, int resource, Transaction[] objects) {
		super(context, resource, objects);
	}

	public TransactioListAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public long getItemId(int position) {
		return this.getItem(position).getID();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TransactionView res;

		if (!(convertView instanceof TransactionView)) {
			res = TransactionView.inflate(parent);
		} else {
			res = (TransactionView) convertView;
		}

		res.setItem(this.getItem(position));

		return res;
	}
}
