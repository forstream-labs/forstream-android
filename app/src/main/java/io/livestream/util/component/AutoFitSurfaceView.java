package io.livestream.util.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class AutoFitSurfaceView extends SurfaceView {

  private float aspectRatio = 0F;

  public AutoFitSurfaceView(Context context) {
    this(context, null);
  }

  public AutoFitSurfaceView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AutoFitSurfaceView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setAspectRatio(int width, int height) {
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Width or height cannot be negative");
    }
    aspectRatio = (float) width / (float) height;
    getHolder().setFixedSize(width, height);
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    if (aspectRatio == 0.0F) {
      setMeasuredDimension(width, height);
    } else {
      int newWidth;
      int newHeight;
      float actualRatio = width > height ? aspectRatio : 1F / aspectRatio;
      if ((float) width < (float) height * actualRatio) {
        newHeight = height;
        newWidth = Math.round((float) height * actualRatio);
      } else {
        newWidth = width;
        newHeight = Math.round((float) width / actualRatio);
      }
      setMeasuredDimension(newWidth, newHeight);
    }
  }
}