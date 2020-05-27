package io.forstream.util.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.Map;

import io.forstream.R;

/**
 * Used to create a {@link Bitmap} that contains a letter used in the English
 * alphabet or digit, if there is no letter or digit available, a default image
 * is shown instead
 */
public class LetterTileProvider {

  /**
   * Singleton instance
   */
  private static LetterTileProvider instance;
  /**
   * Cache of tile bitmaps
   */
  private Map<String, Bitmap> bitmapCache;
  /**
   * The background colors of the tiles
   */
  private int[] colors;

  /**
   * Constructor for <code>LetterTileProvider</code>
   */
  private LetterTileProvider() {
    bitmapCache = new HashMap<>();
  }

  public static LetterTileProvider getInstance() {
    if (instance == null) {
      instance = new LetterTileProvider();
    }
    return instance;
  }

  public Bitmap getLetterTile(Context context, String name, int dimension) {
    return getLetterTile(context, name, dimension, dimension);
  }

  /**
   * @param name    The name used to create the letter for the tile
   * @param context The context used to get the font
   * @param width   The width of first letter and bitmap
   * @param height  The height of first letter and bitmap
   * @return A {@link Bitmap} that contains a letter used in the English
   * alphabet or digit, if there is no letter or digit available, no letter will be displayed
   */
  public Bitmap getLetterTile(Context context, String name, int width, int height) {
    if (colors == null) {
      colors = context.getResources().getIntArray(R.array.tile_colors);
    }
    Bitmap bitmap = getBitmapFromCache(width, height, name);
    if (bitmap != null) {
      return bitmap;
    }

    bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    TextPaint paint = new TextPaint();
    paint.setTypeface(ResourcesCompat.getFont(context, R.font.ubuntu));
    paint.setColor(Color.WHITE);
    paint.setTextAlign(Paint.Align.CENTER);
    paint.setAntiAlias(true);

    Canvas canvas = new Canvas();
    canvas.setBitmap(bitmap);
    canvas.drawColor(pickColor(name));

    char firstLetter = name.charAt(0);
    if (isValid(firstLetter)) {
      char[] letters = new char[]{Character.toUpperCase(firstLetter)};
      Rect bounds = new Rect();
      double size = Math.min(height, width);
      paint.setTextSize((float) (size / 2));
      paint.getTextBounds(letters, 0, 1, bounds);
      canvas.drawText(letters, 0, 1, width / 2, height / 2 + (bounds.bottom - bounds.top) / 2, paint);
    }
    addBitmapToCache(width, height, name, bitmap);
    return bitmap;
  }

  /**
   * @param firstLetter The char to check
   * @return True if <code>firstLetter</code> is in the English alphabet or is a digit, false otherwise
   */
  private boolean isValid(char firstLetter) {
    return 'A' <= firstLetter && firstLetter <= 'Z' || 'a' <= firstLetter && firstLetter <= 'z' || '0' <= firstLetter && firstLetter <= '9';
  }

  /**
   * @param key The key used to generate the tile color
   * @return A new or previously chosen color for <code>key</code> used as the tile background color
   */
  private int pickColor(String key) {
    // String.hashCode() is not supposed to change across java versions,
    // so this should guarantee the same key always maps to the same color
    int index = Math.abs(key.hashCode()) % colors.length;
    return colors[index];
  }

  private String getBitmapCacheKey(int width, int height, String name) {
    return width + "_" + height + "_" + name;
  }

  private void addBitmapToCache(int width, int height, String name, Bitmap bitmap) {
    bitmapCache.put(getBitmapCacheKey(width, height, name), bitmap);
  }

  private Bitmap getBitmapFromCache(int width, int height, String name) {
    return bitmapCache.get(getBitmapCacheKey(width, height, name));
  }
}
