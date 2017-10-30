package app.com.hss.cooking.magatama.top;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.AsyncFileDownload;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.MotionableScrollView;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.ProgressHandler;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.ZipDecompresser;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.coupon.CouponFragment;
import app.com.hss.cooking.magatama.coupon.CouponItem;
import app.com.hss.cooking.magatama.menu.MenuDetailFragment;
import app.com.hss.cooking.magatama.menu.MenuFragment;
import app.com.hss.cooking.magatama.menu.MenuItem;
import app.com.hss.cooking.magatama.news.NewsFragment;
import app.com.hss.cooking.magatama.news.NewsItem;
import app.com.hss.cooking.magatama.setting.SettingFragment;
import app.com.hss.cooking.magatama.shop.ShopCateFragment;
import app.com.hss.cooking.magatama.shop.ShopFragment;
import app.com.hss.cooking.magatama.stampcard.DialogShowShortContent;
import app.com.hss.cooking.magatama.stampcard.FragmentMainStampCard;
import app.com.hss.cooking.magatama.stampcard.FragmentStampCard;
import app.com.hss.cooking.magatama.utils.AppConstants;
import app.com.hss.cooking.magatama.utils.JsonParserUtils;
import app.com.hss.cooking.magatama.utils.PermissionUtils;
import app.com.hss.cooking.viewindicator.ViewIndicator;

@SuppressLint("NewApi")
public class TopFragment extends Fragment implements OnTouchListener {

    private static final int REQUEST_READ_WRITE = 111;
    ArrayAdapter<String> adapter;

    private Globals gl;
    private AQuery aq;
    private static NewsIndexAdapter newsIndexAdapter;
    private static MenuIndexAdapter menuIndexAdapter;
    private static CouponIndexAdapter couponIndexAdapter;
    private int mStart = 0;
    private boolean mIsLoading = false;
    private final static int PAGE_COUNT = 4;
    private GestureDetector mGestureDetector;

    private CustomPagerAdapter pagerAdapter;

    private View rootView;
    private WebView cmsView;
    private MotionableScrollView topScrollView;
    //private ScrollView topScrollView;
    private LinearLayout topLayout;
    private String lat;
    private String lng;
    private String tel;
    private String mail;
    private String snsLink01;
    private String snsLink02;
    private String snsLink03;
    private String snsLink04;
    private String snsLink05;
    private String snsLink06;
    private String tophtml;

    private View imageSlider;
    private ViewPager mViewPager;
    private ViewIndicator mIndicator;

    private int margin;
    ArrayList<MenuItem> mItems;
    ArrayList<NewsItem> nItems;
    ArrayList<CouponItem> cItems;

    ArrayList<String> topImageList;
    private TopImageItem topImageItem;

    private LayoutInflater mInflater;

    private int newsListMaxSize;
    private int menuListMaxSize;
    private int couponListMaxSize;

    // ダウンロード
    private ProgressDialog progressDialog;
    private ProgressHandler progressHandler;
    private AsyncFileDownload asyncFileDownload;

    private int mColorButton = 0;

    final private String DIRECTORY_NAME = "public_html";
//	final private String FILE_NAME      = "public_html.zip";

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static TopFragment newInstance() {
        TopFragment fragment = new TopFragment();
        return fragment;
    }

    public TopFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v("MAGATAMA", "TopFragment onAttach");

        lat = "";
        lng = "";
        snsLink01 = "";
        snsLink02 = "";
        snsLink03 = "";
        snsLink04 = "";
        snsLink05 = "";
        snsLink06 = "";
        tophtml = "";

        newsListMaxSize = 5;
        menuListMaxSize = 5;
        couponListMaxSize = 5;

    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MAGATAMA", "TopFragment onTouchEvent");
        return mGestureDetector.onTouchEvent(event);

    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.v("MAGATAMA", "TopFragment onTouchEvent");
        return mGestureDetector.onTouchEvent(event);
        //return mGestureDetector.onTouchEvent(event) || super.dispatchTouchEvent(event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("MAGATAMA", "TopFragment onCreate");

        rootView = inflater.inflate(R.layout.fragment_top, container, false);
        mInflater = inflater;
        super.onCreate(savedInstanceState);

        //topScrollView = (ScrollView) rootView.findViewById(R.id.scrollView1);
        topScrollView = (MotionableScrollView) new MotionableScrollView(getActivity());
//		topScrollView = (ScrollView) new ScrollView(getActivity());
        topLayout = new LinearLayout(getActivity());
        topLayout.setOrientation(LinearLayout.VERTICAL);
        topScrollView.addView(topLayout);

        ViewGroup vg = (ViewGroup) rootView;
        vg.addView(topScrollView);

        gl = new Globals();
        gl.initPreference(getActivity());
        aq = new AQuery(getActivity());

