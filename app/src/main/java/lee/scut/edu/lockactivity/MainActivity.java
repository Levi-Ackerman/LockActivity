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
import android.view.WindowManager;
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
    String TAG = "lee..";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLauncherList = (ListView) findViewById(R.id.lt_launcher);
        startService(new Intent(this, ScreenOnListenerService.class));

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        mLaunchers = new ArrayList<BaseLauncher>();

        for (ResolveInfo info : resolveInfos) {
            String packageName = info.activityInfo.packageName;
            if (!getPackageName().equals(packageName)) {
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
                Util.saveToPreference(getApplicationContext(), Common.PACKAGE_NAME, launcher.packageName);
                Util.saveToPreference(getApplicationContext(), Common.ACTIVITY_NAME, launcher.activityName);
            }
        });
        getDefaultHome();

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
    private void getDefaultHome() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            Log.d(TAG, "resolveActivity--->activityInfo null");
            // should not happen. A home is always installed, isn't it?
        } else if (res.activityInfo.packageName.equals("android")) {
            // No default selected
            Log.d(TAG, "resolveActivity--->无默认设置");
        } else {
            // res.activityInfo.packageName and res.activityInfo.name gives
            // you the default app
            Log.d(TAG, "默认桌面为：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
        }
    }
}
