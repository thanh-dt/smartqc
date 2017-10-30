package app.com.hss.cooking.magatama.stampcard;

import java.util.ArrayList;

public class StampCardObject {
	private String mId;
	private String mNumberTicket;
	private String mStampType;
	private String mNumBerTicketForPromot;
	private String mUrlThumbPromot;
	private ArrayList<StampObject> mArrayStamp;
	
	public StampCardObject(){
		mStampType = "1";
		mNumberTicket = "0";
		mNumBerTicketForPromot = "0";
	}
	
	public String getId() {
		return mId;
	}
	public void setId(String mId) {
		this.mId = mId;
	}
	public String getNumberTicket() {
		return mNumberTicket;
	}
	public void setNumberTicket(String mNumberTicket) {
		this.mNumberTicket = mNumberTicket;
	}
	public String getStampType() {
		return mStampType;
	}
	public void setStampType(String mStampType) {
		this.mStampType = mStampType;
	}
	public String getNumBerTicketForPromot() {
		return mNumBerTicketForPromot;
	}
	public void setNumBerTicketForPromot(String mNumBerTicketForPromot) {
		this.mNumBerTicketForPromot = mNumBerTicketForPromot;
	}
	public String getUrlThumbPromot() {
		return mUrlThumbPromot;
	}
	public void setUrlThumbPromot(String mUrlThumbPromot) {
		this.mUrlThumbPromot = mUrlThumbPromot;
	}
	public ArrayList<StampObject> getArrayStamp() {
		return mArrayStamp;
	}
	public void setArrayStamp(ArrayList<StampObject> mArrayStamp) {
		this.mArrayStamp = mArrayStamp;
	}
	
}
