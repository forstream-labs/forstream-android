package io.livestream.view.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.livestream.api.enums.ChannelIdentifier;
import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.api.service.ChannelService;
import io.livestream.api.service.StreamService;
import io.livestream.api.service.UserService;
import io.livestream.common.livedata.list.ListHolder;
import io.livestream.common.livedata.list.ListLiveData;
import io.livestream.common.viewmodel.BaseViewModel;
import timber.log.Timber;

public class HomeViewModel extends BaseViewModel {

  private static final String CONNECTED_CHANNELS_POPULATE = "channel";
  private static final String LIVE_STREAM_POPULATE = "providers->connected_channel.channel";

  private UserService userService;
  private ChannelService channelService;
  private StreamService streamService;

  private ListLiveData<ConnectedChannel> connectedChannels = new ListLiveData<>();
  private ListLiveData<LiveStream> liveStreams = new ListLiveData<>();
  private MutableLiveData<LiveStream> createLiveStream = new MutableLiveData<>();

  public HomeViewModel(UserService userService, ChannelService channelService, StreamService streamService) {
    this.userService = userService;
    this.channelService = channelService;
    this.streamService = streamService;
  }

  public LiveData<ListHolder<ConnectedChannel>> getConnectedChannels() {
    return connectedChannels;
  }

  public LiveData<ListHolder<LiveStream>> getLiveStreams() {
    return liveStreams;
  }

  public LiveData<LiveStream> getCreateLiveStream() {
    return createLiveStream;
  }

  public void loadConnectedChannels() {
    userService.listMyConnectedChannels(CONNECTED_CHANNELS_POPULATE).then(connectedChannels -> {
      this.connectedChannels.postValue(connectedChannels);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading connected channels");
      error.postValue(reason);
      return null;
    });
  }

  public void loadLiveStreams() {
    userService.listMyLiveStreams(LIVE_STREAM_POPULATE).then(liveStreams -> {
      this.liveStreams.postValue(liveStreams);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading live streams");
      error.postValue(reason);
      return null;
    });
  }

  public void createLiveStream(String title, String description, List<ChannelIdentifier> channelsIdentifiers) {
    streamService.createLiveStream(title, description, channelsIdentifiers).then(liveStream -> {
      liveStreams.add(0, liveStream);
      createLiveStream.postValue(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error creating live stream");
      error.postValue(reason);
      return null;
    }));
  }

  public void removeLiveStream(LiveStream liveStream) {
    streamService.removeLiveStream(liveStream).then(aVoid -> {
      liveStreams.remove(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error removing live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }

  public void disconnectChannel(ConnectedChannel connectedChannel) {
    channelService.disconnectChannel(connectedChannel.getChannel()).then(liveStream -> {
      connectedChannels.remove(connectedChannel);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error disconnecting channel %s", connectedChannel.getChannel().getIdentifier());
      error.postValue(reason);
      return null;
    }));
  }
}
