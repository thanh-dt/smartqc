package app.com.hss.cooking.magatama.shop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import org.json.JSONObject;

import java.util.List;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class ShopAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<ShopItem> mShopItem;
	private Context mContext;

	public void add(ShopItem newsItem) {
		mShopItem.add(newsItem);
	}

	@Override
	public int getCount() {
		return mShopItem.size();
	}

	@Override
	public ShopItem getItem(int position) {
		// TODO Auto-generated method stub
		return mShopItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ShopAdapter(Context context, List<ShopItem> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mShopItem = objects;
		mContext  = context;
	}

	// 一行ごとのビューを作成する。
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ShopItem item = this.getItem(position);
		
		if (item != null) {
			
			if (!isEnabled(position)) {
				// 見出し行
				if (convertView == null) {
					view = mInflater.inflate(R.layout.shop_list_index, null);
				}
				AQuery aq = new AQuery(view);
				
				// ヘッダーカラー設定
				setupHeaderColor(aq);

				aq.id(R.id.shop_cate_list_index).text(item.getTitle().toString());
				
			} else {
				// データ行
				
				if (convertView == null) {
					view = mInflater.inflate(R.layout.shop_list_row, null);
				}
				AQuery aq = new AQuery(view);
				
				aq.id(R.id.shop_cate_list_title).text(item.getTitle().toString());
				//aq.id(R.id.shop_cate_list_descr).text(item.getDescription().toString());
				if (item.getImage() != null && item.getImage().length() > 0) {
					//aq.id(R.id.shop_cate_list_img).progress(R.id.blank_image).image(item.getImage().toString(), true, true, 400, 0, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
					aq.id(R.id.shop_cate_list_img).image(item.getImage().toString());
					aq.id(R.id.shop_cate_list_img).getView().setVisibility(View.VISIBLE);
				} else {
					aq.id(R.id.shop_cate_list_img).clear();
					//aq.id(R.id.shop_cate_list_img).getView().setVisibility(View.GONE);
					aq.id(R.id.shop_loading_progress).visibility(View.INVISIBLE);
					aq.id(R.id.shop_cate_list_img).width(32).height(32).image(R.drawable.footer_icon_i);
				}
			}
		}else{
			view = mInflater.inflate(R.layout.shop_list_non, null);
		}
		return view;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return !(mShopItem.get(position).getIndexFlg());
	}
	
	public void setupHeaderColor(AQuery aq) {
		ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(mContext, new Globals());
		String jsonStr = api.getCurrentData();
		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject header = jsonObj.getJSONObject("header");
			if (header != null) {
				
				String type = header.getString("type");
				String color = "#" + header.getString("color");
				String barGradColorStart = "#" + header.getString("bar_gradation1");
				String barGradColorEnd = "#" + header.getString("bar_gradation2");
				
				// 背景設定
				if(type.equals("1")) {
					// 単一カラー
					aq.id(R.id.shop_cate_list_index).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(barGradColorStart), Color.parseColor(barGradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.BOTTOM_TOP, colors);
					aq.id(R.id.shop_cate_list_index).getView().setBackground(bgDraw);
				}
				
			}
		} catch(Exception e) {
			Log.v("MAGATAMA", "ViewActivity: setupColorTheme error");
		}
		
	}
	
}