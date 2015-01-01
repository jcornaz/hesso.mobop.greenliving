package com.hesso.greenliving.ui;

import com.hesso.greenliving.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DialogAccountHelp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_account_help);
	}
	
	public void onClickOk(View v) {
		finish();
	}
}
