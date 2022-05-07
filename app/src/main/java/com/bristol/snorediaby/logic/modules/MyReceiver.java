package com.bristol.snorediaby.logic.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.bristol.snorediaby.R;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function: MyReceiver
 * Description:
 * Author: wilso
 * Date: 2022/5/6
 * MaintenancePersonnel: wilso
 */
public class MyReceiver extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();

    private static AtomicInteger num2 = new AtomicInteger(0);

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
            Log.d(TAG, "進到MyReceiver，螢幕已解鎖: " + num2.getAndIncrement() + " 次");
            Toast.makeText(context, R.string.sreen_unlock, Toast.LENGTH_SHORT).show();
        }
    }

}
