package lab.prodigy.socionet;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
class FriendListAdapter extends ArrayAdapter<User>
{
	Context context; // Stores the context where the list is to be shown
    int layoutResourceId;// Stores the layout ID of the custom layout of the row
    ArrayList<User> data = null;// stores the data for the list
    
    //Constructor for the class
    public FriendListAdapter(Context context, int layoutResourceId, ArrayList<User> data) 
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
        userHolder holder = null;
        
        if(row == null)
        {
        	//Caliing the layput inflater to get the row layout from the
        	//XML file which is specified in the layoutResourceId
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new userHolder();
            holder.userName = (TextView)row.findViewById(R.id.userNameList);	
            holder.proPic=(ImageView)row.findViewById(R.id.proPic);
            //as row is null therefore sets the view with the text box id's
            row.setTag(holder);
            
            
        }
        else
        {
        	//as row is already set , therefore getting back the row's holder 
            holder = (userHolder)row.getTag();
        }
        
        //setting the holder with data
        User user = getItem(position);
        holder.userName.setText(user.toString());
        if(!user.getProPic().equals("NOT"))
        {
        	System.out.println("is not NOT="+user.getUserId());
        	File imageFile = new File("/sdcard/SocioNet/pro_"+user.getUserId()+".jpg");
        	if(imageFile.exists()) 
        	{
        		System.out.println("exist"+user.getUserId());
        		//Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        		//holder.proPic.setImageBitmap(bitmap);
        		holder.proPic.setImageBitmap(
    				    SocioNet.decodeSampledBitmapFromResource(imageFile.getAbsolutePath(), 100, 100));
        	}
        }
        else
        {
        	holder.proPic.setImageResource(R.drawable.profile_sam);
        }
        return row;
    }
    
	 //Holder class
	  class userHolder
	  {
	  	TextView userName;
	  	ImageView proPic;
	  }
    
}

	 