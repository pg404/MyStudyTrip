package com.example.broadcastbestpractice;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        Button MusicPlay = (Button)findViewById(R.id.play_music);
        Button MusicPause = (Button)findViewById(R.id.pause_music);
        Button MusicStop = (Button)findViewById(R.id.stop_music);
        MusicPause.setOnClickListener(this);
        MusicPlay.setOnClickListener(this);
        MusicStop.setOnClickListener(this);

       if (ContextCompat.checkSelfPermission(MusicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED){
           ActivityCompat.requestPermissions(MusicActivity.this,
                   new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
       }else {
           initMediaPlayer();
       }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.play_music:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause_music:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop_music:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    initMediaPlayer();
                }
                break;
                default:
                    break;
        }
    }

    private void initMediaPlayer(){
        try {
            File file = new File(Environment.getExternalStorageDirectory(),"music.mp3");
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length >0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else {
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
                    break;
        }
    }
}
