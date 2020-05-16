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

public class ConnectedChannelsAdapter extends RecyclerView.Adapter<ConnectedChannelsAdapter.ViewHolder> {

  public static final int VIEW_STYLE_NONE = 0;
  public static final int VIEW_STYLE_CARD = 1;

  private static final Map<Integer, Integer> LAYOUT_BY_TYPE = new HashMap<>();

  static {
    LAYOUT_BY_TYPE.put(VIEW_STYLE_NONE, R.layout.item_connected_channel);
    LAYOUT_BY_TYPE.put(VIEW_STYLE_CARD, R.layout.item_connected_channel_card);
  }

  private Context context;
  private List<ConnectedChannel> connectedChannels;
  private int viewType = VIEW_STYLE_CARD;
  private boolean showEnabledSwitch;
  private boolean showMenuIcon;

  private Map<ConnectedChannel, Boolean> enableStates = new HashMap<>();

  @Inject
  public ConnectedChannelsAdapter(Context context) {
    this.context = context;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    this.connectedChannels = connectedChannels;
    enableStates.clear();
    for (ConnectedChannel connectedChannel : connectedChannels) {
      enableStates.put(connectedChannel, true);
    }
  }

  public void setViewType(int viewType) {
    this.viewType = viewType;
  }

  public boolean isShowEnabledSwitch() {
    return showEnabledSwitch;
  }

  public void setShowEnabledSwitch(boolean showEnabledSwitch) {
    this.showEnabledSwitch = showEnabledSwitch;
  }

  public boolean isShowMenuIcon() {
    return showMenuIcon;
  }

  public void setShowMenuIcon(boolean showMenuIcon) {
    this.showMenuIcon = showMenuIcon;
  }

  public List<ConnectedChannel> getEnabledConnectedChannels() {
    List<ConnectedChannel> enabledConnectedChannels = new ArrayList<>();
    for (Map.Entry<ConnectedChannel, Boolean> entry : enableStates.entrySet()) {
      if (entry.getValue()) {
        enabledConnectedChannels.add(entry.getKey());
      }
    }
    return enabledConnectedChannels;
  }

  @Override
  public int getItemViewType(int position) {
    return viewType;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(LAYOUT_BY_TYPE.get(viewType), parent, false);
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
      channelEnabledView.setVisibility(showEnabledSwitch ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.channel_enabled)
    void onChannelEnabledChanged() {
      ConnectedChannel connectedChannel = connectedChannels.get(getAdapterPosition());
      enableStates.put(connectedChannel, channelEnabledView.isChecked());
    }
  }
}
