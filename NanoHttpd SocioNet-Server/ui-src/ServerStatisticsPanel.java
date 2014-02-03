import java.text.DecimalFormat;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.io.*;


@SuppressWarnings("serial")
public class ServerStatisticsPanel extends JPanel{
	JLabel jlbl;
	static long startTime ;
	double mem1=0.0,mem2=0.0;
	JLabel memUtil;
	int NumOfRequests=0;
	int MemoryUtil=0;
	JLabel NumRequests=new JLabel(String.valueOf(NumOfRequests));
	public ServerStatisticsPanel()
	{
		// TODO Auto-generated constructor stub
		JPanel panelForAllPanels =new JPanel();
		panelForAllPanels.setLayout(new BoxLayout(panelForAllPanels, BoxLayout.Y_AXIS));
		add(panelForAllPanels);
			JPanel UpTimePanel=new JPanel();
			add(UpTimePanel);
			JLabel ServerUpTime=new JLabel("Server Up Time : ");
			UpTimePanel.add(ServerUpTime);
			jlbl=new JLabel();
			UpTimePanel.add(jlbl);
			UpTimePanel.setVisible(true);
		panelForAllPanels.add(UpTimePanel);
		
		JPanel NumRequestsPanel=new JPanel();
		add(NumRequestsPanel);
			JLabel ServerNumRequests=new JLabel("Number of Requests Processed : ");
			NumRequestsPanel.add(ServerNumRequests);
			NumRequestsPanel.add(NumRequests);
		panelForAllPanels.add(NumRequestsPanel);
		
		
		JPanel MemUtilPanel=new JPanel();
		add(MemUtilPanel);
		JLabel ServermemUtil=new JLabel("Memory Utilization : ");
		MemUtilPanel.add(ServermemUtil);
		memUtil=new JLabel(""+String.valueOf(MemoryUtil)+"");
		MemUtilPanel.add(memUtil);
		panelForAllPanels.add(MemUtilPanel);
		new Thread(new Runnable()
		   {
        		public void run() 
        		{ 
        			try 
        			{
        				updateTime();
        			} 
        			catch (Exception e) 
        			{ 
        				e.printStackTrace();
        			}
        		}
			}).start();
		new Thread(new Runnable()
		   {
     		public void run() 
     		{ 
     			try 
     			{
     				updateMemoryUtilization(); 
     			} 
     			catch (Exception e) 
     			{ 
     				e.printStackTrace();
     			}
			}
			}).start();
		new Thread(new Runnable()
		   {
        		public void run() 
        		{ 
        			try 
        			{
						File folder=new File("./temp");
        				deleteTempFiles(folder);
						Thread.currentThread().sleep(5*60*1000);
        			} 
        			catch (Exception e) 
        			{ 
        				e.printStackTrace();
        			}
        		}
			}).start();
		new Thread(new Runnable()
		   {
        		public void run() 
        		{ 
        			try 
        			{
						updateNoOfRequests();
						Thread.currentThread().sleep(5*60*1000);
        			} 
        			catch (Exception e) 
        			{ 
        				e.printStackTrace();
        			}
        		}
			}).start();
	}
	
	@SuppressWarnings("static-access")
	protected void updateMemoryUtilization() {
		// TODO Auto-generated method stub
		try
		{
			while(true)
			{
				//mem2=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				mem1=Runtime.getRuntime().totalMemory();
				mem2=Runtime.getRuntime().freeMemory();
				//if you want entire memory taken by JVM
				memUtil.setText(String.valueOf(roundTwoDecimals((mem1-0)/(1024*1024))) +" MB");
					//if you want memory allocated to this program inside JVM
				//memUtil.setText(String.valueOf(roundTwoDecimals((mem1-mem2)/(1024*1024))) +" MB");
				Thread.currentThread().sleep(5000);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void updateNoOfRequests()
	{
		try
		{
			while(true)
			{
				if(ServerOperationsPanel.isStarted)
				{
					NumRequests.setText(String.valueOf(SimpleWebServer.noOfRequests));
					Thread.currentThread().sleep(1000);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public void updateTime() {
		try 
		{
			while (true) 
			{
				// Getting Time in desire format
				if(ServerOperationsPanel.isStarted)
				{
					jlbl.setText(/*getTimeElapsed()*/ "hello");
					// Thread sleeping for 1 second
					Thread.currentThread().sleep(1000);
				}
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in Thread Sleep : " + e);
		}
	}

	public String getTimeElapsed() 
	{
		long elapsedTime = System.currentTimeMillis() - startTime;
		elapsedTime = elapsedTime / 1000;

		String seconds = Integer.toString((int) (elapsedTime % 60));
		String minutes = Integer.toString((int) ((elapsedTime % 3600) / 60));
		String hours = Integer.toString((int) (elapsedTime / 3600));

		if (seconds.length() < 2)
		{
			seconds = "0" + seconds;
		}

		if (minutes.length() < 2)
		{
			minutes = "0" + minutes;
		}

		if (hours.length() < 2)
		{
			hours = "0" + hours;
		}

		return hours + ":" + minutes + ":" + seconds;
	}
	
	double roundTwoDecimals(double d) { 
	      DecimalFormat twoDForm = new DecimalFormat("#.##"); 
	      return Double.valueOf(twoDForm.format(d));
	} 
	
	public void deleteTempFiles(File folder) 
	{
		File[] files = folder.listFiles();
		if(files!=null) 
		{ //some JVMs return null for empty dirs
			for(File f: files) 
			{
				if(f.isDirectory()) 
				{
					//deleteFolder(f);
				}
				else 
				{
					f.delete();
				}
			}
		}
	}
}

