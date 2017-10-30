package app.com.hss.cooking.magatama.webview;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
 
/**
 * HttpClient, WebView間でセッションを同期するためのクラス
 */
public class SessionSync {
 
    // COOKIEの送出するドメインを指定します
    private static final String YOUR_DOMAIN = "ucj.sakura.ne.jp/magatama/";
 
    // COOKIE取得用のドメイン(.をつけてサブドメインもカバーします)
    private static final String COOKIE_DOMAIN = "." + YOUR_DOMAIN;
    // COOKIEを送出するURL
    private static final String COOKIE_URL = "http://" + YOUR_DOMAIN;
 
    // セッションIDを格納するパラメータ名を指定します
    // これはPHPの例
    private static final String SESSID = "PHPSESSID";
 
    /**
     * HttpClient側のセッションIDをWebViewに同期します
     *
     * @param httpClient セッションを同期するHttpClient
     */
    public static void httpClient2WebView(DefaultHttpClient httpClient) {
        CookieStore store = httpClient.getCookieStore();
        List<Cookie> cookies = store.getCookies();
        CookieManager cookieManager = CookieManager.getInstance();
        for (Cookie cookie : cookies) {
            if (cookie.getDomain().indexOf(COOKIE_DOMAIN) < 0) {
                continue;
            }
            if (!SESSID.equals(cookie.getName())) {
                continue;
            }
            // ここで削除するとsyncしたタイミングでsetしたcookieも
            // 消える場合があるので削除は見合わせ
            // cookieManager.removeSessionCookie();
            String cookieStr = cookie.getName() + "=" + cookie.getValue();
            cookieManager.setCookie(COOKIE_DOMAIN, cookieStr);
            CookieSyncManager.getInstance().sync();
            
            Log.v("MAGATAMA", "Cookie String : " + cookieStr);
        }
    }
 
    /**
     * WebView側のセッションIDをHttpClientに同期します
     *
     * @param httpClient セッションを同期するHttpClient
     */
    public static void webView2HttpClient(DefaultHttpClient httpClient) {
 
        String cookie = CookieManager.getInstance().getCookie(COOKIE_URL);
        String[] cookies = cookie.split(";");
        for (String keyValue : cookies) {
            keyValue = keyValue.trim();
            String[] cookieSet = keyValue.split("=");
            String key = cookieSet[0];
            String value = cookieSet[1];
            if (!SESSID.equals(key)) {
                continue;
            }
            BasicClientCookie bCookie = new BasicClientCookie(key, value);
            bCookie.setDomain(COOKIE_DOMAIN);
            bCookie.setPath("/");
            CookieStore store = httpClient.getCookieStore();
            store.addCookie(bCookie);
        }
    }

}
