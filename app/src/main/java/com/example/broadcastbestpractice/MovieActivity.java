package com.example.broadcastbestpractice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MovieActivity extends AppCompatActivity implements View.OnClickListener{

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        videoView = (VideoView)findViewById(R.id.vedio_view);
        Button play = (Button)findViewById(R.id.play_movie);
        Button pause = (Button)findViewById(R.id.pause_movie);
        Button stop = (Button)findViewById(R.id.pause_movie);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(MovieActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MovieActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            initVideoPath();
        }
    }

    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),"movie.mp4");
        videoView.setVideoPath(file.getPath());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_movie:
                if (!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.pause_movie:
                if (videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.stop_movie:
                if (videoView.isPlaying()){
                    videoView.resume();//这里是重新播放，layout文件都写成了stop懒得改了
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView !=null){
            videoView.suspend();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initVideoPath();
                }else {
                    Toast.makeText(this,"拒绝权限无法正常播放",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
                    break;
        }
    }
}
