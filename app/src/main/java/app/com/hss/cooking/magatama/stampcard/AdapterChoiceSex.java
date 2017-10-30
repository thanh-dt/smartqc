package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.com.hss.cooking.R;


public class AdapterChoiceSex extends ArrayAdapter<String> {
	private Context mContext;

	public AdapterChoiceSex(Context context, List<String> objects) {
		super(context, 1, objects);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String name = getItem(position);
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_default_spinner, parent,
					false);
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		holder.mTvName.setText(name);
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		String name = getItem(position);
		viewHolder holder;
		if (convertView == null) {

			holder = new viewHolder();
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_choice_sex, parent,
					false);
			holder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		holder.mTvName.setText(name);
		return convertView;
	}

	class viewHolder {
		private TextView mTvName;
	}

}
