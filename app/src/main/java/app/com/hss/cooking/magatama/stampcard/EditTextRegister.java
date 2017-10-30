package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import app.com.hss.cooking.R;
import app.com.hss.cooking.magatama.utils.AppConstants;


public class EditTextRegister extends android.support.v7.widget.AppCompatEditText {
	
	private boolean isCheck = false;

	public EditTextRegister(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initViews(context);
	}

	public EditTextRegister(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initViews(context);
	}

	public EditTextRegister(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initViews(context);
	}

	private void initViews(final Context context) {
		// set max length.
		this.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				AppConstants.MAX_LENGTH_EDITTEXT) });
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
				if (isCheck && s.length() >= AppConstants.MAX_LENGTH_EDITTEXT) {
					Toast.makeText(context,
							context.getString(R.string.msg_cannot_input_more),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				setHint("");
				isCheck = true;
				return false;
			}
		});
	}
}
