package com.example.navigation_smd_7a;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private NewOrderFragment newOrderFragment;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {

        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new ScheduleFragment();
            case 1:
                return new DeliveredFragment();
            default:
                newOrderFragment=
              new NewOrderFragment();
                return  newOrderFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    public void refreshNewOrderFragment() {
        if (newOrderFragment != null) {
            newOrderFragment.refreshProductList(); // Call the refresh method
        }
    }
}
