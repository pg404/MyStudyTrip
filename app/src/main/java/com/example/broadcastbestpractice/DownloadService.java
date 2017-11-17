package com.example.broadcastbestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.*;
import android.widget.Toast;

import java.io.File;

import static android.os.Build.VERSION_CODES.N;

public class DownloadService extends Service {
   // public DownloadService() {
  //  }

    private DownloadTask downloadTask;
    private String downloadUri;
    private DownloadListener listener =  new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @Override
        public void onSuccess() {
                downloadTask = null;
                stopForeground(true);
                getNotificationManager().notify(1,getNotification("Download Successfully !!",-1));
                Toast.makeText(DownloadService.this,"Download Succeed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this,"Download Paused",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask =null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"Download Canceled",Toast.LENGTH_SHORT).show();
        }
    };

    class DownloadBinder extends Binder{

        public void startDownload(String url){
            if (downloadTask == null){
                downloadUri = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUri);
                startForeground(1,getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this,"Downloading...",Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload()
        {
            if (downloadTask != null){
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload(){
            if (downloadTask != null){
                downloadTask.cancelDownload();
            }
            if (downloadUri != null){
                String filename = downloadUri.substring(downloadUri.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS).getPath();
                File file= new File(directory+filename);
                if (file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this,"Canceled",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private DownloadBinder downloadBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downloadBinder;
        //throw new UnsupportedOperationException("Not yet implemented");
    }



    private NotificationManager getNotificationManager() {
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title ,int progress){
        Intent intent = new Intent(this,DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,intent,0);

       NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
       builder.setSmallIcon(R.mipmap.ic_launcher_round);
       builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
       builder.setContentIntent(pendingIntent);
       builder.setContentTitle(title);

       if (progress >= 0){
           builder.setContentText(progress + "%");
           builder.setProgress(100,progress,false);
       }
       return builder.build();
    }
}
