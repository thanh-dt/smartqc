package app.com.hss.cooking.magatama.coupon;

import android.text.format.DateFormat;
import android.view.View;

public class CouponItem {
	private CharSequence mId;
	private CharSequence mTitle;
	private CharSequence mSubTitle;
	private CharSequence mDescription;
	private CharSequence mDate;
	private CharSequence mImage;
	private CharSequence mPolicy;
	private CharSequence mStartDatetime;
	private CharSequence mEndDatetime;
	private CharSequence mDiscount;
	private CharSequence mDiscountType;
	private CharSequence mFileName;
	private CharSequence mTmpFileName;
	private CharSequence mPosition;
	private CharSequence mTermFlg;
	private CharSequence mUseCount;
	private CharSequence mUseFlg;
	private CharSequence mEnableFlg;
	private CharSequence mType;
	private CharSequence mUpdatedAt;
	private CharSequence mCreatedAt;
	private CharSequence mNew;
	private CharSequence mDisplayDays;
	private CharSequence mBlankFlg;
	private View mView;

	public CouponItem() {
		setId("");
		mTitle = "";
		mSubTitle = "";
		mDescription = "";
		mDate = "";
		mImage  = "";
		mView = null;
		mPolicy = "";
		mStartDatetime = "";
		mEndDatetime = "";
		mDiscount = "";
		mDiscountType = "";
		mFileName = "";
		mTmpFileName = "";
		mPosition = "";
		mTermFlg = "";
		mUseCount = "";
		mUseFlg = "";
		mEnableFlg = "";
		mType = "";
		mUpdatedAt = "";
		mCreatedAt = "";
		mNew = "";
		mDisplayDays = "";
		mBlankFlg = "";
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

    public void setPolicy(CharSequence policy) {
        mPolicy = policy;
    }
    
    public void setStartDatetime(CharSequence startDatetime) {
        mStartDatetime = startDatetime;
    }
    
    public void setEndDatetime(CharSequence endDatetime) {
        mEndDatetime = endDatetime;
    }
    
    public void setDiscount(CharSequence discount) {
        mDiscount = discount;
    }

    public void setDiscountType(CharSequence discountType) {
        mDiscountType = discountType;
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
    
    public void setTermFlg(CharSequence termFlg) {
    	mTermFlg = termFlg;
    }
    
    public void setUseCount(CharSequence useCount) {
        mUseCount = useCount;
    }

    public void setUseFlg(CharSequence useFlg) {
        mUseFlg = useFlg;
    }

    public void setEnableFlg(CharSequence enableFlg) {
        mEnableFlg = enableFlg;
    }
    
    public void setType(CharSequence type) {
        mType = type;
    }

    public void setUpdatedAt(CharSequence updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public void setCreatededAt(CharSequence createdAt) {
        mCreatedAt = createdAt;
    }
    
    public void setNewFlg(CharSequence newFlg) {
        mNew = newFlg;
    }
    
    public void setDisplayDays(CharSequence displayDays) {
        mDisplayDays = displayDays;
    }
    
    public void setBlankFlg(CharSequence flg) {
        mBlankFlg = flg;
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
	
	public View getView() {
		return mView;
	}

	public CharSequence getPocliy() {
		return mPolicy;
	}
	
	public CharSequence getStartDatetime() {
		return mStartDatetime;
	}
	
	public CharSequence getEndDatetime() {
		return mEndDatetime;
	}
	
    public CharSequence getDiscount() {
        return mDiscount;
    }

    public CharSequence getDiscountType() {
        return mDiscountType;
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
    
    public CharSequence getTermFlg() {
    	return mTermFlg;
    }
    
    public CharSequence getUseCount() {
        return mUseCount;
    }

    public CharSequence getUseFlg() {
        return mUseFlg;
    }

    public CharSequence getEnableFlg() {
        return mEnableFlg;
    }
    
    public CharSequence getType() {
        return mType;
    }

    public CharSequence getUpdatedAt() {
        return mUpdatedAt;
    }

    public CharSequence getCreatededAt() {
        return mCreatedAt;
    }
    
    public CharSequence getNewFlg() {
        return mNew;
    }
    
    public CharSequence getDisplayDays() {
        return mDisplayDays;
    }

    public CharSequence getBlankFlg() {
        return mBlankFlg;
    }
}
