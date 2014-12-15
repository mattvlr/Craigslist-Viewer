package mvandenb.clviewer;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListFragment extends Fragment {
	
	public static final String LIST_ID = "id";
	public static final String LIST_NAME = "name";
	public static final String LIST_URL = "url";

	String catURL = "";
	
	private ProgressDialog pDialog;
	
	Context context;
	SharedPreferences prefs;
	List<Post> postList = new ArrayList<Post>();
	ListView listView;
	ThumbListAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		        
	}
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
			
	        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
	        
			context = getActivity();
			prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			
			//seeing if the user intended to search or just browse
			if(prefs.getBoolean("SEARCH", false) == false)
				catURL = prefs.getString(CatLevelTwo.CAT_URL, "bad").toString();
			else
				catURL = prefs.getString("QUERY_URL", "bad").toString();

			//setting up 3rd party listview
	        final PullToRefreshListView pV = (PullToRefreshListView) rootView.findViewById(R.id.list);
	        
	        //task to populate the listview
	        new LoadList(getActivity(),pV,false).execute("");

	        //when listview is pulled up, it runs loadlist again
	        pV.setOnRefreshListener(new OnRefreshListener<ListView>(){

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					// TODO Auto-generated method stub
					new LoadList(getActivity(),pV,true).execute("");
				}
	        	
	        });
	        
	        //PostActivity is an entry is clicked
	        pV.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					//passes all this stuff because its formatted nicely
	                Intent i = new Intent(getActivity(), PostActivity.class);
	                i.putExtra("POST_URL", postList.get((int)id).getUrl());
	                i.putExtra("POST_TITLE", postList.get((int)id).getTitle());
	                i.putExtra("POST_FLAG_PIC", postList.get((int)id).getPic());
	                i.putExtra("POST_FLAG_MAP", postList.get((int)id).getMap());
	                i.putExtra("POST_DATE", postList.get((int)id).getDate());
	                i.putExtra("POST_PRICE", postList.get((int)id).getPrice());
	                i.putExtra("POST_CITY", postList.get((int)id).getCity());
	                
	                //put selected cat in intent once viewer is up
	                startActivity(i);
				}
	        });
	        
	        return rootView;
	    }
	 
	 class LoadList extends AsyncTask<String, String, String>{
			PullToRefreshListView mLv;
			Activity mContext;
			Boolean refresh;
		 public LoadList(Activity context,PullToRefreshListView pV,Boolean refresh){
			 mLv = pV;
			 mContext = context;
			 this.refresh = refresh;
		 }
	        protected void onPreExecute() {
	            super.onPreExecute();
	            if(refresh == false){
	            pDialog = new ProgressDialog(context);
	            pDialog.setMessage("Fetching Posts ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	            }
	        }
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				 try {

			            Document doc  = Jsoup.connect(catURL).get(); // getting all html 
			            Elements ccc = doc.select("p.row"); //getting all posts
			            for(int i = 0; i < ccc.size(); i++){ //looping through posts and getting info
			            	
			            	Post post = new Post();
			            	
			            	//internal post id
			            	post.setId(Integer.toString(i));
			            	
			            	//title
			            	StringBuffer mTitle = new StringBuffer(ccc.get(i).text());
			            	
			            	//post url
			            	String post_url = ccc.get(i).select("a").attr("abs:href");
			            	post.setUrl(post_url);
			            	
			            	//detecting if city is attached
			            	if(ccc.get(i).select("span.pnr").select("small").hasText()){
			            		String attached = ccc.get(i).select("span.pnr").select("small").text();
			            		int start = attached.indexOf("(");
			            		int end = attached.indexOf(")");	
			            		String city = attached.substring(start+1, start+end);
			            		post.setCity(city); 
			            		mTitle.replace(mTitle.indexOf("("), mTitle.indexOf(")")+1, "");
			            	}
			            	
			            	//post with $, note this fails if you switch to another country 
			            	if(ccc.get(i).select("a").select("span.price").hasText()){
			            		String attached = ccc.get(i).select("a").select("span.price").text();
			            		post.setPrice(attached);
			            		mTitle.replace(0, attached.length()+1, "");
			            		mTitle.replace(mTitle.indexOf(attached, 0), attached.length()+mTitle.indexOf(attached, 0), "");
			            	}
			            		
			            	//getting date
			            	if(ccc.get(i).select("span.pl").select("time").hasText()){
			            		String attached = ccc.get(i).select("span.pl").select("time").text();
			            		post.setDate(attached);
			            		mTitle.replace(0, attached.length(), "");
			            	}
			            	
			            	//removing extra space in title
			            	if(mTitle.indexOf(" ") == 0)
			            		mTitle.replace(0, 1, "");
			            	
			            	//detecting map text field
			            	if(ccc.get(i).text().contains("map")){
			            		post.setMap("map");
			            		mTitle.replace(mTitle.indexOf("map"),mTitle.indexOf("map")+3, "");
			            	}
			            	else
			            		post.setMap("");
			            	
			            	//detecting pic text field
			            	if(ccc.get(i).text().contains("pic")){
			            		post.setPic("pic  ");
			            		mTitle.replace(mTitle.indexOf("pic"),mTitle.indexOf("pic")+3, "");
			            	}
			            	else
			            		post.setPic("");
			            	//finally setting the title	
			                post.setTitle(mTitle.toString());
			            	postList.add(post);
			            }
			           
			        }
			        catch(Throwable t) {
			            t.printStackTrace();
			        }
				
				return null;
			}
			protected void onPostExecute(String file_url){
				if(refresh == false)
					pDialog.dismiss();
				mLv.onRefreshComplete();
				//universal fall back if the list returns nothing
				if(postList.size() == 0){
					Post post = new Post();
					post.setTitle("There doesn't seem to be anything here!");
					post.setCity("Url used: " + catURL);
					post.setPrice(prefs.getString(CpLevelTwo.CITY_NAME,""));
					post.setDate(prefs.getString(CatLevelTwo.CAT_NAME,""));
					postList.add(post);
					
				}
				ThumbListAdapter  adapter = new ThumbListAdapter(getActivity(), postList);
				mLv.setAdapter(adapter);
			}
		}
			 
	}

