package io.forstream.common;

import android.os.Bundle;

import androidx.annotation.Nullable;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

  //  private FirebaseAnalytics firebaseAnalytics;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //    firebaseAnalytics = FirebaseAnalytics.getInstance(this);
  }

  //  public FirebaseAnalytics getFirebaseAnalytics() {
  //    return firebaseAnalytics;
  //  }
}