        // Set Default Tab - zero based index
        SharedPreferences prefs = getActivity().getSharedPreferences("tabIndex", Context.MODE_PRIVATE);
        int tabIndex = prefs.getInt("tabIndex", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("tabIndex", 0);
        editor.apply();

        // 通知から起動したときニュースへ飛ばす
        if (tabIndex == 2) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, NewsFragment.newInstance(), "NewsFragment");
            fragmentTransaction.commit();
        }

        newsIndexAdapter = new NewsIndexAdapter(getActivity(), new ArrayList<NewsItem>());
        menuIndexAdapter = new MenuIndexAdapter(getActivity(), new ArrayList<MenuItem>());
        couponIndexAdapter = new CouponIndexAdapter(getActivity(), new ArrayList<CouponItem>());
        pagerAdapter = new CustomPagerAdapter(getActivity());

        setHasOptionsMenu(true);

        // TOP情報を通信開始
        topBlockAsyncJson();

        //setWait();

        addDailyStamp();

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.INVISIBLE);

        return rootView;
    }

    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(getActivity(), new Globals());
        String jsonStr = api.getCurrentData();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            if (bg != null) {
                String colorButton2 = "#" + bg.getString("color");
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                mColorButton = Color.parseColor(colorButton2);
                String type = bg.getString("type");
                String gradColorStart = "#" + bg.getString("gradation1");
                String gradColorEnd = "#" + bg.getString("gradation2");
//				String barGradColorStart = "#" + bg.getString("bar_gradation1");
//				String barGradColorEnd = "#" + bg.getString("bar_gradation2");
                String subGradColor = "#" + bg.getString("sub_gradation");
//				colorList.put("color", color);
//				colorList.put("subColor", subColor);
//				colorList.put("type", type);
//				colorList.put("gradation1", gradColorStart);
//				colorList.put("gradation2", gradColorEnd);
//				colorList.put("bar_gradation1", barGradColorStart);
//				colorList.put("bar_gradation2", barGradColorEnd);
//				colorList.put("sub_gradation", subGradColor);

                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    aq.id(R.id.top_background).backgroundColor(Color.parseColor(color));
                    // サブカラー設定
                    aq.id(R.id.news_block_separater).backgroundColor(Color.parseColor(subColor));
                    aq.id(R.id.menu_block_separater).backgroundColor(Color.parseColor(subColor));
                    aq.id(R.id.coupon_block_separater).backgroundColor(Color.parseColor(subColor));
                    aq.id(R.id.news_block_more_btn).backgroundColor(Color.parseColor(subColor));
                    aq.id(R.id.menu_block_more_btn).backgroundColor(Color.parseColor(subColor));
                    aq.id(R.id.coupon_block_more_btn).backgroundColor(Color.parseColor(subColor));

                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
                    //GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)});
                    GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
                    aq.id(R.id.top_background).image(bgDraw);

                    aq.id(R.id.news_block_separater).backgroundColor(Color.parseColor(subGradColor));
                    aq.id(R.id.menu_block_separater).backgroundColor(Color.parseColor(subGradColor));
                    aq.id(R.id.coupon_block_separater).backgroundColor(Color.parseColor(subGradColor));
                    aq.id(R.id.news_block_more_btn).backgroundColor(Color.parseColor(subGradColor));
                    aq.id(R.id.menu_block_more_btn).backgroundColor(Color.parseColor(subGradColor));
                    aq.id(R.id.coupon_block_more_btn).backgroundColor(Color.parseColor(subGradColor));
                }

                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }
                // 背景画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = gl.getImgTopBackground() + fileName;
                    aq.id(R.id.top_background).progress(R.id.blank_image).image(image);
                }

            }
        } catch (Exception e) {
            Log.v("MAGATAMA", "TopFragment: setupColorTheme error: " + e);
        }

    }


    public void topBlockAsyncJson() {
        Log.v("MAGATAMA", "TopFragment topBlockAsyncJson()");

        if (!mIsLoading) {
            mIsLoading = true;
            //setWait();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("client", gl.getClientID());
            params.put("start", mStart);
            params.put("end", PAGE_COUNT);

            aq.ajax(gl.getUrlApiTop(), params, JSONObject.class, this, "setListView");
        }
    }

    /**
     * @param url
     * @param json
     * @param status
     * @throws JSONException
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void setListView(String url, JSONObject json, AjaxStatus status) throws JSONException {
        Log.v("MAGATAMA", "TopFragment JSON : " + json);

        TreeMap<Integer, String> blockData = new TreeMap<Integer, String>();

        JSONArray blocks = json.getJSONArray("block");
        if (blocks != null) {
            int max = blocks.length();
            int index = 0;
            JSONObject obj;
            for (int i = index; i < max + index; i++) {
                try {
                    obj = blocks.getJSONObject(i);
                    String block_key = obj.getString("block_key");
                    int pos = Integer.parseInt(obj.getString("position"));
                    if (block_key.equals("top")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("cms")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("web")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("topic")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("news")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("menu")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("coupon")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("sns")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("gps")) {
                        blockData.put(pos, block_key);
                    } else if (block_key.equals("tel")) {
                        blockData.put(pos, block_key);
                    }

                    margin = Integer.parseInt(obj.getString("margin"));

                } catch (JSONException e) {
                    continue;
                }
            }

            JSONObject list = json.getJSONObject("list");

            Iterator<Integer> it = blockData.keySet().iterator();
            while (it.hasNext()) {
                Integer key = it.next();
                String block = "";
                try {
                    block = blockData.get(key);
                    if (block.equals("cms")) {
                        // CMS
                        try {
                            JSONObject cmsObj = list.getJSONObject("cms");
                            String downloadUrl = cmsObj.getString("url");
                            String scrollFlg = cmsObj.getString("scroll_enable");
                            int height = convertDipToPixels(Integer.parseInt(cmsObj.getString("height_Android")));
                            String zipHash = cmsObj.getString("zip_hash");

                            //cmsView = (WebView) mInflater.inflate(R.layout.web_view_block, null);
                            View view = (View) mInflater.inflate(R.layout.web_view_block, null);
                            cmsView = (WebView) view.findViewById(R.id.top_web_view_block);
                            if (scrollFlg.equals("1")) {
                                // スクロールするようにする
                                cmsView.setHorizontalScrollBarEnabled(true);
                            } else {
                                // スクロールしない
                                cmsView.setHorizontalScrollBarEnabled(false);
                                // スクロールボタン非表示
                                view.findViewById(R.id.web_up).setVisibility(View.INVISIBLE);
                                view.findViewById(R.id.web_down).setVisibility(View.INVISIBLE);
                            }

                            cmsView.getSettings().setJavaScriptEnabled(true);
                            WebViewClient webClient = (new WebViewClient() {
                                @SuppressLint("AddJavascriptInterface")
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    // ひどいパース
                                    // URLリンク
                                    Log.d("thanhdz", "shouldOverrideUrlLoading: " + url);
                                    if (url.startsWith("file:///data/")) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.replace(R.id.container, getReplaceFragment(url));
                                        fragmentTransaction.commit();
                                        return true;
                                    }

                                    if (url.length() >= 8 && url.substring(0, 7).equals("http://")) {
                                        Uri uri = Uri.parse(url);
                                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(i);
                                    } else if (url.length() >= 17 && url.substring(0, 16).equals("app://move?view=")) {
                                        // アプリ内リンク
                                        String key = url.substring(16);
                                        cmsLinkSelected(key);
                                    } else if (url.length() > 0 && url.startsWith("http")) {
                                        Uri uri = Uri.parse(url);
                                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(i);
                                    }
                                    return true;
                                }


                            });
                            cmsView.setWebViewClient(webClient);
                            cmsView.getSettings().setJavaScriptEnabled(true);
                            cmsView.setVerticalScrollbarOverlay(true);
                            cmsView.getSettings().setAppCacheEnabled(true);
                            //cmsView.loadUrl(fileUrl);
                            LinearLayout ll = new LinearLayout(getActivity());
                            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                            params.setMargins(5, 5, 5, margin);
                            ll.setLayoutParams(params);
                            //ll.addView(cmsView);
                            ll.addView(view);
                            LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
                            tl.addView(ll);
//							topLayout.addView(ll);

                            String old = gl.getPrefKeyCmsZipHash();
                            // 保存されたハッシュキーと比較して差違があればあらたにzipをダウンロードする
                            if (zipHash.equals(old)) {
                                // zipを展開する
                                checkPermissionBeforeSave();
                            } else {
                                // ハッシュキーを保存
                                gl.setPrefKeyCmsZipHash(zipHash);
                                // zipをDLして展開する
                                setupCmsData(downloadUrl);
                            }
                        } catch (Exception e) {
                            Log.v("MAGATAMA", "TopFragment setListView (cms) parse error: " + e);
                        }
                    } else if (block.equals("web")) {
                        // ウェブビュー
                        try {
                            JSONObject tophtml_obj = list.getJSONObject("web");
                            tophtml = tophtml_obj.getString("url");
                            String scrollFlg = tophtml_obj.getString("scroll_enable");
                            int height = convertDipToPixels(Integer.parseInt(tophtml_obj.getString("height_Android")));

                            //WebView v = (WebView) mInflater.inflate(R.layout.web_view_block, null);
                            View view = (View) mInflater.inflate(R.layout.web_view_block, null);
                            final WebView v = (WebView) view.findViewById(R.id.top_web_view_block);
                            v.setScrollContainer(true);
                            v.getSettings().setJavaScriptEnabled(true);
                            v.setWebViewClient(new WebViewClient());
                            v.getSettings().setJavaScriptEnabled(true);
                            v.setFocusable(true);

                            if (scrollFlg.equals("1")) {
                                // スクロールするようにする
                                v.setHorizontalScrollBarEnabled(true);
//								v.setOnTouchListener(new View.OnTouchListener() { 
//									 @Override 
//									 public boolean onTouch(View arg0, MotionEvent event) {
//										 return(event.getAction() != MotionEvent.ACTION_MOVE); 
//									 } 
//								});								
                            } else {
                                // スクロールしない
                                v.setHorizontalScrollBarEnabled(false);
                                v.setOnTouchListener(new OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View arg0, MotionEvent event) {
                                        return (event.getAction() == MotionEvent.ACTION_MOVE);
                                    }
                                });

                            }
                            //v.setFocusable(true);
                            //v.setFocusableInTouchMode(true);

//							v.setOnTouchListener(new View.OnTouchListener() {
//				                @Override
//				                public boolean onTouch(View v, MotionEvent event) {
//				                	Log.v("MAGATAMA", ">>>webView");
////				                    return true;
//				                	return false;
//				                }
//				            });

//							v.getSettings().setLoadWithOverviewMode(true);
//							v.getSettings().setUseWideViewPort(true);
                            //v.setFocusableInTouchMode(true);
                            //sv.getParent().requestDisqllowInterceptTouchEvent(true);
                            v.setVerticalScrollbarOverlay(true);
                            //v.getSettings().setAppCacheEnabled(true);
                            //v.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
                            v.loadUrl(tophtml);
                            //v.requestDisallowInterceptTouchEvent(true);

//							final boolean flag = false;
//							ScrollView scrollView = new ScrollView(getActivity());
//							scrollView.setOnTouchListener(new View.OnTouchListener() {
//							    @Override
//							    public boolean onTouch(View v, MotionEvent event) {
//							         if (flag) {
//							             return false;
//							         } else {
//							             //falseを返すと、スクロールする
//							              return true;
//							         }
//							    }
//							});
//							scrollView.addView(v);

                            LinearLayout ll = new LinearLayout(getActivity());
                            //ll.requestDisallowInterceptTouchEvent(true);
                            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                            params.setMargins(5, 5, 5, margin);
                            ll.setLayoutParams(params);
                            //ll.addView(v);
                            ll.addView(view);
                            LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
                            //ViewGroup rv = (ViewGroup) rootView.findViewById(R.id.scrollView1);

                            ScrollView sv = (ScrollView) rootView.findViewById(R.id.scrollView1);
                            sv.bringChildToFront(v);
                            sv.setOnTouchListener(new OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
//				                    return true;
                                    Log.v("MAGATAMA", ">>>scrollView");
                                    return false;
                                }
                            });

                            //rv.requestDisallowInterceptTouchEvent(true);
                            tl.requestDisallowInterceptTouchEvent(true);
                            tl.addView(ll);
//							topLayout.addView(ll);
                            Log.v("MAGATAMA", "---> web block");

                            // 上スクロールボタン
                            ((ViewGroup) view).requestDisallowInterceptTouchEvent(true);
                            Button upBtn = (Button) view.findViewById(R.id.web_up);
                            upBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.v("MAGATAMA", "---->up");
                                    //v.scrollTo(0, 300);
                                    v.scrollBy(0, -300);
                                }
                            });
                            // 下スクロールボタン
                            Button downBtn = (Button) view.findViewById(R.id.web_down);
                            downBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.v("MAGATAMA", "---->down");
                                    v.scrollBy(0, 300);
                                }
                            });

                        } catch (Exception e) {
                            Log.v("MAGATAMA", "TopFragment setListView (web) parse error: " + e);
                        }

                    } else if (block.equals("top")) {
                        try {
                            // 画像スライダー
                            JSONArray top = list.getJSONArray("top");

                            topImageList = new ArrayList();

                            if (top != null) {
                                int size = top.length();
                                JSONObject o;
                                for (int j = 0; j < size; j++) {
                                    o = top.getJSONObject(j);
                                    topImageItem = new TopImageItem();

                                    if (o.getString("file_name") != "" | o.getString("file_name") != null) {
                                        String fileName = o.getString("file_name");
                                        String s = gl.getUrlApiTopImage() + fileName;
                                        topImageList.add(gl.getUrlApiTopImage() + fileName);
                                        topImageItem.setFileName(gl.getUrlApiTopImage() + fileName);
                                    }

                                    topImageItem.setFileType(o.getString("file_type"));
                                    topImageItem.setYoutube(o.getString("youtube"));
                                    topImageItem.setLinkType(o.getString("link_type"));
                                    topImageItem.setLinkApp(o.getString("link_app"));
                                    topImageItem.setUrl(o.getString("url"));
                                    pagerAdapter.add(topImageItem);
                                }
                            }

                            imageSlider = mInflater.inflate(R.layout.image_slider, null);
                            LinearLayout ll = new LinearLayout(getActivity());
                            Resources res = getResources();
                            int height = (int) res.getDimension(R.dimen.imgge_slider_height);
                            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                            params.setMargins(5, 5, 5, margin);
                            ll.setLayoutParams(params);
                            ll.addView(imageSlider);
                            LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
                            tl.addView(ll);
//							topLayout.addView(ll);
                            setupViewPager();

                            RelativeLayout layout = (RelativeLayout) imageSlider.findViewById(R.id.topImage);
                            layout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            Log.v("MAGATAMA", "TopFragment setListView (top) parse error: " + e);
                        }

                    } else if (block.equals("topic")) {
                        try {
                            JSONObject topic = list.getJSONObject("topic");
                            if (topic != null) {

                                String message = topic.getString("message");
//                                MarqueeView marqueeView = new MarqueeView(getActivity());
//                                marqueeView.setText(message);
//                                marqueeView.setTextMoveSpeed(10);
//                                marqueeView.setRepeatLimit(1000);
//                                marqueeView.setTextColor(R.color.black);
//                                marqueeView.setBackgroundResource(R.color.transparent);
//                                LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//                                p.setMargins(0, 30, 0, 30);
//                                marqueeView.setLayoutParams(p);
//                                marqueeView.setTextSize(60);
//                                marqueeView.startMarquee();
//
//                                LinearLayout ll = new LinearLayout(getActivity());
//                                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//                                params.setMargins(5, 5, 5, margin);
//                                ll.setLayoutParams(params);
//                                ll.setBackgroundResource(R.color.block_bg);
//                                ll.addView(marqueeView);
//
//                                LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
//                                tl.addView(ll);
//								topLayout.addView(ll);
                                setupTopicsBlock(message);
                            }
                        } catch (Exception e) {
                            Log.v("MAGATAMA", "TopFragment setListView (topic) parse error: " + e);
                        }

                    } else if (block.equals("news")) {
                        JSONArray list_news_arr = list.getJSONArray("news");

                        if (list_news_arr != null) {
                            nItems = new ArrayList<NewsItem>();
                            int col = list_news_arr.length();
                            if (col > 0) {
                                JSONObject o;
                                for (int j = 0; j < col; j++) {
                                    o = list_news_arr.getJSONObject(j);
                                    NewsItem currentItem = new NewsItem();
                                    currentItem.setId(o.getString("id"));
                                    currentItem.setTitle(o.getString("title"));
                                    currentItem.setBody("body");
                                    currentItem.setDate(o.getString("send_at"));
                                    currentItem.setNoticeStatus(o.getString("notice_status"));
                                    currentItem.setNewFlg(o.getString("new"));
                                    newsIndexAdapter.add(currentItem);
                                    nItems.add(currentItem);
                                }
                                setupNewsBlock();
                            }
                        }


                    } else if (block.equals("menu")) {
                        JSONArray list_menu_arr = list.getJSONArray("menu");

                        if (list_menu_arr != null) {
                            mItems = new ArrayList<MenuItem>();
                            int col = list_menu_arr.length();
                            if (col > 0) {
                                JSONObject o;

                                for (int j = 0; j < col; j++) {
                                    o = list_menu_arr.getJSONObject(j);
                                    MenuItem currentItem = new MenuItem();
                                    currentItem.setId(o.getString("id"));
                                    currentItem.setParentId(o.getString("parent_id"));
                                    currentItem.setTitle(o.getString("title"));
                                    currentItem.setDescription(o.getString("sub_title"));
                                    String image = o.getString("file_name");
                                    if (image != null && image.length() > 0) {
                                        currentItem.setImage(gl.getUrlImgMenuItem() + image);
                                    }
                                    menuIndexAdapter.add(currentItem);
                                    mItems.add(currentItem);
                                }
                                menuIndexAdapter.notifyDataSetChanged();

                                setupMenuBlock();
                            }
                        }

                    } else if (block.equals("coupon")) {

                        JSONArray list_coupon_arr = list.getJSONArray("coupon");
                        if (list_coupon_arr != null) {
                            cItems = new ArrayList<CouponItem>();
                            int col = list_coupon_arr.length();
                            if (col > 0) {
                                try {
                                    JSONObject o;
                                    for (int j = 0; j < col; j++) {
                                        o = list_coupon_arr.getJSONObject(j);
                                        CouponItem currentItem = new CouponItem();
                                        currentItem.setId(o.getString("id"));
                                        currentItem.setTitle(o.getString("title"));
                                        currentItem.setDescription(o.getString("policy"));
                                        currentItem.setNewFlg(o.getString("new"));
                                        currentItem.setEndDatetime(o.getString("end_datetime"));
                                        couponIndexAdapter.add(currentItem);
                                        cItems.add(currentItem);
                                        couponIndexAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.v("MAGATAMA", "coupon Parse Error");
                                }
                                setupCouponBlock();
                            }
                        }

                    } else if (block.equals("tel")) {

                        JSONObject tel_obj = list.getJSONObject("tel");
                        if (tel_obj != null) {
                            tel = tel_obj.getString("tel");
                            mail = tel_obj.getString("email");
                        }

                        setupTelBlock();

                    } else if (block.equals("gps")) {

                        JSONObject map = list.getJSONObject("gps");
                        if (map != null) {
                            lat = map.getString("lat");
                            lng = map.getString("lng");

                            View v = mInflater.inflate(R.layout.map_block, null);
                            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(5, 5, 5, margin);
                            v.setLayoutParams(params);
                            LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
                            tl.addView(v);
//							topLayout.addView(v);

                            // マップ
                            Button mapBtn = (Button) v.findViewById(R.id.mapBtn);
                            mapBtn.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {

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
                            });

                            mapBtn.setVisibility(View.VISIBLE);

                        }

                    } else if (block.equals("sns")) {
                        JSONArray list_sns_arr = list.getJSONArray("sns");
                        int size = list_sns_arr.length();
                        if (list_sns_arr != null) {
                            LinearLayout sns = (LinearLayout) mInflater.inflate(R.layout.sns_block, null);
                            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(5, 5, 5, margin);
                            sns.setLayoutParams(params);
                            LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
                            tl.addView(sns);
//							topLayout.addView(sns);
                            JSONObject o;
                            for (int j = 0; j < size; j++) {
                                o = list_sns_arr.getJSONObject(j);
                                String sns_id = o.getString("sns_id");
                                if (sns_id.equals("1") && !o.getString("value").equals("")) {
                                    snsLink01 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.fbBtn);
                                    btn.setVisibility(View.VISIBLE);
                                } else if (sns_id.equals("2") && !o.getString("value").equals("")) {
                                    snsLink02 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.twBtn);
                                    btn.setVisibility(View.VISIBLE);
                                } else if (sns_id.equals("3") && !o.getString("value").equals("")) {
                                    snsLink03 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.gpBtn);
                                    btn.setVisibility(View.VISIBLE);
                                } else if (sns_id.equals("4") && !o.getString("value").equals("")) {
                                    snsLink04 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.ytBtn);
                                    btn.setVisibility(View.VISIBLE);
                                } else if (sns_id.equals("5") && !o.getString("value").equals("")) {
                                    snsLink05 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.amBtn);
                                    btn.setVisibility(View.VISIBLE);
                                } else if (sns_id.equals("6") && !o.getString("value").equals("")) {
                                    snsLink06 = o.getString("value");
                                    ImageButton btn = (ImageButton) sns.findViewById(R.id.lineBtn);
                                    btn.setVisibility(View.VISIBLE);
                                }

                                // FB
                                ImageButton fbBtn = (ImageButton) sns.findViewById(R.id.fbBtn);
                                fbBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink01);
//								        String appName = "facebook"; 								        
//								        sendApp(appName, uri);
                                        AppConstants.startUriLink(getActivity(), uri);

                                    }
                                });

                                // twitter
                                ImageButton twBtn = (ImageButton) sns.findViewById(R.id.twBtn);
                                twBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink02);
//								        String appName = "twitter"; 								        
//								        sendApp(appName, uri);
                                        AppConstants.startUriLink(getActivity(), uri);
                                    }
                                });
                                // google+
                                ImageButton gpBtn = (ImageButton) sns.findViewById(R.id.gpBtn);
                                gpBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink03);
//								        String appName = "google+"; 								        
//								        sendApp(appName, uri);
                                        AppConstants.startUriLink(getActivity(), uri);
                                    }
                                });
                                // youtube
                                ImageButton ytBtn = (ImageButton) sns.findViewById(R.id.ytBtn);
                                ytBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink04);
//								        String appName = "youtube"; 								        
//								        sendApp(appName, uri);
                                        AppConstants.startUriLink(getActivity(), uri);
                                    }
                                });
                                // アメーバ
                                ImageButton amBtn = (ImageButton) sns.findViewById(R.id.amBtn);
                                amBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink05);
//								        String appName = "ameba"; 								        
//								        sendApp(appName, uri);
                                        AppConstants.startUriLink(getActivity(), uri);
                                    }
                                });
                                // LINE
                                ImageButton lineBtn = (ImageButton) sns.findViewById(R.id.lineBtn);
                                lineBtn.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Uri uri = Uri.parse(snsLink06);
                                        String appName = "line";
                                        sendApp(appName, uri);
                                    }
                                });


                            }

                        }
                    }

                } catch (Exception e) {
                    Log.v("MAGATAMA", "TopFragment setListView parse error: " + e);
                }

            }

            // フッター用マージン
            LinearLayout ll = new LinearLayout(getActivity());
            Resources res = getResources();
            int footerMargin = (int) res.getDimension(R.dimen.footer_margin);
            LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, footerMargin);
            ll.setLayoutParams(p);
            LinearLayout rv = (LinearLayout) rootView.findViewById(R.id.topLayout);
            rv.addView(ll);

        }

        setupColorTheme();

        // スクロールバー一番上へ
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scrollView1);
        scrollView.fullScroll(ScrollView.FOCUS_UP);

        mIsLoading = false;
        progressDialog.dismiss();
    }


    /**
     * セットアップ : ウェブビュー
     */
