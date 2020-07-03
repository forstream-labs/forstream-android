package io.forstream.view.main.home.connectedchannels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.forstream.R;
import io.forstream.api.model.ConnectedChannel;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.util.ImageUtils;

public class ConnectedChannelsAdapter extends RecyclerView.Adapter<ConnectedChannelsAdapter.ViewHolder> {

  private Context context;
  private Listener listener;
  private List<ConnectedChannel> connectedChannels;

  @Inject
  public ConnectedChannelsAdapter(Context context) {
    this.context = context;
  }

  public void setConnectedChannels(List<ConnectedChannel> connectedChannels) {
    this.connectedChannels = connectedChannels;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
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

  public interface Listener {

    void onRemoveConnectedChannelClick(ConnectedChannel connectedChannel);

  }

  class ViewHolder extends BaseViewHolder<ConnectedChannel> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;
    @BindView(R.id.channel_target) TextView channelTargetView;
    @BindView(R.id.channel_menu) View channelMenuView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ConnectedChannel connectedChannel) {
      ImageUtils.loadImage(context, connectedChannel.getChannel(), channelImageView);
      channelNameView.setText(connectedChannel.getChannel().getName());
      channelTargetView.setText(connectedChannel.getTarget().getName());
    }

    @OnClick(R.id.channel_menu)
    void onChannelMenuClick() {
      ConnectedChannel connectedChannel = connectedChannels.get(getAdapterPosition());
      PopupMenu popup = new PopupMenu(context, channelMenuView);
      popup.inflate(R.menu.menu_connected_channel);
      popup.getMenu().findItem(R.id.visit_channel).setVisible(connectedChannel.getTarget().getUrl() != null);
      popup.setOnMenuItemClickListener(item -> {
        switch (item.getItemId()) {
          case R.id.visit_channel:
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(connectedChannel.getTarget().getUrl()));
            context.startActivity(intent);
            return true;
          case R.id.remove_connected_channel:
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context)
              .setTitle(R.string.activity_main_dialog_remove_connected_channel_message)
              .setPositiveButton(R.string.remove, (dialog, which) -> listener.onRemoveConnectedChannelClick(connectedChannel))
              .setNegativeButton(R.string.cancel, null);
            dialogBuilder.create().show();
            return true;
          default:
            return false;
        }
      });
      popup.show();
    }
  }
}
