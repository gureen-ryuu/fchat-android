package cc.pat.fchat.ui;

import cc.pat.fchat.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HeaderView extends RelativeLayout {

	Bitmap line;

	Rect src;
	Rect dst;

	public HeaderView(Context context) {
		super(context);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		line = BitmapFactory.decodeResource(getResources(), R.drawable.seperator);
		src = new Rect(0, 0, line.getWidth(), line.getHeight());
		dst = new Rect();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		dst.left = 0;
		dst.bottom = h;
		dst.top = h - src.height();
		dst.right = w;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(line, src, dst, null);
		super.onDraw(canvas);
	}
}
