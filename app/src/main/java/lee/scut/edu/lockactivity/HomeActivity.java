package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;


public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in == null || !Common.JUMP_FROM_SCREEN_LISTENER.equals(in.getStringExtra(Common.JUMP_FROM))) {
            finish();
            return;
        }
        getWindow().setFlags(0x00020000,0x00020000);
        getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_home);
        findViewById(R.id.btn_unlock).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Deprecated
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
        finish();
    }

    @Override
    public void onBackPressed() {
        //不响应返回键
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus){
            //在recent按下时 发送一个recent事件 使recent失效
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intent.putExtra("reason", "globalactions");
            sendBroadcast(intent);
        }
    }
}
