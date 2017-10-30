package app.com.hss.cooking.magatama.stampcard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewW6 extends TextView {

	public TextViewW6(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initConfig(context);
	}

	public TextViewW6(Context context, AttributeSet attrs) {
		super(context, attrs);
		initConfig(context);
	}

	public TextViewW6(Context context) {
		super(context);
		initConfig(context);
	}

	private void initConfig(Context context) {
//		this.setTypeface(AppConstants.getW6Font(context));
		this.setSingleLine(false);
		this.setLineSpacing(0.0f, 1.25f);
	}


}
