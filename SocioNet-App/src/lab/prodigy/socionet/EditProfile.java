package lab.prodigy.socionet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lab.prodigy.socionet.SocioNet.searchUserProPicTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class EditProfile extends Activity implements OnClickListener{

	private ImageButton browse,upload,editCity,editPass,editStatus,editCollege;
	private EditText eCity,ePassword,eStatus,eCollege;
	private int RESULT_LOAD_IMAGE;
	private String picturePath=null;
	private int UserId=1;
	private SharedPreferences settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		browse=(ImageButton)findViewById(R.id.bSelect);
		upload=(ImageButton)findViewById(R.id.bSendPic);
		editCity=(ImageButton)findViewById(R.id.bCity);
		editStatus=(ImageButton)findViewById(R.id.bStatus);
		editPass=(ImageButton)findViewById(R.id.bPass);
		//editCollege=(ImageButton)findViewById(R.id.bCollege);
		eCity=(EditText)findViewById(R.id.cCity);
		ePassword=(EditText)findViewById(R.id.cPass);
		eStatus=(EditText)findViewById(R.id.cStatus);
		//eCollege=(EditText)findViewById(R.id.cStatus);
		Intent caller=getIntent();
		UserId=Integer.parseInt(caller.getStringExtra("UserID"));
		browse.setOnClickListener(this);
		upload.setOnClickListener(this);
		editCity.setOnClickListener(this);
		editPass.setOnClickListener(this);
		editStatus.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(R.id.bSelect==v.getId())
		{
			Intent i = new Intent(
			Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
		else if(R.id.bSendPic==v.getId())
		{
			File SocioNetDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/SocioNet/");
			if(!SocioNetDirectory.exists()) SocioNetDirectory.mkdirs();
			InputStream in;
			OutputStream out;
			try {
				if(picturePath!=null &&( picturePath.contains(".jpg") || picturePath.contains(".JPG") || picturePath.contains(".png") || picturePath.contains(".PNG")))
				{
					in = new FileInputStream(picturePath);
					out = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/SocioNet/pro_"+((Integer)UserId).toString()+".jpg");
					byte[] buf = new byte[1024];
		            int len;
		            while ((len = in.read(buf)) > 0) {
		                out.write(buf, 0, len);
		            }
		            in.close();
		            out.close();

		    		new UploadFile().execute();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Select .JPG or .PNG image", Toast.LENGTH_LONG).show();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
		else if(R.id.bCity==v.getId())
		{
			new changeParams().execute("city",eCity.getText().toString());
		}
		else if(R.id.bStatus==v.getId())
		{
			new changeParams().execute("status",eStatus.getText().toString());
		}
		else if(R.id.bPass==v.getId())
		{
			new changeParams().execute("college",ePassword.getText().toString());
		}
	}
	
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	      
	     if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         picturePath = cursor.getString(columnIndex);
	         cursor.close();
	         ImageView proPic = (ImageView) findViewById(R.id.proPic);
	         //proPic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	         proPic.setImageBitmap(
 				    SocioNet.decodeSampledBitmapFromResource(picturePath, 100, 100));
	     }
	}
	private class UploadFile extends AsyncTask<Void,Void,Void>
    {
		private ProgressDialog dialog;
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.hide();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog =new ProgressDialog(EditProfile.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setIndeterminate(false);
			dialog.setMessage("Uploading Image");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.d("Upload","Started");
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
			Log.d("Upload","New Post");
	        try{
	        	File file = new File(Environment.getExternalStorageDirectory().getPath()+"/SocioNet/pro_"+((Integer)UserId).toString()+".jpg");
	        	Log.d("Upload","Got File");
	        	
	        	
	            MultipartEntity multiPartEntity = new MultipartEntity () ;
	            Log.d("Upload","Multipart created");
	            //The usual form parameters can be added this way
	            //multiPartEntity.addPart("File",new ByteArrayBody(baos.toByteArray(), R.drawable.ic_launcher)) ;
	            FileBody fileBody = new FileBody(file, "application/octect-stream") ;
	            multiPartEntity.addPart("Image", fileBody) ;
	            Log.d("Upload","Image put up");
	            /*Need to construct a FileBody with the file that needs to be attached and specify the mime type of the file. Add the fileBody to the request as an another part.
	            This part will be considered as file part and the rest of them as usual form-data parts*/
	            //FileBody fileBody = new FileBody(file, "application/octect-stream") ;
	            //multiPartEntity.addPart("attachment", fileBody) ;
	 
	            post.setEntity(multiPartEntity) ;
	            //Log.d("Upload","Entity Set");
	            HttpResponse response=client.execute(post);
	            Log.d("Upload","Executed");
	            new changeParams().execute("propic", ".\\profilepics\\pro_"+((Integer)UserId).toString()+".jpg");
	        }catch (UnsupportedEncodingException ex){
	            ex.printStackTrace() ;
	            Log.d("Upload","Failed");
	        } catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Upload","Failed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Upload","Failed");
			}
			return null;
		}
    	
    }
	private class changeParams extends AsyncTask<String, Void,Void>
    {

    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
    	protected Void doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String retLine = "";
    		System.out.println(params[0]+params[1]);
    		HttpClient client = new DefaultHttpClient();
    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
    	    try {
    	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    	      if(params[0].equals("city"))
    	      {
    	    	  nameValuePairs.add(new BasicNameValuePair("REQUEST","UPDATECITY"));
    	    	  nameValuePairs.add(new BasicNameValuePair("CITY",params[1]));
    	      }
    	      else if(params[0].equals("password"))
    	      {	nameValuePairs.add(new BasicNameValuePair("REQUEST",
          	          "UPDATEPASSWORD"));
    	      nameValuePairs.add(new BasicNameValuePair("PASSWORD",params[1]));
    	      }
    	      else if(params[0].equals("status"))
    	      {    	nameValuePairs.add(new BasicNameValuePair("REQUEST",
    	                "UPDATESTATUS"));
    	      nameValuePairs.add(new BasicNameValuePair("STATUS",params[1]));
    	      }
    	      else if(params[0].equals("college"))
    	      {    	nameValuePairs.add(new BasicNameValuePair("REQUEST",
            	          "UPDATECOLLEGE"));
    	      nameValuePairs.add(new BasicNameValuePair("COLLEGE",params[1]));
    	      }
    	      else if(params[0].equals("propic"))
    	      {    	nameValuePairs.add(new BasicNameValuePair("REQUEST",
            	          "UPDATEPIC"));
    	      nameValuePairs.add(new BasicNameValuePair("PATH",params[1]));
    	      }
    	      nameValuePairs.add(new BasicNameValuePair("UID",
    	    		  ((Integer)UserId).toString()));
    	      
    	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	 
    	      HttpResponse response = client.execute(post);
    	    }
    	    catch(Exception e)
    	    {
    	    	
    	    }
			return null;
    	}
    	protected void onPostExecute(Void result) {
    		//retTextView.setText(result);
    	}
    	
    }
}
