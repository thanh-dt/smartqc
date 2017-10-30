package app.com.hss.cooking.magatama.menu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class MenuDetailFragment extends Fragment {
	private Globals gl;
	private AQuery aq;
	
	private int parent_id;
	private int menu_id;
	private int position;
	
	private View view;
	
	public static MenuDetailFragment newInstance() {
		MenuDetailFragment fragment = new MenuDetailFragment();
		return fragment;
	}

	public MenuDetailFragment() {
	}
	
	@Override
	public void onAttach(Activity act){
		super.onAttach(act);
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_menu_detail, container, false);
		aq = new AQuery(view);
		this.init();

		setupColorTheme();
		detailAsyncJson();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

		return view;
	}
	
	public void init() {
		parent_id = getArguments().getInt("parent_id");
		menu_id = getArguments().getInt("menu_id");
		position = getArguments().getInt("position");
		Log.v("MAGATAMA", "MenuFragment onAttach Place ID : " + parent_id + " / position : " + position);
				
		gl = new Globals();
		
//		aq.id(R.id.menu_detail_title).text("");
//		aq.id(R.id.menu_detail_descr).text("");
//		aq.id(R.id.menu_detail_price).text("");

	}
	
	public void detailAsyncJson() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client", gl.getClientID());
		String url = gl.getUrlApiMenu() + parent_id + "/";
		aq.ajax(url, params, JSONObject.class, this, "asyncCallBack");
	}
	
	public void asyncCallBack(String url, JSONObject json, AjaxStatus status) {
		
		try {			
			JSONArray list = json.getJSONArray("list");
			JSONObject obj = null;			
			
			int len = list.length();			
			for(int i=0; i<len; i++){
				obj = list.getJSONObject(i);
				if (obj != null) {
					int menuId = Integer.parseInt(obj.getString("id"));
					if(menuId==menu_id) {
						aq.id(R.id.menu_detail_title).text(obj.getString("title"));
						aq.id(R.id.menu_detail_descr).text(obj.getString("description"));
						String date = obj.getString("updated_at");
						aq.id(R.id.menu_detail_date).text(date);
						int price = Integer.parseInt(obj.getString("price"));
						if( price==0 | obj.getString("price").equals("") ) {
							aq.id(R.id.menu_detail_price).text("");
						} else {
							NumberFormat numberFormat = NumberFormat.getNumberInstance();
							String priceText = "¥" + numberFormat.format(price) + "-";
							aq.id(R.id.menu_detail_price).text(priceText);
						}
						
						TextView priceView = (TextView) view.findViewById(R.id.menu_detail_price);
						Typeface typeface = Typeface.createFromAsset(aq.getContext().getAssets(), "font/DINEngschriftStd.ttf");
						priceView.setTypeface(typeface);
						priceView.setTextSize(40);
						
						String fileName = obj.getString("file_name");
			 			if(!fileName.equals("")){
			 				String image = gl.getUrlImgMenuItem() + obj.getString("file_name");
							aq.id(R.id.menu_detail_imgView).progress(R.id.blank_image).image(image, true, false, 210, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
						}
			 			
						break;
					}
				}
			}

		} catch (JSONException e) {
			Log.v("MAGATAMA", "MenuDeitalFragment parse error:" + e);
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	public void setupColorTheme() {
		ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(getActivity(), new Globals());
		String jsonStr = api.getCurrentData();
		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject bg = jsonObj.getJSONObject("background");
			if (bg != null) {
				String color = "#" + bg.getString("color");
//				String subColor = "#" + bg.getString("sub_color");
				String type = bg.getString("type");
				String gradColorStart = "#" + bg.getString("gradation1");
				String gradColorEnd = "#" + bg.getString("gradation2");
//				String barGradColorStart = "#" + bg.getString("bar_gradation1");
//				String barGradColorEnd = "#" + bg.getString("bar_gradation2");
//				String subGradColor = "#" + bg.getString("sub_gradation");

				// 背景設定
				if(type.equals("1")) {
					// 単一カラー
					aq.id(R.id.menu_detail_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.menu_detail_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
				}

				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.menu_detail_bg).progress(R.id.blank_image).image(image);
				}
			}
		} catch(Exception e) {
			Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
		}
		
	}
}


