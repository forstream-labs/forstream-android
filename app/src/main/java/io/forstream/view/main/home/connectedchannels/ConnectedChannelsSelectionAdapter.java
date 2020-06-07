package io.forstream.view.main.home.connectedchannels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.forstream.R;
import io.forstream.api.enums.ChannelIdentifier;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.util.ImageUtils;

public class ConnectedChannelsSelectionAdapter extends RecyclerView.Adapter<ConnectedChannelsSelectionAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private List<ConnectedChannel> connectedChannels;
  private Set<ChannelIdentifier> enabledChannels = new HashSet<>();

  @Inject
  public ConnectedChannelsSelectionAdapter(Context context) {
    this.context = context;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    this.connectedChannels = connectedChannels;
    enabledChannels = Stream.of(connectedChannels).map(connectedChannel -> connectedChannel.getChannel().getIdentifier()).collect(Collectors.toSet());
  }

  public List<ChannelIdentifier> getEnabledChannels() {
    return new ArrayList<>(enabledChannels);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_connected_channel_selection, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ConnectedChannel connectedChannel = connectedChannels.get(position);
    holder.bindView(connectedChannel);
  }

  @Override
  public int getItemCount() {
    return connectedChannels != null ? connectedChannels.size() : 0;
  }

  public interface Listener {

    void onConnectedChannelCheckedChanged(ConnectedChannel connectedChannel, boolean checked);

  }

  class ViewHolder extends BaseViewHolder<ConnectedChannel> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;
    @BindView(R.id.channel_enabled) SwitchMaterial channelEnabledView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ConnectedChannel connectedChannel) {
      ImageUtils.loadImage(context, connectedChannel.getChannel(), channelImageView);
      channelNameView.setText(connectedChannel.getChannel().getName());
      channelEnabledView.setChecked(true);
    }

    @OnCheckedChanged(R.id.channel_enabled)
    void onChannelEnabledChanged() {
      ConnectedChannel connectedChannel = connectedChannels.get(getAdapterPosition());
      if (channelEnabledView.isChecked()) {
        enabledChannels.add(connectedChannel.getChannel().getIdentifier());
      } else {
        enabledChannels.remove(connectedChannel.getChannel().getIdentifier());
      }
      if (listener != null) {
        listener.onConnectedChannelCheckedChanged(connectedChannel, channelEnabledView.isChecked());
      }
    }
  }
}
