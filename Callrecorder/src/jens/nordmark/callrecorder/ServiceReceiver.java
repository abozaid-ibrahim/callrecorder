/*
 * This class receives the telephony broadcasts and starts recording when a call is detected.
 */

package jens.nordmark.callrecorder;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ServiceReceiver extends BroadcastReceiver {
    private static MediaRecorder audioRecorder;
    private static boolean isRecording;
    private static boolean isCalling;
    private static String phoneNumber;
    private static TelephonyManager telephony;

    /*
     * Gets a Mediarecorder instance unless one is already in use.
     */
    public static MediaRecorder getAudioRecorder() {
        if (audioRecorder == null) {
        	audioRecorder = new MediaRecorder();
        }
        return audioRecorder;
    }
    
    /*
     * Just a constructor.
     */
    public ServiceReceiver(){
    	Log.d("DEBUG", "ServiceReceiver Constructor");
    }
    
    /*
     * Here the broadcasts relating to telephony state changes are handled and the audio recorded and saved.
     */
	public void onReceive(Context context, Intent intent) {
        String message = intent.getAction();
        Log.d("receiver", "Got message: " + message);
        
    	telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        
    	//Checking if the call is outgoing, if so save the phone number and set the flag isCalling.
        if(intent.getAction() == Intent.ACTION_NEW_OUTGOING_CALL){
        	phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        	isCalling = true;
        }
        //If there is a call but it's not outgoing, it must be an incoming call. If so, save the number.
    	if(!isCalling && (telephony.getCallState() == TelephonyManager.CALL_STATE_RINGING)){
    		phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
    	}
    	
    	//When the telephony state is "offhook" the phonecall is underway and the recording is started.
    	if(telephony.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
    		Log.d("DEBUG", "Startrecording");
    		isRecording = true;
    		Log.d("DEBUG", "phone number " + phoneNumber);

    		//Get time and date
    		Calendar rightNow = Calendar.getInstance();
    		String date = String.valueOf(rightNow.get(Calendar.YEAR))+"."+String.valueOf(rightNow.get(Calendar.MONTH))+"."+String.valueOf(rightNow.get(Calendar.DAY_OF_MONTH))+"-"+String.valueOf(rightNow.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(rightNow.get(Calendar.MINUTE))+":"+String.valueOf(rightNow.get(Calendar.SECOND));
    		String fileName = "date" + date + "nr" + phoneNumber + ".mp4";
    		File dir = getDirectory(context);
    		File file = new File(dir, fileName);
        	//Log.d("FILES DIR", file.getAbsolutePath());
        	
    		//set up the mediarecorder
        	MediaRecorder audio = getAudioRecorder();
        	audio.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        	audio.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            audio.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            audio.setOutputFile(file.getPath());
            try {
                audio.prepare();
            }
            catch (IOException e) {
                Log.d("MediaRecorder", "prepare() failed");
                e.printStackTrace();
            }
    		audio.start();
    	}
    	//When the callstate is IDLE there is no call in progress and any recording should thus be stopped.
    	if((telephony.getCallState() == TelephonyManager.CALL_STATE_IDLE)&&(isRecording)) {
    		Log.d("DEBUG", "Stoprecording");
    		MediaRecorder audio = getAudioRecorder();
    		try{
    		audio.stop();
    		}
    		catch(RuntimeException e){
    			e.printStackTrace();
    		}
    		finally{
    		audio.reset();
    		isRecording = false;
    		isCalling = false;
    		phoneNumber = null;
    		}
    	}    
    }
	
	/*
	 * Returns the directory where the app stores all its files
	 */
	public File getDirectory(Context context) {
	    File file = new File(Environment.getExternalStorageDirectory(), "CallRecorder");
	    if (!file.mkdirs()) {
	        //Log.e("getFile Debug", "Directory not created");
	    }
	    return file;
	}
	
	/*
	 * Checks if external storage is available, returns true if that is the case, false otherwise.
	 */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
    
    public void onCallStateChanged(){
    	//Log.d("DEBUG", "ServiceReceiver.onCallStateChanged");
    }
    
    public void onDestroy() {
        //audio.release();
    	//Log.d("DEBUG", "ServiceReceiver.onDestroy");
    }

}