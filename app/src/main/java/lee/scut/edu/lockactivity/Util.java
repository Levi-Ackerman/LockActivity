package lee.scut.edu.lockactivity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lizhengxian on 16/10/19.
 */

public class Util {
    public static void saveToPreference(Context context, String key,String value) {
        SharedPreferences preferences = context.getSharedPreferences("LockActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getStringFromPreference(Context context,String key,String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("LockActivity", Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
}
