package app.com.hss.cooking.magatama.stampcard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import app.com.hss.cooking.R;


public class DialogShowShortContent extends Dialog implements
		android.view.View.OnClickListener {
	private String mContent = "";
	private TextViewW3 mTvContent;
	private Button mBtnOk;
	private View mViewMain;
	private int mColor = 0;

	public DialogShowShortContent(Context context, String msg) {
		super(context);
		this.mContent = msg;
	}
	public DialogShowShortContent(Context context, String msg , int color) {
		super(context);
		this.mContent = msg;
		this.mColor = color;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog_show_short_content);
		mTvContent = (TextViewW3) findViewById(R.id.tv_content);
		mTvContent.setText(mContent);
		mBtnOk = (Button) findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
		mViewMain = findViewById(R.id.rl_main_content);
		mViewMain.setBackgroundColor(mColor);
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
