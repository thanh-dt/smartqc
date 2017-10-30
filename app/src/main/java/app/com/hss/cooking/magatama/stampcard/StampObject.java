package app.com.hss.cooking.magatama.stampcard;

public class StampObject {
	private String mId;
	private String mDate;
	private int mStyleStamp;

	public StampObject() {
		mDate = "";
		mStyleStamp = 0;
	}

	public String getId() {
		return mId;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String mDate) {
		this.mDate = mDate;
	}

	public boolean isShowStamp() {
		return getDate().equals("") ? false : true;
	}

	public int getStyleStamp() {
		return mStyleStamp;
	}

	public void setStyleStamp(int mStyleStamp) {
		this.mStyleStamp = mStyleStamp;
	}

}
