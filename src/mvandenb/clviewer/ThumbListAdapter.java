package mvandenb.clviewer;

import java.util.List;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThumbListAdapter extends BaseAdapter {
	 private Activity activity;
	 private LayoutInflater inflater;
	 private List<Post> postItems;
	// ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	 
	 public ThumbListAdapter(Activity activity, List<Post> postItems) {
	    this.activity = activity;
	    this.postItems = postItems;
	 }


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return postItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return postItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item_thumb, null);

        TextView title = (TextView) convertView.findViewById(R.id.item_thumb_name);
        TextView map = (TextView) convertView.findViewById(R.id.item_thumb_map);
        TextView pic = (TextView) convertView.findViewById(R.id.item_thumb_pic);
        TextView city = (TextView) convertView.findViewById(R.id.item_thumb_city);
        TextView date = (TextView) convertView.findViewById(R.id.item_thumb_date);
        TextView price = (TextView) convertView.findViewById(R.id.item_thumb_price);
        
        Post p = postItems.get(position);
        title.setText(p.getTitle());
        map.setText(p.getMap());
        pic.setText(p.getPic());
        city.setText(p.getCity());
        date.setText(p.getDate());
        price.setText(p.getPrice());
		return convertView;
	}}
