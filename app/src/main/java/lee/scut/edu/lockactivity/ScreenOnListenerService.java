package lee.scut.edu.lockactivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class ScreenOnListenerService extends Service {
    TelephonyManager telephonyManager;
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
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state){
                    case TelephonyManager.CALL_STATE_IDLE:
                        //空闲
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //响铃
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //挂机
                        break;
                }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);
        return super.onStartCommand(intent, flags, startId);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
                Intent in = new Intent(context,HomeActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra(Common.JUMP_FROM,Common.JUMP_FROM_SCREEN_LISTENER);
                startActivity(in);
            }
        }
    };
}
