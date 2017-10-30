package app.com.hss.cooking.magatama.stampcard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ScrollView;

import app.com.hss.cooking.R;


public class DialogContentPromot extends Dialog implements
		android.view.View.OnClickListener {
	private TextViewW3 mTvContent;
	private String mContent;
	private ImageButton mBtnOk;
	private ScrollView mSrc;
	private String mColor;

	public DialogContentPromot(Context context, String content) {
		super(context);
		mContent = content;
	}

	public DialogContentPromot(Context context, String content , String color) {
		super(context);
		mContent = content;
		this.mColor = color;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog_content_promot);
		mTvContent = (TextViewW3) findViewById(R.id.tv_promot);
		mTvContent.setText(mContent);
		mBtnOk = (ImageButton) findViewById(R.id.btn_ok);
		mSrc = (ScrollView) findViewById(R.id.src_main);
		mBtnOk.setOnClickListener(this);
		if (mColor.length() > 0) {
			mSrc.setBackgroundColor(Color.parseColor(mColor));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			dismiss();
			break;
		default:
			break;
		}

	}

}
