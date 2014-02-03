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

import android.widget.EditText;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.view.View;
import android.util.Log;

public class SignUp extends Activity implements OnClickListener{
	public EditText UserName,age,Password,ConfirmPassword,cityName,collegeName,Gender;
	public Button SignUpButton;
	private Context current;
	public User user=new User();
	private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		current=this;
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		UserName=(EditText)findViewById(R.id.UserName);
		Password=(EditText)findViewById(R.id.Password);
		Gender=(EditText)findViewById(R.id.Gender);
		cityName=(EditText)findViewById(R.id.cityName);
		collegeName=(EditText)findViewById(R.id.collegeName);
		SignUpButton=(Button)findViewById(R.id.SignUpButton);
		ConfirmPassword=(EditText)findViewById(R.id.ConfirmPassword);
		age=(EditText)findViewById(R.id.Age);
		SignUpButton.setOnClickListener(this);
	}
	class SendSignupData extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
    	    System.out.println(settings.getString("IP", "lab.prodigy.socionet"));
    	    try
    	    {
    	    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    	      nameValuePairs.add(new BasicNameValuePair("REQUEST",
	    	          "SIGNUP"));
	    	      nameValuePairs.add(new BasicNameValuePair("USERNAME",
	    		          params[0]));
	    	      nameValuePairs.add(new BasicNameValuePair("PASSWORD",
	    		          params[1]));
	    	      nameValuePairs.add(new BasicNameValuePair("AGE",
	    		          params[2]));
	    	      nameValuePairs.add(new BasicNameValuePair("CITY",
	    		          params[3]));
	    	      nameValuePairs.add(new BasicNameValuePair("COLLEGE",
	    		          params[4]));
	    	      nameValuePairs.add(new BasicNameValuePair("GENDER",
	    		          params[5]));
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
			line=response;
  	      	String[] temp=new String[3];
  	      	if(line.contains("ALREADY SIGNED UP"))
  	    	  {
  	    		  System.out.println("You have already signed up\n");
  	    		  Toast newToast=Toast.makeText(current, "already signed up", Toast.LENGTH_SHORT);
  	    		  newToast.show();
  	    	  }
  	    	  else
  	    	  {
  	    		  temp=line.split(":");
  	    		  System.out.println(temp[1]);
  	    		  user.setUserId(Integer.parseInt(temp[1]));
  	    		  System.out.println(temp[1]);
  	    		  Toast newToast=Toast.makeText(current, "Your LoginID is :"+ user.getUserId(), Toast.LENGTH_SHORT);
  	    		  newToast.show();
  	    		  user.setUsername(temp[2]);
  	    		  Intent SendToHome=new Intent(current,SocioNet.class);
  	    		  SendToHome.putExtra("Username", temp[2]);
  	    		  SendToHome.putExtra("UserID",temp[1]);
  	    		  current.startActivity(SendToHome);
  	    	  }
			
    	}
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(!UserName.getText().toString().equals("") && !Password.getText().toString().equals("") && !ConfirmPassword.getText().toString().equals(""))
		{
			if(Password.getText().toString().equals(ConfirmPassword.getText().toString()))
			{
				System.out.println("inside\n");
				SendSignupData obj=new SendSignupData();
				obj.execute(UserName.getText().toString(),Password.getText().toString(),age.getText().toString(),cityName.getText().toString(),collegeName.getText().toString(),Gender.getText().toString());
			}
			else
			{
				System.out.println("Password and Confirm Password don't match!");
				Toast newToast=Toast.makeText(current, "dont match", Toast.LENGTH_SHORT);
				newToast.show();
			}
		}
		else
		{
			System.out.println("Please Fill all details!");
			Toast newToast=Toast.makeText(current, "fill all details", Toast.LENGTH_SHORT);
			newToast.show();
		}
		
	}
}