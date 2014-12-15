package mvandenb.clviewer;


import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class HomeActivity extends ActionBarActivity {

	Button cat;
	Button region;
	Button catGo;
	Button btn_search;
	
	EditText et_search;
	
	SharedPreferences prefs;
	int firstTime = 0;
	String mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		//checks for first time running app
		firstTime = prefs.getInt("FIRST_TIME", 0);
		
		//sets the title with current region
		mTitle = "Region: " + prefs.getString(CpLevelTwo.CITY_NAME, "Craigslist Viewer");
		
		updateTitle();
		
		//shows alert if first time
		if(firstTime != 1)
			showCityAlert();
		
		//setting up ui elements
		cat = (Button)findViewById(R.id.btn_cat);
		region = (Button)findViewById(R.id.btn_site);
		catGo = (Button)findViewById(R.id.btn_cat_go);
		btn_search =(Button)findViewById(R.id.btn_search);
		et_search = (EditText)findViewById(R.id.et_search);
		
		String regionText = prefs.getString(CpLevelTwo.CITY_NAME, "Choose a region!").toString();
		String regionURL = prefs.getString(CpLevelTwo.CITY_URL, "craigslist.org").toString();
		region.setText(regionText + "\n" + regionURL);
		
		cat.setText(prefs.getString(CatLevelTwo.CAT_NAME, "Categories"));
		
		cat.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,CatPicker.class);
				startActivity(i);
			}
		});
		region.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,CityPicker.class);
				startActivity(i);
			}
		});
		catGo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,ViewActivity.class);
                Editor editor = prefs.edit();
        		editor.putBoolean("SEARCH", false);     
        		editor.putString("QUERY", "");
        		editor.commit();
				startActivity(i);
			}
		});
		btn_search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String search_url = prefs.getString(CatLevelTwo.CAT_URL, "bad");
				if(search_url == "bad")
					Toast.makeText(getApplicationContext(), "Must choose a category to search!", Toast.LENGTH_LONG).show();
				else if(et_search.getText().toString().matches("")){
					String search_cat = prefs.getString(CatLevelTwo.CAT_NAME, "bad");
					Intent j = new Intent(HomeActivity.this,ViewActivity.class);
					startActivity(j);
				}
				else{
					//tokens out the search query
					String search_query = et_search.getText().toString();
					
					Log.i("CLV", "Query: " + search_query);
					Log.e("CLV", "URL: " + search_url);
					
					StringTokenizer st = new StringTokenizer(search_query);
					String temp = "";
					int i = 0;
					while(st.hasMoreTokens()){
						if(i < st.countTokens()-1)
							temp = temp + st.nextToken().toString() + "+";
						else
							temp = temp + st.nextToken().toString();
					}
					search_url = search_url + "?sort=rel&query=" + temp;
					
					Log.i("CLV", "New Search URL: " + search_url);
					Intent k = new Intent(HomeActivity.this, ViewActivity.class);
					
					//so ViewActivity knows how to handle it's input
	                Editor editor = prefs.edit();
	        		editor.putBoolean("SEARCH", true);
	        		editor.putString("QUERY", search_query);
	        		editor.putString("QUERY_URL", search_url);
	        		editor.commit();
					startActivity(k);
				}
				
			}
		});
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.action_home:
			return true;
		case R.id.action_cat:
			Intent j = new Intent(HomeActivity.this, CatPicker.class);
			startActivity(j);
			return true;
		case R.id.action_city:
			Intent i = new Intent(HomeActivity.this, CityPicker.class);
			startActivity(i);
			return true;
		case R.id.action_post:
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
	
	public int updateTitle(){getActionBar().setTitle(mTitle); return 0;}
	
	public void showCityAlert(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
		alertDialog.setTitle("Welcome!");
		alertDialog.setMessage("This is your first time lanuching the app, in order to view postings you need to select a city. Would you like to do that now? You can always change it later in Settings!");
		alertDialog.setPositiveButton("Choose city!", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, CityPicker.class);
				startActivity(intent);
			}
		});
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		alertDialog.show();
	}
}
