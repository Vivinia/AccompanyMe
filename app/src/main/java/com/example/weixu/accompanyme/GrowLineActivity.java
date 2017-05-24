package com.example.weixu.accompanyme;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.weixu.adpter.GrowLineFragmentPagerAdapter;

public class GrowLineActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private ViewPager vpFindPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grow_line);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        initTabLayout();
    }


    private void initTabLayout() {
        //ViewPager
        vpFindPager = (ViewPager) findViewById(R.id.vp_grow_line);
        GrowLineFragmentPagerAdapter adapter = new GrowLineFragmentPagerAdapter(getSupportFragmentManager(), this);
        vpFindPager.setAdapter(adapter);
        //TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tl_grow_line);
        tabLayout.setupWithViewPager(vpFindPager);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_grow_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.ab_add_info:
                Intent intent=new Intent(GrowLineActivity.this,InsertChartInfoActivity.class);
                //这里跳转应该传值mPage，好在插入时区分身高还是体重
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
