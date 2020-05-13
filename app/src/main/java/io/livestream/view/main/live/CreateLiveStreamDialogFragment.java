package io.livestream.view.main.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.livestream.R;

public class CreateLiveStreamDialogFragment extends BottomSheetDialogFragment {

  @BindView(R.id.live_stream_title_layout) TextInputLayout liveStreamTitleLayout;
  @BindView(R.id.live_stream_title_input) TextInputEditText liveStreamTitleInput;
  @BindView(R.id.live_stream_description_input) TextInputEditText liveStreamDescriptionInput;
  @BindView(R.id.create_live_stream_button) Button createLiveStreamButton;

  private Listener clickListener;
  private Listener listener;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_create_live_stream, container, false);
    ButterKnife.bind(this, view);

    setupViews();
    return view;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @OnTextChanged(R.id.live_stream_title_input)
  void onLiveStreamTitleInputChanged() {
    liveStreamTitleLayout.setError(liveStreamTitleInput.getText().toString().isEmpty() ? " " : null);
    updateCreateLiveStreamButtonState();
  }

  @OnClick(R.id.create_live_stream_button)
  void onCreateLiveStreamButtonClick() {
    if (listener != null) {
      listener.onCreateLiveStreamButtonClick(liveStreamTitleInput.getText().toString(), liveStreamDescriptionInput.getText().toString());
    }
  }

  private void setupViews() {
    onLiveStreamTitleInputChanged();
    updateCreateLiveStreamButtonState();
  }

  private void updateCreateLiveStreamButtonState() {
    createLiveStreamButton.setEnabled(!liveStreamTitleInput.getText().toString().isEmpty());
  }

  public interface Listener {

    void onCreateLiveStreamButtonClick(String title, String description);

  }
}
