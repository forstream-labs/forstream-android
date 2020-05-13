package io.livestream.view.main.live;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.ConnectedChannel;
import io.livestream.api.model.LiveStream;
import io.livestream.api.service.StreamService;
import io.livestream.api.service.UserService;
import io.livestream.common.livedata.list.ListHolder;
import io.livestream.common.livedata.list.ListLiveData;
import io.livestream.common.viewmodel.BaseViewModel;
import timber.log.Timber;

public class LiveViewModel extends BaseViewModel {

  private static final String CONNECTED_CHANNELS_POPULATE = "channel";

  private UserService userService;
  private StreamService streamService;

  private ListLiveData<ConnectedChannel> connectedChannels = new ListLiveData<>();
  private ListLiveData<LiveStream> liveStreams = new ListLiveData<>();
  private MutableLiveData<LiveStream> createLiveStream = new MutableLiveData<>();
  private MutableLiveData<LiveStream> startLiveStream = new MutableLiveData<>();

  public LiveViewModel(UserService userService, StreamService streamService) {
    this.userService = userService;
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

  public LiveData<LiveStream> getStartLiveStream() {
    return startLiveStream;
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
    userService.listMyLiveStreams().then(liveStreams -> {
      this.liveStreams.postValue(liveStreams);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading live streams");
      error.postValue(reason);
      return null;
    });
  }

  public void createLiveStream(String title, String description) {
    streamService.createLiveStream(title, description).then(liveStream -> {
      createLiveStream.postValue(liveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error creating live stream");
      error.postValue(reason);
      return null;
    }));
  }

  public void startLiveStream(LiveStream liveStream) {
    streamService.startLiveStream(liveStream).then(updatedLiveStream -> {
      startLiveStream.postValue(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error starting live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }
}
