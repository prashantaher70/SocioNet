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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NotificationFragment extends Fragment implements OnItemClickListener {
	ListView notificationList;
	private String username="";
	private String userID;
	private String[] tmp=new String[3];
	private SharedPreferences settings;
	private List<SoNotification> notificationListCont=new ArrayList<SoNotification>() ;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notifications, container, false);
        Bundle args = getArguments();
        username=args.getString("USERNAME");
        userID=((Integer)args.getInt("USERID")).toString();
        return rootView;
    }
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		notificationList=(ListView)this.getView().findViewById(R.id.notificationList);
		notificationList.setOnItemClickListener(this);
		notificationList.setAdapter(new NotificationAdapter(this.getActivity(),R.layout.notification_list_item ,(ArrayList<SoNotification>)notificationListCont));
		new DownloadNotificationTask().execute();
	}
	
	private class DownloadNotificationTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));
		    try {
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		      nameValuePairs.add(new BasicNameValuePair("REQUEST",
		          "NOTIFICATION"));
		      nameValuePairs.add(new BasicNameValuePair("UID",userID));
		      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		      System.out.println(userID);
		      HttpResponse response = client.execute(post);
		      Log.d("NOTIFICATION","\n\nSent\n\n");
		      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		      String line="";
		      
		      while ((line = rd.readLine()) !=null ) {
		        Log.d("NOTIFICATION",line);
		        tmp=line.split(":");
		        SoNotification not=new SoNotification();
		        not.setUsername(tmp[1]);
		        not.setType(tmp[0]);
		        System.out.println(tmp[0]);
		        not.setUserId(Integer.parseInt(tmp[2]));
		        not.setMessageBody(tmp[3]);
		        notificationListCont.add(not);
		      }
		    }
		    catch(Exception e)
		    {
		    	
		    }
		    Log.d("NOTIFICATION","\n\n\nReceived\n\n");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ArrayAdapter<SoNotification> adapter = (ArrayAdapter<SoNotification>) notificationList.getAdapter();
			adapter.notifyDataSetChanged();
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		if( notificationListCont.get(position).getType().toString().equals("MESSAGES"))
		{
			Intent notificationWindow=new Intent(this.getActivity(),NotifActivity.class);
			notificationWindow.putExtra("Username", notificationListCont.get(position).toString());
			notificationWindow.putExtra("fromUserID", ((Integer)notificationListCont.get(position).getUserId()).toString());
			notificationWindow.putExtra("Body", notificationListCont.get(position).getMessageBody().toString());
			notificationWindow.putExtra("type", notificationListCont.get(position).getType().toString());
			startActivity(notificationWindow);
		}
		else
		{
			Intent wall=new Intent(this.getActivity(),Wall.class);
	    	wall.putExtra("Username", notificationListCont.get(position).toString());
	    	wall.putExtra("toUserID", ((Integer)notificationListCont.get(position).getUserId()).toString());
	    	wall.putExtra("IsFriend", "0");
	    	wall.putExtra("UserID", userID);
	    	startActivity(wall);
		}
	}
}
