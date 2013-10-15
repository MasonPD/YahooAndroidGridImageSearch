package com.android.course.gridimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {

	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	String size;
	String type;
	String color;
	String site;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId){
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}

	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();

		String requestUrl = parseSettings(query);
		Log.d("test", requestUrl);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(requestUrl,
				new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jResponse){
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = jResponse.getJSONObject("responseData").getJSONArray("results");
					imageResults.clear();
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				// create new intent, and return options values when done
				Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivityForResult(i, 1);
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK){  
				size = data.getStringExtra("size");
				color = data.getStringExtra("color");
				type = data.getStringExtra("type");
				site = data.getStringExtra("site");
				Log.d("test", size+" "+color+" "+type+" "+site);
			}
			if (resultCode == RESULT_CANCELED) {    
		         //Write your code if there's no result
		     }
		}
	}
	
	private String parseSettings(String query){
		StringBuilder sb = new StringBuilder();
		sb.append("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start=0&v=1.0&q=");
		sb.append(Uri.encode(query));
		if( size!= null && (!size.equals("any")) ){
			sb.append("&imgsz=");
			sb.append(Uri.encode(size));
		}
		if( color!= null && (!color.equals("any"))){
			sb.append("&imgcolor=");
			sb.append(Uri.encode(color));
		}
		if( type!= null && (!type.equals("all"))){
			sb.append("&imgtype=");
			sb.append(Uri.encode(type));
		}
		if( site!=null){
			sb.append("&as_sitesearch=");
			sb.append(Uri.encode(site));
		}
		return sb.toString();
	}
}
