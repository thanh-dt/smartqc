package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;
import app.com.hss.cooking.magatama.api.ServerAPISidemenu;
import app.com.hss.cooking.magatama.api.ServerAPITabMenu;
import app.com.hss.cooking.magatama.api.ServerAPITopBlock;
import app.com.hss.cooking.magatama.coupon.CouponFragment;
import app.com.hss.cooking.magatama.menu.MenuFragment;
import app.com.hss.cooking.magatama.news.NewsFragment;
import app.com.hss.cooking.magatama.setting.SettingFragment;
import app.com.hss.cooking.magatama.shop.ShopCateFragment;
import app.com.hss.cooking.magatama.shop.ShopFragment;
import app.com.hss.cooking.magatama.stampcard.FragmentHowtoGetStamp;
import app.com.hss.cooking.magatama.stampcard.FragmentMainStampCard;
import app.com.hss.cooking.magatama.stampcard.FragmentStampCard;
import app.com.hss.cooking.magatama.top.TopFragment;
import app.com.hss.cooking.magatama.webview.WebFragment;

public class ViewActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener {

    Globals gl;
    AQuery aq;

    private LocationManager locationManager;
    private int providerNum = 0;

    private boolean FinishFlag;

    private ArrayList<String> mMenuList;
    ArrayList<SideMenuItem> sideMenuItems;
    LinearLayout layoutMenuBottom;
    public static Point screenSize;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);

        if (screenSize == null) {
            screenSize = new Point();
            getWindowManager().getDefaultDisplay().getSize(screenSize);
        }
        // Window window = getWindow();
        // window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        sideMenuItems = getSideMenuItems();
        //mMenuList = setupMenuList();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        aq = new AQuery(this);
        gl = new Globals();
        gl.initPreference(this);
        layoutMenuBottom = (LinearLayout) findViewById(R.id.layout_menu_bottom);
        setupBottomMenu();
        // ヘッダーカラー設定
        setupHeaderColor();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        TopFragment fragment = TopFragment.newInstance();

        View view = (View) findViewById(R.id.container);
        view.setOnTouchListener((TopFragment) fragment);

        setupBackButton();

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();

        String gpsStatus = Secure.getString(this.getContentResolver(), Secure.LOCATION_PROVIDERS_ALLOWED);
        if (gpsStatus.matches(".*" + LocationManager.NETWORK_PROVIDER + ".*")) {
            providerNum++;
            // ネットワークから取得を開始する
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
        }

        if (gpsStatus.matches(".*" + LocationManager.GPS_PROVIDER + ".*")) {
            providerNum++;
            // GPSから取得を開始する
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
        }
    }

    /**
     * GPS情報の取得
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.v("MAGATAMA", "----------");
        Log.v("MAGATAMA", "Latitude" + String.valueOf(location.getLatitude()));
        Log.v("MAGATAMA", "Longitude" + String.valueOf(location.getLongitude()));
        Log.v("MAGATAMA", "Accuracy" + String.valueOf(location.getAccuracy()));
        Log.v("MAGATAMA", "Altitude" + String.valueOf(location.getAltitude()));
        Log.v("MAGATAMA", "Time" + String.valueOf(location.getTime()));
        Log.v("MAGATAMA", "Speed" + String.valueOf(location.getSpeed()));
        Log.v("MAGATAMA", "Bearing" + String.valueOf(location.getBearing()));
        Log.v("MAGATAMA", "----------");

        // 全てのプロバイダからの現在地を取得したら取得処理をやめる
        providerNum--;
        if (providerNum == 0) {
            locationManager.removeUpdates(this);
        }

        this.sendLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
    }

    /**
     * @param lat
     * @param lng
     */
    public void sendLocation(String lat, String lng) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", gl.getClientID());
        params.put("token", gl.getDeviceToken());
        params.put("device", "2");
        params.put("lat", lat);
        params.put("lng", lng);

        aq.ajax(gl.getUrlToken(), params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                // APIのレスポンスが文字列で返ってくる
                Log.v("MAGATAMA", result);
            }
        });
    }

    /**
     * バックキーを押した時の処理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);

        } else {
            if (this.backFragment()) {
                if (FinishFlag) {
                    finish();

                } else {
                    Toast.makeText(this, "もう一度押すと終了します。", Toast.LENGTH_SHORT).show();
                    FinishFlag = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FinishFlag = false;
                        }
                    }, 2000);
                }
            }
            return true;
        }
    }

    /**
     * バックスタックの状態を1つ前に戻す。
     */
    public boolean backFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Log.v("MAGATAMA", "TopFragment getBackStackEntryCount: " + fragmentManager.getBackStackEntryCount());

