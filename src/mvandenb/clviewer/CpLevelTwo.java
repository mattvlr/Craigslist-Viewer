package mvandenb.clviewer;

import java.util.ArrayList;
import java.util.HashMap;

import mvandenb.clviewer.CityPicker.LoadList;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CpLevelTwo extends ListActivity {

		public String baseURL = "http://www.craigslist.org/about/sites";
		
		public static final String CITY_URL = "CITY_URL";
		public static final String CITY_NAME = "CITY_NAME";
		public static final String CITY_ID = "CITY_ID";
		
		public int id;
		public String title;
		SharedPreferences prefs;
		public static final String FIRST_TIME = "FIRST_TIME";
		
		//ConnectionDetector cd;
		//AlertDialogManager alert = new AlertDialogManager();
		
		private ProgressDialog pDialog;
		ArrayList<HashMap<String, String>> cityList;
		
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_cp_level_two);
			
			Context context = getApplicationContext();
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
			Intent intent = getIntent();
			id = (int) intent.getLongExtra(CITY_ID, 0);
			title = intent.getStringExtra("STATE");
			
			//updates the title to reflect what category we're in
			updateTitle();

	        cityList = new ArrayList<HashMap<String, String>>();
	        new LoadList().execute();
	        ListView lv = getListView();
	        
	        lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
					
					
					StringBuffer city_url = new StringBuffer(cityList.get((int)id).get(CITY_URL));
					StringBuffer cat_url = new StringBuffer(prefs.getString(CatLevelTwo.CAT_URL,".org"));
					
					//this has a high change of failure when dealing with other countries url's
					int org_index = cat_url.indexOf(".org", 0);
					String sub = cat_url.substring(0, org_index+4);
					
					//this was needed in case the category url changes with out the region
					if(sub.equals(city_url) == false){
						Editor editor = prefs.edit();
						cat_url.replace(0, org_index+4, city_url.toString());
						editor.putString(CatLevelTwo.CAT_URL, cat_url.toString());
						editor.commit();
					}
					
					
	                Intent i = new Intent(CpLevelTwo.this, HomeActivity.class);
	                Editor editor = prefs.edit();
	        		editor.putInt(FIRST_TIME, 1);     
	        		editor.putString(CITY_URL, cityList.get((int)id).get(CITY_URL));
	                editor.putString(CITY_NAME, cityList.get((int)id).get(CITY_NAME));
	        		editor.commit();
	        		
	        		
	        		Log.d("CLV", "CITY URL: " + prefs.getString(CITY_URL, ""));
	        		Log.i("CLV", "CAT URL: " + prefs.getString(CatLevelTwo.CAT_URL,""));
	                startActivity(i);
	            }

	        });     
	        
	        
		}
		
		public int updateTitle(){getActionBar().setTitle(title); return 0;}
		
		class LoadList extends AsyncTask<String, String, String>{

	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(CpLevelTwo.this);
	            pDialog.setMessage("Fetching Cities ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				 try {
			            Document doc  = Jsoup.connect(baseURL).get(); 
			            Elements child = doc.select("ul");
			            for(int j = 0; j < child.get(id).children().size(); j++){
		            		//insert children
		            		 
		            		String url = child.get(id).children().get(j).select("a").attr("abs:href");
		            		HashMap<String, String> map = new HashMap<String,String>();
		            		
		            		
		            		map.put(CITY_ID, Integer.toString(id));
		            		map.put(CITY_NAME, child.get(id).children().get(j).text());
		            		map.put(CITY_URL, url);
		            		
		            		cityList.add(map);
		            		
		            	}
			        }
			        catch(Throwable t) {
			            t.printStackTrace();
			        }
				
				return null;
			}
			protected void onPostExecute(String file_url){
				pDialog.dismiss();
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						ListAdapter adapter = new SimpleAdapter(
								CpLevelTwo.this,
								cityList,
								R.layout.list_item_city,
								new String[] {CITY_NAME, CITY_ID, CITY_URL},
								new int[] {R.id.city_name,R.id.city_id, R.id.city_url});
						setListAdapter(adapter);
					}
					
				});
			}
		}
	}
