package com.hesso.greenliving.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesso.greenliving.model.Transaction;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

	public TransactionListAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public TransactionListAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public long getItemId(int position) {
		return this.getItem(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TransactionView res;

		if (!(convertView instanceof TransactionView)) {
			res = (TransactionView) convertView;
		} else {
			res = TransactionView.inflate(parent);
			res = (TransactionView) convertView;
		}

		res.setItem(this.getItem(position));

		return res;
	}
}