//		if (fragmentManager.findFragmentByTag("NewsFragment") != null) {
//			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
//			fragmentTransaction.commit();
//			return false;
//		}

        // FragmentManager#popBackStackでフラグメントのバックスタックの状態を一つ戻す
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();

//			if (fragmentManager.findFragmentByTag("BbsFragment") != null) {
//				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//				fragmentTransaction.replace(R.id.container, BbsFragment.newInstance());
//				fragmentTransaction.commit();
//			}

            return false;
        }

        return true;
    }

    /**
     * Drawerを選択した時の処理
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentTransaction.addToBackStack(null);

        if (position == 0) {
            // スプラッシュ画面遷移後用
            fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
            fragmentTransaction.commit();
        } else {
            // サイドメニューボタン押下時用
            int menuPosition = position - 1;
            //String action = mMenuList.get( position + 1 );
            SideMenuItem menuItem = sideMenuItems.get(menuPosition);
            sideMenuSelected(menuItem);
        }
    }

//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		ArrayList<TabMenuItem> tab = setupFooter();
//		int tablen = tab.size();
//		if(tablen > 0) {
//			super.onCreateOptionsMenu(menu);
//
//			int size = tab.size();
//			for (int i=0; i < size; i++){
//				final TabMenuItem t = tab.get(i);
//				final String key = t.getKey();
//				int keyNum = convertTabItemNum(key);
//				String fileName = t.getFileName();
//				String icon = "footer_icon_" + fileName.toLowerCase();
//				Resources res = getResources();
//				int res_id = res.getIdentifier(icon,"drawable", getPackageName());
//				MenuItem actionItem = menu.add(i, keyNum, Menu.NONE, key);
//				actionItem.setIcon(res_id);
//				actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//			}
//
//			return true;
//		}
//
//		return super.onCreateOptionsMenu(menu);
//	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);

        if (id == 0) {
            // top
            fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 1) {
            // news
            fragmentTransaction.replace(R.id.container, NewsFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 2) {
            // menu
            fragmentTransaction.replace(R.id.container, MenuFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 3) {
            // coupon
            fragmentTransaction.replace(R.id.container, CouponFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 4) {
            // setting
            fragmentTransaction.replace(R.id.container, SettingFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 5) {
            // tel
            phoneTo();
        } else if (id == 6) {
            // mail
            mailTo();
            return true;
        } else if (id == 7) {
            // web
            String url = "";
            fragmentTransaction.replace(R.id.container, WebFragment.newInstance(url));
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 8) {
            // map
            mapTo();
        } else if (id == 9) {
            // shop_info
            fragmentTransaction.replace(R.id.container, ShopFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == 10) {
            // shop_multi
            fragmentTransaction.replace(R.id.container, ShopCateFragment.newInstance());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showMessage(String msg) {
        Toast.makeText(
                this,
                msg, Toast.LENGTH_SHORT).show();
    }

//	public ArrayList<String> setupMenuList() {
//		ServerAPISidemenu apiMenu = ServerAPISidemenu.getInstance(this, new Globals());
//		String jsonString = apiMenu.getCurrentData();
//
//		ArrayList<String> menuList = new ArrayList<String>();
//		try {
//			JSONArray jsonArr = new JSONArray(jsonString);
//			int len = jsonArr.length() + 1;
//
//			JSONObject obj;
//
//			for (int i=0; i < len; i++) {
//				if (i == 0) {
//					menuList.add("");
//					continue;
//				}
//				obj = jsonArr.getJSONObject(i - 1);
//				menuList.add(obj.getString("key"));
//			}
//			return menuList;
//
//		} catch (JSONException e) {
//			Log.v("MAGATAMA", "setupMenuList Error :" + e.getMessage());
//		}
//
//		return null;
//	}

    public ArrayList<SideMenuItem> getSideMenuItems() {
        ServerAPISidemenu apiMenu = ServerAPISidemenu.getInstance(this, new Globals());
        String jsonString = apiMenu.getCurrentData();

        ArrayList<SideMenuItem> items = new ArrayList<SideMenuItem>();

        try {
            JSONArray jsonArr = new JSONArray(jsonString);
            int len = jsonArr.length();
            JSONObject obj;

            for (int i = 0; i < len; i++) {
                SideMenuItem item = new SideMenuItem();
                obj = jsonArr.getJSONObject(i);
                item.setKey(obj.getString("key"));
                item.setValue(obj.getString("value"));
                item.setName(obj.getString("name"));
                item.setFileName(obj.getString("file_name"));
                item.setPosition(obj.getString("position"));
                if (item.getKey().equals("tel")) {
                    item.setTel(obj.getString("tel"));
                }
                if (item.getKey().equals("mail")) {
                    item.setEmail(obj.getString("email"));
                }
                if (item.getKey().equals("map")) {
                    item.setGpsLat(obj.getString("lat"));
                    item.setGpsLng(obj.getString("lng"));
                }
                items.add(item);
            }

        } catch (JSONException e) {
            Log.v("MAGATAMA", "getSideMenuItems Error :" + e.getMessage());
        }

        return items;
    }

    public ArrayList<TabMenuItem> setupFooter() {
        ServerAPITabMenu apiTab = ServerAPITabMenu.getInstance(this, new Globals());
        String jsonString = apiTab.getCurrentData();

        ArrayList<TabMenuItem> items = new ArrayList<TabMenuItem>();

        try {
            JSONArray jsonArr = new JSONArray(jsonString);
            int len = jsonArr.length();
            JSONObject obj;

            for (int i = 0; i < len; i++) {
                TabMenuItem item = new TabMenuItem();
                obj = jsonArr.getJSONObject(i);
                item.setId(obj.getString("id"));
                item.setKey(obj.getString("key"));
                item.setFileName(obj.getString("file_name"));
                item.setLat(obj.getString("lat"));
                item.setLng(obj.getString("lng"));
                item.setLink(obj.getString("link"));
                items.add(item);
            }

        } catch (JSONException e) {
            Log.v("MAGATAMA", "setupFooter Error :" + e.getMessage());
        }

        return items;
    }

    private void setupBottomMenu() {
        ArrayList<TabMenuItem> tab = setupFooter();
        int tablen = tab.size();
        layoutMenuBottom.setVisibility(tablen > 0 ? View.VISIBLE : View.GONE);
        if (tablen > 0) {
            int size = tab.size();
            for (int i = 0; i < size; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_menu_bottom, null);
                ImageView iv = (ImageView) view.findViewById(R.id.item_menu);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenSize.x / size, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                final TabMenuItem t = tab.get(i);
                final String key = t.getKey();
                int keyNum = convertTabItemNum(key);

//				if (keyNum == 7) {
//					tabUrl = t.getValue();
//				}

                String fileName = t.getFileName();
                String icon = "footer_icon_" + fileName.toLowerCase();
                Resources res = getResources();
                int res_id = res.getIdentifier(icon, "drawable",
                        getPackageName());
                iv.setImageResource(res_id);
                view.setTag(keyNum);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(t.getLink())) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(t.getLink()));
                            startActivity(i);
                        } else {
                            int id = (int) v.getTag();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in,
                                    R.anim.abc_fade_out);

                            if (id == 0) {
                                // top or register
                                fragmentTransaction.replace(R.id.container,
                                        TopFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            } else if (id == 1) {
                                // news
                                fragmentTransaction.replace(R.id.container,
                                        NewsFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 2) {
                                // menu
                                fragmentTransaction.replace(R.id.container,
                                        MenuFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 3) {
                                // coupon
                                fragmentTransaction.replace(R.id.container,
                                        CouponFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 4) {
                                // setting
                                fragmentTransaction.replace(R.id.container,
                                        SettingFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 5) {
                                // tel
                                phoneTo();
                            } else if (id == 6) {
                                // mail
                                mailTo();
                            } else if (id == 7) {
                                // web
                                String url = "";
                                fragmentTransaction.replace(R.id.container,
                                        WebFragment.newInstance(url));
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 8) {
                                // map
                                mapTo();
                            } else if (id == 9) {
                                // shop_info
                                fragmentTransaction.replace(R.id.container,
                                        ShopFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 10) {
                                // shop_multi
                                fragmentTransaction.replace(R.id.container,
                                        ShopCateFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            } else if (id == 11) {
                                Fragment fragment = FragmentMainStampCard.newInstances();
                                fragmentTransaction.replace(R.id.container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
//                        else if (id == 15) {
//                            // system
//                            fragmentTransaction.replace(R.id.container,
//                                    WebFragment.newInstance(gl.getUrlApiSystem()));
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }
                        }
                    }
                });
                layoutMenuBottom.addView(view);
            }

        }
    }


    public void restoreActionBar() {
        Log.v("MAGATAMA", "restoreActionBar");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(mTitle);
    }

    private void setupBackButton() {
        Log.v("MAGATAMA", "setupBackButton");
//		FragmentManager fragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
        group.findViewById(R.id.titlebar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFragment();
            }
        });
//        if (fragmentManager.getBackStackEntryCount() > 1) {
//	        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);
//	        group.findViewById(R.id.titlebar_back).setOnClickListener( new View.OnClickListener() {
//	        	@Override
//	        	public void onClick(View v) {
//	    			backFragment();
//	        	}
//	        });
//		} else {
//			group.findViewById(R.id.titlebar_back).setVisibility(View.INVISIBLE);
//		}
    }

    /**
     * 戻るボタン: OnClickリスナ登録
     */
