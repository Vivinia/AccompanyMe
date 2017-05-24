package com.example.weixu.accompanyme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weixu.service.RandomNumber;
import com.example.weixu.service.SendEmail;
import com.example.weixu.table.User;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private Button btGetNumber,btRegister,btToLoign;
    private EditText etRegisterEmail,etRegisterNumber,etRegisterPass,etRegisterUserAreBaby,etRegisterBabyName,etRegisterBabySex,etRegisterBabyBirthday;
    private SharedPreferences pref;
    private Editor editor;
    private long verificationCode=0;            //生成的验证码
    private String email,pass,babyName;
    private timeCount tc=new timeCount(60000,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this,"96556b6d6dbe89f2ff4a7c1553d882ec");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        etRegisterEmail = (EditText) findViewById(R.id.etRegisterEmail);
        etRegisterNumber= (EditText) findViewById(R.id.etRegisterNumber);
        etRegisterPass= (EditText) findViewById(R.id.etRegisterPass);
        etRegisterUserAreBaby = (EditText) findViewById(R.id.etRegisterUserAreBaby);
        etRegisterBabyName= (EditText) findViewById(R.id.etRegisterBabyName);
        etRegisterBabySex= (EditText) findViewById(R.id.etRegisterBabySex);
        etRegisterBabyBirthday = (EditText) findViewById(R.id.etRegisterBabyBirthday);
        btGetNumber= (Button) findViewById(R.id.btGetNumber);
        btRegister= (Button) findViewById(R.id.btRegister);
        btToLoign= (Button) findViewById(R.id.btToLoign);
    }

    public void btRegisterClick(View view) {
        switch (view.getId()) {
            case R.id.btGetNumber:
                checkEmail();  //检测邮箱有没有使用过
                break;
            case R.id.btRegister:
                judgeVerificationCode();   //判断输入的验证码是否正确
                break;
            case R.id.btToLoign:
                Intent intent = new Intent( RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void checkEmail() {
        email=etRegisterEmail.getText().toString();
        if(TextUtils.isEmpty(email) && email.equals("")){
            Toast.makeText(RegisterActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT)
                    .show();
        }
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereEqualTo("userParentEmail",email);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size()>0){     //查询到邮箱记录
                    Toast.makeText(RegisterActivity.this, "该邮箱已被注册", Toast.LENGTH_SHORT).show();
                }else{      //未查询到，可以获取验证码
                    sendVerificationCode(email);  //发送验证码
                }
            }
        });
    }

    private void judgeVerificationCode() {
        if(Integer.parseInt(etRegisterNumber.getText().toString())==verificationCode){    //验证码和输入一致
            registerSubmit();   //注册提交
        }else{
            Toast.makeText(RegisterActivity.this, "邮箱验证失败", Toast.LENGTH_SHORT).show();
        }
    }
    public void registerSubmit(){
        final User u=new User();
        u.setUserBabyName(etRegisterBabyName.getText().toString());
        u.setUserParentEmail(etRegisterEmail.getText().toString());
        u.setUserBabyBirthday(etRegisterBabyBirthday.getText().toString());
        u.setUserParentPass(etRegisterPass.getText().toString());
        u.setUserAreBaby(etRegisterUserAreBaby.getText().toString());
        u.setUserBabySex(etRegisterBabySex.getText().toString());
        final int age=getAge(etRegisterBabyBirthday.getText().toString());  //获取年龄
        u.setUserBabyAge(age);
        u.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                Toast.makeText(RegisterActivity.this,"注册成功", Toast.LENGTH_SHORT).show();
                // 获得SharedPreferences对象
                pref = getSharedPreferences("userBabyInfo", MODE_PRIVATE);// 将内容存放到名为userInfo的文档内
                // 获得SharedPreferences.Editor对象
                editor = pref.edit();
                editor.putString("userBabyName", etRegisterBabyName.getText().toString());
                editor.putString("userBabySex", etRegisterBabySex.getText().toString());
                editor.putString("userParentEmail",email);
                editor.putString("userParentPass",etRegisterPass.getText().toString());
                editor.putInt("userBabyAge",age);
                editor.commit();
                finish();
            }
        });
    }

    //获取年龄
    private int getAge(String birthday) {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mBir=Integer.parseInt(birthday.substring(0,4));
        return mYear-mBir;
    }

    private void sendVerificationCode(final String email) {
        try {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        RandomNumber rn = new RandomNumber();
                        verificationCode = rn.getRandomNumber(6);

                        SendEmail se = new SendEmail(email);
                        se.sendHtmlEmail(verificationCode);//发送html邮件

                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this,e.toString(), Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            handler.sendEmptyMessage(0);
            btGetNumber.setText("获取验证码");
            e.printStackTrace();
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            btGetNumber.setText("确定");
            etRegisterNumber.requestFocus();     //输入验证码的EditText获取焦点
            tc.start();

        }

    };
    public class timeCount extends CountDownTimer {

        public timeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            btGetNumber.setText(l / 1000 + "");
            btGetNumber.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btGetNumber.setEnabled(true);
            btGetNumber.setText("获取验证码");
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
