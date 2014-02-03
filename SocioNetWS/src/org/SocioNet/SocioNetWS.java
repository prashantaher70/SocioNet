package org.SocioNet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;

@WebService(endpointInterface = "org.SocioNet.SocioNetWSSEI")
public class SocioNetWS {
	public List<User> getFriendList(int userId)
	{
		List<User> ret=new ArrayList<User>();
		List<String> data;
		String[] str = new String[3];
		String out = ServerRunner.PrintFriendList(userId);
		data = Arrays.asList(out.split("\n"));
		for(int i=0;i<data.size();i++)
		{
			User temp = new User();
			str=data.get(i).split(":");
			temp.setUserName(str[0]);
			temp.setUserID(Integer.parseInt(str[1]));
			temp.setFriend(true);
			ret.add(temp);
		}
		return ret;
	}

	public   String PrintMutualFriends(int UID,int TOUSERID)
	{
		return ServerRunner.PrintMutualFriends(UID, TOUSERID);
	}
	public   WallData LoadProfile(int UID,int TOUSERID){
		WallData temp=new WallData();
		String out=ServerRunner.LoadProfile(UID, TOUSERID);
		String[] str = new String[6];
		str=out.split(":");
		temp.setAge(Integer.parseInt(str[0]));
		temp.setCity((str[1]));
		temp.setCollege(str[2]);
		temp.setGender(str[3].charAt(0));
		temp.setStatus(str[4]);
		switch(Integer.parseInt(str[5]))
		{
		case 0:
				temp.setPending("NFRNS");
				break;
		case 1:
				temp.setPending("FRIEND");
				break;
		case 2:
				temp.setPending("RAS");
				break;
		case 3:
				temp.setPending("HFR");
				break;
		default:
				temp.setPending("SELF");
				break;
		}
		temp.setUserID(TOUSERID);
		temp.setUserName("");
		return temp;
	}
	public List<User> SearchAnyUser(int UID,String Name){//------------------------------------------NEEDS ATTENTION
		String out=ServerRunner.SearchAnyUser(UID, Name);
		List<User> ret=new ArrayList<User>();
		List<String> data;
		String[] str = new String[3];
		data = Arrays.asList(out.split("\n"));
		for(int i=0;i<data.size();i++)
		{
			User temp = new User();
			str=data.get(i).split(":");
			temp.setUserName(str[0]);
			temp.setUserID(Integer.parseInt(str[1]));
			temp.setFriend(true);
			ret.add(temp);
		}
		return ret;
	}
	public   List<User> breadthFirst(int UID,String NAME)
	{
		String out=ServerRunner.breadthFirst(UID,NAME);
		List<User> ret=new ArrayList<User>();
		List<String> data;
		String[] str = new String[5];
		data = Arrays.asList(out.split("\n"));
		for(int i=0;i<data.size();i++)
		{
			User temp = new User();
			str=data.get(i).split(":");
			temp.setUserID(Integer.parseInt(str[0]));
			temp.setUserName(str[1]);
			if(Integer.parseInt(str[2])==1)
			{
				temp.setFriend(true);
			}
			else
			{
				temp.setFriend(false);
			}
			temp.setNoOfMutual(Integer.parseInt(str[3]));
			ret.add(temp);
		}
		return ret;
	}

	public   String UpdateCollegeDetails(int UID,String college){
		return ServerRunner.UpdateCollegeDetails(UID, college);
	}
	public   String UpdateCityDetails(int UID,String city){
		return ServerRunner.UpdateCityDetails(UID, city);
	}
	public   String UpdatePassword(int UID,String password){
		return ServerRunner.UpdatePassword(UID, password);
	}
	public   void updateProfilePic(int UID,String path){
		ServerRunner.updateProfilePic(UID, path);
	}
	public   String UpdateStatus(int UID,String status){
		return ServerRunner.UpdateStatus(UID, status);
	}

	public   int AcceptRejectRequest(int ToUserID,int FromUserID,int accept){
		return ServerRunner.AcceptRejectRequest(ToUserID, FromUserID, accept);
	}
	public   void SendFriendRequest(int FromUser,int ToUser){
		ServerRunner.SendFriendRequest(FromUser, ToUser);
	}
	public   List<Notification> getNotification(int UID){
		String out=ServerRunner.SendNotification(UID);
		out+=ServerRunner.GetAllMessages(UID);
		List<Notification> ret=new ArrayList<Notification>();
		List<String> data;
		String[] str = new String[4];
		data = Arrays.asList(out.split("\n"));
		for(int i=0;i<data.size();i++)
		{
			Notification temp = new Notification();
			str=data.get(i).split(":");
			temp.setType(str[0]);
			temp.setUserName(str[1]);
			temp.setUserID(Integer.parseInt(str[2]));
			temp.setBody(str[3]);
			ret.add(temp);
		}
		return ret;
	}
	public   String GetAllMessages(int UID){
		return ServerRunner.GetAllMessages(UID);
	}
	public   String SendMessageToUser(int FromUserID,int ToUserID,String MessageBody){
		return ServerRunner.SendMessageToUser(FromUserID, ToUserID, MessageBody);
	}

