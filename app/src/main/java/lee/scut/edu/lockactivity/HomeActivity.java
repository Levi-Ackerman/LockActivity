package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in == null || !Common.JUMP_FROM_SCREEN_LISTENER.equals(in.getStringExtra(Common.JUMP_FROM))) {
            //不是亮屏监听器启动的
            if (!LockApplication.getApp().isLockActivity()){
                //锁屏界面不在前台
                jumpToDesktop();//跳到真正的桌面
                finish();
                return ;
            }
        }
        jumpToLockActivity();
    }

    private void jumpToLockActivity() {
        startActivity(new Intent(this,LockActivity.class));
        finish();
    }

    private void jumpToDesktop() {
        String packageName = Util.getStringFromPreference(getApplicationContext(),Common.PACKAGE_NAME,null);
        String activityName = Util.getStringFromPreference(getApplicationContext(),Common.ACTIVITY_NAME,null);
        if (packageName == null || activityName == null){
            Toast.makeText(getApplicationContext(),"还没有选择你解锁后想进入的桌面",Toast.LENGTH_SHORT);
            return ;
        }
        Intent in = new Intent();
        in.setComponent(new ComponentName(packageName,activityName));
        startActivity(in);
    }

}
