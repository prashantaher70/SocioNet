import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.io.*;
@SuppressWarnings("serial")
public class SocioNet extends JFrame {
	public SocioNet() 
	{
		// TODO Auto-generated constructor stub
		   setTitle("Simple example");
	       getContentPane().setSize(500, 500);
	       JTabbedPane jtp=new JTabbedPane();
	       jtp.addTab("Server Operations", new ServerOperationsPanel());
	       jtp.addTab("Server Statistics",new ServerStatisticsPanel() );
	       jtp.addTab("Graph", new GraphPanel());
	       this.add(jtp);
		   
	}
	
	public static void main(String[] args)
	{
		SocioNet ex = new SocioNet();
		ex.pack();
        ex.setVisible(true);
	}
}
