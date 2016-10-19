package lee.scut.edu.lockactivity;

import android.app.Application;

/**
 * Created by lizhengxian on 16/10/18.
 */

public class LockApplication extends Application {
    private static LockApplication instance;
    private boolean isLockActivity = false;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
    }
    public static final LockApplication getApp(){
        return instance;
    }

    public boolean isLockActivity() {
        return isLockActivity;
    }

    public void setLockActivity(boolean lockActivity) {
        isLockActivity = lockActivity;
    }
}
