package com.hesso.greenliving.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.hesso.greenliving.R;

public class DialogTransactionHelp extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_transaction_help);
	}
	
	public void onClickOk(View v) {
		finish();
	}
}