package com.example.weixu.accompanyme;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.weixu.view.ColourImageView;

public class ColorInActivity extends AppCompatActivity {

    private ColourImageView civColorIn;
    private String[] PictureList={"书包","小汽车","海豚","大象","花","葡萄","猴子","乌龟","蝴蝶","哆啦A梦"};//单选列表
    private int number=0;
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
                setColor(255,0,0,255);
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
                civColorIn.setMyColorType(false);   //选择随机则传递false
                break;
        }

    }
    public void btClick(View view){
        switch (view.getId()){
            case R.id.btClean:     //清空
                setImg(number);
                break;
            case R.id.btSearchColorPicture:       //更换图片
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                /*参数一位单选列表文字，参数二为默认第几个选中（-1默认不选中），参数三是创建监听器*/
                builder.setSingleChoiceItems(PictureList,-1,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        number=which;
                        setImg(number);
                        dialog.dismiss();
                    }
                });
                /*添加对话框中取消按钮点击事件*/
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//关闭对话框
                    }
                });
                AlertDialog dialog=builder.create();//获取dialog
                dialog.show();//显示对话框
                break;
        }
    }

    private void setImg(int number) {
        if(number==0){
            civColorIn.setImageResource(R.drawable.bag);
        }else if(number==1){
            civColorIn.setImageResource(R.drawable.car);
        }else if(number==2){
            civColorIn.setImageResource(R.drawable.dolphin);
        }else if(number==3){
            civColorIn.setImageResource(R.drawable.elephant);
        }else if(number==4){
            civColorIn.setImageResource(R.drawable.flower);
        }else if(number==5){
            civColorIn.setImageResource(R.drawable.grape);
        }else if(number==6){
            civColorIn.setImageResource(R.drawable.monkey);
        }else if(number==7){
            civColorIn.setImageResource(R.drawable.tortoise);
        }else if(number==8){
            civColorIn.setImageResource(R.drawable.butterfly);
        }else{
            civColorIn.setImageResource(R.drawable.img_duolaameng);
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
