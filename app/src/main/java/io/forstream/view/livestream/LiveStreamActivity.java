package io.forstream.view.livestream;

import android.Manifest;
import android.os.Bundle;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera2;
import com.pedro.rtplibrary.util.BitrateAdapter;
import com.pedro.rtplibrary.util.SensorRotationManager;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.enums.StreamStatus;
import io.forstream.api.model.LiveStream;
import io.forstream.api.model.ProviderMessage;
import io.forstream.api.model.ProviderStream;
import io.forstream.common.BaseActivity;
import io.forstream.common.Constants;
import io.forstream.util.AlertUtils;
import io.forstream.util.AppUtils;
import io.forstream.util.UIUtils;
import io.forstream.util.component.SpaceItemDecoration;
import io.forstream.view.main.home.livestream.LiveStreamAlertsAdapter;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.PermissionUtils;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LiveStreamActivity extends BaseActivity implements ConnectCheckerRtmp, SurfaceHolder.Callback, LiveStreamAlertsAdapter.Listener {

  @BindView(R.id.live_stream_camera) OpenGlView liveStreamCameraView;
  @BindView(R.id.live_stream_status) TextView liveStreamStatusView;
  @BindView(R.id.live_options_layout) View liveOptionsLayout;
  @BindView(R.id.open_live_stream_info_button) FloatingActionButton openLiveStreamInfoButton;
  @BindView(R.id.switch_camera_button) FloatingActionButton switchCameraButton;
  @BindView(R.id.start_live_stream_button) FloatingActionButton startLiveStreamButton;
  @BindView(R.id.end_live_stream_button) FloatingActionButton endLiveStreamButton;
  @BindView(R.id.live_stream_alerts_view) RecyclerView liveStreamAlertsView;

  @Inject LiveStreamViewModel liveStreamViewModel;
  @Inject LiveStreamDialogFragment liveStreamDialogFragment;
  @Inject LiveStreamAlertsAdapter liveStreamAlertsAdapter;

  private LiveStream liveStream;

  private RtmpCamera2 rtmpCamera;
  private SensorRotationManager sensorRotationManager;
  private BitrateAdapter bitrateAdapter;
  private CameraHelper.Facing cameraFacing = CameraHelper.Facing.BACK;
  private Size cameraResolution;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_stream);
    ButterKnife.bind(this);

    setupLiveStream();
    setupObservers();
    setupViews();
    setupLiveStreamAlertsView();
    setupContent();
  }

  @Override
  protected void onResume() {
    super.onResume();
    Window window = getWindow();
    window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    LiveStreamActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @Override
  public void onProviderMessageClick(ProviderStream providerStream, ProviderMessage providerMessage) {

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    cameraResolution = new Size(width, height);
    if (PermissionUtils.hasSelfPermissions(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
      rtmpCamera.startPreview(cameraFacing, width, height);
    }
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
    if (cameraResolution != null && !rtmpCamera.isOnPreview()) {
      rtmpCamera.startPreview(cameraFacing, cameraResolution.getWidth(), cameraResolution.getHeight());
    }
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
      if (prepareAudio() && prepareVideo()) {
        rtmpCamera.startStream(liveStream.getStreamUrl());
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
    rtmpCamera = new RtmpCamera2(liveStreamCameraView, this);
    rtmpCamera.setReTries(10);
    sensorRotationManager = new SensorRotationManager(this, rotation -> rtmpCamera.getGlInterface().setStreamRotation(rotation));
    liveStreamCameraView.getHolder().addCallback(this);

    openLiveStreamInfoButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_info));
    switchCameraButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_camera));

    int marginMd = getResources().getDimensionPixelSize(R.dimen.margin_md);
    int statusBarMargin = UIUtils.getStatusBarHeight(this) + UIUtils.convertDpToPixel(this, 20);
    int navigationBarMargin = UIUtils.getNavigationBarHeight(this, getRequestedOrientation()) + UIUtils.convertDpToPixel(this, 20);

    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) liveStreamStatusView.getLayoutParams();
    layoutParams.setMargins(marginMd, statusBarMargin, 0, 0);
    liveStreamStatusView.requestLayout();

    startLiveStreamButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_video));
    layoutParams = (ViewGroup.MarginLayoutParams) startLiveStreamButton.getLayoutParams();
    layoutParams.setMargins(0, 0, navigationBarMargin, 0);
    startLiveStreamButton.requestLayout();

    endLiveStreamButton.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_stop));
    layoutParams = (ViewGroup.MarginLayoutParams) endLiveStreamButton.getLayoutParams();
    layoutParams.setMargins(0, 0, navigationBarMargin, 0);
    endLiveStreamButton.requestLayout();
  }

  private void setupLiveStreamAlertsView() {
    liveStreamAlertsAdapter.setListener(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(this.getResources().getDimensionPixelSize(R.dimen.margin_sm), layoutManager.getOrientation());
    liveStreamAlertsView.setLayoutManager(layoutManager);
    liveStreamAlertsView.addItemDecoration(itemDecoration);
    liveStreamAlertsView.setAdapter(liveStreamAlertsAdapter);
  }

  private void setupContent() {
    LiveStreamActivityPermissionsDispatcher.loadLiveStreamWithPermissionCheck(this);
  }

  private void updateContent() {
    liveStreamStatusView.setVisibility(View.VISIBLE);
    liveStreamStatusView.setText(AppUtils.getStreamStatusName(this, liveStream.getStreamStatus(), rtmpCamera));
    UIUtils.setColorFilter(liveStreamStatusView.getBackground(), AppUtils.getStreamStatusColor(this, liveStream.getStreamStatus(), rtmpCamera));

    liveOptionsLayout.setVisibility(View.VISIBLE);
    startLiveStreamButton.setVisibility(!liveStream.getStreamStatus().equals(StreamStatus.ENDED) && (rtmpCamera == null || !rtmpCamera.isStreaming()) ? View.VISIBLE : View.GONE);
    endLiveStreamButton.setVisibility(liveStream.getStreamStatus().equals(StreamStatus.LIVE) && rtmpCamera != null && rtmpCamera.isStreaming() ? View.VISIBLE : View.GONE);

    if (liveStream.getStreamStatus().equals(StreamStatus.ENDED)) {
      onOpenLiveStreamInfoButtonClick();
    }

    liveStreamAlertsAdapter.setLiveStream(liveStream);
    liveStreamAlertsAdapter.notifyDataSetChanged();
  }
}
