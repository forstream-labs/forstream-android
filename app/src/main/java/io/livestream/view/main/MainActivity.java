package io.livestream.view.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;
import io.livestream.R;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.ProviderStream;
import io.livestream.util.AlertUtils;
import io.livestream.util.component.RtmpCamera;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class MainActivity extends DaggerAppCompatActivity implements ConnectCheckerRtmp, SurfaceHolder.Callback, FacebookCallback<LoginResult> {

  private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;
  private static final int YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE = 1;

  private static final String YOUTUBE_MANAGE_SCOPE = "https://www.googleapis.com/auth/youtube";
  private static final String FACEBOOK_PUBLISH_VIDEO_SCOPE = "publish_video";

  @BindView(R.id.surface_view) SurfaceView surfaceView;

  @Inject MainViewModel mainViewModel;

  private GoogleSignInClient googleSignInClient;
  private GoogleSignInClient youtubeChannelSignInClient;
  private CallbackManager callbackManager = CallbackManager.Factory.create();

  private RtmpCamera rtmpCamera;
  private LiveStream liveStream;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setupGoogleSignIn();
    setupYouTubeChannelSignIn();
    setupCamera();
    setupObservers();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case GOOGLE_SIGN_IN_REQUEST_CODE:
        Task<GoogleSignInAccount> googleSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleGoogleSignInResult(googleSignInTask);
        break;
      case YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE:
        Task<GoogleSignInAccount> youtubeChannelSignInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleYouTubeChannelSignInResult(youtubeChannelSignInTask);
        break;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    mainViewModel.connectFacebookChannel(loginResult.getAccessToken().getToken());
  }

  @Override
  public void onCancel() {

  }

  @Override
  public void onError(FacebookException error) {
    Timber.e(error);
  }

  @Override
  public void onConnectionSuccessRtmp() {
    new Handler(Looper.getMainLooper()).postDelayed(() -> mainViewModel.startLiveStream(liveStream), 15000);
    runOnUiThread(() -> AlertUtils.alert(MainActivity.this, "onConnectionSuccessRtmp"));
  }

  @Override
  public void onConnectionFailedRtmp(@NonNull String reason) {
    runOnUiThread(() -> {
      AlertUtils.alert(MainActivity.this, "onConnectionFailedRtmp " + reason);
      if (rtmpCamera.shouldRetry(reason)) {
        rtmpCamera.reTry(5000, reason);
      } else {
        rtmpCamera.stopStream();
      }
    });
  }

  @Override
  public void onNewBitrateRtmp(long bitrate) {
    runOnUiThread(() -> AlertUtils.alert(MainActivity.this, "onNewBitrateRtmp " + bitrate));
  }

  @Override
  public void onDisconnectRtmp() {
    runOnUiThread(() -> AlertUtils.alert(MainActivity.this, "onDisconnectRtmp"));
  }

  @Override
  public void onAuthErrorRtmp() {
    runOnUiThread(() -> AlertUtils.alert(MainActivity.this, "onAuthErrorRtmp"));
  }

  @Override
  public void onAuthSuccessRtmp() {
    runOnUiThread(() -> AlertUtils.alert(MainActivity.this, "onAuthSuccessRtmp"));
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    rtmpCamera.startPreview();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }

  @OnClick(R.id.sign_in_with_google_button)
  public void onSignInWithGoogleClick() {
    Intent signInIntent = googleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
  }

  @OnClick(R.id.connect_youtube_channel_button)
  public void onConnectYouTubeChannelClick() {
    Intent signInIntent = youtubeChannelSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, YOUTUBE_CHANNEL_SIGN_IN_REQUEST_CODE);
  }

  @OnClick(R.id.connect_facebook_channel_button)
  public void onConnectFacebookChannelClick() {
    LoginManager loginManager = LoginManager.getInstance();
    loginManager.registerCallback(callbackManager, this);
    loginManager.logInWithPublishPermissions(this, Collections.singletonList(FACEBOOK_PUBLISH_VIDEO_SCOPE));
  }

  @OnClick(R.id.create_live_stream_button)
  public void onCreateLiveStreamButtonClick() {
    mainViewModel.createLiveStream();
  }

  @OnClick(R.id.start_live_stream_button)
  public void onStartLiveStreamButtonClick() {
    MainActivityPermissionsDispatcher.startLiveStreamWithPermissionCheck(this);
  }

  @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
  public void startLiveStream() {
    if (liveStream != null) {
      rtmpCamera.setReTries(10);
      if (rtmpCamera.prepareAudio() && rtmpCamera.prepareVideo()) {
        StringBuilder urlsBuilder = new StringBuilder();
        for (ProviderStream providerStream : liveStream.getProviders()) {
          urlsBuilder.append(providerStream.getStreamUrl()).append(",");
        }
        rtmpCamera.startStream(urlsBuilder.toString());
      } else {
        // This device can't init encoders, this could be for 2 reasons:
        // The selected encoder doesn't support any configuration provided
        // The device hasn't a H264 or AAC encoder (in this case you can see log error "valid encoder not found")
      }
    }
  }

  private void setupGoogleSignIn() {
    String googleAuthClientId = getString(R.string.google_oauth2_client_id);
    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .requestProfile()
      .requestIdToken(googleAuthClientId)
      .requestServerAuthCode(googleAuthClientId)
      .build();
    googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
  }

  private void setupYouTubeChannelSignIn() {
    String googleAuthClientId = getString(R.string.google_oauth2_client_id);
    GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .requestProfile()
      .requestIdToken(googleAuthClientId)
      .requestServerAuthCode(googleAuthClientId)
      .requestScopes(new Scope(YOUTUBE_MANAGE_SCOPE))
      .build();
    youtubeChannelSignInClient = GoogleSignIn.getClient(this, signInOptions);
  }

  private void setupCamera() {
    rtmpCamera = new RtmpCamera(surfaceView, this);
    surfaceView.getHolder().addCallback(this);
  }

  private void setupObservers() {
    mainViewModel.getCreateLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      rtmpCamera.setupMuxers(liveStream.getProviders().size());
    });
    mainViewModel.getStartLiveStream().observe(this, liveStream -> {

    });
  }

  private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
      mainViewModel.signInWithGoogle(googleSignInAccount.getServerAuthCode());
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

  private void handleYouTubeChannelSignInResult(Task<GoogleSignInAccount> completedTask) {
    try {
      GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
      mainViewModel.connectYouTubeChannel(googleSignInAccount.getServerAuthCode());
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }
}
