package com.example.yangenneng0.myapplication.viewUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.example.yangenneng0.myapplication.MainActivity;
import com.example.yangenneng0.myapplication.R;
import com.example.yangenneng0.myapplication.adapter.ChatMsgViewAdapter;
import com.example.yangenneng0.myapplication.db.HistoryMessage;
import com.example.yangenneng0.myapplication.model.ChatMsgEntity;
import com.example.yangenneng0.myapplication.model.User;
import com.example.yangenneng0.myapplication.smack.SmackManager;
import com.example.yangenneng0.myapplication.utils.APPglobal;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: yangenneng
 * DateTime: 2016/12/11 17:11
 * Description:
 */
public class ChatMainActivity extends Activity implements View.OnClickListener {

    private Button mBtnSend;            // 发送btn
    private Button mBtnBack;            // 返回btn
    private EditText mEditTextContent;  // 内容框
    private ListView mListView;         // 聊天记录列表
    static private ChatMsgViewAdapter mAdapter;// 聊天记录视图的Adapter
    private String Name;//聊天对象名字
    static  List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 聊天记录对象数组
    private Chat chat;//聊天实体类

    private String TAG = "length";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: sequence1" + mAdapter);

            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
            mListView.setSelection(mListView.getCount() - 1);// 接受一条消息时，ListView显示选择最后一项
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatmain);

        TextView textView= (TextView) findViewById(R.id.chatname);
        Bundle bundle = this.getIntent().getExtras();
        textView.setText(bundle.getString("name")); //解析传递过来的参数
        Name=bundle.getString("name");
        Log.d("length", "initData: name "+Name);
        //获得JID
        String JID=Name+"@212.64.92.236";
        //创建聊天
        chat=SmackManager.getInstance().createChat(JID);
        //设置该聊天的监听器
        ChatManager chatmanager = SmackManager.getInstance().getChatManager();



        chatmanager.addChatListener(new ChatManagerListener() {

            @Override
            public void chatCreated(Chat chat, boolean arg1) {
                chat.addMessageListener(new ChatMessageListener() {


                    @Override
                    public void processMessage(Chat chat, Message message) {


                        try {
                            JSONObject json = new JSONObject(message.getBody());
                            //将数据存到数据库
                            HistoryMessage historyMessage=new HistoryMessage();
                            historyMessage.setName(json.optString("fromNickName"));
                            historyMessage.setReceiver(User.getInstance().getName());
                            historyMessage.setContent(json.optString("messageContent"));
                            historyMessage.setDate(getDate());
                            historyMessage.save();


                            ChatMsgEntity chatMsgEntity=new ChatMsgEntity();
                            chatMsgEntity.setName(json.optString("fromNickName"));
                            chatMsgEntity.setDate(getDate());  //设置格式化的发送时间
                            chatMsgEntity.setMessage(json.optString("messageContent")); //设置发送内容
                            chatMsgEntity.setMsgType(true);      //设置消息类型，true 接受的 false发送的
                            Log.d(TAG, "handleMessage: sequence2");
                            mDataArrays.add(chatMsgEntity);
                            Log.d(TAG, "handleMessage: sequence3");
//                            new Thread(){
//                                @Override
//                                public void run() {
//                                    super.run();
//                                    android.os.Message message1 = new android.os.Message();
//                                    handler.sendMessage(message1);
//                                }
//                            }.start();
                            update();
                            Log.d(TAG, "handleMessage: sequence4");
                            //mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
        initView();// 初始化view
        initData();// 初始化数据
    }


    /**
     * 初始化view
     * 找出页面的控件
     */
    public void initView() {
        mListView= (ListView) findViewById(R.id.listview);
        mBtnSend=(Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.chatGoHome);
        mBtnBack.setOnClickListener(this);
        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }




    /**
     * 加载消息历史，从数据库中读出
     */
    public void initData() {
        //从数据库中读出消息记录
        mDataArrays.clear();
        List<HistoryMessage> AllhistoryMessages= DataSupport.findAll(HistoryMessage.class);//得到所有的消息记录

        List<HistoryMessage>historyMessages=new ArrayList<>();

            for(HistoryMessage historyMessage:AllhistoryMessages){//从所有消息记录中抽出属于这两个人的记录

                if((historyMessage.getReceiver().equals(this.Name))
                        ||(historyMessage.getName().equals(this.Name))){
                    historyMessages.add(historyMessage);
                }
            }

            for (HistoryMessage historyMessage:historyMessages) {
                ChatMsgEntity entity = new ChatMsgEntity();
                entity.setDate(historyMessage.getDate());
                if (historyMessage.getName().equals(User.getInstance().getName()) ) {



                    entity.setName(historyMessage.getName() );   //设置对方姓名
                    entity.setMsgType(false); // 收到的消息
                } else {
                    entity.setName(historyMessage.getName() );   //设置自己姓名
                    entity.setMsgType(true);// 发送的消息
                }
                entity.setMessage(historyMessage.getContent());//消息内容
                mDataArrays.add(entity);
            }

            mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
            mListView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:// 发送按钮点击事件
                send();
                break;
            case R.id.chatGoHome:// 返回按钮点击事件
                Intent intent = new Intent();
                intent.setClass(ChatMainActivity.this, MainActivity.class);
                ChatMainActivity.this.startActivity(intent);
                break;
        }
    }


    /**
     * 发送消息
     */
    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {

            //-----------发送者-------------

            //将数据处存到数据库
            //将数据存到数据库
            HistoryMessage historyMessage=new HistoryMessage();
            historyMessage.setName( User.getInstance().getName());
            historyMessage.setReceiver(Name);//设置接受者
            historyMessage.setContent(contString);
            historyMessage.setDate(getDate());
            historyMessage.save();
            //显示数据
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setName(APPglobal.NAME);      //设置发送消息消息者姓名
            entity.setDate(getDate());  //设置格式化的发送时间
            entity.setMessage(contString); //设置发送内容
            entity.setMsgType(false);      //设置消息类型，true 接受的 false发送的
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变



            //发送数据
            try {
                Message message=new Message();
                JSONObject json = new JSONObject();
                json.put("fromNickName", User.getInstance().getName());
                json.put(",messageContent", contString);
                chat.sendMessage(json.toString());

            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mEditTextContent.setText("");// 清空编辑框数据
            mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项



        }
    }


    /**
     * 发送消息时，获取当前事件
     * @return 当前时间
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return format.format(new Date());
    }


    private void update()
    {
        android.os.Message message11 = new android.os.Message();
        handler.sendMessage(message11);
    }

}
