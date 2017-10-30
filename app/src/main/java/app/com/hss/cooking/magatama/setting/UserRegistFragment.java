package app.com.hss.cooking.magatama.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class UserRegistFragment extends Fragment {

	private Globals gl;
	private AQuery aq;
	private ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;

	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static UserRegistFragment newInstance() {
		UserRegistFragment fragment = new UserRegistFragment();
		return fragment;
	}

	public UserRegistFragment() {
		
	}

	@Override
	public void onAttach(Activity act) {
		super.onAttach(act);

		gl = new Globals();
	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_user_regist, container, false);
		aq = new AQuery(view);
		
		EditText passwordEditText = (EditText) aq.id(R.id.setting_editPassword).getView();
		passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {		         
		        @Override
		        public void onFocusChange(View v, boolean hasFocus) {
		            // EditTextのフォーカスが外れた場合
		            if (hasFocus == false) {
		                // ソフトキーボードを非表示にする
		                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		            }
		        }
		});
				
		setupColorTheme();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
        
        aq.id(R.id.user_regist_btn).clicked(this, "registClicked");
                
		return view;
	}
	
	public void registClicked(View v) {
		
		Button bt = (Button) aq.id(R.id.user_regist_btn).getView();
	    bt.setFocusable(true);
	    bt.setFocusableInTouchMode(true);
	    bt.requestFocus();
		
		// バリデーション
		String password = aq.id(R.id.setting_editPassword).getEditText().getText().toString();
		if(password.length() < 8) {
			Toast.makeText(getActivity(), "パスワードは8文字以上で入力して下さい", Toast.LENGTH_SHORT).show();
			return;
		} else if(!password.matches("[0-9a-zA-Z]+")) {
			Toast.makeText(getActivity(), "パスワードは半角英数字で入力して下さい", Toast.LENGTH_SHORT).show();
			return;
		}
		
		gl.initPreference(getActivity());
		String token = gl.getDeviceToken();
		String pref = String.valueOf( aq.id(R.id.userRegist_pref_spinner).getSelectedItemPosition() + 1 );
		DatePicker picker = (DatePicker) aq.id(R.id.userRegist_birth_date_picker).getView();
		
		String y = String.format("%1$04d", picker.getYear());
		String m = String.format("%1$02d", picker.getMonth());
		String d = String.format("%1$02d", picker.getDayOfMonth());
		String birthday = y + "-" + m + "-" + d + " 00:00:00";		
		
		int g = aq.id(R.id.userRegist_gender_spinner).getSelectedItemPosition();
		String gender = String.valueOf(g + 1);
		
		// POST
		postUserData(password, token, pref, birthday, gender);				
	}
	
	private void postUserData(String password, String token, String pref, String birthday, String gender) {
        HashMap<String, String> params = new HashMap<String, String>();
		params.put("client_id", gl.getClientID());
		params.put("password", password);
		params.put("token", token);
		params.put("pref", pref);
		params.put("birthday", birthday);
		params.put("gender", gender);
        
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("情報を送信しています...");
		progressDialog.setCancelable(false);
		progressDialog.show();
				
		String url = gl.getUrlApiUserRegist(); 
		aq.ajax(url, params, String.class, new AjaxCallback<String>() {
		    @Override
		    public void callback(String url, String result, AjaxStatus status) {
		    	Log.v("MAGATAMA", "regist post user date. url:"+url+" result:"+result);
		    	if (status.getCode() != 200) {
			    	Log.v("MAGATAMA", "postUserData error ("+ status.getCode() + "): " + result);
			    	progressDialog.dismiss();
			    	Toast.makeText(getActivity(), "エラーが発生しました", Toast.LENGTH_SHORT).show();
		    	} else {
		    	
		    		// ユーザ情報を取得してローカルに保存
		    		userDataAsyncJson();
		    		
		        	progressDialog.dismiss();
		    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		    		alertDialogBuilder.setTitle("お知らせ");
		    		alertDialogBuilder.setMessage("ユーザー登録・設定が完了しました。");
		    		alertDialogBuilder.setNeutralButton("閉じる", new DialogInterface.OnClickListener() {
		    			@Override
		    			public void onClick(DialogInterface dialog, int which) {
		    				Fragment fragment = new SettingFragment();
		    				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		    				//ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
		    				ft.replace(R.id.container, fragment);
		    				ft.addToBackStack(null);
		    				ft.commit();
		    			}
		    		});
		    		AlertDialog alertDialog = alertDialogBuilder.create();
		    		alertDialog.show();
		    	}
			}
		});
	}
	
	public void userDataAsyncJson() {
        HashMap<String, String> params = new HashMap<String, String>();
		params.put("client_id", gl.getClientID());
		params.put("token", gl.getDeviceToken());
        
		aq.ajax(gl.getUrlApiUserLogin(), params, JSONArray.class, this, "setUserData");
	}
	
	public void setUserData(String url, JSONArray json, AjaxStatus status) throws JSONException {
		if(json!=null) {
			JSONObject obj = json.getJSONObject(0);
			String userId = obj.getJSONObject("user").getString("user_id");
			gl.setPrefKeyUserId(userId);
		}
	}
	
	private void setupColorTheme() {
		ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(getActivity(), new Globals());
		String jsonStr = api.getCurrentData();
		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject bg = jsonObj.getJSONObject("background");
			if (bg != null) {
				String color = "#" + bg.getString("color");
				String subColor = "#" + bg.getString("sub_color");
				String type = bg.getString("type");
				String gradColorStart = "#" + bg.getString("gradation1");
				String gradColorEnd = "#" + bg.getString("gradation2");
//				String barGradColorStart = "#" + bg.getString("bar_gradation1");
//				String barGradColorEnd = "#" + bg.getString("bar_gradation2");
//				String subGradColor = "#" + bg.getString("sub_gradation");

				// 背景設定
				if(type.equals("1")) {
					// 単一カラー
					aq.id(R.id.userRregist_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.userRregist_bg).image(bgDraw);
				}
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
					
				}

				// 画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.userRregist_bg).progress(R.id.blank_image).image(image);
				}
				
			}

		} catch(Exception e) {
			Log.v("MAGATAMA", "UserRegistFragment: setupColorTheme error");
		}		
	}
	
}
