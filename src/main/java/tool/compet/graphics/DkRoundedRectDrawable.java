/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.graphics;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DkRoundedRectDrawable extends DkRippleDrawable {
	private final Paint paint;

	private int borderColor = Color.LTGRAY;

	public DkRoundedRectDrawable() {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1 * metrics.scaledDensity);
		paint.setColor(borderColor);
	}

	@Override
	public void draw(@NonNull Canvas canvas) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		int width = getBounds().width();
		int height = getBounds().height();
		int radius = (int) (12 * metrics.density);
		int padding = (int) (4 * metrics.density);
		int padding2 = padding << 1;

		Path border = generateBorderPath(padding, padding, width - padding2, height - padding2, radius, radius);

		canvas.clipPath(border);
//		canvas.drawColor(borderColor);
		canvas.drawPath(border, paint);
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {
	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	public DkRoundedRectDrawable setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	/**
	 * @param width In dp, not px.
	 */
	public DkRoundedRectDrawable setStrokeWidth(float width) {
		DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
		paint.setStrokeWidth(width * metrics.scaledDensity);
		return this;
	}

	private Path generateBorderPath(float left, float top, float right, float bottom, float rx, float ry) {
		Path path = new Path();
		float width = right - left;
		float height = bottom - top;

		rx = Math.max(0, Math.min(width / 2, rx));
		ry = Math.max(0, Math.min(height / 2, ry));

		float widthMinusCorners = (width - (2 * rx));
		float heightMinusCorners = (height - (2 * ry));

		path.moveTo(right, top + ry);
		path.rQuadTo(0, -ry, -rx, -ry); // top-right corner
		path.rLineTo(-widthMinusCorners, 0);
		path.rQuadTo(-rx, 0, -rx, ry); // top-left corner
		path.rLineTo(0, heightMinusCorners);

		path.rQuadTo(0, ry, rx, ry); // bottom-left corner
		path.rLineTo(widthMinusCorners, 0);
		path.rQuadTo(rx, 0, rx, -ry); // bottom-right corner
		path.rLineTo(0, -heightMinusCorners);

		path.close();

		return path;
	}
}
