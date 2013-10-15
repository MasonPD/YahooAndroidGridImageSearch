package com.android.course.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	private Spinner spnrSize;
	private Spinner spnrColor;
	private Spinner spnrType;
	private EditText etSite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		spnrSize = (Spinner)findViewById(R.id.spinnerImageSize);
		spnrColor = (Spinner)findViewById(R.id.spinnerColorFilter);
		spnrType = (Spinner)findViewById(R.id.spinnerImageType);
		etSite = (EditText)findViewById(R.id.etSiteFilter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void onSave(View v){
		Intent i = new Intent(this, SearchActivity.class);
		i.putExtra("size", spnrSize.getSelectedItem().toString());
		i.putExtra("color", spnrColor.getSelectedItem().toString());
		i.putExtra("type", spnrType.getSelectedItem().toString());
		i.putExtra("site", etSite.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}

}
