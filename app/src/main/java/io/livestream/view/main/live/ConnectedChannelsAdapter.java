package io.livestream.view.main.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.util.ImageUtils;

public class ConnectedChannelsAdapter extends RecyclerView.Adapter<ConnectedChannelsAdapter.ViewHolder> {

  private Context context;
  private AdapterListener adapterListener;
  private List<ConnectedChannel> connectedChannels;

  @Inject
  public ConnectedChannelsAdapter(Context context) {
    this.context = context;
  }

  public void setAdapterListener(AdapterListener adapterListener) {
    this.adapterListener = adapterListener;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    this.connectedChannels = connectedChannels;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_connected_channel, parent, false);
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

  public interface AdapterListener {

    void onConnectedChannelClick(ConnectedChannel connectedChannel);

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
      channelNameView.setText(connectedChannel.getChannel().getName());
      channelEnabledView.setChecked(connectedChannel.getEnabled());
      ImageUtils.loadImage(context, connectedChannel.getChannel(), channelImageView);
    }

    @OnClick(R.id.connected_channel_view)
    void onConnectedChannelViewClick() {
      if (adapterListener != null) {
        adapterListener.onConnectedChannelClick(connectedChannels.get(getAdapterPosition()));
      }
    }
  }
}