//	@SuppressLint("SetJavaScriptEnabled")
//	public void setupWebView(View view, String url) {
//		// WebView
//		//WebView webView = (WebView)view.findViewById(R.id.webView1);
//		webView.setWebChromeClient(new WebChromeClient());
//		
//		// 縮小表示
//		webView.getSettings().setUseWideViewPort(true);
//		webView.getSettings().setLoadWithOverviewMode(true);
//		webView.setBackgroundColor(Color.TRANSPARENT);
//		
//		webView.setWebViewClient(new WebViewClient(){
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				return false;
//			}
//		});
//	
//		// WebView Setting
//		WebSettings webSetting = webView.getSettings();
//		webSetting.setJavaScriptEnabled(true);
//
//		webView.loadUrl(url);
//	}

//	private View getFooter() {
//		if (mViewFooter == null) {
//			// mViewFooter = getLayoutInflater().inflate(R.layout.view_footer,
//			// null);
//			mViewFooter = aq.id(R.id.footer).getView();
//		}
//		return mViewFooter;
//	}
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    private MarqueeView marqueeView;

    /**
     * マーキーの取得
     */
    private int getViewHeight(View view) {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        view.measure(metrics.widthPixels, metrics.heightPixels);
        int height = view.getMeasuredHeight();
        return height;
    }

    /**
     * ViewPager(画像スライダー)セットアップ
     */
    private void setupViewPager() {
        mViewPager = (ViewPager) imageSlider.findViewById(R.id.pager);
        ViewGroup.LayoutParams paramsPager = mViewPager.getLayoutParams();
        Point screenSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);
        paramsPager.width = screenSize.x;
        paramsPager.height = screenSize.x * 175 / 310;
        mViewPager.setLayoutParams(paramsPager);

        mIndicator = (ViewIndicator) imageSlider.findViewById(R.id.indicator);

        pagerAdapter.setupItemList(topImageList);
        mViewPager.setAdapter(pagerAdapter);

        mIndicator.setViewPager(mViewPager);
        mIndicator.setPosition(0);
    }

    @SuppressLint("WrongViewCast")
    public void getMarquee(View view) {

        //MarqueeView marqueeView = (MarqueeView)rootView.findViewById(R.id.marqueeTextView);
        MarqueeView marqueeView = (MarqueeView) view.findViewById(R.id.marqueeTextView);
        marqueeView.setText("マーキーさせる文字列を設定");
        marqueeView.setTextMoveSpeed(4);
        marqueeView.setRepeatLimit(1000);
        marqueeView.setTextSize(28);
        marqueeView.startMarquee();

    }


    private void setupTopicsBlock(String message) {
        View v = mInflater.inflate(R.layout.topic_block, null);
        TextView txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        txtMessage.setSelected(true);

        LinearLayout ll = new LinearLayout(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ll.addView(v);
        params.setMargins(5, 5, 5, margin);
        ll.setLayoutParams(params);
        LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
        tl.addView(ll);
    }


    private void setupNewsBlock() {
        View v = mInflater.inflate(R.layout.news_block, null);

        LinearLayout ll = new LinearLayout(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ll.addView(v);
        params.setMargins(5, 5, 5, margin);
        ll.setLayoutParams(params);
        LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
        tl.addView(ll);
//		topLayout.addView(ll);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListView newsList = (ListView) v.findViewById(R.id.newsListView);
        newsList.setAdapter(newsIndexAdapter);

        // リストの高さ調整
        View listView = newsIndexAdapter.getView(0, null, null);
        int height = getViewHeight(listView);
        ViewGroup.LayoutParams layoutParams = newsList.getLayoutParams();
        int col = newsIndexAdapter.getCount();
        if (col > newsListMaxSize) {
            col = newsListMaxSize;
        }
        layoutParams.height = height * col;
        newsList.setLayoutParams(layoutParams);

        // ニュースリスト項目クリック時
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsItem item = nItems.get(position);
                int itemId = Integer.parseInt(item.getId().toString());
                Log.v("MAGATAMA", "TopFragment newsItemClick: item=" + itemId + " pos=" + position);

                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, NewsFragment.newInstance(itemId), "NewsFragment");
                fragmentTransaction.commit();
            }
        });
        // もっと見る
        Button newsDetailBtn = (Button) v.findViewById(R.id.news_block_more_btn);
        newsDetailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, NewsFragment.newInstance(), "NewsFragment");
                fragmentTransaction.commit();
            }
        });
    }

    private void setupMenuBlock() {
        View v = mInflater.inflate(R.layout.menu_block, null);

        LinearLayout ll = new LinearLayout(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        ll.addView(v);
        params.setMargins(5, 5, 5, margin);
        ll.setLayoutParams(params);
        LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
        tl.addView(ll);
//		topLayout.addView(ll);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListView menuList = (ListView) v.findViewById(R.id.menuListView);
        menuList.setAdapter(menuIndexAdapter);

        // リストの高さ調整
        View listView = menuIndexAdapter.getView(0, null, null);
        int height = getViewHeight(listView);
        ViewGroup.LayoutParams layoutParams = menuList.getLayoutParams();
        int col = menuIndexAdapter.getCount();
        if (col > menuListMaxSize) {
            col = menuListMaxSize;
        }
        layoutParams.height = height * col;
        menuList.setLayoutParams(layoutParams);

        // リスト選択
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuDetailFragment fragment = new MenuDetailFragment();
                Bundle bundle = new Bundle();
                int menuId = Integer.parseInt(mItems.get(position).getId().toString());
                int parentId = Integer.parseInt(mItems.get(position).getParentId().toString());
                bundle.putInt("parent_id", parentId);
                bundle.putInt("menu_id", menuId);
                bundle.putInt("position", position);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        Button menuBtn = (Button) v.findViewById(R.id.menu_block_more_btn);
        menuBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, MenuFragment.newInstance(), "MenuFragment");
                fragmentTransaction.commit();
            }
        });

    }

    private void setupCouponBlock() {
        View v = mInflater.inflate(R.layout.coupon_block, null);

        LinearLayout ll = new LinearLayout(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, margin);
        ll.setLayoutParams(params);
        ll.addView(v);
        LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
        tl.addView(ll);
//		topLayout.addView(ll);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListView couponList = (ListView) v.findViewById(R.id.couponListView);
        couponList.setAdapter(couponIndexAdapter);

        // リストの高さ調整
        View listView = couponIndexAdapter.getView(0, null, null);
        int height = getViewHeight(listView);
        ViewGroup.LayoutParams layoutParams = couponList.getLayoutParams();
        int col = couponIndexAdapter.getCount();
        if (col > couponListMaxSize) {
            col = couponListMaxSize;
        }
        layoutParams.height = height * col;
        couponList.setLayoutParams(layoutParams);

        // リスト選択
        couponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("MAGATAMA", "TopFragment couponItemClick");
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, CouponFragment.newInstance(), "CouponFragment");
                fragmentTransaction.commit();
            }
        });

        //もっと見る
        Button couponBtn = (Button) v.findViewById(R.id.coupon_block_more_btn);
        couponBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, CouponFragment.newInstance(), "CouponFragment");
                fragmentTransaction.commit();
            }
        });

    }

    private void setupTelBlock() {
        LinearLayout v = (LinearLayout) mInflater.inflate(R.layout.tel_block, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, margin);
        v.setLayoutParams(params);
        LinearLayout tl = (LinearLayout) rootView.findViewById(R.id.topLayout);
        tl.addView(v);
//		topLayout.addView(v);

        if (tel.equals("")) {
            View layout = v.findViewById(R.id.leftLayout);
            layout.setVisibility(View.GONE);
        }
        if (mail.equals("")) {
            View layout = v.findViewById(R.id.rightLayout);
            layout.setVisibility(View.GONE);
        }

        // 電話発信
        Button telBtn = (Button) v.findViewById(R.id.telBtn);
        telBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
        // メーラー起動
        Button mailBtn = (Button) v.findViewById(R.id.mailBtn);
        mailBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("メーラーの起動");
                alertDialogBuilder.setMessage("メールアプリを起動しますか？");
                alertDialogBuilder.setPositiveButton("起動", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + mail));
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
        });

        LinearLayout layout = (LinearLayout) v.findViewById(R.id.telLayout);
        layout.setVisibility(View.VISIBLE);

        if (tel.equals("")) {
            telBtn.setVisibility(View.GONE);
        }

        if (mail.equals("")) {
            mailBtn.setVisibility(View.GONE);
        }

    }

    private void sendApp(String appName, Uri uri) {
        // タイムライン共有にする場合
        //Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.setType("text/plain");
        //shareIntent.putExtra(Intent.EXTRA_TEXT, uri);

        // 友達追加にする場合
        Intent shareIntent = new Intent(Intent.ACTION_VIEW, uri);

        PackageManager pm = getActivity().getPackageManager();
        List<?> activityList = pm.queryIntentActivities(shareIntent, 0);
        int len = activityList.size();
        boolean appFlg = false;
        for (int i = 0; i < len; i++) {
            ResolveInfo app = (ResolveInfo) activityList.get(i);
            if ((app.activityInfo.name.contains(appName))) {
                ActivityInfo activity = app.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                appFlg = true;
                break;
            }
        }

        // アプリがなければブラウザで開く
        if (!appFlg) {
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        }
    }

    private void setupCmsData(String url) {
        File dataDir = getActivity().getFilesDir();
        File directory = new File(dataDir.getAbsolutePath());

        if (directory.exists() == false) {
            if (directory.mkdir() == true) {
            } else {
                Toast.makeText(getActivity(), "ディレクトリの作成に失敗しました。", Toast.LENGTH_LONG).show();
            }
        }
        String fileName = "public_html.zip";
        File outputFile = new File(directory, fileName);
        this.asyncFileDownload = new AsyncFileDownload(getActivity(), url, outputFile) {
            @Override
            protected void onPostExecute(Boolean result) {
                checkPermissionBeforeSave();
            }
        };
        this.asyncFileDownload.execute();
    }

    private void checkPermissionBeforeSave() {
        if (PermissionUtils.requestPermission(getActivity(), REQUEST_READ_WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            unzipCmsData();
        }
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    private void unzipCmsData() {
        String zipName = "public_html.zip";
        File dataDir = getActivity().getFilesDir();
        File zipDir = new File(dataDir.getAbsolutePath() + "/" + zipName);
        ZipDecompresser zipDecompresser;
        zipDecompresser = new ZipDecompresser(zipDir);
        zipDecompresser.unzip();
        File fileName = new File(dataDir.getAbsolutePath() + "/" + this.DIRECTORY_NAME);
        String fileUrl = "file://" + fileName.toString() + "/index.html";
        cmsView.loadUrl(fileUrl);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                unzipCmsData();
            }
        }
    }

    private void cmsLinkSelected(String key) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.addToBackStack(null);
        if (key.equals("top")) {
            fragmentTransaction.replace(R.id.container, TopFragment.newInstance(), "TopFragment");
        } else if (key.equals("news")) {
            fragmentTransaction.replace(R.id.container, NewsFragment.newInstance(), "NewsFragment");
        } else if (key.equals("menu")) {
            fragmentTransaction.replace(R.id.container, MenuFragment.newInstance(), "MenuFragment");
        } else if (key.equals("coupon")) {
            fragmentTransaction.replace(R.id.container, CouponFragment.newInstance(), "CouponFragment");
        } else if (key.equals("shop_profile")) {
            fragmentTransaction.replace(R.id.container, ShopFragment.newInstance(), "ShopFragment");
        } else if (key.equals("shop_multi")) {
            fragmentTransaction.replace(R.id.container, ShopCateFragment.newInstance(), "ShopCateFragment");
        } else if (key.equals("setting")) {
            fragmentTransaction.replace(R.id.container, SettingFragment.newInstance(), "SettingFragment");
        } else if (key.equals("web")) {
            // カレンダー
//			String url = "";
//			String postData = "ipost-enterprise-client_id=" + gl.getClientID() + "&ipost-enterprise-user_id=" + gl.getPrefKeyUserID();
//			fragmentTransaction.replace(R.id.container, WebFragment.newInstance(url, postData));
        } else {
            return;
        }
        fragmentTransaction.commit();
    }

    private int convertDipToPixels(int dp) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float xdip = metrics.scaledDensity;
        return (int) ((float) dp * xdip);
    }

    private void addDailyStamp() {
        String url = gl.getUrlAddDailyStamp();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        aq.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("daily", "value :" + object);
                try {
                    JSONObject objectResult = new JSONObject(object);
                    int statusCode = JsonParserUtils.getIntValues(objectResult,
                            AppConstants.KEY_PARSER.STATUS, 0);
                    if (statusCode == AppConstants.STATUS_CODE.SUCCESS) {
                        // show popup to user.
                        JSONObject objectData = objectResult
                                .getJSONObject(AppConstants.KEY_PARSER.RESUTL);
                        String numberTicketAdd = JsonParserUtils
                                .getStringValues(objectData,
                                        AppConstants.KEY_PARSER.STAMP_QUANTITY);
                        String content = getString(
                                R.string.msg_add_stamp_daily, numberTicketAdd);
                        if (getActivity() != null) {
                            DialogShowShortContent dialog = new DialogShowShortContent(
                                    getActivity(), content, mColorButton);
                            dialog.show();
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private Fragment getReplaceFragment(String url) {
        if (url.endsWith("stamp")) {
            return FragmentMainStampCard.newInstances();
        }
        if (url.endsWith("top")) {
            return TopFragment.newInstance();
        }
        if (url.endsWith("menu")) {
            return MenuFragment.newInstance();
        }
        if (url.endsWith("coupon")) {
            return CouponFragment.newInstance();
        }
        if (url.endsWith("shopprofile")) {
            return ShopFragment.newInstance();
        }
        if (url.endsWith("shopmulti")) {
            return ShopCateFragment.newInstance();
        }
        if (url.endsWith("setting")) {
            return SettingFragment.newInstance();
        }
        if (url.endsWith("user")) {
            return FragmentStampCard.newInstances();
        }
        if (url.endsWith("news")) {
            return NewsFragment.newInstance();
        }
        return TopFragment.newInstance();
    }
}
