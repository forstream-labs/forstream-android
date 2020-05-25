package io.livestream.util;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileUtils {

  private FileUtils() {

  }

  public static Uri getFileUri(Context context, Uri uri) throws IOException {
    if (uri.getScheme() != null && uri.getScheme().equals("content")) {
      File file = new File(context.getExternalCacheDir(), UUID.randomUUID().toString());
      file.createNewFile();
      try (OutputStream outputStream = new FileOutputStream(file); InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
      }
      return Uri.fromFile(file);
    }
    return uri;
  }
}
