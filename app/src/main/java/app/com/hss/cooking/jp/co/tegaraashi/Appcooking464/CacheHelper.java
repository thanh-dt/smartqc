package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * キャッシュヘルバー
 *
 * LruCacheを利用してBitmapデータをデバイスキャッシュ(Memory)にて管理します
 */
public class CacheHelper {

	private LruCache<String, Bitmap> mLruCache;

	public CacheHelper(Context context) {
		// デバイスの利用可能最大値
		int deviceMaxSize = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// キャッシュ最大値
		final int cacheMaxSize = 1024 * 1024 * deviceMaxSize / 5;

		mLruCache = new LruCache<String, Bitmap>(cacheMaxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * キャッシュへデータ保存
	 * @param key
	 * @param bitmap
	 */
	public void put(String key, Bitmap bitmap) {
		if (mLruCache.get(key) == null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * キャッシュからデータ取得
	 * @param key
	 * @return
	 */
	public Bitmap get(String key) {
		return mLruCache.get(key);
	}

	/**
	 * キャッシュデータ削除（キー指定)
	 * @param key キャッシュキー値
	 */
	public void remove(String key) {
		mLruCache.remove(key);
	}

	/**
	 * キャッシュデータ全削除
	 */
	public void evictAll() {
		mLruCache.evictAll();
	}
}
