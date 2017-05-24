package com.example.weixu.adpter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.weixu.accompanyme.GrowLineFragment;

/**
 * Created by Dustray for Vivi on 2017/4/2 0002.
 */

public class GrowLineFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    /*这三个都是自己添加*/
    public final int COUNT = 2;//tab的数量
    private String[] titles = new String[]{"身高", "体重"};//tab名
    private Context context;


    public GrowLineFragmentPagerAdapter(FragmentManager fm, Context mcontext) {/*这里添加：Context mcontext*/
        super(fm);
        context = mcontext;
    }

    @Override
    public Fragment getItem(int position) {
        return GrowLineFragment.newInstance(position + 1);//修改return
    }

    @Override
    public int getCount() {
        return COUNT;//修改return
    }

    //添加下面这个方法（直接复制过去就行）
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
