package io.livestream.view.main.live;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.livestream.api.model.LiveStream;
import io.livestream.api.service.StreamService;
import io.livestream.common.viewmodel.BaseViewModel;
import timber.log.Timber;

public class LiveViewModel extends BaseViewModel {

  private StreamService streamService;

  private MutableLiveData<LiveStream> createLiveStream = new MutableLiveData<>();
  private MutableLiveData<LiveStream> startLiveStream = new MutableLiveData<>();

  public LiveViewModel(StreamService streamService) {
    this.streamService = streamService;
  }

  public LiveData<LiveStream> getCreateLiveStream() {
    return createLiveStream;
  }

  public LiveData<LiveStream> getStartLiveStream() {
    return startLiveStream;
  }

  public void createLiveStream() {
    streamService.createLiveStream().then(liveStream -> {
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
