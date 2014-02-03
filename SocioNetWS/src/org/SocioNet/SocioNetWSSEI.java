package org.SocioNet;

import java.util.List;

import javax.jws.WebService;
@WebService(name = "SocioNetWS", targetNamespace = 
		"http://socionet.org/",portName="SocioNetPort")
public interface SocioNetWSSEI {
	public List<User> getFriendList(int userId);
	public   String PrintMutualFriends(int UID,int TOUSERID);
	public   WallData LoadProfile(int UID,int TOUSERID);
	public List<User> SearchAnyUser(int UID,String Name);
	public   List<User> breadthFirst(int UID,String NAME);
	public   String UpdateCollegeDetails(int UID,String college);
	public   String UpdateCityDetails(int UID,String city);
	public   String UpdatePassword(int UID,String password);
	public   void updateProfilePic(int UID,String path);
	public   String UpdateStatus(int UID,String status);
	public   int AcceptRejectRequest(int ToUserID,int FromUserID,int accept);
	public   void SendFriendRequest(int FromUser,int ToUser);
	public   List<Notification> getNotification(int UID);
	public   String GetAllMessages(int UID);
	public   String SendMessageToUser(int FromUserID,int ToUserID,String MessageBody);
	public   String AuthenticateUser(int UID,String Password);
	public   String AddUser(String UserName,int UserID,String Password,int age,char gender,String City,String College);
	public   void deleteUser(int UID);
	public   int getNoOfUsers();
	public   int[] getEdges(int UID);
	public String getGroupInfo(int groupID);
	public List<User> getAllMembers(int fromUserID,int groupID);
	public List<post> getAllPost(int groupID);
	public List<User> searchWithinGroup(int groupID,String userName);
	public List<groupData> searchForGroup(String name);
	public List<groupData> getPerPersonGroup(int userID);
	public List<newsfeed> generateNewsFeed(int userID);
}
