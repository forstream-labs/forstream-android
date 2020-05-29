package io.forstream;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import io.forstream.api.exception.ApiException;
import io.forstream.dagger.DaggerAppComponent;
import timber.log.Timber;

public class ForstreamApplication extends Application implements HasAndroidInjector {

  @Inject DispatchingAndroidInjector<Object> androidInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    setupDagger();
    setupTimber();
    FirebaseApp.initializeApp(getApplicationContext());
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    FirebaseCrashlytics.getInstance().sendUnsentReports();
  }

  @Override
  public AndroidInjector<Object> androidInjector() {
    return androidInjector;
  }

  private void setupDagger() {
    DaggerAppComponent.factory().create(this).inject(this);
  }

  private void setupTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Timber.plant(new ReleaseTree());
    }
  }

  private static class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable throwable) {
      if (priority == Log.ERROR || priority == Log.WARN) {
        FirebaseCrashlytics.getInstance().log(message);
        if (throwable != null) {
          if (throwable instanceof ApiException) {
            FirebaseCrashlytics.getInstance().log(((ApiException) throwable).getErrorBody());
          }
          FirebaseCrashlytics.getInstance().recordException(throwable);
        }
      }
    }
  }
}