package io.forstream.view.livestream;

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
import io.forstream.R;
import io.forstream.api.model.ProviderStream;
import io.forstream.common.adapter.base.BaseViewHolder;
import io.forstream.util.ImageUtils;

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

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

    @Override
    public void bindView(ProviderStream providerStream) {
      ImageUtils.loadImage(context, providerStream.getChannel(), channelImageView);
      channelNameView.setText(providerStream.getChannel().getName());
    }
  }
}
