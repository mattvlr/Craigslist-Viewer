package mvandenb.clviewer;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PostPagerAdapter extends FragmentPagerAdapter {

	public PostPagerAdapter(FragmentManager fragmentManager){
		super(fragmentManager);
	}
	//handles tabs
	@Override
	public Fragment getItem(int arg0) {
	       switch (arg0) {
	        case 0:
	            return new PostFragment();
	        case 1:
	            return new PostMapFragment();
	        }
	 
	        return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}
}
