package com.example.weixu.adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.weixu.accompanyme.EntertainmentPagerFragment;
import com.example.weixu.accompanyme.IntelligenceFragment;
import com.example.weixu.accompanyme.IntelligencePagerFragment;

/**
 * Created by weixu on 2017/4/4.
 */

public class IntelligenceFragmentPagerAdapter extends FragmentPagerAdapter {
    /*这三个都是自己添加*/
    public final int COUNT = 2;//tab的数量
    private String[] titles = new String[]{"小画板","小游戏"};//tab名

    public IntelligenceFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IntelligencePagerFragment.newInstance(position + 1);//修改return
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
