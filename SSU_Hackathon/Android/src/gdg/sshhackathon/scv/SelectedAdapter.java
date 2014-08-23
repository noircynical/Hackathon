package gdg.sshhackathon.scv;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

class ViewHolder {
	   TextView title;
}

public class SelectedAdapter extends ArrayAdapter<Object> {

	private Context mContext= null;
	private ArrayList<Item> items;
	private static final String TAG = "SelectedAdopter";
	View v;
		
	public SelectedAdapter(Context context, int textViewResourceId, ArrayList items) {
		super(context, textViewResourceId, items);
		mContext= context;
		this.items = items;
	}
	
	public void updated(ArrayList<Item> list){
//		items.clear();
//		this.notifyDataSetInvalidated();
		items= (ArrayList<Item>)list.clone();
		this.notifyDataSetChanged();
	
		
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
//		System.out.println(Integer.toString(position));
		Log.d(TAG, "getView(" + position + ")", new RuntimeException("here"));

		v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}

//		int Last = this.getCount();

//		if (Last > position) {
			Item xmlData = (Item) items.get(position);
			if (xmlData != null) {
				TextView tv1 = (TextView) v.findViewById(R.id.text1);
				tv1.setText(xmlData.title);
			}
//		}

		return v;
	}
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//	    View row = convertView;
//	    ViewHolder holder = null;
//	    if(row == null){
//	        LayoutInflater inflater = ((SCVActivity)mContext).getLayoutInflater();
//	        row = inflater.inflate(R.layout.list_item, parent, false);
//
//	        holder = new ViewHolder();
//	        holder.title = (TextView)row.findViewById(R.id.text1);
//	        row.setTag(holder);
//	    } else {
//	        holder = row.getTag();
//	    }
//	    holder.title.setText(items.get(position).title);    
//	    return row;
//	}
}