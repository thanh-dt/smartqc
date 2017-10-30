package app.com.hss.cooking.magatama.shop;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class ShopDetailFragment extends Fragment {

	private Globals gl;
	private AQuery aq;
	 
	private String shopImage;
	private String shopName;
	private String comment;
	private String zipCode;
	private String address;
	private String tel;
	private String fax;
	private String email;
	private String shopUrl;
	private String openHours;
	private String onlineShopUrl;
	private String holiday;
	private String lat;
	private String lng;

	private int parent_id;
	
	private View rootView;
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static ShopDetailFragment newInstance() {
		ShopDetailFragment fragment = new ShopDetailFragment();
		return fragment;
	}

	public ShopDetailFragment() {
		
	}

	@Override
	public void onAttach(Activity act) {
		super.onAttach(act);
		gl = new Globals();
		parent_id = getArguments().getInt("parent_id");
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_detail_view, container, false);
		rootView = view;
		aq = new AQuery(view);
		
		setupColorTheme();
		shopInfoAsyncJson();
		
		aq.id(R.id.shop_mail_btn).clicked(this, "mailTo");
		aq.id(R.id.shop_tel_btn).clicked(this, "phoneTo");
		aq.id(R.id.shop_home_page_btn).clicked(this, "linkToHomepage");
		aq.id(R.id.shop_online_shop_btn).clicked(this, "linkToOnlineShop");
		aq.id(R.id.shop_map_btn).clicked(this, "mapTo");
		
		ActionBar actionBar = getActivity().getActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
		
		return view;
	}
	
	public void shopInfoAsyncJson() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client", gl.getClientID());
