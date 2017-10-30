package app.com.hss.cooking.magatama.setting;

import android.annotation.SuppressLint;
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

import java.util.HashMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class UserInfoFragment extends Fragment {

	private Globals gl;
	private AQuery aq;
	
	private String _point = "0";
	private String _userId = "";
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static UserInfoFragment newInstance() {
		UserInfoFragment fragment = new UserInfoFragment();
		return fragment;
	}

	public UserInfoFragment() {
		
	}

	@Override
	public void onAttach(Activity act) {
		super.onAttach(act);

		gl = new Globals();
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_info, container, false);
		aq = new AQuery(view);
		
		userDataAsyncJson();
		setupColorTheme();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
                
		return view;
	}

	public void userDataAsyncJson() {
        HashMap<String, String> params = new HashMap<String, String>();
        gl.initPreference(getActivity());
		params.put("client_id", gl.getClientID());
		params.put("token", gl.getDeviceToken());
        
		aq.ajax(gl.getUrlApiUserLogin(), params, JSONArray.class, this, "setUserData");
	}
	
	public void setUserData(String url, JSONArray json, AjaxStatus status) throws JSONException {
		JSONObject obj = json.getJSONObject(0);
		_userId = obj.getJSONObject("user").getString("user_id");
		_point = obj.getJSONObject("profile").getString("point");
		
		// 値セット
		aq.id(R.id.userInfo_user_id_text).text(_userId);
		aq.id(R.id.userInfo_point_text).text(_point);

		// フォント
		Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschriftStd.ttf");
		TextView userIdText = (TextView) aq.id(R.id.userInfo_user_id_text).getView();
		TextView pText = (TextView) aq.id(R.id.userInfo_point_text).getView();
		TextView pUnit = (TextView) aq.id(R.id.userInfo_point_unit).getView();
		userIdText.setTypeface(typeface);
		userIdText.setTextSize(46);
		pText.setTypeface(typeface);
		pText.setTextSize(46);
		pUnit.setTypeface(typeface);
		pUnit.setTextSize(46);
	}

	private void setupColorTheme() {
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
					aq.id(R.id.setting_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.setting_bg).image(bgDraw);
				}
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
					
				}

				// 画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.setting_bg).progress(R.id.blank_image).image(image);
				}
				
			}

		} catch(Exception e) {
			Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
		}		
	}
	
	
}
