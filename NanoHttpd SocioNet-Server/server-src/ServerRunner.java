import java.io.IOException;

public class ServerRunner {
	private static native void mainCall();

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

	public static native void SaveUsersToFile();
	public static native void SaveLinksToFile();
	public static native void WriteMessagesToFile();
	public static native void SavePendingStatusesToFile();
	public static boolean graphCreated=false;
	public static int noOfVertices=0;
	static {
			//System.loadLibrary("SocialNetwork");
			// Linux hack, if you can't get your library
			// path set in your environment:
			System.load("C:/Users/Warrior/Desktop/SocioNet-Server/SocialNetwork.dll");
			System.out.println("Library Loaded");
		}

	public static void startServer(NanoHTTPD server)
	{
		try
		{
            server.start();
        }
		catch (IOException ioe)
		{
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }

        System.out.println("Server started, Hit Enter to stop.\n");

		if(graphCreated==false)
		{
			ServerRunner.mainCall();
			System.out.println("Graph Created\n");
			noOfVertices=ServerRunner.getNoOfVertices();
			//System.out.println(noOfVertices +" is number of vertices\n");
			int friendList[][]=new int[noOfVertices][40];
			for(int i=0;i!=noOfVertices;i++)
			{
				friendList[i]=ServerRunner.getEdges(i+1);
			}
		}
	}
	public static void stopServer(NanoHTTPD server)
	{
			SaveUsersToFile();
			SavePendingStatusesToFile();
			SaveLinksToFile();
			WriteMessagesToFile();
			server.stop();
			System.out.println("Server stopped.\n");
	}
}
