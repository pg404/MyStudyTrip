package com.example.broadcastbestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onClick(View view) {
        if (downloadBinder == null){
            return;
        }
        switch (view.getId()){
            case R.id.startDownload:
                String url = "http://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadBinder.startDownload(url);
                break;
            case R.id.pauseDownload:
                downloadBinder.pauseDownload();
                break;
            case R.id.cancelDownload:
                downloadBinder.cancelDownload();
                break;
            default:
                    break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Button startdownload = (Button)findViewById(R.id.startDownload);
        Button pausedownload = (Button)findViewById(R.id.pauseDownload);
        Button canceldownload = (Button)findViewById(R.id.cancelDownload);
        startdownload.setOnClickListener(this);
        pausedownload.setOnClickListener(this);
        canceldownload.setOnClickListener(this);
        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);


        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(DownloadActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DownloadActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
                    break;
        }
    }
}
