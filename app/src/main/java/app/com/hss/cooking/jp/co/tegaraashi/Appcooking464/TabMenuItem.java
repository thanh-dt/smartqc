package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;



public class TabMenuItem {

	private String mId;	
	private String mKey;
	private String mFileName;
	private String mLng;
	private String mLat;
	private String mLink;

	/**
	 * セッター
	 * 
	 * @param
	 */
	public void setFileName(String fileName) {
		this.mFileName = fileName;
	}

	public void setKey(String key) {
		this.mKey = key;
	}

	public void setId(String id) {
		this.mId = id;
	}
	
	public void setLat(String lat) {
		this.mLat = lat;
	}
	
	public void setLng(String lng) {
		this.mLng = lng;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String mLink) {
		this.mLink = mLink;
	}

	/**
	 * ゲッター
	 * 
	 * @return
	 */
	public String getId() {
		return this.mId;
	}

	public String getFileName() {
		return this.mFileName;
	}

	public String getKey() {
		return this.mKey;
	}

	public String getLat() {
		return this.mLat;
	}

	public String getLng() {
		return this.mLng;
	}

}
