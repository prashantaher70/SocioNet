import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class SimpleWebServer extends NanoHTTPD {
	static int noOfRequests=0;
	 /**
     * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
     */
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xml", "text/xml");
        put("java", "text/x-java-source, text/java");
        put("txt", "text/plain");
        put("asc", "text/plain");
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};

	private final File rootDir;
    private final boolean quiet;

    public SimpleWebServer(String host, int port, File wwwroot, boolean quiet) {
        super(host, port);
        this.rootDir = wwwroot;
        this.quiet = quiet;
    }

    File getRootDir() {
        return rootDir;
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        //System.out.println(method + " '" + uri + "' ");

        String msg = "";
        if(!uri.equals("") && !uri.equals("/favicon.ico") && !uri.equals("/"))
		{
			return serveFile(uri, header, getRootDir());
        }
        String request=parms.get("REQUEST");
		if(request!=null)
		{
			//System.out.println("healthy here\n");
			noOfRequests++;
			if(parms.get("REQUEST").equals("FRIENDLIST"))
			{
				msg+=ServerRunner.PrintFriendList(Integer.parseInt(parms.get("UID")));
			}
			else if(parms.get("REQUEST").equals("PROFILE"))
			{
				msg+=ServerRunner.LoadProfile(Integer.parseInt(parms.get("TOUSERID")),Integer.parseInt(parms.get("UID")));
			}
			else if(parms.get("REQUEST").equals("SEARCHUSER"))
			{
				//System.out.println("USER ID JAVA CODE" + parms.get("UID"));
				msg+=ServerRunner.breadthFirst((Integer.parseInt(parms.get("UID"))),(parms.get("SEARCHUSERNAME")));
				if(msg.equals("")) msg="NOT FOUND";
			}
			else if(parms.get("REQUEST").equals("ADDFRIEND"))
			{
				ServerRunner.SendFriendRequest((Integer.parseInt(parms.get("UID"))),(Integer.parseInt(parms.get("TOUSERID"))));
				//System.out.println("ADDEDFRIENDREQUEST");
				msg+="ADDED";
			}
			else if(parms.get("REQUEST").equals("UPDATECOLLEGE"))
			{
				msg+=ServerRunner.UpdateCollegeDetails((Integer.parseInt(parms.get("UID"))),parms.get("COLLEGE"));
			}
			else if(parms.get("REQUEST").equals("UPDATECITY"))
			{
				msg+=ServerRunner.UpdateCityDetails((Integer.parseInt(parms.get("UID"))),parms.get("CITY"));
			}
			else if(parms.get("REQUEST").equals("UPDATEPASSWORD"))
			{
				msg+=ServerRunner.UpdatePassword((Integer.parseInt(parms.get("UID"))),parms.get("PASSWORD"));
			}
			else if(parms.get("REQUEST").equals("UPDATEPIC"))
			{
				ServerRunner.updateProfilePic((Integer.parseInt(parms.get("UID"))),parms.get("PATH"));
			}
			else if(parms.get("REQUEST").equals("NOTIFICATION"))
			{
				//System.out.println("uid:"+ parms.get("UID"));
				msg+=ServerRunner.SendNotification((Integer.parseInt(parms.get("UID"))));
				msg+=ServerRunner.GetAllMessages((Integer.parseInt(parms.get("UID"))));
				//System.out.println("notification\n");
				System.out.println("\n\n\nHELLO"+msg+"HELLO\n\n\n");
			}
			else if(parms.get("REQUEST").equals("ACCEPTREQUEST"))
			{
				ServerRunner.AcceptRejectRequest((Integer.parseInt(parms.get("UID"))),(Integer.parseInt(parms.get("FROMUSERID"))),(Integer.parseInt(parms.get("ACCEPTORREJECT"))));
				if(parms.get("ACCEPTORREJECT").equals("0"))
					msg+="REJECTED";
				else
					msg+="ACCEPTED";
			}
			else if(parms.get("REQUEST").equals("SIGNUP"))
			{
				//System.out.println("request received");
				msg+=ServerRunner.AddVertex(parms.get("USERNAME"),-1,parms.get("PASSWORD"),(Integer.parseInt(parms.get("AGE"))),parms.get("GENDER").charAt(0),parms.get("CITY"),parms.get("COLLEGE"));
			}
			else if(parms.get("REQUEST").equals("GETMESSAGES"))
			{
				msg+=ServerRunner.GetAllMessages((Integer.parseInt(parms.get("UID"))));
				//System.out.println("\n\n\n"+msg+"\n\n\n");
			}
			else if(parms.get("REQUEST").equals("LOGIN"))
			{
				msg+=ServerRunner.AuthenticateUser((Integer.parseInt(parms.get("UID"))),parms.get("PASSWORD"));
				//System.out.println(msg);
			}
			else if (parms.get("REQUEST").equals("SENDMESSAGE"))
			{
				msg+=ServerRunner.SendMessageToUser((Integer.parseInt(parms.get("UID"))),Integer.parseInt(parms.get("TOUSERID")),parms.get("MESSAGEBODY"));
				//System.out.println(msg);
			}
		}
		noOfRequests++;
        return new NanoHTTPD.Response(msg);
    }
    Response serveFile(String uri, Map<String, String> header, File homeDir) {
		File f = new File(homeDir, uri);
		//System.out.println(homeDir+uri);
		Response res = null;
		if (f.exists()) {
			//System.out.println("\n\nexists\n\n");
			try {
				if (res == null) {
					// Get MIME type from file name extension, if possible
					String mime = null;
					int dot = f.getCanonicalPath().lastIndexOf('.');
					if (dot >= 0) {
						mime = MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
					}
					if (mime == null) {
						mime = NanoHTTPD.MIME_DEFAULT_BINARY;
					}

					// Calculate etag
					String etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + "" + f.length()).hashCode());

					// Support (simple) skipping:
					long startFrom = 0;
					long endAt = -1;
					String range = header.get("range");
					if (range != null) {
						if (range.startsWith("bytes=")) {
							range = range.substring("bytes=".length());
							int minus = range.indexOf('-');
							try {
								if (minus > 0) {
									startFrom = Long.parseLong(range.substring(0, minus));
									endAt = Long.parseLong(range.substring(minus + 1));
								}
							} catch (NumberFormatException ignored) {
							}
						}
					}

					// Change return code and add Content-Range header when skipping is requested
					long fileLen = f.length();
					//System.out.println(fileLen);
					if (range != null && startFrom >= 0) {

						//System.out.println("First");
						if (startFrom >= fileLen) {
							res = new Response(Response.Status.RANGE_NOT_SATISFIABLE, NanoHTTPD.MIME_PLAINTEXT, "");
							res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
							res.addHeader("ETag", etag);
						} else {
							if (endAt < 0) {
								endAt = fileLen - 1;
							}
							long newLen = endAt - startFrom + 1;
							if (newLen < 0) {
								newLen = 0;
							}

							final long dataLen = newLen;
							FileInputStream fis = new FileInputStream(f) {
								@Override
								public int available() throws IOException {
									return (int) dataLen;
								}
							};
							fis.skip(startFrom);

							res = new Response(Response.Status.PARTIAL_CONTENT, mime, fis);
							res.addHeader("Content-Length", "" + dataLen);
							res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
							res.addHeader("ETag", etag);
						}
					} else {
						if (etag.equals(header.get("if-none-match")))
						{
							//System.out.println("Second");
							res = new Response(Response.Status.NOT_MODIFIED, mime, "");
						}
						else {
							//System.out.println("Third");
							res = new Response(Response.Status.OK, mime, new FileInputStream(f));
							res.addHeader("Content-Length", "" + fileLen);
							res.addHeader("ETag", etag);
						}
					}
				}
			} catch (IOException ioe) {
				res = new Response(Response.Status.FORBIDDEN, NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
			}

		
		res.addHeader("Accept-Ranges", "bytes");
        }
		return res;
	}
}

