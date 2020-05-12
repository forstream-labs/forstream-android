package io.livestream.view.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.livestream.view.main.channels.ChannelsFragment;
import io.livestream.view.main.live.LiveFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

  private List<Fragment> fragments = new ArrayList<>();

  public MainPagerAdapter(MainActivity activity) {
    super(activity.getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    fragments.add(new LiveFragment());
    fragments.add(new ChannelsFragment());
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return fragments.get(position);
  }

  @Override
  public int getCount() {
    return fragments.size();
  }
}
