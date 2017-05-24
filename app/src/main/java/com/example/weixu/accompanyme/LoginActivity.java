package com.example.weixu.accompanyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weixu.table.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private EditText etEmail,etPass;
    private Button btLogin,btToregister;
    private String email, pass;// 保存转换成字符串的邮箱和密码
    private SharedPreferences pref;
    private Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this,"96556b6d6dbe89f2ff4a7c1553d882ec");   //加载Bmob SDK，参数为bmob服务器中新建应用的Application ID  
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InitControl();

        // 绑定点击事件
        bindClick();
    }

    private void bindClick() {
        btToregister.setOnClickListener(this);
        btLogin.setOnClickListener(this);
    }

    private void InitControl() {
        etEmail= (EditText) findViewById(R.id.etLoginEmail);
        etPass= (EditText) findViewById(R.id.etLoginPass);
        btLogin= (Button) findViewById(R.id.btLogin);
        btToregister= (Button) findViewById(R.id.btToregister);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btToregister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.btLogin:
                submit();
            default:
                break;
        }
    }

    private void submit() {
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        if ((TextUtils.isEmpty(email) && email.equals(""))
                || (TextUtils.isEmpty(pass) && equals(""))) {
            Toast.makeText(LoginActivity.this, "邮箱或密码不能为空", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereEqualTo("userParentEmail",email);
        query.addWhereEqualTo("userParentPass",pass);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null&&list.size()>0){
                    // 获得SharedPreferences对象
                    pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);// 将内容存放到名为userBabyInfo的文档内

                    for (User u : list) { // 查询出来只有一条数据，但还是使用List封装，需要遍历出来
                        // 获得SharedPreferences.Editor对象
                        editor = pref.edit();
                        editor.putString("userBabyName", u.getUserBabyName());
                        editor.putString("userBabySex", u.getUserBabySex());
                        editor.putInt("userBabyAge",u.getUserBabyAge());
                        editor.putString("userParentEmail",email);
                        editor.putString("userParentPass", pass);
                        if(u.getUserBabyHead()!=null){
                            editor.putString("userBabyHeadUrl",u.getUserBabyHead().getUrl());
                        }
                    }
                    editor.commit();
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "邮箱或密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
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