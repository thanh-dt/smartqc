package app.com.hss.cooking.magatama.shop;

import android.text.format.DateFormat;
import android.view.View;

public class ShopItem {
	private CharSequence mId;
	private CharSequence mTitle;
	private CharSequence mSubTitle;
	private CharSequence mShopName;
	private CharSequence mDescription;
	private CharSequence mDate;
	private CharSequence mImage;
	private CharSequence mFileName;
	private CharSequence mTmpFileName;
	private CharSequence mStartDatetime;
	private CharSequence mEndDatetime;
	private CharSequence mPosition;
	private CharSequence mEnable;
	private CharSequence mUpdatedAt;
	private CharSequence mCreatedAt;
	private CharSequence mNewFlg;
	private View mView;
	private boolean mIndexFlg;

	public ShopItem() {
		setId("");
		mTitle = "";
		mSubTitle = "";
		mDescription = "";
		mShopName = "";
		mDate = "";
		mImage  = "";
		mStartDatetime = "";
		mEndDatetime = "";
		mFileName = "";
		mTmpFileName = "";
		mPosition = "";
		mUpdatedAt = "";
		mCreatedAt = "";
		mIndexFlg = false;
		mNewFlg = "";
		mView = null;
	}

	public void setId(CharSequence id) {
		mId = id;
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
	}

	public void setSubTitle(CharSequence sub_title) {
		mSubTitle = sub_title;
	}

	public void setShopName(CharSequence shopName) {
		mShopName = shopName;
	}

	public void setDescription(CharSequence description) {
		mDescription = description;
	}

	public void setDate(CharSequence date) {
		mDate = date;
	}

	public void setImage(CharSequence image) {
		mImage = image;
	}

	public void setView(View view) {
		mView = view;
	}
    public void setStartDatetime(CharSequence startDatetime) {
        mStartDatetime = startDatetime;
    }
    
    public void setEndDatetime(CharSequence endDatetime) {
        mEndDatetime = endDatetime;
    }
    

    public void setFileName(CharSequence fileName) {
        mFileName = fileName;
    }

    public void setTmpFileName(CharSequence tmpFileName) {
        mTmpFileName = tmpFileName;
    }
    
    public void setPosition(CharSequence position) {
        mPosition = position;
    }
    
    public void setEnable(CharSequence enable) {
        mEnable = enable;
    }
    
    public void setUpdatedAt(CharSequence updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public void setCreatededAt(CharSequence createdAt) {
        mCreatedAt = createdAt;
    }

    public void setIndexFlg(boolean indexFlg) {
        mIndexFlg = indexFlg;
    }
    
    public void setNewFlg(CharSequence newFlg) {
        mNewFlg = newFlg;
    }

	public CharSequence getId() {
		return mId;
	}

	public CharSequence getTitle() {
		return mTitle;
	}

	public CharSequence getSubTitle() {
		return mSubTitle;
	}

	public CharSequence getShopName() {
		return mShopName;
	}

	public CharSequence getDescription() {
		return mDescription;
	}

	public CharSequence getDate() {
		Long unix = Long.parseLong(mDate.toString()) * 1000;
		String date = (String) DateFormat.format("yyyy年MM月dd日 kk:mm", unix);
		return date;
	}

	public CharSequence getImage() {
		return mImage;
	}

	public CharSequence getStartDatetime() {
		return mStartDatetime;
	}
	
	public CharSequence getEndDatetime() {
		return mEndDatetime;
	}
	

    public CharSequence getFileName() {
        return mFileName;
    }

    public CharSequence getTmpFileName() {
        return mTmpFileName;
    }
    
    public CharSequence getPosition() {
        return mPosition;
    }
    
    public CharSequence getEnable() {
        return mEnable;
    }
    
    public boolean getIndexFlg() {
        return mIndexFlg;
    }
    
    public CharSequence getUpdatedAt(CharSequence updatedAt) {
        return mUpdatedAt;
    }

    public CharSequence getCreatededAt(CharSequence createdAt) {
        return mCreatedAt;
    }
    
    public CharSequence getNewFlg(CharSequence newFlg) {
        return mNewFlg;
    }	
	
	public View getView() {
		return mView;
	}
	
}
