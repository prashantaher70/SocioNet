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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import java.util.logging.*;

public class Login extends Activity implements OnClickListener {
		public EditText Password;
		public EditText UserId;
		private EditText ipF;
		public Button LoginButton;
		public Button SignUpButton;
		private Context current;
		SharedPreferences settings;
		public User user=new User();
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.login);
			
			settings = PreferenceManager.getDefaultSharedPreferences(this);
			

			current=this;
			UserId=(EditText)findViewById(R.id.UserId);
			ipF=(EditText)findViewById(R.id.ip);
			Password=(EditText)findViewById(R.id.Password);
			LoginButton=(Button)findViewById(R.id.LoginButton);
			LoginButton.setOnClickListener(this);
			SignUpButton=(Button)findViewById(R.id.SignUpButton);
			SignUpButton.setOnClickListener(this);
		}
		class SendLoginData extends AsyncTask<String, Void, String>
		{
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient();
	    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
	    	    try
	    	    {
	    	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		    	      nameValuePairs.add(new BasicNameValuePair("REQUEST",
		    	          "LOGIN"));
		    	      nameValuePairs.add(new BasicNameValuePair("UID",
		    		          params[0]));
		    	      nameValuePairs.add(new BasicNameValuePair("PASSWORD",
		    		          params[1]));
		    	      System.out.println(params[0]+" "+params[1]);
		    	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		    	      
		    	      HttpResponse response = client.execute(post);
		    	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				      String line="";
				      
				      while ((line = rd.readLine()) !=null ) {
				        
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
				String[] arr=new String[3];
				line=response;
				System.out.println(response);
	  	      	if(line.contains("SIGNEDIN"))
	  	    	  {
	  	      		  arr=line.split(":");
	  	    		  System.out.println("Welcome to Socionet\n");
	  	    		  Toast newToast=Toast.makeText(current, "Login Successful!\nWelcome "+arr[1], Toast.LENGTH_SHORT);
	  	    		  newToast.show();
	  	    		  Intent sendToHome=new Intent(current,SocioNet.class);
	  	    		  sendToHome.putExtra("Username", arr[1]);
	  	    		  sendToHome.putExtra("UserID",arr[2]);
	  	    		  current.startActivity(sendToHome);
	  	    	  }
	  	    	  else if(line.contains("USERDOESNOTEXIST"))
	  	    	  {
	  	    		  System.out.println("User does not exist\n");
	  	    		  Toast newToast=Toast.makeText(current, "Please SignUp first!\nUser does not exist", Toast.LENGTH_SHORT);
	  	    		  newToast.show();
	  	    	  }
	  	    	  else if(line.contains("WRONG PASSWORD"))
	  	    	  {
	  	    		  System.out.println("Password entered is Incorrect\n");
	  	    		  Toast newToast=Toast.makeText(current, "Login Failed!\nPassword entered is Incorrect", Toast.LENGTH_SHORT);
	  	    		  newToast.show();
	  	    	  }
	    	}
		}
		//@Override
		public void onClick(View v) 
		{
			if(v.getId()==R.id.LoginButton)
			{
				Log.d("avr", "in onclick");
				if(!UserId.getText().toString().equals("") && !Password.getText().toString().equals(""))
				{
					SendLoginData obj=new SendLoginData();
					obj.execute(UserId.getText().toString(),Password.getText().toString());
				}
				else
				{
					if(!ipF.getText().toString().equals(""))
					{
						Editor editor = settings.edit();
						editor.putString("IP", ipF.getText().toString());
						editor.commit();
						ipF.setText("http://");
					}
					System.out.println("Please Fill all details!!!");
					Toast newToast=Toast.makeText(current, "Fill all details", Toast.LENGTH_SHORT);
					newToast.show();
				}
			}
			else if(v.getId()==R.id.SignUpButton)
			{
				Intent sendToHome =new Intent(current,SignUp.class);
			    current.startActivity(sendToHome);
			}
			// TODO Auto-generated method stub
		}
}
