package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.TYPE_PHONE;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

public class HomeActivity extends Activity {

    boolean needFinish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in == null || !Common.JUMP_FROM_SCREEN_LISTENER.equals(in.getStringExtra(Common.JUMP_FROM))) {
            jumpToDesktop();
            return;
        }
        getWindow().setFlags(0x00020000,0x00020000);
        getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(FLAG_DISMISS_KEYGUARD);
        getWindow().setType(TYPE_SYSTEM_ALERT);
        setContentView(R.layout.activity_home);
        findViewById(R.id.btn_unlock).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jumpToDesktop();
            }
        });
    }
    private void jumpToDesktop() {
        needFinish = true;
        String packageName = Util.getStringFromPreference(getApplicationContext(),Common.PACKAGE_NAME,null);
        String activityName = Util.getStringFromPreference(getApplicationContext(),Common.ACTIVITY_NAME,null);
        if (packageName == null || activityName == null){
            Toast.makeText(getApplicationContext(),"还没有选择你解锁后想进入的桌面",Toast.LENGTH_SHORT);
            return ;
        }
        Intent in = new Intent();
        in.setComponent(new ComponentName(packageName,activityName));
        startActivity(in);
        finish();
    }
    @Override
    protected void onUserLeaveHint() {
        Log.i("Lee..","on user leave hint");
//        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        if (!needFinish) {
//            restartActivity();
//            ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            Intent intent = new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            intent.putExtra("reason", "globalactions");
            sendBroadcast(intent);
        }
    }

    private void restartActivity() {
        Log.i("Lee..","restart Activity");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
