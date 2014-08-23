package gdg.sshhackathon.scv;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SCVAdapter extends ArrayAdapter<Object> {

	private ArrayList<Item> items;

	View v;
		
	public SCVAdapter(Context context, int textViewResourceId, ArrayList items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.search_item_result, null);
		}

		int Last = this.getCount();

		if (Last > position) {
//			TextView tv1 = (TextView) v.findViewById(R.id.result_text);
//			ImageView imv= (ImageView)v.findViewById(R.id.result_image);
//		} else {
			Item xmlData = (Item) items.get(position);
			if (xmlData != null) {
				TextView tv1 = (TextView) v.findViewById(R.id.result_text);
				tv1.setText(xmlData.title);
				ImageView imv= (ImageView)v.findViewById(R.id.result_image);
			}
		}

		return v;
	}
}