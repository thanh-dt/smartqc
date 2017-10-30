package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;



public class SideMenuItem {

	private String mId;
	private String mClientId;
	private String mKey;
	private String mValue;
	private String mName;
	private String mFileName;
	private String mTmpFileName;
	private String mPosition;
	private String mTel;
	private String mEmail;
	private String mLat;
	private String mLng;
	private String mCreatedAt;
	private String mUpdatedAt;

	/**
	 * セッター
	 * 
	 * @param
	 */
	public void setId(String id) {
		this.mId = id;
	}

	public void setClientId(String clientId) {
		this.mClientId = clientId;
	}

	public void setKey(String key) {
		this.mKey = key;
	}

	public void setValue(String value) {
		this.mValue = value;
	}

	public void setName(String name) {
		this.mName = name;
	}
	
	public void setFileName(String fileName) {
		this.mFileName = fileName;
	}

	public void setTmpFileName(String TmpfileName) {
		this.mTmpFileName = TmpfileName;
	}
	
	public void setPosition(String position) {
		this.mPosition = position;
	}
	
	public void setTel(String tel) {
		this.mTel = tel;
	}
	
	public void setEmail(String email) {
		this.mEmail = email;
	}
	
	public void setGpsLat(String lat) {
		this.mLat = lat;
	}
	
	public void setGpsLng(String lng) {
		this.mLng = lng;
	}
	
	public void setCreatedAt(String date) {
		this.mCreatedAt = date;
	}
	
	public void setUpdatedAt(String date) {
		this.mUpdatedAt = date;
	}

	/**
	 * ゲッター
	 * 
	 * @return
	 */
	public String getId() {
		return this.mId;
	}

	public String getClientId() {
		return this.mClientId;
	}

	public String getKey() {
		return this.mKey;
	}
	
	public String getName() {
		return this.mName;
	}

	public String getFileName() {
		return this.mFileName;
	}

	public String getValue() {
		return this.mValue;
	}

	public String getTmpFileName() {
		return this.mTmpFileName;
	}

	public String getPosition() {
		return this.mPosition;
	}
	
	public String getTel() {
		return this.mTel;
	}
	
	public String getEmail() {
		return this.mEmail;
	}
	
	public String getGpsLat() {
		return this.mLat;
	}
	
	public String getGpsLng() {
		return this.mLng;
	}
	
	public String getCreatedAt() {
		return this.mCreatedAt;
	}

	public String getUpdateddAt() {
		return this.mUpdatedAt;
	}
}
