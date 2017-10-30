package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class SocialUtils {
	/**
	 * ネットワーク接続チェック
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null) {
			return false;
		} else {
			return cm.getActiveNetworkInfo().isConnected();
		}
	}
}
