package io.livestream.view.main.channels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import io.livestream.util.UIUtils;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private List<ChannelsViewModel.ViewItem> viewItems;

  @Inject
  public ChannelsAdapter(Context context) {
    this.context = context;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setViewItems(List<ChannelsViewModel.ViewItem> viewItems) {
    this.viewItems = viewItems;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ChannelsViewModel.ViewItem viewItem = viewItems.get(position);
    holder.bindView(viewItem);
  }

  @Override
  public int getItemCount() {
    return viewItems != null ? viewItems.size() : 0;
  }

  public interface Listener {

    void onChannelClick(Channel channel);

  }

  class ViewHolder extends BaseViewHolder<ChannelsViewModel.ViewItem> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;
    @BindView(R.id.channel_connected) TextView channelConnectedView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ChannelsViewModel.ViewItem viewItem) {
      Channel channel = viewItem.getChannel();
      channelNameView.setText(channel.getName());
      ImageUtils.loadImage(context, channel, channelImageView);
      if (viewItem.isConnected()) {
        UIUtils.setColorFilter(channelConnectedView.getBackground(), ContextCompat.getColor(context, R.color.channel_connected));
        channelConnectedView.setVisibility(View.VISIBLE);
      } else {
        channelConnectedView.setVisibility(View.GONE);
      }
    }

    @OnClick(R.id.channel_view)
    void onChannelViewClick() {
      if (listener != null) {
        listener.onChannelClick(viewItems.get(getAdapterPosition()).getChannel());
      }
    }
  }
}
