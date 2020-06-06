package io.forstream.view.main.channels.facebook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.model.Channel;
import io.forstream.api.model.ChannelTarget;
import io.forstream.util.ImageUtils;

public class ChannelTargetDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.channel_image) ImageView channelImageView;
  @BindView(R.id.channel_name) TextView channelNameView;
  @BindView(R.id.channel_target_input) AutoCompleteTextView channelTargetInput;

  private Context context;
  private Listener listener;
  private Channel channel;
  private List<ChannelTarget> channelTargets;
  private ChannelTarget selectedChannelTarget;

  @Inject
  public ChannelTargetDialogFragment(Context context) {
    this.context = context;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_channel_target, container, false);
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

  public void setChannelTargets(List<ChannelTarget> channelTargets) {
    this.channelTargets = channelTargets;
  }

  @OnClick(R.id.connect_target_button)
  void onConnectTargetButtonClick() {
    if (listener != null) {
      listener.onConnectButtonClick(selectedChannelTarget);
    }
  }

  private void setupViews() {
    selectedChannelTarget = channelTargets.get(0);

    channelNameView.setText(channel.getName());
    ImageUtils.loadImage(context, channel, channelImageView);

    List<String> targetsNames = Stream.of(channelTargets).map(ChannelTarget::getName).collect(Collectors.toList());
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_menu_popup_item, targetsNames);
    channelTargetInput.setAdapter(adapter);
    channelTargetInput.setKeyListener(null);
    channelTargetInput.setOnItemClickListener((parent, view, position, id) -> selectedChannelTarget = channelTargets.get(position));
    channelTargetInput.setText(targetsNames.get(0), false);
  }

  public interface Listener {

    void onConnectButtonClick(ChannelTarget channelTarget);

  }
}