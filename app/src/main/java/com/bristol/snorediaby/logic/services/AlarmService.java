package com.bristol.snorediaby.logic.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import com.bristol.snorediaby.MainActivity;
import com.bristol.snorediaby.R;
import java.util.Objects;

public class AlarmService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    int startId;

    boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String alarmState = Objects.requireNonNull(intent.getExtras()).getString("extra");

        // notification set up the notification service
        NotificationManager notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // set up an intent that goes to the Main Activity///////////////檢查是使用AlarmFragment還是MainActivity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        // set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);
        // make the notification parameters
        Notification notification_popup = new Notification.Builder(this)
            .setContentTitle("An alarm is going off!")
            .setContentText("Click me!")
            .setSmallIcon(R.drawable.ic_action_call)
            .setContentIntent(pending_intent_main_activity)
            .setAutoCancel(true)
            .build();

        Log.e(TAG, "Alarm State: " + alarmState);
        // this converts the extra strings from the intent to start IDs, values 0 or 1
        assert alarmState != null;
        switch (alarmState) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if (!this.isRunning) {
            if (Objects.equals(1, startId)) {
                // if there is no music playing, and the user pressed "alarm on" music should start playing
                Log.e(TAG, "There is no music, and you want start");
                this.isRunning = true;
                this.startId = 0;
                //set up the start command for the notification
                assert notify_manager != null;
                notify_manager.notify(0, notification_popup);
                MediaPlayer media_song = MediaPlayer.create(this, R.raw.machiko);
                media_song.setLooping(true);
                media_song.start();
            } else if (Objects.equals(0, startId)) {
                // these are if the user presses random buttons just to bug-proof the app
                // if there is no music playing, and the user pressed "alarm off" do nothing
                Log.e(TAG, "There is no music, and you want end");
                this.isRunning = false;
                this.startId = 0;
                // 關閉服務
                stopSelf();
            }
        } else if (this.isRunning) {
            if (Objects.equals(1, startId)) {
                // if there is music playing and the user pressed "alarm on" do nothing
                Log.e(TAG, "There is music, and you want start");
                this.isRunning = true;
                this.startId = 1;
            } else if (Objects.equals(0, startId)) {
                // if there is music playing, and the user pressed "alarm off" music should stop playing
                Log.e(TAG, "There is no music, and you want end");
                this.isRunning = false;
                this.startId = 0;
                // 關閉服務
                stopSelf();
            }
        } else {
            // Can't think of anything else, just to catch the odd event
            Log.e(TAG, "Else somehow you reached this");
        }

        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        //Log.e(TAG, "AlarmService onDestroy()");
        super.onDestroy();
        this.isRunning = false;
    }

}
