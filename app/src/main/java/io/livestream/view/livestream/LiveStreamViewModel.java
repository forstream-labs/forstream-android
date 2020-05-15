package io.livestream.view.livestream;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.LiveStream;
import io.livestream.api.model.User;
import io.livestream.api.service.StreamService;
import io.livestream.common.viewmodel.BaseViewModel;
import io.livestream.service.AuthenticatedUser;
import timber.log.Timber;

public class LiveStreamViewModel extends BaseViewModel {

  private static final String LIVE_STREAM_POPULATE = "providers->connected_channel.channel";

  private AuthenticatedUser authenticatedUser;
  private StreamService streamService;

  private MutableLiveData<LiveStream> liveStream = new MutableLiveData<>();
  private MutableLiveData<LiveStream> startLiveStream = new MutableLiveData<>();
  private MutableLiveData<LiveStream> endLiveStream = new MutableLiveData<>();

  public LiveStreamViewModel(AuthenticatedUser authenticatedUser, StreamService streamService) {
    this.authenticatedUser = authenticatedUser;
    this.streamService = streamService;
  }

  public LiveData<LiveStream> getLiveStream() {
    return liveStream;
  }

  public LiveData<LiveStream> getStartLiveStream() {
    return startLiveStream;
  }

  public LiveData<LiveStream> getEndLiveStream() {
    return endLiveStream;
  }

  public User getAuthenticatedUser() {
    return authenticatedUser.get();
  }

  public void loadLiveStream(String id) {
    streamService.getLiveStream(id, LIVE_STREAM_POPULATE).then(liveStream -> {
      this.liveStream.postValue(liveStream);
      return null;
    })._catch(reason -> {
      Timber.e(reason, "Error loading live stream");
      return null;
    });
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

  public void endLiveStream(LiveStream liveStream) {
    streamService.endLiveStream(liveStream).then(updatedLiveStream -> {
      endLiveStream.postValue(updatedLiveStream);
      return null;
    })._catch((reason -> {
      Timber.e(reason, "Error ending live stream %s", liveStream.getId());
      error.postValue(reason);
      return null;
    }));
  }
}