package io.livestream.view.main;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.livestream.R;
import io.livestream.api.model.User;
import io.livestream.common.BaseActivity;
import io.livestream.util.AlertUtils;
import io.livestream.util.ImageUtils;
import io.livestream.view.intro.IntroActivity;
import io.livestream.view.profile.ProfileActivity;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

  private static final String GOOGLE_PLAY_URI = "https://play.google.com/store/apps/details?id=%s";
  private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
  @BindView(R.id.navigation_view) NavigationView navigationView;
  @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
  @BindView(R.id.view_pager) ViewPager viewPager;

  @Inject MainViewModel mainViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setupToolbar();
    setupDrawer();
    setupNavigation();
    setupObservers();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()) {
      case R.id.profile:
        onProfileClick();
        break;
      case R.id.share:
        onShareClick();
        break;
      case R.id.rate_us:
        onRateUsClick();
        break;
      case R.id.log_out:
        onLogOutClick();
        break;
    }
    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    updateToolbarTitle(0);
  }

  private void setupDrawer() {
    ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_open, R.string.menu_close);
    drawerLayout.addDrawerListener(drawerToggle);
    drawerToggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);

    User user = mainViewModel.getAuthenticatedUser();

    // Drawer header
    View headerView = navigationView.getHeaderView(0);
    TextView nameView = headerView.findViewById(R.id.user_name);
    nameView.setText(user.getFullName());
    ImageView imageView = headerView.findViewById(R.id.user_image);
    ImageUtils.loadImage(this, user, imageView);

    // Menu icons
    Menu menu = navigationView.getMenu();
    menu.findItem(R.id.profile).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_user_alt).size(IconicsSize.res(R.dimen.icon_menu)));
    menu.findItem(R.id.share).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_share_alt).size(IconicsSize.res(R.dimen.icon_menu)));
    menu.findItem(R.id.rate_us).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_star1).size(IconicsSize.res(R.dimen.icon_menu)));
    menu.findItem(R.id.log_out).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_sign_out_alt).size(IconicsSize.res(R.dimen.icon_menu)));
  }

  private void setupNavigation() {
    MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this);
    viewPager.setAdapter(mainPagerAdapter);
    viewPager.setOffscreenPageLimit(mainPagerAdapter.getCount());

    int index = 0;
    bottomNavigationView.getMenu().getItem(index++).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_video).size(IconicsSize.res(R.dimen.icon_tab)));
    bottomNavigationView.getMenu().getItem(index++).setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_layer_group).size(IconicsSize.res(R.dimen.icon_tab)));

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        int menuId = 0;
        switch (position) {
          case 0:
            menuId = R.id.live;
            break;
          case 1:
            menuId = R.id.channels;
            break;
        }
        bottomNavigationView.setSelectedItemId(menuId);
        updateToolbarTitle(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      Menu bottomNavigationMenu = bottomNavigationView.getMenu();
      for (int i = 0; i < bottomNavigationMenu.size(); i++) {
        MenuItem menuItem = bottomNavigationMenu.getItem(i);
        menuItem.setChecked(menuItem.getItemId() == item.getItemId());
      }
      int position = 0;
      switch (item.getItemId()) {
        case R.id.live:
          position = 0;
          break;
        case R.id.channels:
          position = 1;
          break;
      }
      viewPager.setCurrentItem(position);
      updateToolbarTitle(position);
      return true;
    });
  }

  private void setupObservers() {
    mainViewModel.getSignOut().observe(this, user -> {
      Intent intent = new Intent(this, IntroActivity.class);
      startActivity(intent);
      finish();
    });
    mainViewModel.getError().observe(this, throwable -> AlertUtils.alert(this, throwable));
  }

  private void updateToolbarTitle(int position) {
    MenuItem menuItem = bottomNavigationView.getMenu().getItem(position);
    setTitle(menuItem.getTitle());
  }

  private void onProfileClick() {
    Intent intent = new Intent(this, ProfileActivity.class);
    startActivity(intent);
  }

  private void onShareClick() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.activity_main_share_message, String.format(GOOGLE_PLAY_URI, getPackageName())));
    startActivity(Intent.createChooser(intent, getString(R.string.activity_main_share_title)));
  }

  private void onRateUsClick() {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(GOOGLE_PLAY_URI, getPackageName())));
    PackageManager packageManager = getPackageManager();
    List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
    for (ApplicationInfo installedApplication : installedApplications) {
      if (GOOGLE_PLAY_PACKAGE_NAME.equals(installedApplication.packageName)) {
        intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
      }
    }
    startActivity(intent);
  }

  private void onLogOutClick() {
    mainViewModel.signOut();
  }
}
