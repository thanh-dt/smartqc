package app.com.hss.cooking.magatama.stampcard;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.com.hss.cooking.R;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.Globals;
import app.com.hss.cooking.jp.co.tegaraashi.Appcooking464.ViewActivity;
import app.com.hss.cooking.magatama.api.ServerAPIColorTheme;


public class FragmentStampCard extends BaseFragment {
    private View mBtnGoMain;
    private TextView mTvNoOfUser, mTvName;
    private Globals mGlobals;
    private AQuery mAquery;
    private ImageView mImvButton;

    public FragmentStampCard() {

    }

    public static FragmentStampCard newInstances() {
        FragmentStampCard fragmentStampCard = new FragmentStampCard();
        return fragmentStampCard;
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_stamp_card;
    }

    @Override
    protected void initVariable() {
        mBtnGoMain = finViewById(R.id.rl_go_main);
        mTvNoOfUser = (TextView) finViewById(R.id.tv_no_user_content);
        mTvName = (TextView) finViewById(R.id.tv_button_go_main);
        // mImvButton = (ImageView) finViewById(R.id.imv_button);
        // change color.

    }

    @Override
    protected void initControls() {
        Globals globals = getGlobals();
        mGlobals = getGlobals();
        mAquery = getAquery();
        HashMap<String, Object> params = new HashMap<>();
        params.put("client_id", globals.getClientID());
        params.put("token", globals.getDeviceToken());
        mAquery.ajax(globals.getUrlConfigRegister(), params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.e("Response", object.toString());
                try {
                    JSONObject response = new JSONObject(object);
                    JSONObject profile = response.getJSONObject("result").getJSONObject("profile");
                    String id = profile.optString("membership_id");
                    String name = profile.optString("fullname");
                    mTvNoOfUser.setText(id);
                    mTvName.setText(name);
                    if (mTvName.getLineCount() == 2) {
                        mTvName.setTextSize(10);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        setupColorTheme();
    }

    /**
     * Dialog error register.
     *
     * @param resId
     */
    private void showDialogError(String resId) {
        DialogWarningStamp dialog = new DialogWarningStamp(getActivity(), resId);
        dialog.setListener(new DialogWarningStamp.onChoiceDialog() {

            @Override
            public void onOk() {
                if (SocialUtils.isConnected(getActivity())) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStack();
                    ((ViewActivity) getActivity()).goMainStampCard();
                } else {
                    showDialogError(getString(R.string.error_lost_connect));
                }
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show();
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
                Log.e("sub color", "values:" + subColor);
                int colorButton = Color.parseColor(subColor);
                // mImvButton.setColorFilter(colorButton);

                // get drawable.
                Drawable mDrawable = getActivity().getResources().getDrawable(
                        R.drawable.ic_stamp_card_go_main_normal);
                mDrawable.setColorFilter(Color.parseColor(subColor),
                        Mode.SRC_ATOP);
                mBtnGoMain.setBackgroundDrawable(mDrawable);
                // end
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
                    mAquery.id(R.id.setting_bg).backgroundColor(
                            Color.parseColor(color));
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
                    mAquery.id(R.id.setting_bg).progress(R.id.blank_image)
                            .image(image);
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
