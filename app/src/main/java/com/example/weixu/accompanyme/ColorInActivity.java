package com.example.weixu.accompanyme;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.weixu.view.ColourImageView;

public class ColorInActivity extends AppCompatActivity {

    private ColourImageView civColorIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        civColorIn.setMyColorType(false);    //默认为false，只有选择颜色财位true
    }
    private void init() {
        civColorIn= (ColourImageView) findViewById(R.id.civColorIn);
    }
    public void changeColor(View view){
        switch (view.getId()){
            case R.id.rbRed:
                setColor(255,255,0,0);
                break;
            case R.id.rbOrange:
                setColor(255,255,125,0);
                break;
            case R.id.rbYellow:
                setColor(255,255,255,0);
                break;
            case R.id.rbGreen:
                setColor(255,0,255,0);
                break;
            case R.id.rbCyan:
                setColor(255,0,255,255);
                break;
            case R.id.rbBlue:
                setColor(255,0,255,255);
                break;
            case R.id.rbPurple:
                setColor(255,255,0,255);
                break;
            case R.id.rbBrown:
                setColor(255,125,100,10);
                break;
            case R.id.rbBlack:
                setColor(255,0,0,0);
                break;
            case R.id.rbRandom:
                civColorIn.setMyColorType(false);   //选择随机则传递false,随机颜色数值由页面返回
                break;
        }

    }
    public void btClick(View view){
        switch (view.getId()){
            case R.id.btClean:
                civColorIn.setImageResource(R.drawable.butterfly);   //清空
                break;
        }
    }
    //如果选择了颜色，传递选中的颜色，并设置为true
    public void setColor(int a,int r,int g,int b){
        civColorIn.setMyColor(Color.argb(a,r,g,b));
        civColorIn.setMyColorType(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