//	private void setupBackButton() {
//		Activity parent = this.getParent();
//		ImageButton button = (ImageButton)parent.findViewById(R.id.titlebar_back);
//		button.setOnClickListener(new OnClickListener(){
//			public void onClick(View v) {
//				Log.v("MAGATAMA", "-->Back");
//				//buttonBack_OnClickHandler(v);
//			}
//		});
//	}

    /**
     * 戻るボタン: OnClickイベントハンドラ
     *
     * @param
     */
//	private void buttonBack_OnClickHandler(View v) {
//		this.back();
//	}

//	public void back() {
//		if (mHistory.size() > 1) {
//
//			mHistory.remove(mHistory.size() - 1);
//
//			if (mHistory.size() == 1) {
//				this.setVisibilityBackButton(View.GONE);
//			} else {
//				this.setVisibilityBackButton(View.VISIBLE);
//			}
//			mContainer.removeAllViews();
//			mContainer.addView(mHistory.get(mHistory.size() - 1));
//		}
//	}

//	public void setVisibilityBackButton(int visibility) {
//		Activity parent = this.getParent();
//		ImageButton button = (ImageButton)parent.findViewById(R.id.titlebar_back);
//		button.setVisibility(visibility);
//
//		// 戻るボタンvisibilityをSharedPreferenceへ保存(※タブが変更された際に状態を復元する為)
//		this.getPreferenceHelper().put(gl.prefKeyImageButtonVisibility, visibility);
//	}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO 自動生成されたメソッド・スタブ

    }

    public void setupHeaderColor() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(this, new Globals());
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
                if (type.equals("1")) {
                    // 単一カラー
                    aq.id(R.id.header_bg).backgroundColor(Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{Color.parseColor(barGradColorStart), Color.parseColor(barGradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(Orientation.BOTTOM_TOP, colors);
                    aq.id(R.id.header_bg).image(bgDraw);
                }

                String fileName = "";
                try {
                    fileName = header.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = gl.getImgHeader() + fileName;
                    //aq.id(R.id.header_bg).progress(R.id.blank_image).image(image);
                    aq.id(R.id.titlebar_icon_btn).progress(R.id.blank_image).image(image);
                    // タイトルは表示しない
                    aq.id(R.id.titlebar_title).visibility(View.GONE);
                    aq.id(R.id.titlebar_title_btn).visibility(View.GONE);
                }

            }
        } catch (Exception e) {
            Log.v("MAGATAMA", "ViewActivity: setupColorTheme error");
        }

    }

    private int convertTabItemNum(String key) {
        int num = -1;
        if (key.equals("top")) num = 0;
        else if (key.equals("news")) num = 1;
        else if (key.equals("menu")) num = 2;
        else if (key.equals("coupon")) num = 3;
        else if (key.equals("setting")) num = 4;
        else if (key.equals("tel")) num = 5;
        else if (key.equals("mail")) num = 6;
        else if (key.equals("web")) num = 7;
        else if (key.equals("map")) num = 8;
        else if (key.equals("shopprofile")) num = 9;
        else if (key.equals("shopmulti")) num = 10;
        else if (key.equals("stamp")) num = 11;
        return num;
    }

    FragmentMainStampCard fragmentMainStampCard;

    private void sideMenuSelected(SideMenuItem menuItem) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentTransaction.addToBackStack(null);

        if (menuItem.getKey().equals("top")) {
            fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("news")) {
            fragmentTransaction.replace(R.id.container, NewsFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("menu")) {
            fragmentTransaction.replace(R.id.container, MenuFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("coupon")) {
            fragmentTransaction.replace(R.id.container, CouponFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("shopprofile")) {
            fragmentTransaction.replace(R.id.container, ShopFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("shopmulti")) {
            fragmentTransaction.replace(R.id.container, ShopCateFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("setting")) {
            fragmentTransaction.replace(R.id.container, SettingFragment.newInstance());
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("web")) {
            String url = menuItem.getValue();
            //gl.initPreference(this);
            String userId = gl.getPrefKeyUserID();
//			if(userId.equals("")){
//				Toast.makeText(this, "この機能はユーザー登録後に使用できます。", Toast.LENGTH_SHORT).show();
//			} else {
//				String postData = "ipost-enterprise-client_id=" + gl.getClientID() + "&ipost-enterprise-user_id=" + userId;
//				fragmentTransaction.replace(R.id.container, WebFragment.newInstance(url, postData));
//				fragmentTransaction.commit();
//			}
            // (2014/12/16) ユーザー登録しなくても使えるように変更
            String postData = "ipost-enterprise-client_id=" + gl.getClientID();
            fragmentTransaction.replace(R.id.container, WebFragment.newInstance(url, postData));
            fragmentTransaction.commit();

        } else if (menuItem.getKey().equals("mail")) {
            String mail = menuItem.getEmail();
            sideMenuMailTo(mail);
        } else if (menuItem.getKey().equals("tel")) {
            String tel = menuItem.getTel();
            sideMenuPhoneTo(tel);
        } else if (menuItem.getKey().equals("map")) {
            String lat = menuItem.getGpsLat();
            String lng = menuItem.getGpsLng();
            sideMenuMapTo(lat, lng);
        } else if (menuItem.getKey().equals("user")) {
            Fragment fragment = FragmentStampCard.newInstances();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        } else if (menuItem.getKey().equals("stamp")) {
            fragmentMainStampCard = FragmentMainStampCard.newInstances();
            fragmentTransaction.replace(R.id.container, fragmentMainStampCard);
            fragmentTransaction.commit();
        }
    }

    private void mailTo() {
        ServerAPITopBlock apiData = ServerAPITopBlock.getInstance(this, new Globals());
        final String mail = apiData.getMailAddr();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("メールアプリの起動");
        alertDialogBuilder.setMessage("メールアプリを起動しますか？");
        alertDialogBuilder.setPositiveButton("起動", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + mail));
                intent.putExtra(Intent.EXTRA_SUBJECT, "タイトル");
                intent.putExtra(Intent.EXTRA_TEXT, "本文");
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

    private void phoneTo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        ServerAPITopBlock apiData = ServerAPITopBlock.getInstance(this, new Globals());
        final String tel = apiData.getTel();
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

    private void mapTo() {
        ServerAPITopBlock apiData = ServerAPITopBlock.getInstance(this, new Globals());
        final String lat = apiData.getGpsLat();
        final String lng = apiData.getGpsLng();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

    private void sideMenuMailTo(final String mail) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("メールアプリの起動");
        alertDialogBuilder.setMessage("メールアプリを起動しますか？");
        alertDialogBuilder.setPositiveButton("起動",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + mail));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "タイトル");
                        intent.putExtra(Intent.EXTRA_TEXT, "本文");
                        startActivity(Intent.createChooser(intent, "select"));
                    }
                });
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sideMenuPhoneTo(final String tel) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("電話発信");
        alertDialogBuilder.setMessage("電話をかけますか？");
        alertDialogBuilder.setPositiveButton("発信",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("tel:" + tel);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sideMenuMapTo(final String lat, final String lng) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("地図アプリの起動");
        alertDialogBuilder.setMessage("地図アプリを起動しますか？");
        alertDialogBuilder.setPositiveButton("起動",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setClassName("com.google.android.apps.maps",
                                "com.google.android.maps.driveabout.app.NavigationActivity");
                        Uri uri = Uri.parse("google.navigation:///?ll=" + lat
                                + "," + lng + "&q=MAP");
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Go main stamp card.
    public void goMainStampCard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in,
                R.anim.abc_fade_out);
        fragmentTransaction.addToBackStack(null);
        FragmentMainStampCard fragmentMain = FragmentMainStampCard
                .newInstances();
        fragmentTransaction.replace(R.id.container, fragmentMain);
        fragmentTransaction.commit();
    }

    // Go how to get stamp card.
    public void goHowToGetStamp(boolean isShowPhone, boolean isShowShare,
                                int numberGift, int numberSns) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in,
                R.anim.abc_fade_out);
        fragmentTransaction.addToBackStack(null);
        FragmentHowtoGetStamp fragmentMain = FragmentHowtoGetStamp
                .newInstances(isShowPhone, isShowShare, numberGift, numberSns);
        fragmentTransaction.replace(R.id.container, fragmentMain);
        fragmentTransaction.commit();
    }

    // go main ipost.
    public void goMainIpost() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in,
                R.anim.abc_fade_out);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
        fragmentTransaction.commit();
    }
}
