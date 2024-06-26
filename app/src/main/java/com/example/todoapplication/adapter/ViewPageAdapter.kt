package com.example.todoapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todoapplication.fragments.LoginTabFragment
import com.example.todoapplication.fragments.SignupTabFragment

class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    // Returns the fragment based on the position
    override fun createFragment(position: Int): Fragment {
        if (position == 1) {
            return SignupTabFragment()
        }
        return LoginTabFragment()
    }
}