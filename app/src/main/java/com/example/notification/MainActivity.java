package com.example.notification;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {
    private static  final  String CHANNEL_ID="MESSAGE CHANNEL";
    private static  final  int NOTIFICATION_ID=100;
    private static  final  int PI_REQ_CODE=10;
    NotificationManager notificationManager;
    Notification notification;
    ActivityResultLauncher<String> postNotificationPermission= registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result){
                        notificationManager.notify(NOTIFICATION_ID,notification);
                    }
                    else{
                        showAlertDialog("POST NOTIFICATION permission denied","Therefore we cannot throw notification");
                    }
                }
            }
    );

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Drawable
        Drawable drawable= ResourcesCompat.getDrawable(getResources(),R.drawable.img,null);
        BitmapDrawable bitmapDrawable=(BitmapDrawable) drawable;
        Bitmap largeIcon=bitmapDrawable.getBitmap();

        //Big Picture Style
        Notification.BigPictureStyle bigPictureStyle=new Notification.BigPictureStyle();
        bigPictureStyle.bigPicture(((BitmapDrawable)ResourcesCompat.getDrawable(getResources(),R.drawable.img_1,null)).getBitmap());
        bigPictureStyle.bigLargeIcon(largeIcon);
        bigPictureStyle.setBigContentTitle("Big Content Title");
        bigPictureStyle.setSummaryText("Summary Text");

        //Inbox Style
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        inboxStyle.addLine("Hi");
        inboxStyle.addLine("I am Arijit");
        inboxStyle.addLine("I am an android developer");
        inboxStyle.addLine("I am the android lead of android club");
        inboxStyle.setBigContentTitle("Inbox Style Big Content Title");
        inboxStyle.setSummaryText("Inbox Style Summary text");

        //Pending Intent
        Intent nIntent=new Intent(getApplicationContext(),MainActivity.class);
        nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,PI_REQ_CODE,nIntent,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

        //Step1 Notification Manager
        notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //step2: Notification

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.img)
                    .setContentIntent(pi)
                    .setOngoing(true)
                    .setStyle(inboxStyle)
                    .setContentText("F Notification")
                    .setSubText("First Notification Implementation")
                    .setChannelId(CHANNEL_ID)
                    .build();
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"My Channel",NotificationManager.IMPORTANCE_HIGH));
        }
        else{
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.img)
                    .setContentIntent(pi)
                    .setOngoing(true)
                    .setStyle(inboxStyle)
                    .setContentText("F Notification")
                    .setSubText("First Notification Implementation")
                    .build();
        }
        postNotificationPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS);

    }
    private void showAlertDialog(String title,String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }
}