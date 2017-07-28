package br.com.i9algo.autaz.pdv.helpers;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

public class BitmapUtils {

	/**
	 * <p>
	 * Cantos arredondados em Bitmap
	 * </p>
	 * <b>Exemplo de uso:</b>
	 * 
	 * <pre>
	 * int w = imageView.getWidth();
	 * int h = imageView.getHeight();
	 * bm =  BitmapUtils.getToundBitmap(bitmap, w);
	 * </pre>
	 * @param bm
	 * @param radius
	 * @return
	 */
	public static Bitmap getRoundBitmap(Bitmap bm, int radius) {
		Bitmap bitmap = bm.copy(Config.ARGB_8888, true);
		Bitmap sbmp;
		if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
		else
			sbmp = bitmap;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
				sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}
}
