package explod.io.dpadhelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import static explod.io.dpadhelper.R.id.pager;

public class MainActivity extends AppCompatActivity implements HasFocusWithinViewPager {

	private static final String TAG = "MainActivity";

	private final FocusHandler mFocusHandler = new FocusHandler(this);

	private LockingViewPager mLockingViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mLockingViewPager = (LockingViewPager) findViewById(pager);
		mLockingViewPager.setAdapter(new MyPagerAdapter());

		mFocusHandler.sendEmptyMessage(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mLockingViewPager.setHasFocusWithinView(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLockingViewPager.setHasFocusWithinView(null);
	}

	@Override
	public boolean hasFocusWithinViewPager() {
		View focus = getCurrentFocus();
		while (focus != null) {
			if (focus instanceof ViewPager) {
				return true;
			}
			ViewParent parent = focus.getParent();
			if (!(parent instanceof View)) {
				return false;
			}
			focus = (View) parent;
		}
		return false;
	}

	public static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		private static final int VIEW_HORI = 0;
		private static final int VIEW_HEADER = 1;

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			switch (viewType) {
				case VIEW_HORI: {
					Context context = parent.getContext();
					ComplexRecyclerView view = new ComplexRecyclerView(context);
					RecyclerView recycler = view.getReyclerView();
					recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
					recycler.setAdapter(new HorizontalAdapter());

					int height = context.getResources().getDimensionPixelSize(R.dimen.horiz_adapter_height);

					RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height);
					view.setLayoutParams(params);

					return new HorizontalViewHolder(view);
				}
				case VIEW_HEADER: {
					Context context = parent.getContext();
					TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
					int height = context.getResources().getDimensionPixelSize(R.dimen.text_height);
					RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height);
					view.setLayoutParams(params);
					return new TextViewHolder(view);
				}
				default:
					return null;
			}
		}

		@Override
		public int getItemViewType(int position) {
			return (position + 1) % 2;
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			int viewType = getItemViewType(position);
			switch (viewType) {
				case VIEW_HEADER: {
					TextViewHolder tvh = (TextViewHolder) holder;
					tvh.view.setText(String.valueOf(position / 2 + 1));
				}
				default:
					// no-op
			}
		}

		@Override
		public int getItemCount() {
			return 10;
		}
	}

	public static class HorizontalViewHolder extends RecyclerView.ViewHolder {

		public HorizontalViewHolder(View itemView) {
			super(itemView);
		}
	}

	public static class HorizontalAdapter extends RecyclerView.Adapter<TextViewHolder> {

		@Override
		public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			Context context = parent.getContext();
			TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
			return new TextViewHolder(view);
		}

		@Override
		public void onBindViewHolder(TextViewHolder holder, int position) {
			int verticalPosition = holder.getAdapterPosition();
			holder.view.setText("(" + String.valueOf(verticalPosition + 1) + "x" + String.valueOf(position + 1) + ")");
		}

		@Override
		public int getItemCount() {
			return 10;
		}
	}

	public static class TextViewHolder extends RecyclerView.ViewHolder {

		TextView view;

		public TextViewHolder(TextView itemView) {
			super(itemView);
			view = itemView;
		}
	}

	public static class FocusHandler extends Handler {

		@NonNull
		private final WeakReference<Activity> mActivityRef;

		public FocusHandler(@NonNull Activity activity) {
			super(Looper.getMainLooper());
			mActivityRef = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Activity activity = mActivityRef.get();
			if (activity == null) return;

			View view = activity.getCurrentFocus();
			if (view == null) {
				Log.d(TAG, "No view in focus.");
			} else if (view instanceof TextView) {
				CharSequence text = ((TextView) view).getText();
				Log.d(TAG, "View in focus: " + text);
			} else {
				Log.d(TAG, "View in focus: " + view);
			}

			sendEmptyMessageDelayed(0, 1_000);
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = container.getContext();
			RecyclerView view = new RecyclerView(context);
			view.setLayoutManager(new LinearLayoutManager(context));
			view.setAdapter(new Adapter());
			view.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
			container.removeView((View) view);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
}
