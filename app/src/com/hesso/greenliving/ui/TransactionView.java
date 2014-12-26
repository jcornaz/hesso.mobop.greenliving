package com.hesso.greenliving.ui;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionView extends View {

	private static final DecimalFormat DEC_FORMAT = new DecimalFormat("#0.00");

	public static TransactionView inflate(ViewGroup parent) {
		return (TransactionView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
	}

	private Transaction transaction;

	private TextView from;
	private TextView to;
	private TextView amount;

	public TransactionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.from = (TextView) this.findViewById(R.id.budgetFrom);
		this.to = (TextView) this.findViewById(R.id.budgetTo);
		this.amount = (TextView) this.findViewById(R.id.amount);
	}

	public void setItem(Transaction item) {
		this.transaction = item;

		this.amount.setText(DEC_FORMAT.format(this.transaction.getAmount()));
		this.from.setText(this.transaction.getSourceEntry().getName());

		if (item.isTransfert()) {
			this.to.setVisibility(View.VISIBLE);
			this.to.setText(item.getDestinationEntry().getName());
		} else {
			this.to.setVisibility(View.INVISIBLE);
		}
	}

	public Transaction getItem() {
		return this.transaction;
	}
}
