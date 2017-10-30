package app.com.hss.cooking.magatama.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.com.hss.cooking.R;

public class WebFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_URL = "section_url";
    private static final String ARG_POST_PARAM = "post_param";

    private WebView webView;
    private SessionSync sessionSync;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WebFragment newInstance(String url) {
        WebFragment fragment = new WebFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SECTION_URL, url);
        fragment.setArguments(args);

        return fragment;
    }

    public static WebFragment newInstance(String url, String postData) {
        WebFragment fragment = new WebFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SECTION_URL, url);
        args.putString(ARG_POST_PARAM, postData);
        fragment.setArguments(args);
        return fragment;
    }

    public WebFragment() {
    }

    @Override
    public void onAttach(Activity act) {
        super.onAttach(act);
    }

    @Override
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);

        String url = getArguments().getString(ARG_SECTION_URL);
        String postData = getArguments().getString(ARG_POST_PARAM);
        Log.v("MAGATAMA", "Drawer Selected : " + url);

        // WebView
        webView = (WebView) view.findViewById(R.id.webView1);
        webView.setWebChromeClient(new WebChromeClient());
        WebViewClient webClient = (new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Cookieを取得
                String cookieManager = CookieManager.getInstance().getCookie(url);
                if (cookieManager != null) {
                    String[] cookies = cookieManager.split(";");
                    for (String cookie : cookies) {
                        Log.d("MAGATAMA", cookie);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // mailtoでメーラ起動
                Intent intent = null;
                if (url.substring(0, 7).equals("mailto:")) {
                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                }
                if (intent != null) {
                    startActivity(intent);
                    webView.reload();
                }
                // 電話発信
                if (url.substring(0, 4).equals("tel:")) {
                    Uri uri = Uri.parse(url);
                    intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                }

                return true;
            }
        });
        webView.setWebViewClient(webClient);

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAppCacheEnabled(true);
        //		webSetting.setAppCacheMaxSize(8 * 1024 * 1024);
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (postData != null) {
            webView.postUrl(url, postData.getBytes());
        } else {
            webView.loadUrl(url);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
