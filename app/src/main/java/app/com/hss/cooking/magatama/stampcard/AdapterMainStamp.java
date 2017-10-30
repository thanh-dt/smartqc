package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.com.hss.cooking.R;


public class AdapterMainStamp extends ArrayAdapter<StampObject> {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int mMaxArray = 0;
	private List<StampObject> mArrayObject;

	public AdapterMainStamp(Context context, List<StampObject> objects) {
		super(context, 1, objects);
		mContext = context;
		mArrayObject = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		StampObject object = getItem(position);
		if (convertView == null) {
			holder = new viewHolder();
			mLayoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mLayoutInflater.inflate(R.layout.item_stamp_card,
					parent, false);
			holder.mTvDate = (TextView) convertView.findViewById(R.id.tv_date);
			holder.mImvIconStamp = (ImageView) convertView
					.findViewById(R.id.imv_stamp_show);
			holder.mTvIndex = (TextView) convertView
					.findViewById(R.id.tv_number_stamp);
			holder.mViewLine = convertView.findViewById(R.id.line_vertical);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}

		if (mMaxArray > 5) {
			for (int i = mMaxArray - 5; i < mMaxArray; i++) {
				if (position == i) {
					holder.mViewLine.setVisibility(View.GONE);
				}
			}
		}
		if (object != null) {
			if (object.isShowStamp()) {
				switch (object.getStyleStamp()) {
				case StampConstants.STYLE_STAMP.STYLE_BOX:
					holder.mImvIconStamp.setImageResource(R.drawable.stamp_1);
					break;
				case StampConstants.STYLE_STAMP.STYLE_APPLE:
					holder.mImvIconStamp.setImageResource(R.drawable.stamp_2);
					break;
				case StampConstants.STYLE_STAMP.STYLE_FLOWER:
					holder.mImvIconStamp.setImageResource(R.drawable.stamp_3);
					break;

				default:
					break;
				}
				holder.mImvIconStamp.setVisibility(View.VISIBLE);
				holder.mTvDate.setVisibility(View.VISIBLE);
			} else {
				holder.mImvIconStamp.setVisibility(View.INVISIBLE);
				holder.mTvDate.setVisibility(View.INVISIBLE);
			}
			holder.mTvIndex.setText(object.getId());
			holder.mTvDate.setText(object.getDate());
		}

		return convertView;
	}

	public class viewHolder {
		TextView mTvDate, mTvIndex;
		ImageView mImvIconStamp;
		View mViewLine;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mMaxArray = mArrayObject.size();
	}
}
