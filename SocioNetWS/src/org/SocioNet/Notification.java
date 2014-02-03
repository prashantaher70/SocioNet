package org.SocioNet;

public class Notification {
	private String type;
	private int userID;
	private String userName;
	private String body;
	public String getType()
	{
		return type;
	}
	public String getUserName()
	{
		return userName;
	}
	public String getBody()
	{
		return body;
	}
	public int getUserID()
	{
		return userID;
	}
	public void setType(String inp)
	{
		type=inp;
	}
	public void setUserName(String inp)
	{
		userName=inp;
	}
	public void setBody(String inp)
	{
		body=inp;
	}
	public void setUserID(int inp)
	{
		userID=inp;
	}
}
