package lee.scut.edu.lockactivity;

import android.app.Activity;
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
    String TAG = "lee..";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, ScreenOnListenerService.class));
        findViewById(R.id.set_default_launcher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultHome();
            }
        });
    }



    private void setDefaultHome() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            Log.d(TAG, "resolveActivity--->activityInfo null");
            // should not happen. A home is always installed, isn't it?
        } else if (res.activityInfo.packageName.equals("android")) {
            // No default selected
            Log.d(TAG, "resolveActivity--->无默认设置");
        } else if (res.activityInfo.packageName.equals(getPackageName())){
            // res.activityInfo.packageName and res.activityInfo.name gives
            // you the default app
            Log.d(TAG, "默认桌面为我们的锁屏Activity：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
            Toast.makeText(MainActivity.this, "默认设置已经完成,可以直接使用", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "默认桌面为其他的锁屏应用：" + res.activityInfo.packageName + "."
                    + res.activityInfo.name);
            Toast.makeText(getApplicationContext(),"默认锁屏桌面是其他应用",Toast.LENGTH_SHORT).show();
        }
    }
}
