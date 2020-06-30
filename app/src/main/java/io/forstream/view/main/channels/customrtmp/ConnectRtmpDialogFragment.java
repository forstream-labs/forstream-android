package io.forstream.view.main.channels.customrtmp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.forstream.R;
import io.forstream.api.model.Channel;
import io.forstream.util.ImageUtils;

public class ConnectRtmpDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.channel_image) ImageView channelImageView;
  @BindView(R.id.channel_name) TextView channelNameView;

  @BindView(R.id.rtmp_channel_name_layout) TextInputLayout channelNameLayout;
  @BindView(R.id.rtmp_channel_name_input) EditText channelNameInput;
  @BindView(R.id.rtmp_url_layout) TextInputLayout rtmpUrlLayout;
  @BindView(R.id.rtmp_url_input) EditText rtmpUrlInput;
  @BindView(R.id.rtmp_stream_key_layout) TextInputLayout rtmpStreamKeyLayout;
  @BindView(R.id.rtmp_stream_key_input) EditText streamKeyInput;
  @BindView(R.id.connect_target_button) Button connectTargetButton;

  private Context context;
  private Listener listener;
  private Channel channel;

  public ConnectRtmpDialogFragment(Context context) {
    this.context = context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_connect_rtmp_channel, container, false);
    ButterKnife.bind(this, view);

    setupViews();
    return view;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  @OnTextChanged({R.id.rtmp_channel_name_input, R.id.rtmp_url_input, R.id.rtmp_stream_key_input})
  void onInputChanged() {
    boolean emptyChannelName = channelNameInput.getText().toString().isEmpty();
    boolean emptyRtmpUrl = rtmpUrlInput.getText().toString().isEmpty();
    boolean emptyStreamKey = streamKeyInput.getText().toString().isEmpty();

    channelNameLayout.setError(emptyChannelName ? " " : null);
    rtmpUrlLayout.setError(emptyRtmpUrl ? " " : null);
    rtmpStreamKeyLayout.setError(emptyStreamKey ? " " : null);

    connectTargetButton.setEnabled(!emptyChannelName && !emptyRtmpUrl && !emptyStreamKey);
  }

  @OnClick(R.id.connect_target_button)
  void onConnectTargetButtonClick() {
    if (listener != null) {
      listener.onConnectRtmpChannelButtonClick(channelNameInput.getText().toString(), rtmpUrlInput.getText().toString(), streamKeyInput.getText().toString());
    }
  }

  private void setupViews() {
    channelNameView.setText(channel.getName());
    ImageUtils.loadImage(context, channel, channelImageView);
    onInputChanged();
  }

  public interface Listener {

    void onConnectRtmpChannelButtonClick(String channelName, String rtmpUrl, String streamKey);

  }
}