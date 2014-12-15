package mvandenb.clviewer;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CityPicker extends ListActivity {

	public String baseURL = "http://www.craigslist.org/about/sites";
	
	
	
	public static final String CITY_NAME = "CITY_NAME";
	public static final String CITY_ID = "CITY_ID";


	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> siteList;
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_picker);
		
		updateTitle();
		//decided to use hashmaps because I didn't want to make classes to hold the data.
        siteList = new ArrayList<HashMap<String, String>>();
        new LoadList().execute();
        ListView lv = getListView();
        
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
    	Editor editor = prefs.edit();
    	editor.putString(CpLevelTwo.CITY_URL, baseURL);
    	editor.putString(CatLevelTwo.CAT_URL, baseURL);
    	editor.commit();
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent i = new Intent(CityPicker.this, CpLevelTwo.class);
				i.putExtra(CITY_ID, id);
				i.putExtra("STATE", siteList.get((int)id).get(CITY_NAME));
				startActivity(i);
			}
        });
        
	}
	public int updateTitle(){getActionBar().setTitle("Regions"); return 0;}
	class LoadList extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CityPicker.this);
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
		            Elements sites = doc.select("h4");
		            //Elements child = doc.select("ul");
		            for(int i = 0; i < sites.size(); i++){
		            	Log.d("CLV", sites.get(i).text());
		            	HashMap<String, String> map = new HashMap<String, String>();
		            	
		            	String id = Integer.toString(i);
		            	String name = sites.get(i).text();
		            
		            	map.put(CITY_ID, id);
		            	map.put(CITY_NAME, name);
		            	
		            	siteList.add(map);

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
							CityPicker.this,
							siteList,
							R.layout.list_item_site,
							new String[] {CITY_NAME,CITY_ID},
							new int[] {R.id.site_name,R.id.site_id});
					setListAdapter(adapter);
				}
				
			});
		}
	}
}
