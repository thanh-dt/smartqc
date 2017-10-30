package app.com.hss.cooking.magatama.top;



public class BlockItem {

	private String mMainte;
	private String mId;
	private String mClientId;
	private String mStatus;
	private String mExpredat;
	private String mUpdatedat;
	
	private String mNews;

	/**
	 * セッター
	 * 
	 * @param
	 */
	public void setMainte(String mainte) {
		this.mMainte = mainte;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public void setClientId(String clientid) {
		this.mClientId = clientid;
	}

	public void setStatus(String status) {
		this.mStatus = status;
	}

	public void setExpredat(String expredat) {
		this.mExpredat = expredat;
	}

	public void setUpdatedat(String updatedat) {
		this.mUpdatedat = updatedat;
	}


	/**
	 * ゲッター
	 * 
	 * @return
	 */
	public String getMainte() {
		return this.mMainte;
	}

	public String getId() {
		return this.mId;
	}

	public String getClientId() {
		return this.mClientId;
	}

	public String getStatus() {
		return this.mStatus;
	}

	public String getExpredat() {
		return this.mExpredat;
	}

	public String getUpdatedat() {
		return this.mUpdatedat;
	}


	/**
	 * 権限情報の確認
	 * 
	 * @return boolean
	 */
	public boolean checkStatus(){
		if(this.getStatus() != null &&  this.getStatus() != ""){
			final int status = Integer.parseInt(this.getStatus()); 

			if(status < 600){
				return true;
			}
		}
		return false;
	}


	/**
	 * メンテナンス状況の確認
	 * 
	 * @return boolean
	 */
	public boolean checkMainte(){
		if(this.getMainte() != "null" && this.getMainte() != ""){
			return true;
		}
		return false;
	}
}
