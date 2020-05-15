package io.livestream.view.livestream;

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
import butterknife.OnCheckedChanged;
import io.livestream.R;
import io.livestream.api.model.ProviderStream;
import io.livestream.common.adapter.base.BaseViewHolder;
import io.livestream.util.ImageUtils;

public class ProviderStreamsAdapter extends RecyclerView.Adapter<ProviderStreamsAdapter.ViewHolder> {

  private Context context;
  private List<ProviderStream> providerStreams;

  @Inject
  public ProviderStreamsAdapter(Context context) {
    this.context = context;
  }

  public void setProviderStreams(List<ProviderStream> providerStreams) {
    this.providerStreams = providerStreams;
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

  class ViewHolder extends BaseViewHolder<ProviderStream> {

    @BindView(R.id.channel_image) ImageView channelImageView;
    @BindView(R.id.channel_name) TextView channelNameView;
    @BindView(R.id.channel_enabled) SwitchMaterial channelEnabledView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ProviderStream providerStream) {
      channelNameView.setText(providerStream.getConnectedChannel().getChannel().getName());
      channelEnabledView.setChecked(providerStream.getEnabled());
      ImageUtils.loadImage(context, providerStream.getConnectedChannel().getChannel(), channelImageView);
    }

    @OnCheckedChanged(R.id.channel_enabled)
    void onChannelEnabledChanged() {
      ProviderStream providerStream = providerStreams.get(getAdapterPosition());
      providerStream.setEnabled(channelEnabledView.isChecked());
    }
  }
}
