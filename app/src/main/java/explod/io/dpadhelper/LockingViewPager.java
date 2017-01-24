package explod.io.dpadhelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.ref.WeakReference;

public class LockingViewPager extends ViewPager implements HasFocusWithinViewPager {

	private static final String TAG = "LockingViewPager";

	private WeakReference<HasFocusWithinViewPager> mHasFocusWithinViewPagerRef;

	public LockingViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setHasFocusWithinView(@Nullable HasFocusWithinViewPager focus) {
		if (focus != null) {
			mHasFocusWithinViewPagerRef = new WeakReference<>(focus);
		} else {
			mHasFocusWithinViewPagerRef = null;
		}
	}

	@Override
	public boolean hasFocusWithinViewPager() {
		HasFocusWithinViewPager focus = mHasFocusWithinViewPagerRef == null ? null : mHasFocusWithinViewPagerRef.get();
		boolean can = focus != null && focus.hasFocusWithinViewPager();
		Log.d(TAG, "hasFocusWithinViewPager() called -> " + can);
		return can;
	}

	@Override
	public boolean canScrollHorizontally(int direction) {
		return !hasFocusWithinViewPager() && super.canScrollHorizontally(direction);
	}

}
