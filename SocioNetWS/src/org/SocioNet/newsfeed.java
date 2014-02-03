package org.SocioNet;

public class newsfeed {
	int userID;
	String userName;
	long timeStamp;
	String status;
	String type;
	public String getType()
	{
		return type;
	}
	public void setType(String Type)
	{
		type = Type;
	}
	public String getUserName()
	{
		return userName;
	}
	public int getUserID()
	{
		return userID;
	}
	public void setUserName(String inp)
	{
		userName=inp;
	}
	public void setUserID(int inp)
	{
		userID=inp;
	}
	public void setTimeStamp(long inp)
	{
		timeStamp=inp;
	}
	public long getTimeStamp()
	{
		return timeStamp;
	}
	public void setStatus(String Status)
	{
		status=Status;
	}
	public String getStatus()
	{
		return status;
	}
}
