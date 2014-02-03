package lab.prodigy.socionet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lab.prodigy.socionet.SocioNet.DownloadFriendListProPicTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LaunchpadSectionFragment extends Fragment {
	private String username="";
	private String toUserID="-1",isFriend,userID;
	private String[] tmp=new String[7];
	private TextView editLink,usernameText,collegeText,cityText,statusText;
	Fragment caller;
	private String proPic="YES";
	private File SocioNetDirectory;
	private ImageView profilePic;
	private SharedPreferences settings;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        Bundle args = getArguments();
        caller=this;
        username=args.getString("USERNAME");
        userID=((Integer)args.getInt("USERID")).toString();
        return rootView;
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		usernameText=(TextView)this.getView().findViewById(R.id.username);
		collegeText=(TextView)this.getView().findViewById(R.id.College);
		cityText=(TextView)this.getView().findViewById(R.id.City);
		statusText=(TextView)this.getView().findViewById(R.id.Status);
		editLink=(TextView)this.getView().findViewById(R.id.editProfile);	
        editLink.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edit=new Intent(caller.getActivity(),EditProfile.class);
				edit.putExtra("UserID",userID);
				caller.getActivity().startActivity(edit);
			}
		});
        profilePic=(ImageView)this.getView().findViewById(R.id.pro_pic);	
		usernameText.setText(username);
		new DownloadDataTask().execute();
		new DownloadFriendListProPicTask().execute();
		/*if(!proPic.equals("NOT")){
			File imageFile = new File("/sdcard/SocioNet/pro_2"+".jpg");
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
		    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
		    try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("REQUEST",
		          "PROFILE"));
		      nameValuePairs.add(new BasicNameValuePair("UID",toUserID));
		      nameValuePairs.add(new BasicNameValuePair("TOUSERID",userID));
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
			proPic=tmp[5];
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
	    			if(!proPic.equals("NOT")){
	    				File outputFile = new File(SocioNetDirectory,"pro_"+userID+".jpg");
	    				if(!outputFile.exists())
	    				{
	    					//System.out.println(i+"=user="+friendList.get(i).getProPic());
				    		//HttpGet httpget = new HttpGet(settings.getString("IP", "lab.prodigy.socionet")+"profilepics/pro_"+userID+".jpg");
	    					String IP=settings.getString("IP", "lab.prodigy.socionet");
	    					IP=IP.concat("profilepics/pro_"+userID+".jpg");
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
    			File imageFile = new File("/sdcard/SocioNet/pro_"+userID+".jpg");
    			//Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    			profilePic.setImageBitmap(
    				    SocioNet.decodeSampledBitmapFromResource(imageFile.getAbsolutePath(), 100, 100));
    			//profilePic.setImageBitmap(bitmap);
    		}
    	}
    	
    }
}
