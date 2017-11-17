package com.example.broadcastbestpractice;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraAlbumActivity extends AppCompatActivity {

    public static final int TAKE_PICTURE = 1;
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);
        picture = (ImageView)findViewById(R.id.picture);

        //从相册选择按钮
        Button choseFromAlbum = (Button)findViewById(R.id.choose_picture);
        choseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CameraAlbumActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CameraAlbumActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });

        //拍照按钮
        Button takepicture = (Button)findViewById(R.id.take_picture);
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try {
                    if (outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24){
                    imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this,
                            "com.example.broadcastbestpractice.fileprovider",outputImage);
                }else {
                    imageUri = Uri.fromFile(outputImage);
                }
                if (ContextCompat.checkSelfPermission(CameraAlbumActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CameraAlbumActivity.this,
                            new String[]{Manifest.permission.CAMERA},2);
                }else {
                    openCamera();
                }
               // Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                //startActivityForResult(intent,TAKE_PICTURE);
            }
        });
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PICTURE);
    }

    private void openCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(CameraAlbumActivity.this,
                            "You denied the open album permission",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }else {
                    Toast.makeText(CameraAlbumActivity.this,
                            "You denied the open camera permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK){
                    if (Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitkat(data);
                    }
                }
                break;
            default:
                break;
        }
    }


    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.provider.downloads.documents".equals(uri.getAuthority())){
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(documentId));
                imagePath = getImagePath(contenturi ,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath =uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitkat(Intent data){

        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }


    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null
                ,null);
        if (cursor != null){
            if (cursor.moveToNext()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(CameraAlbumActivity.this,
                    "Failed to get image",Toast.LENGTH_SHORT).show();
        }

    }
}
