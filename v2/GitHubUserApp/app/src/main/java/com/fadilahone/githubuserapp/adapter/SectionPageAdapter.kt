package com.fadilahone.githubuserapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fadilahone.githubuserapp.R
import com.fadilahone.githubuserapp.fragment.FollowFragment

class SectionPageAdapter(
    mContext: Context,
    fm: FragmentManager,
    conFollowers: String?,
    conFollowing: String?,
    username: String?
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val TAB_TITLES = arrayOf(
        "${mContext.getString(R.string.follower)} ($conFollowers)",
        "${mContext.getString(R.string.following)} ($conFollowing)"
    )

    private val followersFragment = FollowFragment.newInstance("$username/followers")
    private val followingFragment = FollowFragment.newInstance("$username/following")

    private val fragment = arrayOf(followersFragment, followingFragment)

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return 2
    }
}