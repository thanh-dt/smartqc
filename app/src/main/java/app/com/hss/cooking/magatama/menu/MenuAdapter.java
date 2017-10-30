package app.com.hss.cooking.magatama.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import java.util.List;

import app.com.hss.cooking.R;

public class MenuAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<MenuItem> mMenuItems;

	public void add(MenuItem newsItem) {
		mMenuItems.add(newsItem);
	}

	@Override
	public int getCount() {
		return mMenuItems.size();
	}

	@Override
	public MenuItem getItem(int position) {
		// TODO Auto-generated method stub
		return mMenuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public MenuAdapter(Context context, List<MenuItem> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuItems = objects;
	}

	// 一行ごとのビューを作成する。
	public View getView(int position, View convertView, ViewGroup parent) {

//		View view = convertView;
//		if (convertView == null) {
//			view = mInflater.inflate(R.layout.menu_list_row, null);
//		}
		View view = mInflater.inflate(R.layout.menu_list_row, null);
		MenuItem item = this.getItem(position);
		
		Log.v("YOSHITAKA", "menu item : "+item);
		
		if (item != null) {
			
			// マージンとるための空行
			if(item.getBlankFlg().equals("1")){
				view = mInflater.inflate(R.layout.menu_list_row_blank, null);
				return view;
			}
			
			AQuery aq = new AQuery(view);
			aq.id(R.id.menu_list_title).text(item.getTitle().toString());
			aq.id(R.id.menu_list_descr).text(item.getDescription().toString());
			Log.v("MAGATAMA", "Menu Image"+item.getImage().toString());
			if (item.getImage() != null && item.getImage().length() > 0) {
				//aq.id(R.id.menu_list_img).progress(R.id.blank_image).image(item.getImage().toString(), true, true, 400, 0, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
				aq.id(R.id.menu_list_img).image(item.getImage().toString());
				aq.id(R.id.menu_list_img).getView().setVisibility(View.VISIBLE);
			} else {
				aq.id(R.id.menu_list_img).clear();
				//aq.id(R.id.menu_list_img).getView().setVisibility(View.GONE);
				//aq.id(R.id.menu_list_img).getView().setVisibility(View.INVISIBLE);
				//aq.id(R.id.menu_img_layout).visibility(View.INVISIBLE);
				aq.id(R.id.menu_loading_progress).visibility(View.INVISIBLE);
				aq.id(R.id.menu_list_img).width(32).height(32).image(R.drawable.footer_icon_g);
			}
		}else{
			view = mInflater.inflate(R.layout.menu_list_non, null);
		}
		return view;
	}

	@Override
	public boolean isEnabled(int position) {
		boolean ret = true;
		// 空行はクリックできないようにする
		if(mMenuItems.get(position).getBlankFlg().equals("1")) {
			ret = false;
		}
		return ret;
	}

}