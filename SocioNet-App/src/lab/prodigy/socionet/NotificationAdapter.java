package lab.prodigy.socionet;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<SoNotification>{
	Context context; // Stores the context where the list is to be shown
    int layoutResourceId;// Stores the layout ID of the custom layout of the row
    ArrayList<SoNotification> data = null;// stores the data for the list
    
    //Constructor for the class
    public NotificationAdapter(Context context, int layoutResourceId, ArrayList<SoNotification> data) 
    {
        super(context, layoutResourceId, data);//Calls the super class constructor
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    
	@Override
    //Over riding the get view function so the appearance looks as we want
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        View row = convertView;
        notificationHolder holder = null;
        
        if(row == null)
        {
        	//Caliing the layput inflater to get the row layout from the
        	//XML file which is specified in the layoutResourceId
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new notificationHolder();
            holder.userName = (TextView)row.findViewById(R.id.frmUsername);
            holder.type = (TextView)row.findViewById(R.id.type);
            holder.NotImg=(ImageView)row.findViewById(R.id.NotImg);
            //as row is null therefore sets the view with the text box id's
            row.setTag(holder);
            
            
        }
        else
        {
        	//as row is already set , therefore getting back the row's holder 
            holder = (notificationHolder)row.getTag();
        }
        
        //setting the holder with data
        SoNotification user = data.get(position);
        holder.userName.setText(user.toString());
        if(user.getType().equals("MESSAGES"))
        {
        	holder.NotImg.setImageResource(R.drawable.f1);
        	holder.type.setText("Message");
        }
        else
        {
        	holder.NotImg.setImageResource(R.drawable.f2);
        	holder.type.setText("Friend Request");
        }
        return row;
    }
    
	 //Holder class
	  class notificationHolder
	  {
	  	TextView userName;
	  	TextView type;
	  	ImageView NotImg;
	  }
}
