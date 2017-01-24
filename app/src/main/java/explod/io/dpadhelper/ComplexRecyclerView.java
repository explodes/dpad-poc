package explod.io.dpadhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ComplexRecyclerView extends FrameLayout {

	private RecyclerView mReyclerView;

	private TextView mTextView;

	public ComplexRecyclerView(@NonNull Context context) {
		super(context);
		init(context);
	}

	public ComplexRecyclerView(@NonNull Context context, @NonNull AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ComplexRecyclerView(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(@NonNull Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_complex_recycler, this);
		int padding = getResources().getDimensionPixelSize(R.dimen.complex_pad);
		setPadding(padding, padding, padding, padding);
		mReyclerView = (RecyclerView) findViewById(R.id.reycler);
		mTextView = (TextView) findViewById(R.id.text);
	}

	public RecyclerView getReyclerView() {
		return mReyclerView;
	}

	public TextView getTextView() {
		return mTextView;
	}
}
