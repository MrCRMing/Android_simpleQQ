package com.example.yangenneng0.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.example.yangenneng0.myapplication.dao.PersonDAO;
import com.example.yangenneng0.myapplication.model.User;
import com.example.yangenneng0.myapplication.smack.SmackManager;
import com.example.yangenneng0.myapplication.utils.APPglobal;
import com.example.yangenneng0.myapplication.viewUI.RegistActivity;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {

    private static boolean isExit=false;//判断是否直接退出程序

    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);   //用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);         //密码控件

        //setOnEditorActionListener这个方法，并不是在我们点击EditText的时候触发，
        // 也不是在我们对EditText进行编辑时触发，而是在我们编辑完之后点击软键盘上的回车键才会触发。﻿﻿
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ( id == R.id.login || id == EditorInfo.IME_NULL ) {
                    attemptLogin();//调用函数检查登陆信息是否合法
                    return true;
                }
                return false;
            }
        });

        //登录按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });

        //注册按钮
        Button registButton= (Button) findViewById(R.id.register);
        registButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,RegistActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit=false;
        }
    };

    //退出确认
    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if(keyCode==KeyEvent.KEYCODE_BACK){
    //        if(!isExit){
    //            isExit=true;
    //            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
    //            handler.sendEmptyMessageDelayed(0,2000);
    //        }
    //    }else {
    //        finish();
    //        System.exit(0);
    //    }
    //    return super.onKeyDown(keyCode, event);
    //}

    /**
     * 输入信息的检查
     */
    private void attemptLogin() {

        // 初始化错误信息为null
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;//是否是非法信息
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //  检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if ( cancel ) {//非法信息
            focusView.requestFocus();//标签用于指定屏幕内的焦点View。
        } else {//合法信息
           //登陆跳转逻辑

            boolean sussess= false;
            try {
                sussess = SmackManager.getInstance().login(email,password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(sussess){  //信息合法

                User.getInstance().setAccount(email);
                User.getInstance().setPassword(password);
                User.getInstance().setName(email);
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this,MainActivity.class);
                LoginActivity.this.startActivity(intent);
            }
            else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 密码是否合法：至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }

}

