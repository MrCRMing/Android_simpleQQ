package com.example.yangenneng0.myapplication.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.yangenneng0.myapplication.db.DbConnection;
import com.example.yangenneng0.myapplication.model.Person;
import com.example.yangenneng0.myapplication.smack.SmackManager;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: yangenneng
 * DateTime: 2016/12/17 15:25
 * Description:联系人数据访问对象
 */
public class PersonDAO {
    private static ArrayList<Person> personList=null;//保存联系人数据

    //获取所有联系人
    public static ArrayList<Person> getPersonList(){
        if(null==personList){
            synchronized ( PersonDAO.class ){
                if(null==personList){
                    personList=new ArrayList<Person>();
                }
            }

            //把数据库中已有的数据拿出来

            Set<RosterEntry> friends = SmackManager.getInstance().getAllFriends();
            for(RosterEntry friend:friends){
                String name=friend.getName();
                String username=friend.getUser();
                Person person=new Person(name,username);
                personList.add(person);
            }



        }

        return personList;
    }


    /**
     * 根据用户名查找用户真实姓名
     * @param username
     * @return
     */
    public static String findNameByUsername(String username){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            Person person=personList.get(i);
            if(username.equals(person.getUsername())){
                return person.getName();
            }
        }
        return "";
    }

    /**
     * 查找用户名是否存在
     * @param username
     * @return
     */
    public boolean checkUsername(String username){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            Person book=personList.get(i);
            if(username.equals(book.getUsername())){
                return true;
            }
        }
        return false;
    }

    /**
     * 插入联系人
     * @param person
     * @return
     */
    public boolean insert(Person person){

        if(checkUsername(person.getUsername())){  //如果用户名存在则直接返回失败
            return false;
        }
        try {
            personList.add(person);
            DbConnection conn=new DbConnection();
            SQLiteDatabase db=conn.getConnection();
            String sql="insert into tb_person(name,username,password) values('"
                    +person.getName()+"','"+person.getUsername()+"')";
            db.execSQL(sql);
            db.close();
            return true;
        }catch ( Exception e ){
            return false;
        }

    }

    public boolean chechLogin(String username,String password){
        if(null==personList){
            getPersonList();
        }
        for ( int i = 0; i < personList.size(); i++ ) {
            Person book=personList.get(i);
            if(username.equals(book.getUsername())){
                return true;
            }
        }
        return false;
    }


}
