package io.livestream.view.livestream;

import android.Manifest;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.util.BitrateAdapter;
import com.pedro.rtplibrary.util.SensorRotationManager;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.enums.StreamStatus;
import io.livestream.api.model.LiveStream;
import io.livestream.api.model.ProviderStream;
import io.livestream.common.BaseActivity;
import io.livestream.common.Constants;
import io.livestream.util.AlertUtils;
import io.livestream.util.AppUtils;
import io.livestream.util.UIUtils;
import io.livestream.util.component.RtmpCamera;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LiveStreamActivity extends BaseActivity implements ConnectCheckerRtmp, SurfaceHolder.Callback {

  @BindView(R.id.live_stream_camera) OpenGlView liveStreamCameraView;
  @BindView(R.id.live_stream_status) TextView liveStreamStatusView;
  @BindView(R.id.live_options_layout) View liveOptionsLayout;
  @BindView(R.id.open_live_stream_info_button) FloatingActionButton openLiveStreamInfoButton;
  @BindView(R.id.switch_camera_button) FloatingActionButton switchCameraButton;
  @BindView(R.id.start_live_stream_button) FloatingActionButton startLiveStreamButton;
  @BindView(R.id.end_live_stream_button) FloatingActionButton endLiveStreamButton;

  @Inject LiveStreamViewModel liveStreamViewModel;
  @Inject LiveStreamDialogFragment liveStreamDialogFragment;

  private LiveStream liveStream;
  private RtmpCamera rtmpCamera;
  private SensorRotationManager sensorRotationManager;
  private BitrateAdapter bitrateAdapter;
  private CameraHelper.Facing cameraFacing = CameraHelper.Facing.BACK;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_stream);
    ButterKnife.bind(this);

    setupLiveStream();
    setupObservers();
    setupViews();
    setupContent();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    LiveStreamActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    rtmpCamera.startPreview(cameraFacing, 1280, 720);
    sensorRotationManager.start();
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    sensorRotationManager.stop();
    if (rtmpCamera.isStreaming()) {
      rtmpCamera.stopStream();
    }
    rtmpCamera.stopPreview();
  }

  @Override
  public void onConnectionSuccessRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "Connection success"));
    bitrateAdapter = new BitrateAdapter(bitrate -> rtmpCamera.setVideoBitrateOnFly(bitrate));
    bitrateAdapter.setMaxBitrate(rtmpCamera.getBitrate());
  }

  @Override
  public void onConnectionFailedRtmp(@NonNull String reason) {
    runOnUiThread(() -> {
      AlertUtils.alert(LiveStreamActivity.this, "Connection failed: " + reason);
      if (rtmpCamera.shouldRetry(reason)) {
        rtmpCamera.reTry(5000, reason);
      } else {
        rtmpCamera.stopStream();
      }
    });
  }

  @Override
  public void onNewBitrateRtmp(long bitrate) {
    if (bitrateAdapter != null) {
      bitrateAdapter.adaptBitrate(bitrate);
    }
  }

  @Override
  public void onDisconnectRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "Disconnected"));
  }

  @Override
  public void onAuthErrorRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "Authentication error"));
  }

  @Override
  public void onAuthSuccessRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "Authentication success"));
  }

  @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
  void loadLiveStream() {
    liveStreamViewModel.loadLiveStream(liveStream.getId());
  }

  @OnClick(R.id.open_live_stream_info_button)
  void onOpenLiveStreamInfoButtonClick() {
    Bundle arguments = new Bundle();
    arguments.putSerializable(Constants.LIVE_STREAM, liveStream);
    liveStreamDialogFragment.setArguments(arguments);
    liveStreamDialogFragment.show(getSupportFragmentManager(), LiveStreamActivity.class.getSimpleName());
  }


  @OnClick(R.id.switch_camera_button)
  void onSwitchCameraButtonClick() {
    cameraFacing = cameraFacing.equals(CameraHelper.Facing.BACK) ? CameraHelper.Facing.FRONT : CameraHelper.Facing.BACK;
    rtmpCamera.switchCamera();
  }

  @OnClick(R.id.start_live_stream_button)
  void onStartLiveStreamButtonClick() {
    liveStreamViewModel.startLiveStream(liveStream);
  }

  @OnClick(R.id.end_live_stream_button)
  void onEndLiveStreamButtonClick() {
    if (rtmpCamera.isStreaming()) {
      rtmpCamera.stopStream();
      rtmpCamera.stopPreview();
    }
    liveStreamViewModel.endLiveStream(liveStream);
  }

  private void setupLiveStream() {
    liveStream = (LiveStream) getIntent().getSerializableExtra(Constants.LIVE_STREAM);
  }

  private void setupObservers() {
    liveStreamViewModel.getLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      updateContent();
    });
    liveStreamViewModel.getStartLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      StringBuilder urlsBuilder = new StringBuilder();
      for (ProviderStream providerStream : liveStream.getProviders()) {
        urlsBuilder.append(providerStream.getStreamUrl()).append(",");
      }
      rtmpCamera.setupMuxers(liveStream.getProviders().size());
      if (prepareAudio() && prepareVideo()) {
        rtmpCamera.startStream(urlsBuilder.toString());
      } else {
        // TODO: What to do when device can't go live?
      }
      updateContent();
    });
    liveStreamViewModel.getEndLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      updateContent();
    });
    liveStreamViewModel.getError().observe(this, throwable -> AlertUtils.alert(this, throwable));
  }

  private boolean prepareAudio() {
    int bitrate = 128 * 1024;
    int sampleRate = 48 * 1000;
    return rtmpCamera.prepareAudio(bitrate, sampleRate, true, false, false);
  }

  private boolean prepareVideo() {
    int width = 1280;
    int height = 720;
    int fps = 30;
    int bitrate = 3000 * 1024;
    return rtmpCamera.prepareVideo(width, height, fps, bitrate, false, CameraHelper.getCameraOrientation(this));
  }

  private void setupViews() {
    rtmpCamera = new RtmpCamera(liveStreamCameraView, this);
    rtmpCamera.setReTries(10);
    sensorRotationManager = new SensorRotationManager(this, rotation -> rtmpCamera.getGlInterface().setStreamRotation(rotation));
    liveStreamCameraView.getHolder().addCallback(this);

    int marginMd = getResources().getDimensionPixelSize(R.dimen.margin_md);
    int marginTop = UIUtils.getStatusBarHeight(this) + UIUtils.convertDpToPixel(this, 20);
    int marginBottom = UIUtils.getNavigationBarHeight(this, getRequestedOrientation()) + UIUtils.convertDpToPixel(this, 20);

    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) liveStreamStatusView.getLayoutParams();
    layoutParams.setMargins(marginMd, marginTop, 0, 0);
    liveStreamStatusView.requestLayout();

    layoutParams = (ViewGroup.MarginLayoutParams) liveOptionsLayout.getLayoutParams();
    layoutParams.setMargins(0, marginTop, marginMd, 0);
    liveOptionsLayout.requestLayout();
    openLiveStreamInfoButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_info));
    switchCameraButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_camera));

    startLiveStreamButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_video));
    layoutParams = (ViewGroup.MarginLayoutParams) startLiveStreamButton.getLayoutParams();
    layoutParams.setMargins(0, 0, 0, marginBottom);
    startLiveStreamButton.requestLayout();

    endLiveStreamButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_stop));
    layoutParams = (ViewGroup.MarginLayoutParams) endLiveStreamButton.getLayoutParams();
    layoutParams.setMargins(0, 0, 0, marginBottom);
    endLiveStreamButton.requestLayout();
  }

  private void setupContent() {
    LiveStreamActivityPermissionsDispatcher.loadLiveStreamWithPermissionCheck(this);
  }

  private void updateContent() {
    liveStreamStatusView.setVisibility(View.VISIBLE);
    liveStreamStatusView.setText(AppUtils.getStreamStatusName(this, liveStream.getStatus(), rtmpCamera));
    UIUtils.setColorFilter(liveStreamStatusView.getBackground(), AppUtils.getStreamStatusColor(this, liveStream.getStatus(), rtmpCamera));

    liveOptionsLayout.setVisibility(View.VISIBLE);
    startLiveStreamButton.setVisibility(!liveStream.getStatus().equals(StreamStatus.COMPLETE) && (rtmpCamera == null || !rtmpCamera.isStreaming()) ? View.VISIBLE : View.GONE);
    endLiveStreamButton.setVisibility(liveStream.getStatus().equals(StreamStatus.LIVE) && rtmpCamera != null && rtmpCamera.isStreaming() ? View.VISIBLE : View.GONE);

    if (liveStream.getStatus().equals(StreamStatus.COMPLETE)) {
      onOpenLiveStreamInfoButtonClick();
    }
  }
}
