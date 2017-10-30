package app.com.hss.cooking.magatama.menu;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class MenuFragment extends Fragment {
	private final static int PAGE_COUNT = 10;
	private Globals gl;
	private AQuery aq;

	private static MenuAdapter mMenuAdapter;
	private int mStart = 0;
	private boolean mIsLoading = false;
	private View mViewFooter;
	private PullToRefreshListView mListView;
	
	private ArrayList<MenuItem> mItems;
	
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static MenuFragment newInstance() {
		MenuFragment fragment = new MenuFragment();
		return fragment;
	}

	public MenuFragment() {
	}

	@Override
	public void onAttach(Activity act){
		super.onAttach(act);
	} 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);

		Log.v("MAGATAMA", "MenuFragment : onCreateView");
		
		gl = new Globals();
		aq = new AQuery(view);

		mMenuAdapter = new MenuAdapter(getActivity(), new ArrayList<MenuItem>());		
		mListView = (PullToRefreshListView) view.findViewById(R.id.menu_listView);
		mListView.setAdapter(mMenuAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v("MAGATAMA", "MenuFragment ListView Selected : " + mItems.get(position - 1).getTitle());
				
				int menuId = Integer.parseInt((String)mItems.get(position - 1).getId());
				int simpleMode = Integer.parseInt(mItems.get(position - 1).getSimpleMode().toString());
				replaceFragment(position, menuId, simpleMode);
			}
		});
		
//		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//			public void onLastItemVisible() {
//				if (!mIsLoading && mStart > 0) {
//					getFooter().setVisibility(View.VISIBLE);
//					menuListAsyncJson();
//				}
//			}
//		});
		
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mMenuAdapter = new MenuAdapter(getActivity(), new ArrayList<MenuItem>());
				mListView.setAdapter(mMenuAdapter);
				mStart = 0;
				menuListAsyncJson();
			}
		});
		
		setupColorTheme();
		menuListAsyncJson();

		android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
		ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
		
		return view;
	}

    private void replaceFragment(int position, int menuId, int simpleMode) {
    	
    	if(simpleMode==1) {
    		
    		MenuDetailFragment fragment = new MenuDetailFragment();
    		Bundle bundle = new Bundle();
    		
    		bundle.putInt("position", position);
    		bundle.putInt("parent_id", menuId);
    		bundle.putInt("menu_id", menuId);
    		fragment.setArguments(bundle);

    		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
			ft.addToBackStack(null);
    		//ft.add(R.id.container, fragment);
			ft.replace(R.id.container, fragment);
    		ft.commit();
    		
    	} else {
    		
    		SubMenuFragment fragment = new SubMenuFragment();
    		Bundle bundle = new Bundle();
    		
    		bundle.putInt("position", position);
    		bundle.putInt("parent_id", menuId);
    		fragment.setArguments(bundle);

    		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
			ft.addToBackStack(null);
    		//ft.add(R.id.container, fragment);
    		ft.replace(R.id.container, fragment);
    		ft.commit();
    	}
    }

	/**
	 *
	 */
	public void menuListAsyncJson() {
		mIsLoading = true;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("client", gl.getClientID());
		aq.ajax(gl.getUrlApiMenu(), params, JSONObject.class, this, "setListView");
	}

	/**
	 * @param url
	 * @param json
	 * @param status
	 */
	public void setListView(String url, JSONObject json, AjaxStatus status) {
		Log.v("MAGATAMA", "MenuFragment JSON : " + json);
		
		if (json != null) {
			mItems = new ArrayList<MenuItem>();
			
			try {
				
				JSONObject top = json.getJSONObject("menu_top");
				String topImage = gl.getUrlApiMenuTop() + top.getString("file_name");
				if(!topImage.equals("")){
					aq.id(R.id.menuTopImage).progress(R.id.blank_image).image(topImage, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
				}
				
				JSONArray list = json.getJSONArray("list");
				int max = list.length();
				if(max > 0){
					JSONObject obj;
					for (int i = 0; i < max; i++) {
						obj = list.getJSONObject(i);
	
						MenuItem currentItem = new MenuItem();
						currentItem.setId(obj.getString("id"));
						currentItem.setTitle(obj.getString("title"));
						currentItem.setDescription(obj.getString("sub_title"));
						currentItem.setSimpleMode(obj.getString("simple"));
						String image = obj.getString("file_name");
						
						if (image != null && image.length() > 0) {
							if(obj.getString("simple").equals("1")) {
								currentItem.setImage(gl.getUrlImgMenuItem() + image);
							} else { 
								currentItem.setImage(gl.getUrlImgMenuCate() + image);
							}
						}
						
						mMenuAdapter.add(currentItem);
						mItems.add(currentItem);
						mStart += 1;
					}
					
					// リストの最後尾に空白行を追加する
					MenuItem blankItem = new MenuItem();
					blankItem.setBlankFlg("1");
					mMenuAdapter.add(blankItem);
				} else {
					// カテゴリがないとき
					aq.id(R.id.menu_blank_text).text("カテゴリがありません。");
					aq.id(R.id.menu_blank_text).visible();
					aq.id(R.id.menu_back_icon).visible();
				}
				if (max == 0 || max < PAGE_COUNT) {
					mStart = -1;
					getFooter().setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.v("MAGATAMA", "メニューの読み込みでエラー");
			}
		}
		
		mIsLoading = false;
		getFooter().setVisibility(View.GONE);
		mMenuAdapter.notifyDataSetChanged();
		mListView.onRefreshComplete();
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
					aq.id(R.id.menu_bg).backgroundColor(Color.parseColor(color));
				} else if (type.equals("2")) {
					// グラデーション設定
					int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
					GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
					aq.id(R.id.menu_bg).image(bgDraw);
				}
				
				String fileName = "";
				try{
					fileName = bg.getString("file_name");
				} catch(Exception e){
				}
				
				// 背景画像が設定されているとき
				if(fileName.length() > 0) {
					String image = gl.getImgTopBackground() + fileName;
					aq.id(R.id.menu_bg).progress(R.id.blank_image).image(image);
				}
				
			}

		} catch(Exception e) {
			Log.v("MAGATAMA", "MenuFragment: setupColorTheme error");
		}
		 			
	}
	
	private View getFooter() {
		if (mViewFooter == null) {
			//			mViewFooter = getLayoutInflater().inflate(R.layout.view_footer, null);
			mViewFooter = aq.id(R.id.footer).getView();
		}
		return mViewFooter;
	}
	
}
