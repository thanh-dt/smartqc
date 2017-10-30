package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import app.com.hss.cooking.R;


public class AdapterPromotion extends ArrayAdapter<PromotionObject> {
	private Context mContext;
	private LayoutInflater mInflater;
	private onUsingPromot mOnclick;
	private String mColor;
	private String mBgColor;

	public AdapterPromotion(Context context, List<PromotionObject> objects, String color, String bgColor) {
		super(context, 1, objects);
		mContext = context;
		this.mColor = color;
		this.mBgColor = bgColor;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final PromotionObject object = getItem(position);
		viewHolder holder;
		if (convertView == null) {
			holder = new viewHolder();
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.item_promotions, parent,
					false);
			holder.mTvNumberStamp = (TextViewW6) convertView
					.findViewById(R.id.tv_using_stamp_sub_one);
			holder.mImvThumb = (ResizableImageView) convertView
					.findViewById(R.id.imv_stamp_using);
			holder.mBtnUsingPromotions = (Button) convertView
					.findViewById(R.id.btn_using_stamp);
			holder.mTvTitle = (TextViewW3) convertView
					.findViewById(R.id.tv_using_stamp_sub_two);
			holder.mImvUsed = (ImageView) convertView
					.findViewById(R.id.imv_used);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		if (mColor.length() > 0) {
			GradientDrawable gd = (GradientDrawable) holder.mTvNumberStamp.getBackground();
			//To shange the solid color
			gd.setColor(Color.parseColor(mColor));
//			holder.mTvNumberStamp.setBackgroundColor(Color.parseColor(mColor));
		}
		holder.mTvNumberStamp.setText(mContext.getString(
				R.string.stamp_using_sub_one, object.getStampNeed()));
		holder.mTvTitle.setText(object.getTitlePromot());
		holder.mTvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (object.getContentPromot() != null) {
					DialogContentPromot dialog = new DialogContentPromot(
							mContext, object.getContentPromot() ,mBgColor);
					dialog.show();
				} else {
					Toast.makeText(mContext, "No content", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
		
		holder.mImvThumb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (object.getContentPromot() != null) {
					DialogContentPromot dialog = new DialogContentPromot(
							mContext, object.getContentPromot(),mBgColor);
					dialog.show();
				} else {
					Toast.makeText(mContext, "No content", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		if (object.getUrlThumb().length() > 0) {
			Glide.with(mContext).load(object.getUrlThumb())
					.into(holder.mImvThumb);
		}
//		if (object.getStatusPromot() == AppConstants.STATUS_PROMOT.USED) {
//			holder.mBtnUsingPromotions.setEnabled(false);
//			holder.mImvUsed.setVisibility(View.VISIBLE);
//		} else {
//			holder.mImvUsed.setVisibility(View.GONE);
//			holder.mBtnUsingPromotions.setEnabled(true);
//		}
		if (mColor.length() > 0) {
			holder.mBtnUsingPromotions.setBackgroundColor(Color.parseColor(mColor));
		}
		holder.mBtnUsingPromotions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Integer.valueOf(object.getStampNeed()) <= Integer
						.valueOf(object.getNumberStampHave())) {
					if (mOnclick != null) {
						mOnclick.onRequest(object);
					}
				} else {
					Toast.makeText(
							mContext,
							mContext.getString(R.string.error_using_promot_not_enough),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		return convertView;
	}

	public void setListener(onUsingPromot event) {
		mOnclick = event;
	}

	private class viewHolder {
		private TextViewW6 mTvNumberStamp;
		private TextViewW3 mTvTitle;
		private ResizableImageView mImvThumb;
		private Button mBtnUsingPromotions;
		private ImageView mImvUsed;
	}

	public interface onUsingPromot {
		void onRequest(PromotionObject object);
	}

}
