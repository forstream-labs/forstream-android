package io.livestream.view.main.channels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.api.model.Channel;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.util.ImageUtils;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private List<Channel> channels;

  @Inject
  public ChannelsAdapter(Context context) {
    this.context = context;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setChannels(List<Channel> channels) {
    this.channels = channels;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Channel channel = channels.get(position);
    holder.bindView(channel);
  }

  @Override
  public int getItemCount() {
    return channels != null ? channels.size() : 0;
  }

  public interface Listener {

    void onChannelClick(Channel channel);

  }

  class ViewHolder extends BaseViewHolder<Channel> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(Channel channel) {
      channelNameView.setText(channel.getName());
      ImageUtils.loadImage(context, channel, channelImageView);
    }

    @OnClick(R.id.channel_view)
    void onChannelViewClick() {
      if (listener != null) {
        listener.onChannelClick(channels.get(getAdapterPosition()));
      }
    }
  }
}
