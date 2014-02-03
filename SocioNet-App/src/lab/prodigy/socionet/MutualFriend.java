package lab.prodigy.socionet;

public class MutualFriend {
	private String username;
	private int userId;
	private int mutualOf;
	public void setUsername(String Username)
	{
		username=Username;
	}
	public void setUserId(int UserId)
	{
		userId=UserId;
	}
	public void setMutualOf(int mutualOF)
	{
		mutualOf=mutualOF;
	}
	public String toString()
	{
		return username;
	}
	public int getUserId()
	{
		return userId;
	}
	public int getMutualOf()
	{
		return mutualOf;
	}
}
