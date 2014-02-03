package lab.prodigy.socionet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lab.prodigy.socionet.LaunchpadSectionFragment.DownloadFriendListProPicTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Wall extends Activity implements OnClickListener{

	private String username="";
	private String toUserID,isFriend,userID;
	private String[] tmp=new String[7];
	private TextView usernameText,collegeText,cityText,statusText;
	private Button addFriend,accept,reject,messageButton;
	private File SocioNetDirectory;
	private Context current;
	private String proPic="NOT";
	private ImageView profilePic;
	private SharedPreferences settings;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.wall);
		current=this;
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		usernameText=(TextView)findViewById(R.id.username);
		collegeText=(TextView)findViewById(R.id.College);
		cityText=(TextView)findViewById(R.id.City);
		statusText=(TextView)findViewById(R.id.Status);
		addFriend=(Button)findViewById(R.id.addFriendBt);
		accept=(Button)findViewById(R.id.accept);
		reject=(Button)findViewById(R.id.reject);
		messageButton=(Button)findViewById(R.id.messageButton);
		profilePic=(ImageView)findViewById(R.id.pro_pic);
		addFriend.setOnClickListener(this);
		accept.setOnClickListener(this);
		reject.setOnClickListener(this);
		messageButton.setOnClickListener(this);
		Intent callerAct=getIntent();
		
		username=callerAct.getStringExtra("Username");
		toUserID=callerAct.getStringExtra("toUserID");
		userID=callerAct.getStringExtra("UserID");
		isFriend=callerAct.getStringExtra("IsFriend");
		
		addFriend.setVisibility(View.GONE);
		usernameText.setText(username);
		new DownloadDataTask().execute();
		new DownloadFriendListProPicTask().execute();
		/*if(!proPic.equals("NOT")){
			File imageFile = new File("/sdcard/SocioNet/pro_"+toUserID+".jpg");
			System.out.println(userID);
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			profilePic.setImageBitmap(bitmap);
		}*/
	}
	private class DownloadDataTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//---------------------------------IP TO BE CHANGED
		    try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("REQUEST",
		          "PROFILE"));
		      nameValuePairs.add(new BasicNameValuePair("UID",userID));
		      nameValuePairs.add(new BasicNameValuePair("TOUSERID",toUserID));
		      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 
		      HttpResponse response = client.execute(post);
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		      String line="";
		      
		      while ((line = rd.readLine()) !=null ) {
		        
		        tmp=line.split(":");
		      }
		    }
		    catch(Exception e)
		    {
		    	
		    }
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			cityText.setText(tmp[1]);
			collegeText.setText(tmp[2]);
			statusText.setText(tmp[4]);
			if(tmp[5].equals("0"))
			{
				addFriend.setVisibility(View.VISIBLE);
				addFriend.setText("Add Friend");
			}
			else if(tmp[5].equals("2"))
			{
				addFriend.setVisibility(View.VISIBLE);
				addFriend.setText("Request Sent");
				addFriend.setClickable(false);
			}
			else if(tmp[5].equals("3"))
			{
				addFriend.setVisibility(View.GONE);
				accept.setVisibility(View.VISIBLE);
				reject.setVisibility(View.VISIBLE);
			}
			
			proPic=tmp[6];
			System.out.println("\n\n"+proPic+"\n\n");
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.addFriendBt)
		{
			new AddFriendTask().execute();
			addFriend.setClickable(false);
		}
		else if(v.getId()==R.id.accept)
		{
			new AcceptReject().execute("1");
			reject.setClickable(false);
			accept.setClickable(false);
		}
		else if(v.getId()==R.id.reject)
		{
			new AcceptReject().execute("0");
			reject.setClickable(false);
			accept.setClickable(false);
		}
		else if(v.getId()==R.id.messageButton)
		{
			Intent message=new Intent(current,Message.class);
			message.putExtra("toUserName", username);
			message.putExtra("toUserID", toUserID);
			message.putExtra("UID", userID);
			current.startActivity(message);
		}
	}
	private class AddFriendTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String line="";
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//---------------------------------IP TO BE CHANGED
		    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			      nameValuePairs.add(new BasicNameValuePair("REQUEST",
			          "ADDFRIEND"));
			      nameValuePairs.add(new BasicNameValuePair("UID",userID));
			      nameValuePairs.add(new BasicNameValuePair("TOUSERID",toUserID));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      
			      
			      while ((line = rd.readLine()) !=null ) {
			    	  System.out.println(line);
			    	  return line;
			      }
		    }
		    catch(Exception e)
		    {
		    	
		    }
		    return line;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if(result.equals("ADDED"))
			{
				addFriend.setVisibility(View.VISIBLE);
				addFriend.setText("Request Sent");
				addFriend.setClickable(false);
			}
			else
			{
				addFriend.setClickable(true);
			}
		}
		
	}
	private class AcceptReject extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String line="";
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//---------------------------------IP TO BE CHANGED
		    try {
			      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			      nameValuePairs.add(new BasicNameValuePair("REQUEST",
			          "ACCEPTREQUEST"));
			      nameValuePairs.add(new BasicNameValuePair("UID",userID));
			      nameValuePairs.add(new BasicNameValuePair("FROMUSERID",toUserID));
			      nameValuePairs.add(new BasicNameValuePair("ACCEPTORREJECT",params[0]));
			      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			 
			      HttpResponse response = client.execute(post);
			      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			      
			      
			      while ((line = rd.readLine()) !=null ) {
			    	  System.out.println(line);
			    	  return line;
			      }
		    }
		    catch(Exception e)
		    {
		    	
		    }
		    return line;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if(result.equals("REJECTED") || result.equals("ACCEPTED"))
			{
				accept.setVisibility(View.GONE);
				reject.setVisibility(View.GONE);
			}
			else
			{
				reject.setClickable(true);
				accept.setClickable(true);
			}
			if(result.equals("REJECTED"))
			{
				addFriend.setVisibility(View.VISIBLE);
				addFriend.setClickable(true);
			}
				
		}
		
	}
	public class DownloadFriendListProPicTask extends AsyncTask<Void, Void, String>
    {

    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// create a File object for the parent directory
			SocioNetDirectory = new File("/sdcard/SocioNet/");
			// have the object build the directory structure, if needed.
			if(!SocioNetDirectory.exists()) SocioNetDirectory.mkdirs();
		}
    	@Override
    	protected String doInBackground(Void... params) {
    		// TODO Auto-generated method stub
	    	try{
	    		HttpClient httpclient = new DefaultHttpClient();
	    			System.out.println("\n\nHELLO"+proPic+"HELLO\n\n");
	    			if(!proPic.equals("NOT")){
	    				File outputFile = new File(SocioNetDirectory,"pro_"+toUserID+".jpg");
	    				if(!outputFile.exists())
	    				{
	    					//System.out.println(i+"=user="+friendList.get(i).getProPic());
	    					//String IP=settings.getString("IP", "lab.prodigy.socionet");
				    		//HttpGet httpget = new HttpGet(IP+"profilepics/pro_"+toUserID+".jpg");//---------------------------------IP TO BE CHANGED
	    					String IP=settings.getString("IP", "lab.prodigy.socionet");
	    					IP=IP.concat("profilepics/pro_"+toUserID+".jpg");
				    		System.out.println(IP);
	    					HttpGet httpget = new HttpGet(IP);
	    					HttpResponse response = httpclient.execute(httpget);
				    		System.out.println("sent");
				    		HttpEntity entity = response.getEntity();
				    		//System.out.println("before entity");
				    		if (entity != null) 
				    		{
				    		    long contentSize = entity.getContentLength();
				    		    InputStream inputStream = entity.getContent();
				    		    //BufferedReader bf=new BufferedReader(new InputStreamReader(inputStream));
				    		    
				    		    // now attach the OutputStream to the file object, instead of a String representation
				    		    //System.out.println("Created");
				    		    if(!outputFile.exists()){
				    		    	FileOutputStream fos = new FileOutputStream(outputFile);
		    		    	        System.out.println("Content size ["+contentSize+"]");
		    		    	        BufferedInputStream bis = new BufferedInputStream(inputStream, 512);
		    		    	        byte[] data = new byte[(int) contentSize];
		    		    	        int bytesRead = 0;
		    		    	        int offset = 0;

					    		    while (bytesRead != -1 && offset < contentSize) {
					    		    	 bytesRead=bis.read(data, offset,(int)contentSize-offset);
					    		            offset += bytesRead;
					    		    }
					    		    fos.write(data,0,(int)contentSize);
				    		}
				    		//System.out.println("after entity");
				    	}
	    			}
		    	}
    	    }
    	    catch(Exception e)
    	    {
    	    	e.printStackTrace();
    	    }
    		return null;
    	}
    	protected void onPostExecute(String result) {
    		if(!proPic.equals("NOT")){
    			File imageFile = new File("/sdcard/SocioNet/pro_"+toUserID+".jpg");
    			//Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    			//profilePic.setImageBitmap(bitmap);
    			profilePic.setImageBitmap(
    				    SocioNet.decodeSampledBitmapFromResource(imageFile.getAbsolutePath(), 100, 100));
    		}
    	}
    	
    }
}
