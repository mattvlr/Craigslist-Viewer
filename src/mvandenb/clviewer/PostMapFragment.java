package mvandenb.clviewer;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PostMapFragment extends Fragment {
	GoogleMap map;
	private static View view;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

	}
	//destroys old view
    public void onDestroyView() {
        super.onDestroyView();
        Fragment f = (Fragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null) 
            getFragmentManager().beginTransaction().remove(f).commit();
    }
	 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
		 //view handler
		 if (view != null) {
             ViewGroup parent = (ViewGroup) view.getParent();
             if (parent != null)
                     parent.removeView(view);
		 }
		 try {
			 view = inflater.inflate(R.layout.fragment_map, container, false);
		 } catch (InflateException e) {
             /* map is already there, just return view as it is */
			 Log.e("CLV", e.toString());
		 	}
		 return view;
	 }
     public void onResume() {
         super.onResume();

     }
    
     
	 
}