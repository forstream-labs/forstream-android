package io.forstream.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import io.forstream.api.model.Channel;
import io.forstream.api.model.User;
import io.forstream.util.component.LetterTileProvider;

public class ImageUtils {

  private ImageUtils() {

  }

  public static void loadImage(Context context, User user, ImageView imageView) {
    loadImage(context, user.getImageUrl(), user.getFirstName(), imageView);
  }

  public static void loadImage(Context context, Channel channel, ImageView imageView) {
    loadImage(context, channel.getImageUrl(), channel.getName(), imageView);
  }

  private static void loadImage(Context context, @Nullable String url, String name, ImageView imageView) {
    imageView.post(() -> {
      if (imageView.getWidth() > 0) {
        Bitmap placeholder = LetterTileProvider.getInstance().getLetterTile(context, name, imageView.getWidth());
        if (url != null) {
          Glide.with(context).load(url).placeholder(new BitmapDrawable(context.getResources(), placeholder)).into(imageView);
        } else {
          imageView.setImageBitmap(placeholder);
        }
      } else if (url != null) {
        Glide.with(context).load(url).into(imageView);
      }
    });
  }
}
