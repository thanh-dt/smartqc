package app.com.hss.cooking.magatama.stampcard;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;


public class FragmentHowtoGetStamp extends BaseFragment {
    private View mPhone, mShare;
    private boolean mIsShowPhone;
    private boolean mIsShowShare;
    private View mViewLineOne, mViewLineTwo;
    private int mNumberGift, mNumberSns;
    private TextViewW3 mTvGift, mTvSns, mTvNumberShare;
    private Globals mGlobals;
    private AQuery mAquery;
    private ImageView ivBackground;

    public FragmentHowtoGetStamp() {

    }

    public static FragmentHowtoGetStamp newInstances(boolean isShowPhone,
                                                     boolean isShowShare, int numberGift, int numberSns) {
        FragmentHowtoGetStamp fragment = new FragmentHowtoGetStamp();
        fragment.mIsShowPhone = isShowPhone;
        fragment.mIsShowShare = isShowShare;
        fragment.mNumberGift = numberGift;
        fragment.mNumberSns = numberSns;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.dialog_how_to_use;
    }

    @Override
    protected void initVariable() {
        mPhone = finViewById(R.id.rl_content_two);
        mShare = finViewById(R.id.rl_content_three);
        mViewLineOne = finViewById(R.id.line_one);
        mViewLineTwo = finViewById(R.id.line_two);
        mTvGift = (TextViewW3) finViewById(R.id.retult_two);
        mTvSns = (TextViewW3) finViewById(R.id.retult_three);
        mTvNumberShare = (TextViewW3) finViewById(R.id.tv_number_three);
        ivBackground = (ImageView) finViewById(R.id.setting_bg);
    }

    @Override
    protected void initControls() {
        setControlsView(mIsShowPhone, mIsShowShare);
        mTvSns.setText("x " + mNumberSns);
        mTvGift.setText("x " + mNumberGift);
        mGlobals = getGlobals();
        mAquery = getAquery();
        // show back button on action bar.
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ViewGroup group = (ViewGroup) actionBar.getCustomView();
        group.findViewById(R.id.titlebar_back).setVisibility(View.VISIBLE);

        setupColorTheme();
    }

    public void setControlsView(boolean isShowPhone, boolean isShowShare) {
        if (isShowPhone) {
            mPhone.setVisibility(View.VISIBLE);
            mViewLineOne.setVisibility(View.VISIBLE);
        } else {
            mViewLineOne.setVisibility(View.GONE);
            mPhone.setVisibility(View.GONE);
            mTvNumberShare.setText(getActivity().getString(R.string.txt_2));
        }

        if (isShowShare) {
            mViewLineTwo.setVisibility(View.VISIBLE);
            mShare.setVisibility(View.VISIBLE);
        } else {
            mViewLineTwo.setVisibility(View.GONE);
            mShare.setVisibility(View.GONE);
        }
    }

    private void setupColorTheme() {
        ServerAPIColorTheme api = ServerAPIColorTheme.getInstance(
                getActivity(), mGlobals);
        String jsonStr = api.getCurrentData();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject bg = jsonObj.getJSONObject("background");
            if (bg != null) {
                String color = "#" + bg.getString("color");
                String subColor = "#" + bg.getString("sub_color");
                // String fileName = "";
                // fileName = bg.getString("file_name");
                String type = bg.getString("type");
                String gradColorStart = "#" + bg.getString("gradation1");
                String gradColorEnd = "#" + bg.getString("gradation2");
                // String barGradColorStart = "#" +
                // bg.getString("bar_gradation1");
                // String barGradColorEnd = "#" +
                // bg.getString("bar_gradation2");
                // String subGradColor = "#" + bg.getString("sub_gradation");

                // 背景設定
                if (type.equals("1")) {
                    // 単一カラー
                    ivBackground.setBackgroundColor(Color.parseColor(color));
                } else if (type.equals("2")) {
                    // グラデーション設定
                    int[] colors = new int[]{
                            Color.parseColor(gradColorStart),
                            Color.parseColor(gradColorEnd)};
                    GradientDrawable bgDraw = new GradientDrawable(
                            Orientation.TOP_BOTTOM, colors);
                    mAquery.id(R.id.setting_bg).image(bgDraw);
                }
                String fileName = "";
                try {
                    fileName = bg.getString("file_name");
                } catch (Exception e) {

                }

                // 画像が設定されているとき
                if (fileName.length() > 0) {
                    String image = mGlobals.getImgTopBackground() + fileName;
                    Glide.with(getActivity()).load(image).into(ivBackground);
                }

                // aq.id(R.id.notifyLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.userInfoLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.companyInfoLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.setting_versionLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.setting_inqueryLayout).backgroundColor(Color.parseColor(subColor));
                // aq.id(R.id.settings_version_detail_btn).backgroundColor(Color.parseColor(subColor));
            }

        } catch (Exception e) {
            Log.v("MAGATAMA", "SettingFragment: setupColorTheme error");
        }
    }

}
