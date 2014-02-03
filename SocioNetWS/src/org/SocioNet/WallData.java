package org.SocioNet;

public class WallData {
	private int age;
	private String userName;
	private int userID;//whose wall you want to see
	private boolean isFriend;
	private char gender;
	private String city;
	private String status;
	private String college;
	private String pending;
	public WallData()
	{
		isFriend=false;
	}
	public String getUserName()
	{
		return userName;
	}
	public String getPending()
	{
		return pending;
	}
	public String getCity()
	{
		return city;
	}
	public String getStatus()
	{
		return status;
	}
	public String getCollege()
	{
		return college;
	}
	public int getUserID()
	{
		return userID;
	}
	public char getGender()
	{
		return gender;
	}
	public int getAge()
	{
		return age;
	}
	public boolean chkFriend()
	{
		return isFriend;
	}
	public void setAge(int inp)
	{
		age=inp;
	}
	public void setUserName(String inp)
	{
		userName=inp;
	}
	public void setCity(String inp)
	{
		city=inp;
	}
	public void setStatus(String inp)
	{
		status=inp;
	}
	public void setCollege(String inp)
	{
		college=inp;
	}
	public void setUserID(int inp)
	{
		userID=inp;
	}
	public void setGender(char inp)
	{
		gender=inp;
	}
	public void setPending(String inp)
	{
		pending=inp;
	}
}
