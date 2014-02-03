package lab.prodigy.socionet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Message extends Activity implements OnClickListener {
	public Button sendMessageButton;
	public Button clearMessageButton;
	public EditText messageBodyEditText;
	public TextView toUserNameTextView;
	public String toUserID,fromUserID,toUserName;
	private Context current;
	private Intent caller;
	private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		sendMessageButton=(Button)findViewById(R.id.sendButton);
		clearMessageButton=(Button)findViewById(R.id.clearButton);
		toUserNameTextView=(TextView)findViewById(R.id.toUserName);
		messageBodyEditText=(EditText)findViewById(R.id.messageBodyEditText);
		current=this;
		caller=getIntent();
		Bundle args=caller.getExtras();
		toUserID=args.getString("toUserID");
		toUserName=args.getString("toUserName");
		fromUserID=args.getString("UID");
		sendMessageButton.setOnClickListener(this);
		clearMessageButton.setOnClickListener(this);
		toUserNameTextView.setText(toUserName);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.sendButton)
		{
			if(!messageBodyEditText.getText().toString().equals(""))
			{
				if(messageBodyEditText.getText().toString().contains(","))
				{
					//replace commas with '|'
				}
				Log.d("trial", "in send button");
				sendMessageData obj=new sendMessageData();
				obj.execute(fromUserID,toUserID,messageBodyEditText.getText().toString());
			}
		}
		else if(v.getId()==R.id.clearButton)
		{
			messageBodyEditText.setText("");
		}
	}
	class sendMessageData extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//-------------------IP TO BE CHANGED
    	    try
    	    {
    	    	Log.d("avr", "tried");
    	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    	      nameValuePairs.add(new BasicNameValuePair("REQUEST",
	    	          "SENDMESSAGE"));
	    	      nameValuePairs.add(new BasicNameValuePair("UID",
	    		          params[0]));
	    	      nameValuePairs.add(new BasicNameValuePair("TOUSERID",
	    		          params[1]));
	    	      nameValuePairs.add(new BasicNameValuePair("MESSAGEBODY",
	    		          params[2]));
	    	      System.out.println(params[0]+" "+params[1]);
	    	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	      
	    	      HttpResponse response = client.execute(post);
	    	      Log.d("avr",response.toString());
	    	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      String line="";
			      
			      while ((line = rd.readLine()) !=null ) 
			      {
			        return line;
			      }
    	    }
    	    catch(Exception e)
    	    {
    	    	e.printStackTrace();
    	    }
			return null;
		}
		
		protected void onPostExecute(String response) 
		{
			String line="";
			line=response;
			if(line.contains("MESSAGESENT"))
			{
				Toast newToast=Toast.makeText(current, "Message sent successfully", Toast.LENGTH_SHORT);
				newToast.show();
			}
			else
			{
				Toast newToast=Toast.makeText(current, "Message sending failed\nplease try aagain later", Toast.LENGTH_SHORT);
				newToast.show();
			}
			finish();
		}
		
	}
}
