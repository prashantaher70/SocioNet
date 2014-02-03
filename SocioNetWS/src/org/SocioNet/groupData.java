package org.SocioNet;

public class groupData {
	int groupID;
	String adminName;
	int adminID;
	String groupName;
	public String getAdminName()
	{
		return adminName;
	}
	public int getUserID()
	{
		return groupID;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public int getAdminID()
	{
		return adminID;
	}
	public void setGroupName(String inp)
	{
		groupName=inp;
	}
	public void setAdminName(String inp)
	{
		adminName=inp;
	}
	public void setGroupID(int inp)
	{
		groupID=inp;
	}
	public void setAdminID(int inp)
	{
		adminID=inp;
	}
}
