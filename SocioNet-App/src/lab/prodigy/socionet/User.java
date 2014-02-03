package lab.prodigy.socionet;

public class User {
	private String username;
	private int userId;
	private int isFriend;
	private int mutFriendCount;
	private String proPic;
	public void setUsername(String Username)
	{
		username=Username;
	}
	public void setUserId(int UserId)
	{
		userId=UserId;
	}
	public void setIsFriend(int IsFriend)
	{
		isFriend=IsFriend;
	}
	public String toString()
	{
		return username;
	}
	public int getUserId()
	{
		return userId;
	}
	public int getIsFriend()
	{
		return isFriend;
	}
	public int getMutFriendCount()
	{
		return mutFriendCount;
	}
	public void setMutFriendCount(int MutFriendCount)
	{
		mutFriendCount=MutFriendCount;
	}
	public String getProPic()
	{
		return proPic;
	}
	public void setProPic(String ProPic)
	{
		proPic=ProPic;
	}
}
