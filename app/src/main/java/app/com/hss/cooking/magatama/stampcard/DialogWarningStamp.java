package app.com.hss.cooking.magatama.stampcard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import app.com.hss.cooking.R;


public class DialogWarningStamp extends Dialog implements
		android.view.View.OnClickListener {
	private TextView mTvContent, mTvCancel, mTvOk;
	private onChoiceDialog mChoice;
	private String mMsgError;

	public DialogWarningStamp(Context context, String msg) {
		super(context);
		this.mMsgError = msg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog_warning);
		mTvContent = (TextView) findViewById(R.id.tv_content);
		mTvCancel = (TextView) findViewById(R.id.tv_cancel);
		mTvOk = (TextView) findViewById(R.id.tv_ok);
		mTvOk.setOnClickListener(this);
		mTvCancel.setOnClickListener(this);
		mTvContent.setText(mMsgError);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			if (mChoice != null) {
				mChoice.onOk();
			}
			dismiss();
			break;

		case R.id.tv_cancel:
			if (mChoice != null) {
				mChoice.onCancel();
			}
			dismiss();
			break;

		default:
			break;
		}
	}

	public void setListener(onChoiceDialog choice) {
		mChoice = choice;
	}

	public interface onChoiceDialog {
		void onOk();

		void onCancel();
	}

}
