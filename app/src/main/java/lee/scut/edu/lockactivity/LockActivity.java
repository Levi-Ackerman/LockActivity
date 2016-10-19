package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LockActivity extends Activity {

    boolean needFinish = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
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
    public void onBackPressed() {

    }
    @Override
    protected void onUserLeaveHint() {
        Log.i("lee..","on user leave hint");
//        super.onUserLeaveHint();
        if (!needFinish) {
            restartActivity();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    private void restartActivity() {
        Intent intent = new Intent(this,LockActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LockApplication.getApp().setLockActivity(true);
    }

    @Override
    protected void onStop() {
        LockApplication.getApp().setLockActivity(false);
        super.onStop();
    }
}
