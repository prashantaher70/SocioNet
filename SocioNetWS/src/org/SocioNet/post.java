package org.SocioNet;

public class post {
	String content;
	int postUserID;
	String userName;
	long timeStamp;
	public void setPostUserID(int inp)
	{
		postUserID=inp;
	}
	public void setUserName(String UserName)
	{
		userName=UserName;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setTimeStamp(long inp)
	{
		timeStamp=inp;
	}
	public void setContent(String inp)
	{
		content=inp;
	}
	public String getContent()
	{
		return content;
	}
	public long getTimeStamp()
	{
		return timeStamp;
	}
	public int getPostUserID()
	{
		return postUserID;
	}
}
