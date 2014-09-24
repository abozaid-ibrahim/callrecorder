/*
 * This is the main activity that brings up the instruction text and starts up the callrecordservice.
 */

package jens.nordmark.callrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	public Intent callRecordIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		callRecordIntent = new Intent(this,CallRecordService.class);
		callRecordIntent.setData(null);
	
		try{
			this.startService(callRecordIntent);
		}
		catch(SecurityException e){
		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText("jens.nordmark.callrecorder.SECURITY_EXCEPTION_MESSAGE");
		    setContentView(textView);
		}
		catch(Exception e){
		    TextView textView = new TextView(this);
		    textView.setTextSize(40);
		    textView.setText("jens.nordmark.callrecorder.UNKNOWN_EXCEPTION_MESSAGE");
		    setContentView(textView);
		}
	}
	
	public void okButtonClick(View view){
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}
}
