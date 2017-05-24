package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class InsertChartInfoActivity extends AppCompatActivity {
    private EditText etBabyTall,etBabyWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_chart_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        etBabyTall = (EditText) findViewById(R.id.etBabyTall);
        etBabyWeight = (EditText) findViewById(R.id.etBabyWeight);
    }

    public void btSubmit(View view) {
        switch (view.getId()) {
            case R.id.btInsertSubmit:
                SharedPreferences prefBaby = getSharedPreferences("userBabyInfo", Context.MODE_PRIVATE); //打开保存宝宝信息的文件
//                SharedPreferences.Editor editorBaby = prefBaby.edit();
                int babyAge=prefBaby.getInt("userBabyAge",0);
                SharedPreferences prefGrow = getSharedPreferences("babyGrowInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorGrow = prefGrow.edit();          //打开保存成长的文件
                switch (babyAge){
                    case 0:
                        editorGrow.putInt("tallZero", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightZero", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 1:
                        editorGrow.putInt("tallOne", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightOne", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 2:
                        editorGrow.putInt("tallTwo", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightTwo", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 3:
                        editorGrow.putInt("tallThree", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightThree", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 4:
                        editorGrow.putInt("tallFour", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightFour", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 5:
                        editorGrow.putInt("tallFive", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightFive", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                    case 6:
                        editorGrow.putInt("tallSix", Integer.parseInt(etBabyTall.getText().toString()));//保存输入的身高
                        editorGrow.putInt("WeightSix", Integer.parseInt(etBabyWeight.getText().toString()));//保存输入的体重
                        break;
                }
                editorGrow.commit();
                finish();
                break;
        }
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
