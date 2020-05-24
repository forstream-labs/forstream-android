package io.livestream.view.main.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.livestream.R;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.util.ImageUtils;

public class ConnectedChannelsSelectionAdapter extends RecyclerView.Adapter<ConnectedChannelsSelectionAdapter.ViewHolder> {

  private Context context;
  private List<ConnectedChannel> connectedChannels;

  private Map<ConnectedChannel, Boolean> enableStates = new HashMap<>();

  @Inject
  public ConnectedChannelsSelectionAdapter(Context context) {
    this.context = context;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    this.connectedChannels = connectedChannels;
    enableStates.clear();
    for (ConnectedChannel connectedChannel : connectedChannels) {
      enableStates.put(connectedChannel, true);
    }
  }

  public List<ConnectedChannel> getEnabledChannels() {
    List<ConnectedChannel> enabledConnectedChannels = new ArrayList<>();
    for (Map.Entry<ConnectedChannel, Boolean> entry : enableStates.entrySet()) {
      if (entry.getValue()) {
        enabledConnectedChannels.add(entry.getKey());
      }
    }
    return enabledConnectedChannels;
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
      enableStates.put(connectedChannel, channelEnabledView.isChecked());
    }
  }
}
