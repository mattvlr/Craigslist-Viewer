package mvandenb.clviewer;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	public ViewPagerAdapter(FragmentManager fm){
		super(fm);
	}
	//handles tabs
	@Override
	public Fragment getItem(int arg0) {
	       switch (arg0) {
	        case 0:
	            return new ListFragment();
	        case 1:
	            return new ThumbFragment();
	       }
	 
	        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
}
