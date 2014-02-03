package lab.prodigy.socionet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

@SuppressLint("NewApi")
public class SocioNet extends FragmentActivity implements TextWatcher, OnClickListener, OnItemClickListener,ActionBar.TabListener, OnEditorActionListener{

	private DrawerLayout mDrawerLayout;
    private ListView friendResList,searchResList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ProgressBar searchProgress,fSearchProgress;
    private static int userID=1;
    private static String userName="Prashant";
    private List<User> friendList=new ArrayList<User>();
    private List<User> frinedListCopy=new ArrayList<User>();
    private List<User> tmp=new ArrayList<User>();
    private List<User> searchList=new ArrayList<User>();
    private List<User> searchListCopy=new ArrayList<User>();
    private File SocioNetDirectory;
    private SharedPreferences settings;
    //private List<MutualFriend> mutualList=new ArrayList<MutualFriend>();
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    //private Button searchUser;
    private EditText searchedUser,fSearchedUser;
    @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socio_net);
		//retTextView=(TextView)findViewById(R.id.TvRet);
		Intent caller=getIntent();
		userName=caller.getStringExtra("Username");
		userID=Integer.parseInt(caller.getStringExtra("UserID"));
		
		
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		// Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.contentPager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
		
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		friendResList = (ListView) findViewById(R.id.fSearchedRes);
		//searchUser=(Button)findViewById(R.id.searchBt);
        searchedUser=(EditText)findViewById(R.id.searchUser);
        fSearchedUser=(EditText)findViewById(R.id.fSearchUser);
        fSearchedUser.addTextChangedListener(this);
        searchedUser.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(!searchedUser.getText().toString().equals(""))
					new searchUserTask().execute(searchedUser.getText().toString());
			}
		});
        //searchedUser.setOnEditorActionListener(this);
        searchResList=(ListView)findViewById(R.id.searchedRes);
        searchProgress=(ProgressBar)findViewById(R.id.searchProgressBar);
        fSearchProgress=(ProgressBar)findViewById(R.id.fSearchProgressBar);
        
        //searchUser.setOnClickListener(this);
       // 
       // 
        getActionBar().setTitle(userName);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        friendResList.setAdapter(new FriendListAdapter(this,R.layout.drawer_list_item ,(ArrayList<User>)friendList));
        //searchResList.setAdapter(new ArrayAdapter<User>(this,
          //      android.R.layout.simple_list_item_1,searchList));
        
        searchResList.setAdapter(new FriendListAdapter(this,R.layout.drawer_list_item ,(ArrayList<User>)searchList));
        
        searchResList.setOnItemClickListener(this);
        
        friendResList.setOnItemClickListener(this);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            @SuppressLint("NewApi")
			public void onDrawerClosed(View view) {
            	if((!mDrawerLayout.isDrawerOpen(Gravity.END)) && (!mDrawerLayout.isDrawerOpen(Gravity.START)))
            		getActionBar().setTitle(userName);
            	invalidateOptionsMenu();
            }

            @SuppressLint("NewApi")
			public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                if(mDrawerLayout.isDrawerVisible(Gravity.START))
                	getActionBar().setTitle("SocioNet");
                if(mDrawerLayout.isDrawerVisible(Gravity.END))
                	getActionBar().setTitle("Friends");
            }
        };
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
		if(tmp.size()==0)
		{
			new DownloadFriendListTask().execute();
			new DownloadFriendListProPicTask().execute();
		}
	}

    private void selectItem(int position) {
    	//User word=(User) mDrawerList.getAdapter().getItem(position);
    	Intent wall=new Intent(this,Wall.class);
    	wall.putExtra("Username", friendList.get(position).toString());
    	wall.putExtra("toUserID", ((Integer)friendList.get(position).getUserId()).toString());
    	wall.putExtra("IsFriend", ((Integer)friendList.get(position).getIsFriend()).toString());
    	wall.putExtra("UserID", ((Integer)userID).toString());
    	startActivity(wall);
    	mDrawerLayout.closeDrawer(Gravity.END);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.socio_net, menu);
		return true;
	}
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(Gravity.END);
        menu.findItem(R.id.show_friendlist).setVisible(!drawerOpen);
    	
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NewApi")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        // Handle action buttons
    	switch(item.getItemId()) {
            case android.R.id.home:
            	if(mDrawerLayout.isDrawerOpen(Gravity.END))
            	{
            		mDrawerLayout.closeDrawer(Gravity.END);
            	}
            	mDrawerLayout.openDrawer(Gravity.START);
                return true;
            case R.id.show_friendlist:
            	if(mDrawerLayout.isDrawerOpen(Gravity.START))
            	{
            		mDrawerLayout.closeDrawer(Gravity.START);
            	}
            	 mDrawerLayout.openDrawer(Gravity.END);
                return true;
            case R.id.edit:
            	
            	return true;
            default:
        		return super.onOptionsItemSelected(item);
    		}
            	
    }
    
    private class DownloadFriendListTask extends AsyncTask<Void, Void, String>
    {

    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
    		fSearchProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}
    	@Override
    	protected String doInBackground(Void... params) {
    		// TODO Auto-generated method stub
    		String retLine = "";
    		HttpClient client = new DefaultHttpClient();
    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//-----------------------IP TO BE CHANGED
    	    try {
    	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    	      nameValuePairs.add(new BasicNameValuePair("REQUEST",
    	          "FRIENDLIST"));
    	      nameValuePairs.add(new BasicNameValuePair("UID",((Integer)userID).toString()));
    	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	 
    	      Log.d("Download friend","ready");
    	      HttpResponse response = client.execute(post);
    	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    	      String line="";
    	      String[] tmpS=new String[3];
    	      while ((line = rd.readLine()) !=null ) {
    	        
    	        tmpS=line.split(":");
    	        //System.out.println(tmpS[1]);
    	        User friend=new User();
    	        
    	        friend.setUsername(tmpS[0]);
    	        friend.setUserId(Integer.parseInt(tmpS[1]));
    	        friend.setIsFriend((Integer)1);
    	        friend.setMutFriendCount(0);
    	        tmpS[2]=tmpS[2].replace(".\\","");
    	        tmpS[2]=tmpS[2].replace("\\","/");
    	        //System.out.println(tmpS[2]);
    	        friend.setProPic(tmpS[2]);
    	        frinedListCopy.add(friend);
    	      }
    	    }
    	    catch(Exception e)
    	    {
    	    	e.printStackTrace();
    	    }
    		return retLine;
    	}
    	protected void onPostExecute(String result) {
    		//retTextView.setText(result);
    		ArrayAdapter<User> adapter = (ArrayAdapter<User>) friendResList.getAdapter();
    		friendList.clear();
    		adapter.notifyDataSetChanged();
    		for(int i=0;i< frinedListCopy.size();i++)
    		{
    			friendList.add(frinedListCopy.get(i));
    			tmp.add(frinedListCopy.get(i));
    		}
    		adapter.notifyDataSetChanged();
    	}
    	
    }
    public class DownloadFriendListProPicTask extends AsyncTask<Void, Void, String>
    {

    	@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
    		fSearchProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
			// create a File object for the parent directory
			SocioNetDirectory = new File(Environment.getExternalStorageDirectory().getPath()+"/SocioNet/");
			// have the object build the directory structure, if needed.
			if(!SocioNetDirectory.exists()) SocioNetDirectory.mkdirs();
		}
    	@Override
    	protected String doInBackground(Void... params) {
    		// TODO Auto-generated method stub
	    	try{
	    		HttpClient httpclient = new DefaultHttpClient();
	    		//System.out.println("In image download");
		    		for(int i=0;i<friendList.size();i++)
		    		{
		    			
		    			if(!friendList.get(i).getProPic().equals("NOT")){
		    				File outputFile = new File(SocioNetDirectory,"pro_"+friendList.get(i).getUserId()+".jpg");
		    				if(!outputFile.exists())
		    				{
		    					//System.out.println(i+"=user="+friendList.get(i).getProPic());
		    					String IP=settings.getString("IP", "lab.prodigy.socionet");
		    					IP=IP.concat(friendList.get(i).getProPic());
					    		System.out.println(IP);
		    					HttpGet httpget = new HttpGet(IP);//-------------------IP TO BE CHANGED
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
    	    }
    	    catch(Exception e)
    	    {
    	    	e.printStackTrace();
    	    }
    		return null;
    	}
    	protected void onPostExecute(String result) {
    		//retTextView.setText(result);
    		ArrayAdapter<User> adapter = (ArrayAdapter<User>) friendResList.getAdapter();
    		adapter.notifyDataSetChanged();

    		fSearchProgress.setVisibility(View.GONE);
    	}
    	
    }
    @Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
    	int i=0;
		
		//System.out.println("CHANGED");
		friendList.clear();
		if(s.length() !=0)
		{
			
			for(i=0;i< tmp.size();i++)
			{
				if((tmp.get(i).toString()).startsWith(s.toString()))
				{
					friendList.add(tmp.get(i));
				}
			}
		}
		else
		{
			for(i=0;i< tmp.size();i++)
			{
				friendList.add(tmp.get(i));
			}
		}
		@SuppressWarnings("unchecked")
		ArrayAdapter<User> adapter = (ArrayAdapter<User>) friendResList.getAdapter();
		adapter.notifyDataSetChanged();
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClick(View v) {
	}
	 private class searchUserTask extends AsyncTask<String, Void,Void>
	    {

	    	@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
	    		searchProgress.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}
			@Override
	    	protected Void doInBackground(String... params) {
	    		// TODO Auto-generated method stub
	    		String retLine = "";
	    		HttpClient client = new DefaultHttpClient();
	    	    HttpPost post = new HttpPost(settings.getString("IP", "lab.prodigy.socionet"));//------------------------IP TO BE CHANGED
	    	    try {
	    	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	    	      nameValuePairs.add(new BasicNameValuePair("REQUEST",
	    	          "SEARCHUSER"));
	    	      nameValuePairs.add(new BasicNameValuePair("UID",
	    	    		  ((Integer)userID).toString()));
	    	      nameValuePairs.add(new BasicNameValuePair("SEARCHUSERNAME",params[0]));
	    	      //System.out.println(params[0]);
	    	      post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    	 
	    	      HttpResponse response = client.execute(post);
	    	      BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    	      String line="";
	    	      String[] tmpS=new String[5];
	    	      int searchedId=0;
	    	      searchListCopy.clear();
	    	      //mutualList.clear();
	    	      while ((line = rd.readLine()) !=null ) {
	    	    	  //System.out.println(line);
	    	    	  if(line.contains("NO USER FOUND"))
	    	    	  {
	    	    		 
	    	    	  }
	    	    	  else
	    	    	  {	  tmpS=line.split(":");
		    	    	  if(tmpS[0].startsWith("@"))
		    	    	  {
		    	    		  User tmpU=new User();
		    	    		  tmpS[0]=tmpS[0].substring(1,tmpS[0].length());
		    	    		  searchedId=Integer.parseInt(tmpS[0]);
		    	    		  tmpU.setUserId(searchedId);
		    	    		  tmpU.setUsername(tmpS[1]);
		    	    		  tmpU.setIsFriend(Integer.parseInt(tmpS[2]));
		    	    		  tmpU.setMutFriendCount(Integer.parseInt(tmpS[3]));
		    	    		  tmpS[4]=tmpS[4].replace(".\\","");
		    	    	      tmpS[4]=tmpS[4].replace("\\","/");
		    	    		  tmpU.setProPic(tmpS[4]);
		    	    		  searchListCopy.add(tmpU);
		    	    	  }
		    	    	 /* else
		    	    	  {
		    	    		  MutualFriend tmpU=new MutualFriend();
		    	    		  tmpU.setUserId(Integer.parseInt(tmpS[1]));
		    	    		  tmpU.setUsername(tmpS[0]);
		    	    		  tmpU.setMutualOf(searchedId);
		    	    		  mutualList.add(tmpU);
		    	    	  }*/
	    	      	}
	    	      }
	    	    }
	    	    catch(Exception e)
	    	    {
	    	    	
	    	    }
				return null;
	    	}
	    	protected void onPostExecute(Void result) {
	    		//retTextView.setText(result);
	    		ArrayAdapter<User> adapter = (ArrayAdapter<User>) searchResList.getAdapter();
	    		searchList.clear();
	    		adapter.notifyDataSetChanged();
	    		for(int i=0;i< searchListCopy.size();i++)
	    		{
	    			searchList.add(searchListCopy.get(i));
	    		}
	    		adapter.notifyDataSetChanged();
	    		new searchUserProPicTask().execute();
	    	}
	    	
	    }
	 
	 public class searchUserProPicTask extends AsyncTask<Void, Void, String>
	    {

	    	@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
	    		searchProgress.setVisibility(View.VISIBLE);
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
		    		//System.out.println("In image download");
			    		for(int i=0;i<searchList.size();i++)
			    		{
			    			
			    			if(!searchList.get(i).getProPic().equals("NOT")){
			    				File outputFile = new File(SocioNetDirectory,"pro_"+searchList.get(i).getUserId()+".jpg");
			    				if(!outputFile.exists())
			    				{
			    					//System.out.println(i+"=user="+friendList.get(i).getProPic());
						    		//HttpGet httpget = new HttpGet(settings.getString("IP", "lab.prodigy.socionet")+searchList.get(i).getProPic());//-----------------IP TO BE CHANGED
			    					String IP=settings.getString("IP", "lab.prodigy.socionet");
			    					IP=IP.concat(searchList.get(i).getProPic());
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
	    	    }
	    	    catch(Exception e)
	    	    {
	    	    	e.printStackTrace();
	    	    }
	    		return null;
	    	}
	    	protected void onPostExecute(String result) {
	    		//retTextView.setText(result);
	    		ArrayAdapter<User> adapter = (ArrayAdapter<User>) searchResList.getAdapter();
	    		adapter.notifyDataSetChanged();

	    		searchProgress.setVisibility(View.GONE);
	    	}
	    	
	    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		// TODO Auto-generated method stub
		if(((View)v.getParent()).getId()==R.id.searchedRes)
		{
			Intent wall=new Intent(this,Wall.class);
			//System.out.println(searchList.get(position).toString());
	    	wall.putExtra("Username", searchList.get(position).toString());
	    	wall.putExtra("toUserID", ((Integer)searchList.get(position).getUserId()).toString());
	    	wall.putExtra("UserID", ((Integer)userID).toString());
	    	wall.putExtra("IsFriend", ((Integer)searchList.get(position).getIsFriend()).toString());
	    	startActivity(wall);
	    	mDrawerLayout.closeDrawer(Gravity.START);
		}
		else if(((View)v.getParent()).getId()==R.id.fSearchedRes)
		{
			friendResList.setItemChecked(position, true);
	        selectItem(position);
		}
	}
	
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
        	android.support.v4.app.Fragment fragment;
        	Bundle args = new Bundle();
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                	fragment = new LaunchpadSectionFragment();
                	
                    args.putString("USERNAME",userName);
                    args.putInt("USERID", userID);
                    fragment.setArguments(args);
                    return fragment;

                default:
                    // The other sections of the app are dummy placeholders.
                	 fragment = new NotificationFragment();
                	 args.putString("USERNAME",userName);
                     args.putInt("USERID", userID);
                     fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0)
            {
            	return "Profile";
            }
            return "Notifications";
        }
    }

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
            	if(!searchedUser.getText().toString().equals(""))
            		new searchUserTask().execute(searchedUser.getText().toString());
            }    
            return false;
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }
	
	    return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(String path,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    //BitmapFactory.decodeResource(res, resId, options);
	    BitmapFactory.decodeFile(path, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
	}
}