//		params.put("parent_id", parent_id);
//		params.put("start", mStart);
//		params.put("end", PAGE_COUNT);
		
		int parent_id = getArguments().getInt("parent_id");		
		aq.ajax(gl.getUrlApiShopCate() + parent_id, params, JSONObject.class, this, "setListView");
	}

	/**
	 * @param url
	 * @param json
	 * @param status
	 */
	public void setListView(String url, JSONObject json, AjaxStatus status) {
		Log.v("MAGATAMA", "ShopDetailFragment JSON : " + json);

		if (json != null) {

			int shopId = getArguments().getInt("shop_id");
			
			try {
				JSONArray list = json.getJSONArray("list");
				int max = list.length();
				JSONObject obj;
				for (int i = 0; i < max; i++) {
					obj = list.getJSONObject(i);
					
					int id = Integer.parseInt(obj.getString("id"));					
					if(id == shopId) {
						shopName = obj.getString("shop_name");
						comment = obj.getString("comment");
						zipCode = obj.getString("zip_code1") + "-" + obj.getString("zip_code2");
						address = obj.getString("pref") + obj.getString("city") + obj.getString("address_opt1") + obj.getString("address_opt2");
						tel = obj.getString("tel1") + "-" + obj.getString("tel2") + "-" + obj.getString("tel3");
						fax = obj.getString("fax1") + "-" + obj.getString("fax2") + "-" + obj.getString("fax3");
						email = obj.getString("email");
						openHours = obj.getString("open_hours");
						onlineShopUrl = obj.getString("online_shop");
						holiday = obj.getString("holiday");
						shopUrl = obj.getString("url");
						lat = obj.getString("lat");
						lng = obj.getString("lng");
						String image = obj.getString("file_name");
						shopImage = "";
						if (image != null && image.length() > 0) {
							shopImage = gl.getUrlImgShopMulti() + image;
						}
					}					
					
				}
								
			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("MAGATAMA", "店舗情報の読み込みでエラー");
			}
			
			setupShopItem();
			
		}
		
	}
	
	private void setupShopItem() {
		aq.id(R.id.textViewShopName).text(shopName);
		aq.id(R.id.textViewComment).text(comment);
		aq.id(R.id.textViewAddress).text(address);
		aq.id(R.id.textViewZip).text(zipCode);
		aq.id(R.id.textViewTel).text(tel);
		aq.id(R.id.textViewFax).text(fax);
		aq.id(R.id.textViewEmail).text(email);
		aq.id(R.id.textViewURL).text(shopUrl);
		aq.id(R.id.textViewOnlineShop).text(onlineShopUrl);
		aq.id(R.id.textViewOpen).text(openHours);
		aq.id(R.id.textViewHoliday).text(holiday);
		
		if(shopImage == "" || shopImage.length() == 0) {
			aq.id(R.id.imageViewShopImage).image(R.drawable.footer_icon_i).width(72).height(72);;
		} else {
			aq.id(R.id.imageViewShopImage).image(shopImage, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
		}
		
		// 項目がなければ非表示
		if (comment == "" || comment.length() == 0) aq.id(R.id.shop_detail_comment).visibility(View.GONE);
		if (zipCode == "-" || zipCode.length() == 1) aq.id(R.id.shop_detail_zip).visibility(View.GONE);
		if (address == "" || address.length() == 0) aq.id(R.id.shop_detail_add).visibility(View.GONE);
		if (tel == "--" || tel.length() == 2) {
			aq.id(R.id.shop_detail_tel).visibility(View.GONE);
			aq.id(R.id.shop_tel_btn).visibility(View.GONE);
		}
		if (fax == "--" || fax.length() == 2) aq.id(R.id.shop_detail_fax).visibility(View.GONE);
		if (email == "" || email.length() == 0) {
			aq.id(R.id.shop_detail_email).visibility(View.GONE);
			aq.id(R.id.shop_mail_btn).visibility(View.GONE);
		}
		if ((tel == "--" || tel.length() == 2) && (email == "" || email.length() == 0)) {
			aq.id(R.id.shop_contact_layout).visibility(View.GONE);
		}

		if (shopUrl == "" || shopUrl.length() == 0) {
			aq.id(R.id.shop_detail_url).visibility(View.GONE);
			aq.id(R.id.shop_home_page_layout).visibility(View.GONE);
		}

		if (onlineShopUrl == "" || onlineShopUrl.length() == 0) {
			aq.id(R.id.shop_detial_onlineShop).visibility(View.GONE);
			aq.id(R.id.shop_online_shop_layout).visibility(View.GONE);
		}

		if(lat.length() == 0 || lng.length() == 0) {
			aq.id(R.id.shop_detail_map_layout).visibility(View.GONE);
		}
		
		if (openHours == "" || openHours.length() == 0) aq.id(R.id.shop_detail_open).visibility(View.GONE);
		if (holiday == "" || holiday.length() == 0) aq.id(R.id.shop_detail_holiday).visibility(View.GONE);

		aq.id(R.id.menuBlock).visibility(View.VISIBLE);
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
					aq.id(R.id.shop_info_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.shop_info_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){

				}
				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.shop_info_bg).progress(R.id.blank_image).image(image);
				}
			}

		} catch(Exception e) {
			Log.v("MAGATAMA", "ShopDetailFragment: setupColorTheme error");
		}		
	}

	public void linkToHomepage() {
		Uri uri = Uri.parse(shopUrl);
		Intent i = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(i);
	}

	public void linkToOnlineShop() {
		Uri uri = Uri.parse(onlineShopUrl);
		Intent i = new Intent(Intent.ACTION_VIEW,uri);
		startActivity(i);
	}

	public void mailTo() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("メーラーの起動");
		alertDialogBuilder.setMessage("メールアプリを起動しますか？");
		alertDialogBuilder.setPositiveButton("起動", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("mailto:" + email));
				intent.putExtra(Intent.EXTRA_SUBJECT, "タイトル：");
				intent.putExtra(Intent.EXTRA_TEXT, "本文：");
				startActivity(Intent.createChooser(intent, "select"));
			}
		});
		alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	
	}
	
	public void phoneTo() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("電話発信");
		alertDialogBuilder.setMessage("電話をかけますか？");
		alertDialogBuilder.setPositiveButton("発信", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Uri uri = Uri.parse("tel:" + tel);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
			}
		});
		alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();				
	}
	
	public void mapTo() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setTitle("地図アプリの起動");
		alertDialogBuilder.setMessage("地図アプリを起動しますか？");
		alertDialogBuilder.setPositiveButton("起動", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.driveabout.app.NavigationActivity");
				Uri uri = Uri.parse("google.navigation:///?ll=" + lat + "," + lng + "&q=MAP");
				intent.setData(uri);
				startActivity(intent);
			}
		});
		alertDialogBuilder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertDialogBuilder.setCancelable(true);
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
}
