package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewW3 extends TextView {

	public TextViewW3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public TextViewW3(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public TextViewW3(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
//		this.setTypeface(AppConstants.getW3Font(context));
		this.setSingleLine(false);
		this.setLineSpacing(0.0f, 1.25f);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
