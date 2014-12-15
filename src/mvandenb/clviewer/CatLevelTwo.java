package mvandenb.clviewer;

import java.util.ArrayList;
import java.util.HashMap;

import mvandenb.clviewer.CpLevelTwo.LoadList;

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
import android.widget.AdapterView.OnItemClickListener;

public class CatLevelTwo extends ListActivity {
	
	public static final String CAT_URL = "url";
	public static final String CAT_NAME = "name";
	public static final String CAT_ID = "id";
	public String baseURL;
	public String selectedURL;
	public String name;
	public String title;
	public String _id;
	public int id;
	int catID = 0;
	
	SharedPreferences prefs;
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> catList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cp_level_two);
		
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		baseURL = prefs.getString(CpLevelTwo.CITY_URL, "craigslist.org");
		
		Intent intent = getIntent();
		id = (int) intent.getLongExtra(CAT_ID, 0);
		title = intent.getStringExtra("name");
		updateTitle();

        catList = new ArrayList<HashMap<String, String>>();
        new LoadList().execute();
        ListView lv = getListView();
        
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
                Intent i = new Intent(CatLevelTwo.this, HomeActivity.class);
                //put selected cat in intent once viewer is up
                Editor editor = prefs.edit();    
                editor.putString(CAT_URL, catList.get((int)id).get(CAT_URL));
                editor.putString(CAT_NAME, catList.get((int)id).get(CAT_NAME));
        		editor.commit();
                startActivity(i);
            }

        });     
        
        
	}
	public int updateTitle(){getActionBar().setTitle(title); return 0;}
	class LoadList extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CatLevelTwo.this);
            pDialog.setMessage("Fetching subcategories ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			 try {
		            Document doc  = Jsoup.connect(baseURL).get(); 
		            	
		            	//huge switch statement to get the categories, all are very similar
	            		Elements ccc = doc.select("div.col");
	            			switch (id){
	            			case 0: // community
	            				HashMap<String, String> no_cat = new HashMap<String, String>();
	            				no_cat.put(CAT_ID, "0");
	            				no_cat.put(CAT_NAME, "community | no subcategory");
	            				no_cat.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad") + "/search/ccc?sort=rel");
	            				catList.add(no_cat);
	            				
	            				catID = 1;
	            				
	            				//take care of first entry
	            				Element cheader = ccc.get(id).children().get(0);
	            				
	            				name = cheader.text();
				            	_id = Integer.toString(catID);
				                selectedURL = cheader.select("a").attr("abs:href");
				            	HashMap<String, String> map = new HashMap<String, String>();
				            	map.put(CAT_ID, _id);
				            	map.put(CAT_NAME, name);
				            	map.put(CAT_URL, selectedURL);
				            	catList.add(map);
				            	catID++;
				            	
				            	//add two columns for community
				            	Element col1 = ccc.get(id).children().get(1).children().get(0);
				            	Element col2 = ccc.get(id).children().get(1).children().get(1);
				            	
				            	for(int _col1 = 0; _col1 < col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		
				            		name = col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = col1.children().get(_col1).select("a").attr("abs:href");
				            		
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	
					            	catID++;
				            	}
				            	for(int _col2 = 0; _col2 < col2.children().size(); _col2++){
				            		HashMap<String, String> map2 = new HashMap<String, String>();
				            		
				            		name = col2.children().get(_col2).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = col2.children().get(_col2).select("a").attr("abs:href");
				            		
				            		
				            		map2.put(CAT_ID, _id);
					            	map2.put(CAT_NAME, name);
					            	map2.put(CAT_URL, selectedURL);
					            	catList.add(map2);

					            	catID++;
				            	}
				            	
	            				break;
	            			case 1: // personals
	            				HashMap<String, String> no_cat1 = new HashMap<String, String>();
	            				no_cat1.put(CAT_ID, "0");
	            				no_cat1.put(CAT_NAME, "personals | no subcategory");
	            				no_cat1.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/ppp?sort=rel");
	            				catList.add(no_cat1);
	            				
	            				catID = 1;
	            				Element p_col1 = ccc.get(id).children().get(1).children().get(0);
				            	

				            	for(int _col1 = 0; _col1 < p_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		
				            		name = p_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = p_col1.children().get(_col1).select("a").attr("abs:href");
				            		
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	
					            	catID++;
				            	}
	            				break;
	            			case 2: //discussion
	            				HashMap<String, String> no_cat2 = new HashMap<String, String>();
	            				no_cat2.put(CAT_ID, "0");
	            				no_cat2.put(CAT_NAME, "discussion | no subcategory");
	            				no_cat2.put(CAT_URL,"bad");
	            				catList.add(no_cat2);
	            				
	            				catID = 1;
	            				//take care of first entry
	            				Element d_header = ccc.get(id).children().get(0);
	            				
	            				
	            				name = d_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = d_header.select("a").attr("abs:href");
				            	
				            	HashMap<String, String> d_map = new HashMap<String, String>();
				            	d_map.put(CAT_ID, _id);
				            	d_map.put(CAT_NAME, name);
				            	d_map.put(CAT_URL, selectedURL);
				            	catList.add(d_map);
				            	catID++;
				            	
				            	//three discussion columns
				            	Element d_col1 = ccc.get(id).children().get(1).children().get(0);
				            	Element d_col2 = ccc.get(id).children().get(1).children().get(1);
				            	Element d_col3 = ccc.get(id).children().get(1).children().get(2);

				            	for(int _col1 = 0; _col1 < d_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            
				            		name = d_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = d_col1.children().get(_col1).select("a").attr("abs:href");
				            		//Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	
					            	catID++;
				            	}
				            	for(int _col2 = 0; _col2 < d_col2.children().size(); _col2++){
				            		HashMap<String, String> map2 = new HashMap<String, String>();
				            		
				            		name = d_col2.children().get(_col2).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = d_col2.children().get(_col2).select("a").attr("abs:href");
				            	
				            		
				            		map2.put(CAT_ID, _id);
					            	map2.put(CAT_NAME, name);
					            	map2.put(CAT_URL, selectedURL);
					            	catList.add(map2);
					            	
					            	catID++;
				            	}
				            	for(int _col3 = 0; _col3 < d_col3.children().size(); _col3++){
				            		HashMap<String, String> map3 = new HashMap<String, String>();
				            		
				            		name = d_col3.children().get(_col3).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = d_col3.children().get(_col3).select("a").attr("abs:href");
				            		
				            		
				            		map3.put(CAT_ID, _id);
					            	map3.put(CAT_NAME, name);
					            	map3.put(CAT_URL, selectedURL);
					            	catList.add(map3);
					            	
					            	catID++;
				            	}
	            				break;
	            			case 3: //housing
	            				HashMap<String, String> no_cat3 = new HashMap<String, String>();
	            				no_cat3.put(CAT_ID, "0");
	            				no_cat3.put(CAT_NAME,prefs.getString(CpLevelTwo.CITY_URL, "bad")+  "housing | no subcategory");
	            				no_cat3.put(CAT_URL,"/search/hhh?sort=rel");
	            				catList.add(no_cat3);
	            				
	            				catID = 1;
	            				//take care of first entry
	            				Element h_header = ccc.get(id).children().get(0);
	            				
	            				
	            				name = h_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = h_header.select("a").attr("abs:href");
				            	
				            	HashMap<String, String> h_map = new HashMap<String, String>();
				            	h_map.put(CAT_ID, _id);
				            	h_map.put(CAT_NAME, name);
				            	h_map.put(CAT_URL, selectedURL);
				            	catList.add(h_map);
				            	catID++;
				            	
				            	Element h_col1 = ccc.get(id).children().get(1).children().get(0);
				            	
				            	
				            	for(int _col1 = 0; _col1 < h_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		Log.i("CLV", h_col1.children().get(_col1).text());
				            		name = h_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = h_col1.children().get(_col1).select("a").attr("abs:href");	
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	catID++;
				            	}
	            				break;
	            			case 4: // for sale
	            				HashMap<String, String> no_cat4 = new HashMap<String, String>();
	            				no_cat4.put(CAT_ID, "0");
	            				no_cat4.put(CAT_NAME, "for sale | no subcategory");
	            				no_cat4.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/sss?sort=rel");
	            				catList.add(no_cat4);
	            				catID = 1;
	            				//take care of first entry
	            				Element f_header = ccc.get(id).children().get(0);
	            				Log.i("CLV", f_header.text());
	            				
	            				name = f_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = f_header.select("a").attr("abs:href");
				            	Log.i("CLV", "Header url: " + selectedURL);
				            	HashMap<String, String> f_map = new HashMap<String, String>();
				            	f_map.put(CAT_ID, _id);
				            	f_map.put(CAT_NAME, name);
				            	f_map.put(CAT_URL, selectedURL);
				            	catList.add(f_map);
				            	catID++;
				            	
				            	//add two columns for community
				            	Element f_col1 = ccc.get(id).children().get(1).children().get(0);
				            	Element f_col2 = ccc.get(id).children().get(1).children().get(1);
				            	Log.i("CLV", f_col1.text());
				            	Log.i("CLV", f_col2.text());
				            	
				            	for(int _col1 = 0; _col1 < f_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		Log.i("CLV", f_col1.children().get(_col1).text());
				            		name = f_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = f_col1.children().get(_col1).select("a").attr("abs:href");
				            		//Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            	for(int _col2 = 0; _col2 < f_col2.children().size(); _col2++){
				            		HashMap<String, String> map2 = new HashMap<String, String>();
				            		Log.i("CLV", f_col2.children().get(_col2).text());
				            		name = f_col2.children().get(_col2).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = f_col2.children().get(_col2).select("a").attr("abs:href");
				            		Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map2.put(CAT_ID, _id);
					            	map2.put(CAT_NAME, name);
					            	map2.put(CAT_URL, selectedURL);
					            	catList.add(map2);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            
	            				break;
	            			case 5: //services
	            				HashMap<String, String> no_cat5 = new HashMap<String, String>();
	            				no_cat5.put(CAT_ID, "0");
	            				no_cat5.put(CAT_NAME, "services | no subcategory");
	            				no_cat5.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/bbb?sort=rel");
	            				catList.add(no_cat5);
	            				
	            				catID = 1;
	            				//take care of first entry
	            				Element s_header = ccc.get(id).children().get(0);
	            				Log.i("CLV", s_header.text());
	            				
	            				name = s_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = s_header.select("a").attr("abs:href");
				            	Log.i("CLV", "Header url: " + selectedURL);
				            	HashMap<String, String> s_map = new HashMap<String, String>();
				            	s_map.put(CAT_ID, _id);
				            	s_map.put(CAT_NAME, name);
				            	s_map.put(CAT_URL, selectedURL);
				            	catList.add(s_map);
				            	catID++;
				            	
				            	//add two columns for community
				            	Element s_col1 = ccc.get(id).children().get(1).children().get(0);
				            	Element s_col2 = ccc.get(id).children().get(1).children().get(1);
				            	Log.i("CLV", s_col1.text());
				            	Log.i("CLV", s_col2.text());
				            	
				            	for(int _col1 = 0; _col1 < s_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		Log.i("CLV", s_col1.children().get(_col1).text());
				            		name = s_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = s_col1.children().get(_col1).select("a").attr("abs:href");
				            		//Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            	for(int _col2 = 0; _col2 < s_col2.children().size(); _col2++){
				            		HashMap<String, String> map2 = new HashMap<String, String>();
				            		Log.i("CLV", s_col2.children().get(_col2).text());
				            		name = s_col2.children().get(_col2).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = s_col2.children().get(_col2).select("a").attr("abs:href");
				            		Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map2.put(CAT_ID, _id);
					            	map2.put(CAT_NAME, name);
					            	map2.put(CAT_URL, selectedURL);
					            	catList.add(map2);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            
	            				break;
	            			case 6: // jobs
	            				HashMap<String, String> no_cat6 = new HashMap<String, String>();
	            				no_cat6.put(CAT_ID, "0");
	            				no_cat6.put(CAT_NAME, "jobs | no subcategory");
	            				no_cat6.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/jjj?sort=rel");
	            				catList.add(no_cat6);
	            				catID = 1;
	            				//take care of first entry
	            				Element j_header = ccc.get(id).children().get(0);
	            				Log.i("CLV", j_header.text());
	            				
	            				name = j_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = j_header.select("a").attr("abs:href");
				            	Log.i("CLV", "Header url: " + selectedURL);
				            	HashMap<String, String> j_map = new HashMap<String, String>();
				            	j_map.put(CAT_ID, _id);
				            	j_map.put(CAT_NAME, name);
				            	j_map.put(CAT_URL, selectedURL);
				            	catList.add(j_map);
				            	catID++;
				            	
				            	Element j_col1 = ccc.get(id).children().get(1).children().get(0);
				            	Log.i("CLV", j_col1.text());
				            	
				            	for(int _col1 = 0; _col1 < j_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		Log.i("CLV", j_col1.children().get(_col1).text());
				            		name = j_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = j_col1.children().get(_col1).select("a").attr("abs:href");
				            		//Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
	            				break;
	            			case 7: //gigs
	            				HashMap<String, String> no_cat7 = new HashMap<String, String>();
	            				no_cat7.put(CAT_ID, "0");
	            				no_cat7.put(CAT_NAME, "gigs | no subcategory");
	            				no_cat7.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/ggg?sort=rel");
	            				catList.add(no_cat7);
	            				catID = 1;
	            				//take care of first entry
	            				Element g_header = ccc.get(id).children().get(0);
	            				Log.i("CLV", g_header.text());
	            				
	            				name = g_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = g_header.select("a").attr("abs:href");
				            	Log.i("CLV", "Header url: " + selectedURL);
				            	HashMap<String, String> g_map = new HashMap<String, String>();
				            	g_map.put(CAT_ID, _id);
				            	g_map.put(CAT_NAME, name);
				            	g_map.put(CAT_URL, selectedURL);
				            	catList.add(g_map);
				            	catID++;
				            	
				            	//add two columns for community
				            	Element g_col1 = ccc.get(id).children().get(1).children().get(0);
				            	Element g_col2 = ccc.get(id).children().get(1).children().get(1);
				            	Log.i("CLV", g_col1.text());
				            	Log.i("CLV", g_col2.text());
				            	
				            	for(int _col1 = 0; _col1 < g_col1.children().size(); _col1++){
				            		HashMap<String, String> map1 = new HashMap<String, String>();
				            		Log.i("CLV", g_col1.children().get(_col1).text());
				            		name = g_col1.children().get(_col1).text();
				            		_id = Integer.toString(catID);
				            		String selectedURL1 = g_col1.children().get(_col1).select("a").attr("abs:href");
				            		//Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map1.put(CAT_ID, _id);
					            	map1.put(CAT_NAME, name);
					            	map1.put(CAT_URL, selectedURL1);
					            	catList.add(map1);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            	for(int _col2 = 0; _col2 < g_col2.children().size(); _col2++){
				            		HashMap<String, String> map2 = new HashMap<String, String>();
				            		Log.i("CLV", g_col2.children().get(_col2).text());
				            		name = g_col2.children().get(_col2).text();
				                    _id = Integer.toString(catID);
				            		selectedURL = g_col2.children().get(_col2).select("a").attr("abs:href");
				            		Log.i("CLV", "Current cat url: " + selectedURL);
				            		
				            		map2.put(CAT_ID, _id);
					            	map2.put(CAT_NAME, name);
					            	map2.put(CAT_URL, selectedURL);
					            	catList.add(map2);
					            	Log.i("CLV", "Current id: " + catID + " name: " + name);
					            	catID++;
				            	}
				            
	            				break;
	            			case 8: //resumes
	            				HashMap<String, String> no_cat8 = new HashMap<String, String>();
	            				no_cat8.put(CAT_ID, "0");
	            				no_cat8.put(CAT_NAME, "resumes | no subcategory");
	            				no_cat8.put(CAT_URL,prefs.getString(CpLevelTwo.CITY_URL, "bad")+ "/search/rrr?sort=rel");
	            				catList.add(no_cat8);
	            				catID = 1;
	            				//take care of first entry
	            				Element r_header = ccc.get(id).children().get(0);
	            				Log.i("CLV", r_header.text());
	            				
	            				name = r_header.text();
				            	_id = Integer.toString(catID);
				                selectedURL = r_header.select("a").attr("abs:href");
				            	Log.i("CLV", "Header url: " + selectedURL);
				            	HashMap<String, String> r_map = new HashMap<String, String>();
				            	r_map.put(CAT_ID, _id);
				            	r_map.put(CAT_NAME, name);
				            	r_map.put(CAT_URL, selectedURL);
				            	catList.add(r_map);
	            				break;
	            			default:
	            				break;
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
							CatLevelTwo.this,
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
