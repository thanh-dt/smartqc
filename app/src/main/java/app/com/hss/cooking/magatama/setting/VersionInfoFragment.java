package app.com.hss.cooking.magatama.setting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import org.json.JSONObject;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;

public class VersionInfoFragment extends Fragment {

    private Globals gl;
    private AQuery aq;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static VersionInfoFragment newInstance() {
        VersionInfoFragment fragment = new VersionInfoFragment();
        return fragment;
    }

    public VersionInfoFragment() {

    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);

        gl = new Globals();
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.version_view, container, false);
        aq = new AQuery(view);

        setupColorTheme();

        String version = "";
        try {
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getActivity().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        aq.id(R.id.versionText).text(version);

        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

        return view;
    }

    public void setupColorTheme() {
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
                if (type.equals("1")) {
                    // 単一カラー
                    aq.id(R.id.setting_bg).backgroundColor(Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{Color.parseColor(gradColorStart), Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
                    aq.id(R.id.setting_bg).image(bgDraw);
                }
                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = gl.getImgTopBackground() + fileName;
                    aq.id(R.id.setting_bg).progress(R.id.blank_image).image(image);
                }

                //aq.id(R.id.versionDetailLayout).backgroundColor(Color.parseColor(subColor));
            }

        } catch (Exception e) {
            Log.v("MAGATAMA", "MenuDetailFragment: setupColorTheme error");
        }
    }


}
