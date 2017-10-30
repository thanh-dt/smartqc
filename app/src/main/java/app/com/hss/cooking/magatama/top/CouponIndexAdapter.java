package app.com.hss.cooking.magatama.top;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.coupon.CouponItem;

public class CouponIndexAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<CouponItem> mCouponItems;

	public void add(CouponItem couponItem) {
		mCouponItems.add(couponItem);
	}

	@Override
	public int getCount() {
		return mCouponItems.size();
	}

	@Override
	public CouponItem getItem(int position) {
		// TODO Auto-generated method stub
		return mCouponItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public CouponIndexAdapter(Context context, List<CouponItem> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mCouponItems = objects;
	}

    // 一行ごとのビューを作成する。
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.coupon_index_list_row, null);
		}
		CouponItem item = this.getItem(position);
		
		Log.v("MAGATAMA", "coupon item : "+item);
		
		if (item != null) {
			AQuery aq = new AQuery(view);
			
			// フォントからアイコン
			TextView icon = (TextView) view.findViewById(R.id.coupon_index_icon);
			Typeface typeface = Typeface.createFromAsset(aq.getContext().getAssets(), "font/ipost-icon01-regular.ttf");
			icon.setTypeface(typeface);
			icon.setTextSize(25);
			
			aq.id(R.id.coupon_list_title).text(item.getTitle().toString());
			//aq.id(R.id.coupon_list_descr).text(item.getDescription().toString());
			String limit = item.getEndDatetime().toString();
			if(limit.equals("")) {
				limit = "有効期限なし";
			} else {
				limit = "有効期限 " + limit + "まで";
			}
			aq.id(R.id.coupon_list_descr).text(limit);
			if(item.getNewFlg().toString().equals("0")) {
				aq.id(R.id.coupon_new_label).visibility(View.INVISIBLE);
			}
			Log.v("MAGATAMA", item.getImage().toString());
		}else{
			view = mInflater.inflate(R.layout.menu_list_non, null);
		}
		return view;
	}
}