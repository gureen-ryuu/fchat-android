package cc.pat.fchat.utils;

import android.content.Context;
import android.util.TypedValue;

public class Utils {
	public static int dipToPixels(Context mContext,float value)
	{
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
		
		return (int)pixels;
	}
}
