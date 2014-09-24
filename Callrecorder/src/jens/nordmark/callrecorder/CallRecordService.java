package jens.nordmark.callrecorder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallRecordService extends IntentService {

    static MyPhoneStateListener phoneListener;
    static TelephonyManager telephony;
    static ServiceReceiver callStateReceiver;

	public CallRecordService() {
		super("callrecordservicenamn");

	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.d("DEBUG", "CallRecordService.onHandleIntent");
        
    	telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	telephony.listen(new MyPhoneStateListener(this), PhoneStateListener.LISTEN_CALL_STATE);
        
        callStateReceiver = new ServiceReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(callStateReceiver,new IntentFilter());
        getApplicationContext().registerReceiver(callStateReceiver,new IntentFilter());
        
        BootReceiver bootReceiver = new BootReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(bootReceiver,new IntentFilter(Intent.ACTION_BOOT_COMPLETED));
	}

}
