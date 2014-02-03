package lab.prodigy.socionet;

public class SoNotification {
	private String username;
	private int userId;
	private String type;
	private String messageBody;
	public void setUsername(String Username)
	{
		username=Username;
	}
	public void setUserId(int UserId)
	{
		userId=UserId;
	}
	public void setType(String Type)
	{
		type=Type;
	}
	public String toString()
	{
		return username;
	}
	public int getUserId()
	{
		return userId;
	}
	public String getType()
	{
		return type;
	}
	public String getMessageBody()
	{
		return messageBody;
	}
	public void setMessageBody(String m)
	{
		messageBody=m;
	}
}
