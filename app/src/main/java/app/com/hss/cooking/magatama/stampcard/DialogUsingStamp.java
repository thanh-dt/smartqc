package app.com.hss.cooking.magatama.stampcard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import app.com.hss.cooking.R;


public class DialogUsingStamp extends Dialog implements View.OnClickListener {
	private TextView mTvOk, mTvCancel, mTvTitle;
	private onPickDialog mOnPickDialog;
	private String mTitle;

	public DialogUsingStamp(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DialogUsingStamp(Context context, String title) {
		super(context);
		this.mTitle = title;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_using_stamp);
		setCanceledOnTouchOutside(false);
		mTvOk = (TextView) findViewById(R.id.tv_ok);
		mTvOk.setOnClickListener(this);
		mTvCancel = (TextView) findViewById(R.id.tv_cancel);
		mTvCancel.setOnClickListener(this);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		if (mTitle.length() > 0) {
			mTvTitle.setText(mTitle);
		}
	}

	public void setListenner(onPickDialog onListenner) {
		mOnPickDialog = onListenner;
	}

	public interface onPickDialog {
		void onOk();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			if (mOnPickDialog != null) {
				mOnPickDialog.onOk();
			}
			dismiss();
			break;
		case R.id.tv_cancel:
			dismiss();
			break;

		default:
			break;
		}

	}

}
