package app.com.hss.cooking.magatama.shop;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
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

public class ShopCateFragment extends Fragment {
    private final static int PAGE_COUNT = 10;
    private Globals gl;
    private AQuery aq;

    private static ShopAdapter mShopAdapter;
    private int mStart = 0;
    private boolean mIsLoading = false;
    private View mViewFooter;
    private PullToRefreshListView mListView;

    private ArrayList<ShopItem> mItems;
    private View view;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ShopCateFragment newInstance() {
        ShopCateFragment fragment = new ShopCateFragment();
        return fragment;
    }

    public ShopCateFragment() {
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_cate, container, false);

        mShopAdapter = new ShopAdapter(getActivity(), new ArrayList<ShopItem>());

        gl = new Globals();
        aq = new AQuery(view);

        mListView = (PullToRefreshListView) view.findViewById(R.id.shop_cate_listView);
        mListView.setAdapter(mShopAdapter);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = Integer.parseInt((String) mItems.get(position - 1).getId());
                replaceFragment(position, itemId);
            }
        });

        mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            public void onLastItemVisible() {
                if (!mIsLoading && mStart > 0) {
                    getFooter().setVisibility(View.VISIBLE);
                    listAsyncJson();
                }
            }
        });

        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mShopAdapter = new ShopAdapter(getActivity(), new ArrayList<ShopItem>());
                mListView.setAdapter(mShopAdapter);
                mStart = 0;
                listAsyncJson();
            }
        });

        setupColorTheme();
        listAsyncJson();

        ActionBar actionBar = getActivity().getActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

        return view;
    }

    private void replaceFragment(int position, int itemId) {

        ShopSubCateFragment fragment = new ShopSubCateFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("position", position);
        bundle.putInt("parent_id", itemId);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.addToBackStack(null);
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    /**
     *
     */
    public void listAsyncJson() {

        if (!mIsLoading) {
            mIsLoading = true;
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("client", gl.getClientID());
            params.put("start", mStart);
            params.put("end", PAGE_COUNT);

            aq.ajax(gl.getUrlApiShopCate(), params, JSONObject.class, this, "setListView");
        }
    }

    /**
     * @param url
     * @param json
     * @param status
     */
    public void setListView(String url, JSONObject json, AjaxStatus status) {
        Log.v("MAGATAMA", "ShopFragment JSON : " + json);

        if (json != null) {
            mItems = new ArrayList<ShopItem>();

            try {

                JSONObject top = json.getJSONObject("shop_top");
                String fileName = top.getString("file_name");
                if (!fileName.equals("")) {
                    String topImage = gl.getUrlImgShopMulti() + top.getString("file_name");
                    aq.id(R.id.shopTopImage).progress(R.id.blank_image).image(topImage, true, false, 400, R.id.blank_image, null, AQuery.FADE_IN_NETWORK, AQuery.RATIO_PRESERVE);
                }

                JSONArray list = json.getJSONArray("list");
                int max = list.length();
                JSONObject obj;
                for (int i = 0; i < max; i++) {
                    obj = list.getJSONObject(i);

                    ShopItem currentItem = new ShopItem();
                    currentItem.setId(obj.getString("id"));
                    //currentItem.setTitle(obj.getString("parent_id"));
                    currentItem.setTitle(obj.getString("title"));
                    //currentItem.setTitle(obj.getString("enable"));
                    String image = obj.getString("file_name");

                    if (image != null && image.length() > 0) {
                        currentItem.setImage(gl.getUrlImgShopMulti() + image);
                    }

                    mShopAdapter.add(currentItem);
                    mItems.add(currentItem);
                    mStart += 1;
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
        mShopAdapter.notifyDataSetChanged();
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
                if (type.equals("1")) {
                    // 単一カラー
                    aq.id(R.id.menu_bg).backgroundColor(Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
                    aq.id(R.id.menu_bg).image(bgDraw);
                }

                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {
                }

                // 背景画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = gl.getImgTopBackground() + fileName;
                    aq.id(R.id.menu_bg).progress(R.id.blank_image).image(image);
                } else {
                    //aq.id(R.id.newsIconText).visibility(View.VISIBLE);
                }

            }

        } catch (Exception e) {
            Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
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
