package explod.io.dpadhelper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class DPadViewPager extends ViewPager {

	public DPadViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean arrowScroll(int direction) {
		return false;
	}

}
