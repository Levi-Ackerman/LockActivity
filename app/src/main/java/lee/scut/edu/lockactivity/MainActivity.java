package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final int CLEAR_DEFAULT_LAUNCHER_REQUEST_CODE = 100;
    public static final int SET_DEFAULT_LAUNCHER_REQUEST_CODE = 101;
    public static final int CLEAR_SYSTEM_LOCK_REQUEST_CODE = 102;
    String TAG = "lee..";

    Switch mLockSwitch ;
    Button mSetLauncherBtn;
    Button mClearLauncherBtn;
    Button mClearSystemLock;
    TextView mResultTv;

    String currentLauncherPackageName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLockSwitch = (Switch) findViewById(R.id.lock_switch);
        mLockSwitch.setOnCheckedChangeListener(this);

        mSetLauncherBtn = (Button) findViewById(R.id.set_default_launcher);
        mSetLauncherBtn.setOnClickListener(this);
        mClearLauncherBtn = (Button) findViewById(R.id.clear_default_launcher);
        mClearLauncherBtn.setOnClickListener(this);
        mClearSystemLock = (Button)findViewById(R.id.clear_system_lock);
        mClearSystemLock.setOnClickListener(this);

        mResultTv = (TextView)findViewById(R.id.show_result);
        mLockSwitch.setChecked(getPreferences(MODE_PRIVATE).getBoolean(Common.LOCK_SWITCH,true));
        initBtnByDefaultLauncher();
        initBtnBySystemLock();
    }

    /**
     * 查看系统是否有锁,决定是否要清楚锁
     */
    private void initBtnBySystemLock() {
        boolean isSystemLockSeted = ((KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE)).isKeyguardSecure();
        mClearSystemLock.setEnabled(isSystemLockSeted);
    }


    /**
     * 根据默认Launcher初始化设置按钮
     */
    private void initBtnByDefaultLauncher() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            Log.d(TAG, "resolveActivity--->activityInfo null");
            // should not happen. A home is always installed, isn't it?
        } else if (res.activityInfo.packageName.equals("android")) {
            // No default selected
            Log.d(TAG, "resolveActivity--->无默认设置");
            mClearLauncherBtn.setEnabled(false);
            mSetLauncherBtn.setEnabled(true);
        } else if (res.activityInfo.packageName.equals(getPackageName())){
            // res.activityInfo.packageName and res.activityInfo.name gives
            // you the default app
            Log.d(TAG, "默认桌面为我们的锁屏Activity：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
            mClearLauncherBtn.setEnabled(false);
            mSetLauncherBtn.setEnabled(false);
            mResultTv.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "默认桌面为其他的锁屏应用：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
            mClearLauncherBtn.setEnabled(true);
            mSetLauncherBtn.setEnabled(false);
            currentLauncherPackageName = res.activityInfo.packageName;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_default_launcher:
                Intent in = new Intent(Intent.ACTION_MAIN);
                in.addCategory(Intent.CATEGORY_HOME);
                startActivity(in);
                Toast.makeText(getApplicationContext(),"选择'我的锁屏'并勾选'作为默认设置'",Toast.LENGTH_LONG).show();
                 break;
            case R.id.clear_default_launcher:
                Uri packageURI = Uri.parse("package:" + currentLauncherPackageName);
                Intent intent = new
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
                startActivityForResult(intent, CLEAR_DEFAULT_LAUNCHER_REQUEST_CODE);
                Toast.makeText(getApplicationContext(),"点击'清除默认设置'",Toast.LENGTH_LONG).show();
                break;
            case R.id.clear_system_lock:
                Intent i = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivityForResult(i, CLEAR_SYSTEM_LOCK_REQUEST_CODE);
                Toast.makeText(getApplicationContext(),"锁屏密码设置为无,以防止出现双重锁屏",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CLEAR_DEFAULT_LAUNCHER_REQUEST_CODE:
            case SET_DEFAULT_LAUNCHER_REQUEST_CODE:
                initBtnByDefaultLauncher();
                break;
            case CLEAR_SYSTEM_LOCK_REQUEST_CODE:
                initBtnBySystemLock();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        getPreferences(MODE_PRIVATE).edit().putBoolean(Common.LOCK_SWITCH,isChecked).commit();
        if (isChecked){
            startService(new Intent(this, ScreenListenerService.class));
        }else {
            stopService(new Intent(this,ScreenListenerService.class));
        }
    }
}
