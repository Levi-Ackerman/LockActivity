package lee.scut.edu.lockactivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ListView mLauncherList;
    List<BaseLauncher> mLaunchers;
    boolean needFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in != null){
            String from = in.getStringExtra(Common.JUMP_FROM);
            if (Common.JUMP_FROM_SCREEN_LISTENER.equals(from)){
                jumpToDesktop();
                return ;
            }
        }
        setContentView(R.layout.activity_main);
        mLauncherList = (ListView) findViewById(R.id.lt_launcher);
        startService(new Intent(this,ScreenOnListenerService.class));
        Toast.makeText(this,"是锁屏界面在顶层,"+LockApplication.getApp().isLockActivity(),Toast.LENGTH_SHORT).show();
        Log.i("lee..",LockApplication.getApp().isLockActivity()+"");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        mLaunchers = new ArrayList<BaseLauncher>();

        for (ResolveInfo info : resolveInfos) {
            String packageName = info.activityInfo.packageName;
            if (!getPackageName().equals(packageName)){
                BaseLauncher launcher = new BaseLauncher();
                launcher.icon = info.loadIcon(getPackageManager());
                launcher.label = info.loadLabel(getPackageManager());
                launcher.packageName = packageName;
                launcher.activityName = info.activityInfo.name;
                mLaunchers.add(launcher);
            }
        }
        mLauncherList.setAdapter(mAdapter);
        mLauncherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseLauncher launcher = mLaunchers.get(position);
                Util.saveToPreference(getApplicationContext(),Common.PACKAGE_NAME,launcher.packageName);
                Util.saveToPreference(getApplicationContext(),Common.ACTIVITY_NAME,launcher.activityName);
            }
        });

        findViewById(R.id.btn_unlock).setOnClickListener(new View.OnClickListener(){

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
    protected void onUserLeaveHint() {
        Log.i("Lee..","on user leave hint");
//        super.onUserLeaveHint();
        if (!needFinish) {
            restartActivity();
        }
    }
    private void restartActivity() {
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
            return mLaunchers.size();
        }

        @Override
        public BaseLauncher getItem(int position) {
            return mLaunchers.get(position);
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
            view.name.setText(launcher.label);
            return convertView;
        }
    };

    class LauncherView{
        ImageView icon;
        TextView name;
    }
}
