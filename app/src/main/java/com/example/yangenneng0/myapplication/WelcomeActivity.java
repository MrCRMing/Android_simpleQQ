package com.example.yangenneng0.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.yangenneng0.myapplication.db.DbConnection;
import com.example.yangenneng0.myapplication.smack.SmackManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * User: yangenneng
 * DateTime: 2016/12/10 13:58
 * Description:欢迎页面
 */
public class WelcomeActivity  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        //连接服务器
        new Thread(){
            public void run(){
                SmackManager.getInstance();
            }
        }.start();

        final Intent intent=new Intent();
        intent.setClass(WelcomeActivity.this,LoginActivity.class);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };

        timer.schedule(timerTask,2000);//此处的Delay可以是2*1000，代表两秒

    }

}
/*
    Timer是一种定时器工具，用来在一个后台线程计划执行指定任务。
    TimerTask一个抽象类，它的子类代表一个可以被Timer计划的任务。

    用Timer线程实现和计划执行一个任务的基础步骤：
    1.实现自定义的TimerTask的子类，run方法包含要执行的任务代码。
    2.实例化Timer类，创建计时器后台线程。
    3.制定执行计划。这里用schedule方法，第一个参数是TimerTask对象，第二个参数表示开始执行前的延时时间
     （单位是milliseconds，这里定义了2000）。还有一种方法可以指定任务的执行时间。
 */