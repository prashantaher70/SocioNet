package org.SocioNet;
import java.io.IOException;

public class ServerRunner {
	/*public ServerRunner()
	{
		System.load("C:/glassfish-4.0/glassfish4/glassfish/domains/domain1/lib/applibs/SocialNetwork.dll");
		ServerRunner.mainCall();
		System.out.println("LOADED #########################");
	}*/
	public static native void mainCall();

	public static native String printGraph();
	public static native String PrintFriendList(int user1);
	public static native String PrintMutualFriends(int user1,int user2);
	public static native String LoadProfile(int Profileuser1,int FromUser);
	public static native String SearchAnyUser(int UserID,String request);
	public static native String breadthFirst(int UserId,String SearchText);

	public static native String UpdateCollegeDetails(int UserID,String college);
	public static native String UpdateCityDetails(int UserID,String city);
	public static native String UpdatePassword(int UserID,String password);
	public static native void updateProfilePic(int UserID,String path);
	public static native String UpdateStatus(int userID,String status);

	public static native int AcceptRejectRequest(int ToUserID,int FromUserID,int accept);
	public static native void SendFriendRequest(int FromUser,int ToUser);
	public static native String SendNotification(int UserID);
	public static native String GetAllMessages(int UserID);
	public static native String SendMessageToUser(int FromUserID,int ToUserID,String MessageBody);

	public static native String AuthenticateUser(int UserID,String Password);
	public static native String AddVertex(String UserName,int UserID,String Password,int age,char gender,String City,String College);
	public static native void DeleteVertex(int UserID);

	public static native int getNoOfVertices();
	public static native int[] getEdges(int UserID);
	
	
	public static native String createNewGroup(int adminID,String groupName);
	public static native String addMemberToGroup(int groupID,int userID);
	public static native String getAllMembersOfGroup(int fromUserID,int groupID);
	public static native String postToGroup(int groupID,int fromUserID,String postContent,long timestamp);
	public static native String getAllPosts(int groupID);
	public static native String getGroupInfo(int groupID);
	public static native String searchWithinTheGroup(int groupID,String name);
	public static native String searchForGroup(String name);
	public static native String getPerPersonGroupList(int userID);
	public static native String generateNewsFeed(int userID);
	public static native String acceptOrRejectGroupRequest(int groupID,int fromUserID,int ofUserID,int accept);
	

	public static native void SaveUsersToFile();
	public static native void SaveLinksToFile();
	public static native void WriteMessagesToFile();
	public static native void SavePendingStatusesToFile();
	public static native void writeGroupsToFile();
	public static boolean graphCreated=false;
	public static int noOfVertices=0;
	static {
		//System.loadLibrary("SocialNetwork");
		// Linux hack, if you can't get your library
		// path set in your environment:
		System.load("F:/Devesh/Done in 2nd year/WebServices/SocioNetWS/native/SocialNetwork.dll");
		ServerRunner.mainCall();
		System.out.println("Library Loaded");
	}
}
