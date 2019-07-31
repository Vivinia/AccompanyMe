package com.example.weixu.accompanyme;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weixu.service.RandomNumber;
import com.example.weixu.service.SendEmail;
import com.example.weixu.table.CameraGridView;
import com.example.weixu.table.Dynamic;
import com.example.weixu.table.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btGetNumber,btRegister,btToLoign;
    private EditText etRegisterEmail,etRegisterNumber,etRegisterPass,etRegisterUserAreBaby,etRegisterBabyName,etRegisterBabySex,etRegisterBabyBirthday;
    private SharedPreferences pref;
    private Editor editor;
    private long verificationCode=0;            //生成的验证码
    private String email,pass,babyName;
    private timeCount tc=new timeCount(60000,1000);
    private CameraGridView camera;
    private String[] sexList={"男","女"};//单选列表
    private String[] familyList={"爸爸","妈妈","爷爷","奶奶","姥姥","姥爷"};//单选列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this,"96556b6d6dbe89f2ff4a7c1553d882ec");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        bindClick();
    }

    private void bindClick() {
        btGetNumber.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        btToLoign.setOnClickListener(this);
        etRegisterBabyBirthday.setOnClickListener(this);
        etRegisterUserAreBaby .setOnClickListener(this);
        etRegisterBabySex.setOnClickListener(this);
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
                upload("camera.jpg");   //上传封面

            }
        });
    }

    private void createCamera() {
        camera.setUserEmail(etRegisterEmail.getText().toString());
        camera.setCameraName("动态圈");
        camera.setCameraClass("1");
        camera.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this, "相册创建成功",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
    //图片上传
    private void upload(String imgpath){
        copyAssetAndWrite(imgpath);
        final File dataFile=new File(getCacheDir(),imgpath);
        final BmobFile icon=new BmobFile(dataFile);
        icon.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    camera=new CameraGridView();
                    Toast.makeText(RegisterActivity.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                    camera.setCameraPhoto(icon);
                    createCamera();
                }else{
                    Toast.makeText(RegisterActivity.this,"图片上传失败"+e.toString()+"///"+dataFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName){
        try {
            File cacheDir=getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return false;
                }
            }else {
                if (outFile.length()>10){//表示已经写入一次
                    return true;
                }
            }
            InputStream is=getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this,e.toString(), Toast.LENGTH_SHORT)
                                .show();
                        Looper.loop(); //这种情况下，Runnable对象是运行在子线程中的，可以进行联网操作，但是不能更新UI
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

    @Override
    public void onClick(View view) {
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
            case R.id.etRegisterBabySex:
                getSex();      //获取宝宝性别
                break;
            case R.id.etRegisterBabyBirthday:
                getDate();
                break;
            case R.id.etRegisterUserAreBaby:
                getFamily();
                break;
        }
    }

    private void getDate() {
        Calendar cal=Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        int month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        int day=cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {
                etRegisterBabyBirthday.setText(year+"-"+(++month)+"-"+day);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
            }
        };
        DatePickerDialog dialog=new DatePickerDialog(RegisterActivity.this, 0,listener,year,month,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
        dialog.show();
    }

    private void getFamily() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        /*参数一位单选列表文字，参数二为默认第几个选中（-1默认不选中），参数三是创建监听器*/
        builder.setSingleChoiceItems(familyList,-1,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etRegisterUserAreBaby.setText(familyList[which]);
                dialog.dismiss();//关闭对话框
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
    }

    /*获取宝宝性别*/
    private void getSex() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        /*参数一位单选列表文字，参数二为默认第几个选中（-1默认不选中），参数三是创建监听器*/
        builder.setSingleChoiceItems(sexList,-1,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etRegisterBabySex.setText(sexList[which]);
                dialog.dismiss();//关闭对话框
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
    }

    /*倒计时显示获取验证码*/
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
