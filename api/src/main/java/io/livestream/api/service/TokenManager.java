package io.livestream.api.service;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

class TokenManager {

  private static final String TOKEN = "TOKEN";

  private TokenManager() {

  }

  static void setToken(Context context, String token) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    if (token != null) {
      preferences.edit().putString(TOKEN, token).apply();
    } else {
      preferences.edit().remove(TOKEN).apply();
    }
  }

  static String getToken(Context context) {
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    return preferences.getString(TOKEN, null);
  }
}
