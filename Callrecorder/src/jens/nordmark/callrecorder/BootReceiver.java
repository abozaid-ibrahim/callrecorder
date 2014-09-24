/*
 * This class receives the boot_completed broadcast and restarts the callrecordservice.
 */

package jens.nordmark.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Log.d("DEBUG","Bootreceiver received BOOT_COMPLETED");
			Intent myIntent = new Intent(context, CallRecordService.class);
			context.startService(myIntent);
		}
	}

}
