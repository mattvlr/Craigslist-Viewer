package mvandenb.clviewer;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mvandenb.clviewer.ListFragment.LoadList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ThumbFragment extends Fragment {
	
	public static final String LIST_ID = "id";
	public static final String LIST_NAME = "name";
	public static final String LIST_URL = "url";
	public static final String LIST_PIC_URL = "pic_url";

	
	String catURL = "";
	
	private ProgressDialog pDialog;
	//ArrayList<HashMap<String, String>> postList;
	List<Post> postList = new ArrayList<Post>();
	ListView listView;
	ThumbListAdapter adapter;
	
	SharedPreferences prefs;
	Context context;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		context = getActivity();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		catURL = prefs.getString(CatLevelTwo.CAT_URL, "bad");
		
		//postList = new ArrayList<HashMap<String, String>>();
		
        
        
	}
	
	//below is my failed attempt at a thumbnail view
	//still need this so the tabs will work, commented everything out that
	//is unneeded
	
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
	       // listView = (ListView)rootView.findViewById(R.id.list);
	        
	        
	       // new LoadList(getActivity(),listView).execute("");
	       // adapter = new ThumbListAdapter(getActivity(), postList);
	       // listView.setAdapter(adapter);
	        
	        return rootView;
	    }
	 
	 /*class LoadList extends AsyncTask<String, String, String>{
			ListView mLv;
			Activity mContext;
			ImageView mIv;
			Drawable mD;
		 public LoadList(Activity context,ListView lView){
			 mLv = lView;
			 mContext = context;
		 }
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(context);
	            pDialog.setMessage("Fetching Posts ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				 try {
					 	Log.d("CLV", "Connecting to: " + catURL);
			            Document doc  = Jsoup.connect(catURL).get(); 
			            Elements ccc = doc.select("p.row");
			            
			            
			            Log.d("CLV", "Thumb Posts: " + Integer.toString(ccc.size()));
			            for(int i = 0; i < ccc.size(); i++){
			            	Log.d("CLV", "Thumb -->" + ccc.get(i).text());
			            	//HashMap<String, String> map = new HashMap<String, String>();
			            	Post post = new Post();
			            	post.setId(Integer.toString(i));
			            	post.setTitle(ccc.get(i).text());
			            	post.setUrl(ccc.get(i).select("a").attr("abs:href"));
			            	post.setCity("city");
			            	post.setDate("date");
			            	post.setAttached("map");
			            	//String id = Integer.toString(i);
			            	//String name = ccc.get(i).text();
			            	//String url = ccc.get(i).select("a").attr("abs:href");
			            	
			            	if(post.getTitle().contains("pic")){
			            		post.setAttached("pic map");
			            		
			            	Document doc_post = Jsoup.connect(post.getUrl()).get();
			            	Elements img = doc_post.select("img");
			            	String img_url = img.get(0).attr("abs:src");
			            	post.setImgUrl(img_url);
			            	//Log.e("CLV", img_url);
			            	//map.put(LIST_PIC_URL, img_url);
			            	

			            	}
			            	
			            	//map.put(LIST_ID, id);
			            	//map.put(LIST_NAME, name);
			            	//map.put(LIST_URL, url);
			            	
			            	
			            	//postList.add(map);
			            	postList.add(post);
			            }
			           
			        }
			        catch(Throwable t) {
			            t.printStackTrace();
			        }
				
				return null;
			}
			protected void onPostExecute(String file_url){
				pDialog.dismiss();
						/*ListAdapter adapter = new SimpleAdapter(
								mContext,
								postList,
								R.layout.list_item_thumb,
								new String[] {"LIST_NAME","LIST_ID","LIST_PIC_URL"},
								new int[] {R.id.thumb_item_name,R.id.thumb_item_id,R.id.iv_small});
						
						
						mLv.setAdapter(adapter);
						*/
						

			//}

		

	}