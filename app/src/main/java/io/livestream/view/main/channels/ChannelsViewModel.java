package io.livestream.view.main.channels;

import androidx.lifecycle.LiveData;

import io.livestream.api.model.Channel;
import io.livestream.api.service.ChannelService;
import io.livestream.common.livedata.list.ListHolder;
import io.livestream.common.livedata.list.ListLiveData;
import io.livestream.common.viewmodel.BaseViewModel;
import timber.log.Timber;

public class ChannelsViewModel extends BaseViewModel {

  private ChannelService channelService;

  private ListLiveData<Channel> channels = new ListLiveData<>();

  public ChannelsViewModel(ChannelService channelService) {
    this.channelService = channelService;
  }

  public LiveData<ListHolder<Channel>> getChannels() {
    return channels;
  }

  public void loadChannels() {
    channelService.listChannels().then(channels -> {
      this.channels.postValue(channels);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error loading channels");
      error.postValue(reason);
      return null;
    }));
  }

  public void loadConnectedChannels() {

  }

  public void connectYouTubeChannel(String authCode) {
    channelService.connectYouTubeChannel(authCode).then(connectedChannel -> null)._catch((reason -> {
      Timber.e(reason, "Error connecting YouTube channel");
      error.postValue(reason);
      return null;
    }));
  }

  public void connectFacebookChannel(String accessToken) {
    channelService.connectFacebookChannel(accessToken).then(connectedChannel -> null)._catch((reason -> {
      Timber.e(reason, "Error connecting Facebook channel");
      error.postValue(reason);
      return null;
    }));
  }
}
