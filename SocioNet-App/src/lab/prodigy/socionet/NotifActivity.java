package lab.prodigy.socionet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NotifActivity extends Activity{
	
	private String username,body,type,fromUserID;
	TextView tBody,tFromUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifwindow);
		tFromUser=(TextView)findViewById(R.id.fUser);
		tBody=(TextView)findViewById(R.id.mBody);
		Intent callerAct=getIntent();
		
		username=callerAct.getStringExtra("Username");
		fromUserID=callerAct.getStringExtra("fromUserID");
		type=callerAct.getStringExtra("type");
		body=callerAct.getStringExtra("Body");
		tBody.setText(body);
		tFromUser.setText(username);
	}
}
