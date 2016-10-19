package lee.scut.edu.lockactivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ScreenOnListenerService extends Service {
    public ScreenOnListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
        return super.onStartCommand(intent, flags, startId);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                Intent in = new Intent(context,MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra(Common.JUMP_FROM,Common.JUMP_FROM_SCREEN_LISTENER);
                startActivity(in);
            }
        }
    };
}
