package com.example.weixu.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weixu.accompanyme.EntertainmentPagerFragment;

/**
 * Created by Dustray on 2017/4/3 0003.
 */

public class EntertainmentFragmentPagerAdapter extends FragmentPagerAdapter {
    /*这三个都是自己添加*/
    public final int COUNT = 3;//tab的数量
    private String[] titles = new String[]{"唱儿歌", "听故事","识图片"};//tab名


    public EntertainmentFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return EntertainmentPagerFragment.newInstance(position + 1);//修改return
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    //添加下面这个方法（直接复制过去就行）
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
