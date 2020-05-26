package io.livestream.view.livestream;

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
import io.livestream.R;
import io.livestream.api.model.ProviderStream;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.util.ImageUtils;

public class ProviderStreamsAdapter extends RecyclerView.Adapter<ProviderStreamsAdapter.ViewHolder> {

  private Context context;
  private List<ProviderStream> providerStreams;
  private Listener listener;
  private boolean stateSwitchEnabled;

  @Inject
  public ProviderStreamsAdapter(Context context) {
    this.context = context;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  public void setProviderStreams(List<ProviderStream> providerStreams) {
    this.providerStreams = providerStreams;
  }

  public void setStateSwitchEnabled(boolean stateSwitchEnabled) {
    this.stateSwitchEnabled = stateSwitchEnabled;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.item_provider_stream, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ProviderStream providerStream = providerStreams.get(position);
    holder.bindView(providerStream);
  }

  @Override
  public int getItemCount() {
    return providerStreams != null ? providerStreams.size() : 0;
  }

  public interface Listener {

    void onProviderStreamEnabledChanged(ProviderStream providerStream);

  }

  class ViewHolder extends BaseViewHolder<ProviderStream> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;
    //    @BindView(R.id.channel_enabled) SwitchMaterial channelEnabledView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ProviderStream providerStream) {
      ImageUtils.loadImage(context, providerStream.getChannel(), channelImageView);
      channelNameView.setText(providerStream.getChannel().getName());
      //      channelEnabledView.setChecked(providerStream.getEnabled());
      //      channelEnabledView.setEnabled(stateSwitchEnabled);
    }

    //    @OnCheckedChanged(R.id.channel_enabled)
    //    void onChannelEnabledChanged() {
    //      ProviderStream providerStream = providerStreams.get(getAdapterPosition());
    //      boolean stateChanged = providerStream.getEnabled() != channelEnabledView.isChecked();
    //      providerStream.setEnabled(channelEnabledView.isChecked());
    //      if (listener != null && stateChanged) {
    //        listener.onProviderStreamEnabledChanged(providerStream);
    //      }
    //    }
  }
}
