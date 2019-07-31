package com.example.weixu.accompanyme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weixu.table.GrownLine;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class InsertChartInfoActivity extends AppCompatActivity {
    private EditText etBabyTall,etBabyWeight;
    private GrownLine babyLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_chart_info);
        Bmob.initialize(InsertChartInfoActivity.this, "96556b6d6dbe89f2ff4a7c1553d882ec");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        etBabyTall = (EditText) findViewById(R.id.etBabyTall);
        etBabyWeight = (EditText) findViewById(R.id.etBabyWeight);
    }

    public void btSubmit(View view) {
        babyLine=new GrownLine();
        switch (view.getId()) {
            case R.id.btInsertSubmit:
                SharedPreferences prefBaby = getSharedPreferences("userBabyInfo", Context.MODE_PRIVATE); //打开保存宝宝信息的文件
//                SharedPreferences.Editor editorBaby = prefBaby.edit();
                int babyAge=prefBaby.getInt("userBabyAge",0);
                String userEmail=prefBaby.getString("userParentEmail",null);
                babyLine.setBabyAge(babyAge);
                babyLine.setBabyWeight(etBabyWeight.getText().toString());
                babyLine.setBabyTall(etBabyTall.getText().toString());
                babyLine.setUserEmail(userEmail);
                BmobQuery<GrownLine> query=new BmobQuery<GrownLine>();
                query.addWhereEqualTo("babyAge",babyAge);
                query.addWhereEqualTo("userEmail",userEmail);
                query.findObjects(new FindListener<GrownLine>() {
                    @Override
                    public void done(List<GrownLine> list, BmobException e) {
                        if(list.size()>0){     //有数据，修改
                            babyLine.update(list.get(0).getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Toast.makeText(InsertChartInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{     //无数据，存储
                            babyLine.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    Toast.makeText(InsertChartInfoActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Intent intent=new Intent(InsertChartInfoActivity.this,GrowLineActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });


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
