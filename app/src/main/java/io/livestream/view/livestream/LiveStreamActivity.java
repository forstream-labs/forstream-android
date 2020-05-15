package io.livestream.view.livestream;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.button.MaterialButton;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

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
import io.livestream.util.ImageUtils;
import io.livestream.util.UIUtils;
import io.livestream.util.component.RtmpCamera;
import io.livestream.util.component.SpaceItemDecoration;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LiveStreamActivity extends BaseActivity implements ConnectCheckerRtmp, SurfaceHolder.Callback {

  @BindView(R.id.live_stream_thumbnail) ImageView liveStreamThumbnail;
  @BindView(R.id.live_stream_camera_view) SurfaceView liveStreamCameraView;
  @BindView(R.id.live_stream_title) TextView liveStreamTitle;
  @BindView(R.id.live_stream_description) TextView liveStreamDescription;
  @BindView(R.id.live_stream_status) TextView liveStreamStatus;
  @BindView(R.id.live_stream_date) TextView liveStreamDate;
  @BindView(R.id.provider_streams_view) RecyclerView providerStreamsView;
  @BindView(R.id.start_live_stream_button) MaterialButton startLiveStreamButton;
  @BindView(R.id.end_live_stream_button) MaterialButton endLiveStreamButton;

  @Inject LiveStreamViewModel liveStreamViewModel;
  @Inject ProviderStreamsAdapter providerStreamsAdapter;

  private RtmpCamera rtmpCamera;
  private LiveStream liveStream;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_stream);
    ButterKnife.bind(this);

    setupLiveStream();
    setupObservers();
    setupViews();
    setupProviderStreamsView();
    setupContent();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    LiveStreamActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
  void startCameraPreview() {
    liveStreamThumbnail.setVisibility(View.GONE);
    liveStreamCameraView.setVisibility(View.VISIBLE);
    new Handler().post(() -> {
      rtmpCamera = new RtmpCamera(liveStreamCameraView, this);
      rtmpCamera.setupMuxers(liveStream.getProviders().size());
      rtmpCamera.startPreview();
    });
  }

  @OnClick(R.id.start_live_stream_button)
  void onStartLiveStreamButtonClick() {
    rtmpCamera.setReTries(10);
    if (rtmpCamera.prepareAudio() && rtmpCamera.prepareVideo()) {
      StringBuilder urlsBuilder = new StringBuilder();
      for (ProviderStream providerStream : liveStream.getProviders()) {
        urlsBuilder.append(providerStream.getStreamUrl()).append(",");
      }
      rtmpCamera.startStream(urlsBuilder.toString());
      liveStreamViewModel.startLiveStream(liveStream);
    } else {
      // This device can't init encoders, this could be for 2 reasons:
      // The selected encoder doesn't support any configuration provided
      // The device hasn't a H264 or AAC encoder (in this case you can see log error "valid encoder not found")
      AlertUtils.alert(this, "Can't init encoders");
    }
  }

  @OnClick(R.id.end_live_stream_button)
  void onEndLiveStreamButtonClick() {
    if (rtmpCamera.isStreaming()) {
      rtmpCamera.stopStream();
    }
    liveStreamViewModel.endLiveStream(liveStream);
  }

  private void setupLiveStream() {
    liveStream = (LiveStream) getIntent().getSerializableExtra(Constants.LIVE_STREAM);
  }

  private void setupObservers() {
    liveStreamViewModel.getLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      providerStreamsAdapter.setProviderStreams(liveStream.getProviders());
      providerStreamsAdapter.notifyDataSetChanged();
      updateContent();
      updateStreamStatus();
      if (!liveStream.getStatus().equals(StreamStatus.COMPLETE)) {
        LiveStreamActivityPermissionsDispatcher.startCameraPreviewWithPermissionCheck(this);
      }
    });
    liveStreamViewModel.getStartLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      updateStreamStatus();
    });
    liveStreamViewModel.getEndLiveStream().observe(this, liveStream -> {
      this.liveStream = liveStream;
      updateStreamStatus();
      if (!liveStream.getStatus().equals(StreamStatus.COMPLETE)) {
        liveStreamThumbnail.setVisibility(View.VISIBLE);
        liveStreamCameraView.setVisibility(View.GONE);
      }
    });
    liveStreamViewModel.getError().observe(this, throwable -> AlertUtils.alert(this, throwable));
  }

  private void setupViews() {
    liveStreamCameraView.getHolder().addCallback(this);
    startLiveStreamButton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_video));
    endLiveStreamButton.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_stop));
  }

  private void setupProviderStreamsView() {
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    SpaceItemDecoration itemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.margin_sm), layoutManager.getOrientation());
    providerStreamsView.setLayoutManager(layoutManager);
    providerStreamsView.addItemDecoration(itemDecoration);
    providerStreamsView.setAdapter(providerStreamsAdapter);
    RecyclerView.ItemAnimator itemAnimator = providerStreamsView.getItemAnimator();
    if (itemAnimator instanceof SimpleItemAnimator) {
      ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
    }
  }

  private void setupContent() {
    updateContent();
    liveStreamViewModel.loadLiveStream(liveStream.getId());
  }

  private void updateContent() {
    ImageUtils.loadImage(this, liveStreamViewModel.getAuthenticatedUser(), liveStreamThumbnail);
    liveStreamTitle.setText(liveStream.getTitle());
    liveStreamDescription.setText(liveStream.getDescription());
    if (liveStream.getEndDate() != null) {
      liveStreamDate.setText(DateUtils.formatDateRange(this, liveStream.getStartDate().getTime(), liveStream.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
    } else {
      liveStreamDate.setText(DateUtils.formatDateTime(this, liveStream.getStartDate().getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR));
    }
  }

  private void updateStreamStatus() {
    liveStreamStatus.setVisibility(View.VISIBLE);
    liveStreamStatus.setText(AppUtils.getStreamStatusName(this, liveStream.getStatus()));
    int streamStatusColor = 0;
    switch (liveStream.getStatus()) {
      case READY:
        streamStatusColor = ContextCompat.getColor(this, R.color.stream_status_ready);
        break;
      case LIVE:
        streamStatusColor = ContextCompat.getColor(this, R.color.stream_status_live);
        break;
      case COMPLETE:
        streamStatusColor = ContextCompat.getColor(this, R.color.stream_status_complete);
        break;
    }
    UIUtils.setColorFilter(liveStreamStatus.getBackground(), streamStatusColor);

    startLiveStreamButton.setVisibility(liveStream.getStatus().equals(StreamStatus.READY) ? View.VISIBLE : View.GONE);
    endLiveStreamButton.setVisibility(liveStream.getStatus().equals(StreamStatus.LIVE) ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onConnectionSuccessRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "onConnectionSuccessRtmp"));
  }

  @Override
  public void onConnectionFailedRtmp(@NonNull String reason) {
    runOnUiThread(() -> {
      AlertUtils.alert(LiveStreamActivity.this, "onConnectionFailedRtmp " + reason);
      if (rtmpCamera.shouldRetry(reason)) {
        rtmpCamera.reTry(5000, reason);
      } else {
        rtmpCamera.stopStream();
      }
    });
  }

  @Override
  public void onNewBitrateRtmp(long bitrate) {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "onNewBitrateRtmp " + bitrate));
  }

  @Override
  public void onDisconnectRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "onDisconnectRtmp"));
  }

  @Override
  public void onAuthErrorRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "onAuthErrorRtmp"));
  }

  @Override
  public void onAuthSuccessRtmp() {
    runOnUiThread(() -> AlertUtils.alert(LiveStreamActivity.this, "onAuthSuccessRtmp"));
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {

  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    if (rtmpCamera != null) {
      rtmpCamera.startPreview();
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {

  }
}
