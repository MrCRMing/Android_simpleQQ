package com.example.yangenneng0.myapplication.viewUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.yangenneng0.myapplication.LoginActivity;
import com.example.yangenneng0.myapplication.R;
import com.example.yangenneng0.myapplication.dao.PersonDAO;
import com.example.yangenneng0.myapplication.model.Person;

/**
 * User: yangenneng
 * DateTime: 2016/12/10 15:10
 * Description:
 */
public class RegistActivity   extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;  //用户名
    private EditText mPasswordView;           //密码
    private EditText repassword;              //确认密码
    private EditText rename;                  //真实姓名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);//查找用户名控件
        mPasswordView = (EditText) findViewById(R.id.password);      //查找密码控件
        repassword=(EditText) findViewById(R.id.repassword);         //重复密码控件
        rename=(EditText) findViewById(R.id.rename);                 //真实姓名控件


        //注册按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();//调用函数检查登陆信息是否合法
            }
        });


    }


    /**
     * 输入的检查
     */
    private void attemptLogin() {

        // 初始化控件错误信息
        mEmailView.setError(null);
        mPasswordView.setError(null);
        repassword.setError(null);

        // 获取输入信息.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String mrepassword = repassword.getText().toString();
        String mname=rename.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 检查密码是否有效
        if ( !TextUtils.isEmpty(password) && !isPasswordValid(password) ) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if(!mrepassword.equals(password)){
            repassword.setError("两次密码不一致");
            focusView = repassword;
            cancel = true;
        }

        // 检查邮箱
        if ( TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }



        //检查真实姓名
        if(mname.equals("")){
            rename.setError("真实姓名不能为空");
            focusView = rename;
            cancel = true;
        }



        if ( cancel ) {
            focusView.requestFocus();
        } else {
            //注册逻辑实现
            /*-------------------------------*/

            String mrename=rename.getText().toString();
            String memail=mEmailView.getText().toString();
            String mpassword=repassword.getText().toString();
            Person person=new Person(mrename,memail);

            PersonDAO personDAO=new PersonDAO();
            boolean isSucess=false;

            if(personDAO.checkUsername(memail)){
                Toast.makeText(RegistActivity.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();

            }
            else{

                isSucess= personDAO.insert(person); //添加到数据库

                if(isSucess){  //合法信息
                    Toast.makeText(RegistActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(RegistActivity.this,LoginActivity.class);//转到登陆
                    RegistActivity.this.startActivity(intent);
            /*-------------------------------*/
                }
                else {
                    Toast.makeText(RegistActivity.this, "信息不合法，请确认输入", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }


    /**
     * 密码是否和非法，至少需要4位
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }



}
