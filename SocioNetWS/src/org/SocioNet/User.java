package org.SocioNet;

public class User {
	private String userName;
	private int userID;
	private boolean isFriend;
	private int noOfMutual=0;
	public User()
	{
		isFriend=false;
	}
	public String getUserName()
	{
		return userName;
	}
	public int getUserID()
	{
		return userID;
	}
	public boolean chkFriend()
	{
		return isFriend;
	}
	public void setUserName(String inp)
	{
		userName=inp;
	}
	public void setUserID(int inp)
	{
		userID=inp;
	}
	public void setFriend(boolean b) {
		// TODO Auto-generated method stub
		isFriend=b;
	}
	public void setNoOfMutual(int inp)
	{
		noOfMutual=inp;
	}
	public int getNoOfMutual()
	{
		return noOfMutual;
	}
}
