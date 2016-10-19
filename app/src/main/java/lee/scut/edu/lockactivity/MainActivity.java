package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    ListView mLauncherList;
    List<BaseLauncher> launchers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLauncherList = (ListView) findViewById(R.id.lt_launcher);
        startService(new Intent(this,ScreenOnListenerService.class));
        Toast.makeText(this,"是锁屏界面在顶层,"+LockApplication.getApp().isLockActivity(),Toast.LENGTH_SHORT).show();
        Log.i("lee..",LockApplication.getApp().isLockActivity()+"");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        launchers = new ArrayList<BaseLauncher>();

        for (ResolveInfo info : resolveInfos) {
            String packageName = info.activityInfo.packageName;
            if (!getPackageName().equals(packageName)){
                BaseLauncher launcher = new BaseLauncher();
                launcher.icon = info.loadIcon(getPackageManager());
                launcher.name = info.loadLabel(getPackageManager());
                launcher.packageName = packageName;
                launchers.add(launcher);
            }
        }
        mLauncherList.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LockApplication.getApp().setLockActivity(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LockApplication.getApp().setLockActivity(false);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("lee..key down",keyCode+"");
        if (keyCode == KeyEvent.KEYCODE_APP_SWITCH){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        Log.i("Lee..","on user leave hint");
//        super.onUserLeaveHint();
        exitAction();
    }
    private void exitAction() {
        try {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return launchers.size();
        }

        @Override
        public BaseLauncher getItem(int position) {
            return launchers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LauncherView view;
            if (convertView == null){
                view = new LauncherView();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_launcher,null);
                view.icon = (ImageView) convertView.findViewById(R.id.item_launcher_img);
                view.name = (TextView) convertView.findViewById(R.id.item_launcher_name);
                convertView.setTag(view);
            }else{
                view = (LauncherView) convertView.getTag();
            }
            BaseLauncher launcher = getItem(position);
            view.icon.setImageDrawable(launcher.icon);
            view.name.setText(launcher.name);
            return convertView;
        }
    };

    class LauncherView{
        ImageView icon;
        TextView name;
    }
}
