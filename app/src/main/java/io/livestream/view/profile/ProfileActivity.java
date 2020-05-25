package io.livestream.view.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.kroegerama.imgpicker.BottomSheetImagePicker;
import com.kroegerama.imgpicker.ButtonType;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.livestream.R;
import io.livestream.common.BaseActivity;
import io.livestream.util.AlertUtils;
import io.livestream.util.FileUtils;
import io.livestream.util.ImageUtils;
import io.livestream.util.UIUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ProfileActivity extends BaseActivity implements BottomSheetImagePicker.OnImagesSelectedListener {

  @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;

  @BindView(R.id.select_user_image_layout) View selectUserImageLayout;
  @BindView(R.id.user_image_layout) View userImageLayout;

  @BindView(R.id.user_image) ImageView userImageView;

  @BindView(R.id.user_first_name_layout) View userFirstNameLayout;
  @BindView(R.id.user_first_name) TextView userFirstNameView;
  @BindView(R.id.user_first_name_edit_layout) View userFirstNameEditLayout;
  @BindView(R.id.user_first_name_input) TextInputEditText userFirstNameInput;
  @BindView(R.id.save_user_first_name_icon) ImageView saveUserFirstNameIconView;
  @BindView(R.id.cancel_user_first_name_icon) ImageView cancelUserFirstNameIconView;

  @BindView(R.id.user_last_name_layout) View userLastNameLayout;
  @BindView(R.id.user_last_name) TextView userLastNameView;
  @BindView(R.id.user_last_name_edit_layout) View userLastNameEditLayout;
  @BindView(R.id.user_last_name_input) TextInputEditText userLastNameInput;
  @BindView(R.id.save_user_last_name_icon) ImageView saveUserLastNameIconView;
  @BindView(R.id.cancel_user_last_name_icon) ImageView cancelUserLastNameIconView;

  @Inject ProfileViewModel profileViewModel;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);

    setupToolbar();
    setupObservers();
    setupViews();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
      try {
        selectUserImageLayout.setVisibility(View.GONE);
        userImageLayout.setVisibility(View.VISIBLE);
        Uri selectedImageUri = FileUtils.getFileUri(this, UCrop.getOutput(data));
        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
        userImageView.setImageDrawable(Drawable.createFromStream(inputStream, selectedImageUri.toString()));
        profileViewModel.updateImage(selectedImageUri);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    ProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  @Override
  public void onImagesSelected(@NotNull List<? extends Uri> uris, @org.jetbrains.annotations.Nullable String tag) {
    if (!uris.isEmpty()) {
      try {
        File outputFile = File.createTempFile("user_image_", null, getCacheDir());
        UCrop.Options options = new UCrop.Options();
        options.setToolbarWidgetColor(ContextCompat.getColor(this, android.R.color.white));
        options.setToolbarColor(ContextCompat.getColor(this, R.color.primary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark));
        options.setToolbarTitle(getString(R.string.activity_profile_edit_photo));
        UCrop uCrop = UCrop.of(uris.get(0), Uri.fromFile(outputFile))
          .withAspectRatio(1, 1)
          .withMaxResultSize(320, 320)
          .withOptions(options);
        uCrop.start(this);
      } catch (IOException e) {
        AlertUtils.alert(this, R.string.activity_profile_image_not_found);
      }
    }
  }

  @OnClick({R.id.select_user_image_layout, R.id.user_image_layout})
  public void onUserImageClick() {
    ProfileActivityPermissionsDispatcher.selectUserImageWithPermissionCheck(this);
  }

  @SuppressLint("MissingPermission")
  @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
  public void selectUserImage() {
    new BottomSheetImagePicker.Builder(getString(R.string.app_file_provider))
      .cameraButton(ButtonType.Button)
      .galleryButton(ButtonType.Button)
      .singleSelectTitle(R.string.activity_profile_select_image)
      .peekHeight(R.dimen.image_picker_peek_height)
      .columnSize(R.dimen.image_picker_column_size)
      .requestTag("single")
      .show(getSupportFragmentManager(), null);
  }

  @OnClick(R.id.edit_first_name_button)
  public void onEditFirstNameButtonClick() {
    userFirstNameLayout.setVisibility(View.GONE);
    userFirstNameEditLayout.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.cancel_user_first_name_icon)
  public void onCancelFirstNameIconViewClick() {
    userFirstNameEditLayout.setVisibility(View.GONE);
    userFirstNameLayout.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.save_user_first_name_icon)
  public void onSaveFirstNameIconViewClick() {
    UIUtils.hideKeyboard(this);
    profileViewModel.updateFirstName(userFirstNameInput.getText().toString());
    onCancelFirstNameIconViewClick();
  }

  @OnClick(R.id.edit_user_last_name_button)
  public void onEditLastNameButtonClick() {
    userLastNameLayout.setVisibility(View.GONE);
    userLastNameEditLayout.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.cancel_user_last_name_icon)
  public void onCancelLastNameIconViewClick() {
    userLastNameEditLayout.setVisibility(View.GONE);
    userLastNameLayout.setVisibility(View.VISIBLE);
  }

  @OnClick(R.id.save_user_last_name_icon)
  public void onSaveLastNameIconViewClick() {
    UIUtils.hideKeyboard(this);
    profileViewModel.updateLastName(userLastNameInput.getText().toString());
    onCancelLastNameIconViewClick();
  }

  private void setupToolbar() {
    UIUtils.defaultToolbar(this);
    collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
  }

  private void setupObservers() {
    profileViewModel.getUser().observe(this, user -> {
      userImageLayout.setVisibility(user.getImageUrl() != null ? View.VISIBLE : View.GONE);
      selectUserImageLayout.setVisibility(user.getImageUrl() == null ? View.VISIBLE : View.GONE);
      if (user.getImageUrl() != null) {
        ImageUtils.loadImage(this, user, userImageView);
      }
      collapsingToolbarLayout.setTitle(user.getFullName());
      userFirstNameView.setText(user.getFirstName());
      userFirstNameInput.setText(user.getFirstName());
      userLastNameView.setText(user.getLastName());
      userLastNameInput.setText(user.getLastName());
    });
    profileViewModel.getError().observe(this, throwable -> AlertUtils.alert(this, throwable));
  }

  private void setupViews() {
    profileViewModel.loadUser();
  }
}
