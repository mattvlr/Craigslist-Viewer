package mvandenb.clviewer;

import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PostFragment extends Fragment {

	TextView title;
	TextView body;
	TextView time;
	TextView id;
	TextView price;
	
	ImageView pic;
	
	private ProgressDialog pDialog;
	
	String post_url;
	String pic_flag;
	String post_title;
	String post_price;
	String post_date;
	String post_city;
	public String s_lat;
	public String s_lng;
	Boolean hasLoc = false;
	
	GoogleMap map;
	
	SharedPreferences prefs;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//get post info
		Intent intent = getActivity().getIntent();
		post_url = intent.getStringExtra("POST_URL");
		pic_flag = intent.getStringExtra("POST_FLAG_PIC");
		post_title = intent.getStringExtra("POST_TITLE");
		post_price = intent.getStringExtra("POST_PRICE");
		post_date = intent.getStringExtra("POST_DATE");
		post_city = intent.getStringExtra("POST_CITY");
		Context context = getActivity();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 

		 
	       View rootView = inflater.inflate(R.layout.activity_post, container, false);

	       //setup ui
	       title = (TextView)rootView.findViewById(R.id.tv_post_title);
		   body = (TextView)rootView.findViewById(R.id.tv_post_body);
		   time = (TextView)rootView.findViewById(R.id.tv_post_time);
		   id = (TextView)rootView.findViewById(R.id.tv_post_id);
		   price = (TextView)rootView.findViewById(R.id.tv_post_price);
		   pic = (ImageView)rootView.findViewById(R.id.iv_post_image);
		   

		   
			new LoadPost().execute();
			
	        return rootView;
			
			
	    }
	    public void onDestroyView() {
	        super.onDestroyView();
	        Fragment f = (Fragment) getFragmentManager().findFragmentById(R.id.map);
	        if (f != null) 
	            getFragmentManager().beginTransaction().remove(f).commit();
	    }
	 class LoadPost extends AsyncTask<String, String, String>{
			//String title_text;
		    Spanned body_text;
			Bitmap img = null;
			String img_url;

	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(getActivity());
	            pDialog.setMessage("Fetching Post...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(false);
	            pDialog.show();
	        }
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				 try {
			            Document doc  = Jsoup.connect(post_url).get();
			            Log.v("CLV", post_url);
			            Elements post_body = doc.select("#postingbody");
			            
			            //get body and format as html
			            body_text = Html.fromHtml(post_body.toString());
			           
			            
			            //select img if there is one and download as bitmap
				        img_url = doc.select("img").attr("src");
				        InputStream in = new java.net.URL(img_url).openStream();
				        img = BitmapFactory.decodeStream(in);
			            
				        //get map data
				        if(doc.select("#map").attr("data-latitude").isEmpty() == false){
				            s_lat = doc.select("#map").attr("data-latitude");
				            s_lng = doc.select("#map").attr("data-longitude");
				            Log.e("CLV", "S_LAT: "+s_lat);
				            Log.e("CLV", "S_LNG: "+s_lng);
				            Log.e("CLV", doc.select("#map").toString());
				            hasLoc = true;
				        }
				        			           
			        }
			        catch(Throwable t) {
			            t.printStackTrace();
			        }
				
				return null;
			}
			protected void onPostExecute(String file_url){
				pDialog.dismiss();
				//setup ui
			    pic.setImageBitmap(img);
				
				title.setText(post_title);
				time.setText(post_city);
				id.setText(post_date);
				body.setText(body_text);
				price.setText(post_price);
				body.setMovementMethod(new ScrollingMovementMethod());
				
				map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
				//if the post had location values, go to them
				if(hasLoc){
				 Double lat = Double.parseDouble(s_lat);
				 Double lng = Double.parseDouble(s_lng);
	             
	            	 map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 14.0f));
	                 map.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
	             }
	             else
	            	 Log.e("CLV","LAT HAS NO VAL");
				
			}
		}
	}

