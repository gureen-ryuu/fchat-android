package cc.pat.fchat.utils;

import cc.pat.fchat.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DefaultHeaderPreparer implements HeaderPreparer {
	private static final int NOTHING = -1;
	private int leftIcon = NOTHING;
	private int rightIcon = NOTHING;
	private View middleView = null;
	
	View.OnClickListener leftListener;
	View.OnClickListener rightListener;
	View.OnClickListener middleListener;

	RelativeLayout rl;

	private DefaultHeaderPreparer(View view) {
		rl = (RelativeLayout) view;
		rl.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.white));
		rl.setMinimumHeight(Utils.dipToPixels(view.getContext(), 50));
		middleView = new TextView(view.getContext());
		((TextView)middleView).setText(view.getContext().getResources().getString(R.string.fchat));
	}

	public static DefaultHeaderPreparer get(View view) {
		DefaultHeaderPreparer result;
		Object o = view.getTag();
		if (o instanceof DefaultHeaderPreparer) {
			result = (DefaultHeaderPreparer) o;
			result.reset(view);

			return result;
		}
		result = new DefaultHeaderPreparer(view);
		view.setTag(result);
		return result;
	}

	private void reset(View view) {
		rl = (RelativeLayout) view;
		removeLeftIcon();
		removeRightIcon();
	}

	public DefaultHeaderPreparer removeLeftIcon() {
		this.leftIcon = NOTHING;
		this.leftListener = null;
		return this;
	}

	public DefaultHeaderPreparer setLeftIcon(int icon, View.OnClickListener listener) {
		this.leftIcon = icon;
		this.leftListener = listener;
		return this;
	}

	public DefaultHeaderPreparer removeRightIcon() {
		this.rightIcon = NOTHING;
		this.rightListener = null;
		return this;
	}

	public DefaultHeaderPreparer setRightIcon(int icon, View.OnClickListener listener) {
		this.rightIcon = icon;
		this.rightListener = listener;
		return this;
	}

	public DefaultHeaderPreparer setMiddleView(View view, View.OnClickListener listener) {
		if(middleView != null)
			rl.removeView(middleView);
		
		this.middleView = view;
		this.middleListener = listener;
		return this;
	}

	public DefaultHeaderPreparer setHeaderBackgroundColor(int color) {
		rl.setBackgroundColor(color);
		return this;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public DefaultHeaderPreparer setHeaderBackground(Drawable drawable) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			rl.setBackgroundDrawable(drawable);
		} else {
			rl.setBackground(drawable);
		}
		return this;
	}

	public void prepare() {
		Context cxt = rl.getContext();
		ImageView iv;

		RelativeLayout.LayoutParams params;
		rl.removeAllViews();
		// iv = new ImageView(cxt);
		
		ViewGroup middleViewParent = (ViewGroup) middleView.getParent();
		if (middleViewParent != null) {
			middleViewParent.removeView(middleView);
		}
		middleView.setId(1);
	
		
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		rl.addView(middleView, params);
		//--
		iv = new ImageView(cxt);
		iv.setImageResource(leftIcon);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.setMargins(30, 0, 0, 0);
		rl.addView(iv, params);
		
		if (leftListener != null)
			iv.setOnClickListener(leftListener);
		
		//--
		iv = new ImageView(cxt);
		iv.setImageResource(rightIcon);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.setMargins(0, 0, 30, 0);
		rl.addView(iv, params);
		if (rightListener != null)
			iv.setOnClickListener(rightListener);
		
		//--
		
//		iv = new ImageView(cxt);
//		iv.setImageResource(R.drawable.seperator);
//		iv.setScaleType(ScaleType.FIT_XY);
//		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//		params.setMargins(0, S.utils.dipToPixels(cxt, 20), 0 ,0);
////		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		params.addRule(RelativeLayout.BELOW, middleView.getId());
//
//		rl.addView(iv, params);
	}

}
