/*
 * Listen for changes in the CallState, then broadcast that a change has occured.
 */

package jens.nordmark.callrecorder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {

    public static Boolean phoneRinging = false;
    private static Context theContext;

    /*
     * Constructor
     */
    public MyPhoneStateListener(Context context){
    	Log.d("DEBUG", "MyPhoneStateListener constructor");
    	theContext = context;
    }
    
    /*
     * This is invoked when the call state has changed.
     */
    public void onCallStateChanged(int state, String incomingNumber) {
    	
        Intent callIntent = new Intent();
        callIntent.setAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        LocalBroadcastManager.getInstance(theContext).sendBroadcast(callIntent);
        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            Log.d("DEBUG", "IDLE");
            phoneRinging = false;
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
            Log.d("DEBUG", "OFFHOOK");
            phoneRinging = false;
            break;
        case TelephonyManager.CALL_STATE_RINGING:
            Log.d("DEBUG", "RINGING");
            phoneRinging = true;
            break;
        }
    }

}