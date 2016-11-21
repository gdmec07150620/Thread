package com.example.zsl.thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Time;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


import static android.R.attr.duration;
import static com.example.zsl.thread.R.styleable.View;

public class MainActivity extends AppCompatActivity {
    private TextView tv1;
    private int seconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        Date theLastDay = new Date(117,5,23);
        Date toDay = new Date();
        seconds = (int)(theLastDay.getTime()-toDay.getTime())/1000;
    }
    public void anr(View v){
for(int i=0;i<10000;i++){
    BitmapFactory.decodeResource(getResources(),R.drawable.android);
}
    }

    public void threadclass(View v){
        class ThreadSample extends  Thread{
            Random rm;
            public ThreadSample(String tname){
                super(tname);
                rm=new Random();
            }
            public void run(){
            for(int i=0;i<10;i++){
                System.out.print(i+" "+getName());
                try {
                    sleep(rm.nextInt(1000));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
                System.out.println(getName()+"完成");
            }
        }
        ThreadSample thread1 = new ThreadSample("线程一");
        thread1.start();
        ThreadSample thread2 = new ThreadSample("线程二");
        thread2.start();
    }

    public void runnableinterface(View v){
        class  RunnableExample implements Runnable{
            Random rm;
            String name;
            public RunnableExample(String tname){
                this.name=tname;
                rm = new Random();
            }
            public void run() {
            for(int i = 0;i<10;i++){
                System.out.println(i+" "+name);
                try{
                    Thread.sleep(rm.nextInt(1000));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
                System.out.println(name+"完成");
            }
        }
        Thread thread1 = new Thread(new RunnableExample("线程一"));
        thread1.start();
        Thread thread2 = new Thread(new RunnableExample("线程二"));
        thread2.start();
    }
    public void timertask(View v){
        class MyThread extends TimerTask{
            Random rm;
            String name;
            public MyThread(String tname){
                this.name = tname;
                rm = new Random();
            }
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    System.out.println(i+" "+name);
                    try{
                        Thread.sleep(rm.nextInt(1000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                System.out.println(name+"完成");
            }
        }
        Timer timer1=new Timer();
        Timer timer2 = new Timer();
        MyThread thread1 = new MyThread("线程一");
        MyThread thread2 = new MyThread("线程二");
        timer1.schedule(thread1,0);
        timer2.schedule(thread2,0);
    }

    public void handlermessage(View v){
        final Handler myHandler = new Handler(){
            public void handleMessage(Message msg) {
            super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        showmsg(String.valueOf(msg.arg1+msg.getData().get("attac").toString()));
                }
            }
        };
        //创建MyTask类继承于TimerTssk抽象类
        class  MyTask extends TimerTask{
            int countdown;
            double achievement1=1,achievement2=1;
            //构造方法，传入倒计时秒数
            public MyTask(int seconds){
                this.countdown = seconds;
            }
            @Override
            public void run() {
                //obtain方法直接从消息池中去取出一个可用的消息对象，比new Message()方法效率高
                Message msg =Message.obtain().obtain();
                //定义消息标志位1
                msg.what=1;
                //每次运行把countdow减一，arg1和arg2是消息传递信息的高效率方法，但只能传int类型
                msg.arg1= countdown--;
                achievement1 = achievement1*1.01;
                achievement1 = achievement2*1.02;
                //用bundle传递的效率低
                Bundle bundle  = new Bundle();
                bundle.putString("attach","\n努力多1%："+achievement1+"\n努力多2%："+achievement2);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new MyTask(seconds),1,1000);
    }
    public void showmsg(String msg){
            tv1.setText(msg);
    }
    public void asynctask(View v){
        class LeARHard extends AsyncTask<Long,String,String> {
            private Context context;
            final int durattion = 10;
            int count = 0;

            public LeARHard(Activity context){
                this.context = context;
            }
            @Override
            protected String doInBackground(Long... params) {
                long num = params[0].longValue();
                while (count < duration) {
                    num--;
                    count++;
                    String status = "离毕业还有" + num + "秒，努力学习" + count + "秒。";
                    publishProgress(status);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return "这" + duration + "秒有收获，没速度.";
            }

            @Override
            protected void onProgressUpdate(String... values) {
                ((MainActivity)context).tv1.setText(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                showmsg(s);
                super.onPostExecute(s);
            }
        }
        LeARHard learHard = new LeARHard(this);
        learHard.execute((long)seconds);
    }
}
