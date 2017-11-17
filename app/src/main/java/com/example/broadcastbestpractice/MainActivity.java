package com.example.broadcastbestpractice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //下载功能（下载Eclipse）
        Button download = (Button)findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DownloadActivity.class);
                startActivity(intent);
            }
        });

        //webview控件打开虎扑
        Button webview = (Button)findViewById(R.id.web);
        webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });

        //音乐
        Button musicbutton = (Button)findViewById(R.id.music);
        musicbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MusicActivity.class);
                startActivity(intent);
            }
        });

        //视频
        Button moviebutton = (Button)findViewById(R.id.movie);
        moviebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MovieActivity.class);
                startActivity(intent);
            }
        });

        //使用通知
        Button sendnotice = (Button)findViewById(R.id.send_notice);
        sendnotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(MainActivity.this)
                        .setContentTitle("This is content title")
                        .setContentText("This is content text,这是我的第一个结合"+
                                "了好多功能的APP,这是一段长文字，用来测试长通知的显示效果，我们一起快乐地继续学习吧。")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{0,1000,1000,1000})
                        .setLights(Color.BLUE,1000,1000)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText("这是我的第一个结合" +
                          //     "了好多功能的APP,这是一段长文字，用来测试长通知的显示效果，我们一起快乐地继续学习吧。"))
                        //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.
                          //     decodeResource(getResources(),R.drawable.ic_huowen_1)))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                notificationManager.notify(1,notification);
            }
        });
        //通知页面的显示的转换UI


        //读取联系人数据
        Button read_contacts = (Button)findViewById(R.id.read_contacts);
        read_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        ReadContactActivity.class);
                startActivity(intent);
            }
        });


        //打开日历
        Button calendar = (Button)findViewById(R.id.calendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"学习中，等待开发完成",Toast.LENGTH_SHORT).show();
            }
        });

        //打开网页button功能实现
        Button openweb = (Button)findViewById(R.id.open_web);
        openweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.bing.com"));
                startActivity(intent);
            }
        });

        //打开相机功能实现
        Button opencamera = (Button)findViewById(R.id.open_camera);
        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},2);
                }else {
                    opencamera();
                }*/
                opencamera();
            }
        });

        //显示图片
        Button take_photo = (Button)findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CameraAlbumActivity.class);
                startActivity(intent);
            }
        });

        //打电话功能实现
        Button makecall = (Button)findViewById(R.id.make_call);
        makecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},1);
                }else {
                    call();
                }
            }
        });

        //强制下线
        Button forceOffline = (Button)findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }



    private  void opencamera(){
        try {
            Intent intent = new Intent();
            intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }


    private void call(){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:1008611"));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    call();
                }else{
                    Toast.makeText(this,"You denied the phone permission",Toast.LENGTH_SHORT).show();
                }break;

            default:break;
        }
    }



}
