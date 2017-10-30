package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.api.ServerAPISidemenu;
import app.com.hss.cooking.magatama.top.TopFragment;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private NavigationDrawerFragment mNavi;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    private ArrayList<String> mMenuItem;
    private ArrayList<String> mMenuKeys;

    AQuery aq;
    Globals gl = new Globals();

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        mMenuItem = setupMenuItems();
        mMenuKeys = getMenuKeys();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position + 1;
                selectItem(itemPosition);
            }
        });

        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                //android.R.layout.simple_list_item_1,
                R.layout.drawer_row_layout,
                android.R.id.text1,
                mMenuItem) {
            @SuppressLint("ResourceAsColor")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // 偶数列に色を付ける
                if (position % 2 == 0) {
                    view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_alpha));
                }

                // アイコンの表示
                TextView iconText = (TextView) view.findViewById(R.id.drawer_icon_text);
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "font/ipost-icon01-regular.ttf");
                iconText.setTypeface(tf);
                String key = mMenuKeys.get(position);
                iconText.setText(getSideMenuIconString(key));

                return view;
            }
        });

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public ArrayList<String> setupMenuItems() {
        ServerAPISidemenu apiMenu = ServerAPISidemenu.getInstance(getActivity(), new Globals());
        String jsonString = apiMenu.getCurrentData();

        ArrayList<String> menuList = new ArrayList<String>();
        try {
            JSONArray jsonArr = new JSONArray(jsonString);
            //int len = jsonArr.length() + 1;
            int len = jsonArr.length();

            JSONObject obj;

            for (int i = 0; i < len; i++) {
                obj = jsonArr.getJSONObject(i);
                Log.v("MAGATAMA SideMenu: ", obj.getString("name"));

                menuList.add(obj.getString("name"));
            }
            return menuList;

        } catch (JSONException e) {
            Log.v("MAGATAMA", "setupMenuItems Error :" + e.getMessage());
        }

        return null;
    }

    public ArrayList<String> getMenuKeys() {
        ServerAPISidemenu apiMenu = ServerAPISidemenu.getInstance(getActivity(), new Globals());
        String jsonString = apiMenu.getCurrentData();

        ArrayList<String> list = new ArrayList<String>();
        try {
            JSONArray jsonArr = new JSONArray(jsonString);
            int len = jsonArr.length();

            JSONObject obj;

            for (int i = 0; i < len; i++) {
                obj = jsonArr.getJSONObject(i);
                list.add(obj.getString("key"));
            }
            return list;

        } catch (JSONException e) {
            Log.v("MAGATAMA", "getMenuKeys Error :" + e.getMessage());
        }

        return null;
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        // actionBar.setDisplayHomeAsUpEnabled(true); // タイトルタップ
        // actionBar.setHomeButtonEnabled(true); // ボタン表示

        actionBar.setDisplayShowTitleEnabled(false); // 標準のタイトル非表示
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME); // 標準のアイコンを非表示

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.custom_titlebar);
        actionBar.getCustomView().setPadding(0, 0, 0, 0);
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.height = convertDpToPixels(50, getActivity());
        parent.setLayoutParams(layoutParams);
//        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixels(50, getActivity())));
        parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        parent.setContentInsetsAbsolute(0, 0);


        mNavi = this;

        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.naviBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNavi.isDrawerOpen()) {
                    mDrawerLayout.closeDrawer(mFragmentContainerView);
                } else {
                    mDrawerLayout.openDrawer(mFragmentContainerView);
                }
            }
        });
        group.findViewById(R.id.titlebar_title_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
                fragmentTransaction.commit();
            }
        });
        group.findViewById(R.id.titlebar_icon_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, TopFragment.newInstance());
                fragmentTransaction.commit();
            }
        });
        //mDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes)
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {

                //getActivity().setTitle("Closed");  

                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {

            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
//        if (mDrawerLayout != null && isDrawerOpen()) {
//            inflater.inflate(R.menu.global, menu);
//            showGlobalContextActionBar();
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);

//		void onNavigationDrawerItemSelected(String position);
    }

    private String getSideMenuIconString(String key) {
        String iconStr = " ";
        if (key.equals("top")) {
            iconStr = "A";
        } else if (key.equals("news")) {
            iconStr = "L";
        } else if (key.equals("menu")) {
            iconStr = "G";
        } else if (key.equals("coupon")) {
            iconStr = "D";
        } else if (key.equals("shopprofile")) {
            iconStr = "I";
        } else if (key.equals("shopmulti")) {
            iconStr = "I";
        } else if (key.equals("setting")) {
            iconStr = "B";
        } else if (key.equals("mail")) {
            iconStr = "O";
        } else if (key.equals("tel")) {
            iconStr = "J";
        } else if (key.equals("map")) {
            iconStr = "F";
        } else if (key.equals("web")) {
            iconStr = "I";
        } else if (key.equals("user")) {
            iconStr = "N";
        } else if (key.equals("stamp")) {
            iconStr = "K";
        }

        return iconStr;
    }


}
