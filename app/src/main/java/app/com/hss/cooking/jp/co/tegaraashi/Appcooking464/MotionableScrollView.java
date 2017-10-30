package app.com.hss.cooking.jp.co.tegaraashi.Appcooking464;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MotionableScrollView extends ScrollView {
    public MotionableScrollView(Context context) {
        super(context);
    }
    public MotionableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MotionableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if ( this.canScroll() ) {
                	Log.v("MAGATAMA", "---->canScroll：true");
                    //スクロール可能な場合は既定の処理を実施
                    return super.onInterceptTouchEvent(ev);
                } else {
                	Log.v("MAGATAMA", "---->canScroll：false");
                    return false;
                }
            }

            case MotionEvent.ACTION_DOWN: {
                return super.onInterceptTouchEvent(ev);
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_POINTER_UP:
                return super.onInterceptTouchEvent(ev);
        }

        return super.onInterceptTouchEvent(ev);
    }

    protected boolean canScroll() {
    	
        View child = this.getChildAt(0);
//    	ViewGroup c1 = (ViewGroup) this.getChildAt(0);
//    	View child = c1.getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            Log.v("MAGATAMA", "---->this.getHeight: " + this.getHeight());
            Log.v("MAGATAMA", "---->childHeight: " + (childHeight + this.getPaddingTop() + this.getPaddingBottom()));
            return this.getHeight() < childHeight + this.getPaddingTop() + this.getPaddingBottom();
        }
        return false;
    }
    
    private int mStartPos = 0;
    private int mEndPos = 0;
    public void setMotionScrollPosition (int startPos, int endPos) {
    	mStartPos = startPos;
    	mEndPos = endPos; 
    }
    
}