	public   String AuthenticateUser(int UID,String Password){
		return ServerRunner.AuthenticateUser(UID, Password);
	}
	public   String AddUser(String UserName,int UserID,String Password,int age,char gender,String City,String College)
	{
		return ServerRunner.AddVertex(UserName, -1, Password, age, gender, City, College);
	}
	public   void deleteUser(int UID){
		ServerRunner.DeleteVertex(UID);
	}

	public   int getNoOfUsers(){
		return ServerRunner.getNoOfVertices();
	}
	public   int[] getEdges(int UID){
		return ServerRunner.getEdges(UID);
	}
	
	//----------------------------------------------GROUPS-------------------------------------------------
	
	public String getGroupInfo(int groupID)
	{
		return ServerRunner.getGroupInfo(groupID);
	}
	
	public List<User> getAllMembers(int fromUserID,int groupID)
	{
		String out=ServerRunner.getAllMembersOfGroup(fromUserID, groupID);
		List<User> ret=new ArrayList<User>();
		List<String> data;
		String[] str = new String[3];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			User temp = new User();
			str=data.get(i).split(":");
			temp.setUserName(str[1]);
			temp.setUserID(Integer.parseInt(str[0]));
			temp.setFriend(true);
			ret.add(temp);
		}
		return ret;
	}
	
	public List<post> getAllPost(int groupID)
	{
		String out=ServerRunner.getAllPosts(groupID);
		List<post> ret=new ArrayList<post>();
		List<String> data;
		String[] str = new String[4];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			post temp = new post();
			str=data.get(i).split(":");
			temp.setUserName(str[1]);
			temp.setPostUserID(Integer.parseInt(str[0]));
			temp.setContent(str[2]);
			temp.setTimeStamp(Long.parseLong(str[3]));
			ret.add(temp);
		}
		return ret;
	}
	
	public List<User> searchWithinGroup(int groupID,String userName)
	{
		String out=ServerRunner.searchWithinTheGroup(groupID, userName);
		List<User> ret=new ArrayList<User>();
		List<String> data;
		String[] str = new String[2];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			User temp = new User();
			str=data.get(i).split(":");
			temp.setUserName(str[1]);
			temp.setUserID(Integer.parseInt(str[0]));
			ret.add(temp);
		}
		return ret;
	}
	
	public List<groupData> searchForGroup(String name)
	{
		String out=ServerRunner.searchForGroup(name);
		List<groupData> ret=new ArrayList<groupData>();
		List<String> data;
		String[] str = new String[5];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			groupData temp = new groupData();
			str=data.get(i).split(":");
			temp.setAdminName(str[3]);
			temp.setGroupID(Integer.parseInt(str[0]));
			temp.setAdminID(Integer.parseInt(str[2]));
			temp.setGroupName(str[1]);
			ret.add(temp);
		}
		return ret;
	}
	
	public List<groupData> getPerPersonGroup(int userID)
	{
		String out=ServerRunner.getPerPersonGroupList(userID);
		List<groupData> ret=new ArrayList<groupData>();
		List<String> data;
		String[] str = new String[5];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			groupData temp = new groupData();
			str=data.get(i).split(":");
			temp.setAdminName(str[3]);
			temp.setGroupID(Integer.parseInt(str[0]));
			temp.setAdminID(Integer.parseInt(str[2]));
			temp.setGroupName(str[1]);
			ret.add(temp);
		}
		return ret;
	}
	
	public List<newsfeed> generateNewsFeed(int userID)
	{
		String out=ServerRunner.generateNewsFeed(userID);
		List<newsfeed> ret=new ArrayList<newsfeed>();
		List<String> data;
		String[] str = new String[5];
		data = Arrays.asList(out.split("|"));
		for(int i=0;i<data.size();i++)
		{
			newsfeed temp = new newsfeed();
			str=data.get(i).split(":");
			temp.setType(str[0]);
			temp.setUserID(Integer.parseInt(str[1]));
			temp.setUserName(str[2]);
			if(str[0].equals("PROFILEPICCHANGE"))
				temp.setTimeStamp(Long.parseLong(str[4]));
			else
				temp.setStatus(str[4]);
			ret.add(temp);
		}
		return ret;
	}
}
