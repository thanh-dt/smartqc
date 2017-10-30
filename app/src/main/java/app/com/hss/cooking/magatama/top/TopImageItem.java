package app.com.hss.cooking.magatama.top;

public class TopImageItem {
	private CharSequence mId;
	private CharSequence mTitle;
	private CharSequence mBody;
	private CharSequence mYoutube;
	private CharSequence mFileName;
	private CharSequence mTmpFileName;
	private CharSequence mNotice;
    private CharSequence mNoticeStatus;
    private CharSequence mIineCount;
    private CharSequence mCommentCount;
    private CharSequence mSendAt;
	private CharSequence mUpdatedAt;
	private CharSequence mCreatedAt;
	private CharSequence mNewFlg;
	private CharSequence mIineFlg;
	
	private CharSequence mDescription;
	private CharSequence mDate;
	private CharSequence mImage;
	private CharSequence mType;
	private CharSequence mFileType;
	private CharSequence mLinkType;
	private CharSequence mLinkApp;
	private CharSequence mUrl;
	
	public TopImageItem() {
		setId("");
		mTitle = "";
		mBody = "";
		mYoutube = "";

		mNotice = "";
		mNoticeStatus = "";
		mIineCount = "";
		mCommentCount = "";
		mSendAt = "";
		mUpdatedAt = "";
		mCreatedAt = "";
		mNewFlg = "";
		mIineFlg = "";

		mDescription = "";
		mDate = "";
		setImage("");
		mType = "0";
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
	}
	
	public void setBody(CharSequence body) {
		mBody = body;
	}
	
	public void setYoutube(CharSequence youtube) {
		mYoutube = youtube;
	}

	public void setDescription(CharSequence description) {
		mDescription = description;
	}

	public void setDate(CharSequence date) {
		mDate = date;
	}
	
    public void setFileName(CharSequence fileName) {
        mFileName = fileName;
    }

    public void setTmpFileName(CharSequence tmpFileName) {
        mTmpFileName = tmpFileName;
    }
    
	public void setNotice(CharSequence notice) {
		mNotice = notice;
	}
	
	public void setNoticeStatus(CharSequence noticeStatus) {
		mNoticeStatus = noticeStatus;
	}

    public void setUpdatedAt(CharSequence updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public void setCreatededAt(CharSequence createdAt) {
        mCreatedAt = createdAt;
    }
	
	
	
	public CharSequence getTitle() {
		return mTitle;
	}
	
	public CharSequence getBody() {
		return mBody;
	}

	public CharSequence getYoutube() {
		return mYoutube;
	}

	public CharSequence getFileName() {
		return mFileName;
	}
	
	public CharSequence getTmpFileName() {
		return mTmpFileName;
	}
	
	public CharSequence getNotice() {
		return mNotice;
	}
	
	public CharSequence getIineCount() {
		return mIineCount;
	}
	
	public CharSequence getCommentCount() {
		return mCommentCount;
	}
	
	public CharSequence getSendAt() {
		return mSendAt;
	}
	
	public CharSequence getNewFlg() {
		return mNewFlg;
	}
	
	public CharSequence getIineFlg() {
		return mIineFlg;
	}
	
	public CharSequence getDescription() {
		return mDescription;
	}

	public CharSequence getDate() {
		//Long unix = Long.parseLong(mDate.toString()) * 1000;
		//String date = (String) DateFormat.format("yyyy年MM月dd日 kk:mm", unix);
		//return date;
		return mDate;
	}

	public CharSequence getImage() {
		return mImage;
	}

	public void setImage(CharSequence image) {
		mImage = image;
	}

	public CharSequence getId() {
		return mId;
	}
	
	public void setType(CharSequence type) {
		mType = type;
	}
	
	public CharSequence getType() {
		return mType;
	}

	public void setId(CharSequence id) {
		mId = id;
	}
	
	public void setIineCount(CharSequence iineCount) {
		mIineCount = iineCount;
	}
	
	public CharSequence getNoticeStatus(CharSequence noticeStatus) {
        return noticeStatus;
    }
	
	public void setNewFlg(CharSequence newFlg) {
		mNewFlg = newFlg;
	}

	public void setLinkType(CharSequence linkType) {
		mLinkType = linkType;
	}

	public void setLinkApp(CharSequence linkApp) {
		mLinkApp = linkApp;
	}
	
	public void setFileType(CharSequence fileType) {
		mFileType = fileType;
	}
	
	public void setUrl(CharSequence url) {
		mUrl = url;
	}

	public CharSequence getLinkApp() {
		return mLinkApp;
	}
	
	public CharSequence getFileType() {
		return mFileType;
	}
	
	public CharSequence getLinkType() {
		return mLinkType;
	}
	public CharSequence getUrl() {
		return mUrl;
	}

	
}