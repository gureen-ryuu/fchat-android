package cc.pat.fchat;

import cc.pat.fchat.objects.Commands;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends FragmentActivity{

	Button orsButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		orsButton = (Button) findViewById(R.id.orsButton);
		orsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FApp.getInstance().mBoundService.sendCommand(Commands.CHA);
			}
		});
	}
}
