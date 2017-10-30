package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpHelper {

	/**
	 * Httpリクエスト(String取得)
	 * @param url
	 * @param params
	 * @return
	 */
	public String get(String url, HashMap<String, String> params) {
		String result = "";

		String requestUrl = this.createGetUri(url, params);
		if (requestUrl.isEmpty()) return result;

		HttpGet request   = new HttpGet(requestUrl);
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = httpClient.execute(request);
			switch (response.getStatusLine().getStatusCode()) {

			// 200 OK
			case HttpStatus.SC_OK:
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
			// 404 NOT FOUND
			case HttpStatus.SC_NOT_FOUND:
				// TODO データ取得失敗時のロギング等
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return result;
	}

	/**
	 * Httpリクエスト(Bitmap取得)
	 * @param url
	 * @param params
	 * @return
	 */
	public Bitmap getBitmap(String url, HashMap<String, String> params) {
		Bitmap result = null;

		String requestUrl = this.createGetUri(url, params);
		if (requestUrl.isEmpty()) return result;

		HttpGet request   = new HttpGet(requestUrl);
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = httpClient.execute(request);
			switch (response.getStatusLine().getStatusCode()) {

			// 200 OK
			case HttpStatus.SC_OK:
		        HttpEntity entity = response.getEntity();
		        BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
		        InputStream instream = bufHttpEntity.getContent();
		        result = BitmapFactory.decodeStream(instream);

			// 404 NOT FOUND
			case HttpStatus.SC_NOT_FOUND:
				// TODO データ取得失敗時のロギング等
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * URI生成
	 *
	 * @param url
	 * @param params
	 *  [scheme:][//authority][path][?query][#fragment]
	 * @return
	 */
	public String createGetUri(String url, HashMap<String, String> params) {
		if (url == null || url.isEmpty()) return "";

		URL urlObj;
		Uri.Builder builder = new Uri.Builder();

		try {
			urlObj = new URL(url);
			builder.scheme("http");
			builder.encodedAuthority(urlObj.getAuthority());
			builder.path(urlObj.getPath());

			if (params != null) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					builder.appendQueryParameter(entry.getKey(), entry.getValue());
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}

		return builder.build().toString();
	}

}
