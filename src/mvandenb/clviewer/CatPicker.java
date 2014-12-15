package mvandenb.clviewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import mvandenb.clviewer.CityPicker.LoadList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CatPicker extends ListActivity {
	
	public String baseURL;
	public static final String CAT_NAME = "name";
	public static final String CAT_ID = "id";
		
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> catList;
	
	SharedPreferences prefs;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cp_level_two);
		
		updateTitle();
		
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		baseURL = prefs.getString(CpLevelTwo.CITY_URL, "craigslist.org");
		
        catList = new ArrayList<HashMap<String, String>>();
        new LoadList().execute();
        ListView lv = getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent i = new Intent(CatPicker.this, CatLevelTwo.class);
				i.putExtra(CAT_ID, id);
				i.putExtra(CAT_NAME, catList.get((int)id).get(CAT_NAME));
				startActivity(i);
			}
        });

	}
	public int updateTitle(){getActionBar().setTitle("Categories"); return 0;}
	class LoadList extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CatPicker.this);
            pDialog.setMessage("Fetching Categories ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			 try {
				 	
				 	Log.d("CLV", "Connecting to: " + baseURL);
		            Document doc  = Jsoup.connect(baseURL).get(); 
		            Elements ccc = doc.select("div.col");
		            
		            //getting list of categories
		            for(int i = 0; i < ccc.size(); i++){
		            	
		            	Log.d("CLV", ccc.get(i).children().get(0).text());
		            	HashMap<String, String> map = new HashMap<String, String>();
		            	
		            	String id = Integer.toString(i);
		            	String name = ccc.get(i).children().get(0).text();
		            
		            	map.put(CAT_ID, id);
		            	map.put(CAT_NAME, name);
		            	
		            	catList.add(map);

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
							CatPicker.this,
							catList,
							R.layout.list_item_site,
							new String[] {CAT_NAME,CAT_ID},
							new int[] {R.id.site_name,R.id.site_id});
					setListAdapter(adapter);
				}
				
			});
		}
	}
		 
	}
	
	
	
	

