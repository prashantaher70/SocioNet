
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.*;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
@SuppressWarnings("serial")
public class ServerOperationsPanel extends JPanel implements ActionListener
{
	JButton btnStart;
	JButton btnStop;
	static JTextArea jTArea;
	static boolean isStarted=false;
	private NanoHTTPD server;
	BufferedReader reader;
	int port = 8080;
	String host = "192.168.43.31";
	File wwwroot = new File(".").getAbsoluteFile();
	//System.out.println(wwwroot);
	boolean quiet = false;
	public ServerOperationsPanel()
	{
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panelForAllPanels=new JPanel();
		panelForAllPanels.setLayout(new BoxLayout(panelForAllPanels,BoxLayout.Y_AXIS));
		add(panelForAllPanels);

		JPanel startPanel=new JPanel();
			btnStart=new JButton("Start");
			btnStop=new JButton("Stop");
			btnStart.addActionListener(this);
			btnStop.addActionListener(this);
			btnStop.setEnabled(false);
			startPanel.add(btnStart);
			startPanel.add(btnStop);
		panelForAllPanels.add(startPanel);

		JPanel txtAreaPanel=new JPanel();
			jTArea=new JTextArea(20,50);
			jTArea.setEditable(false);
			JScrollPane scp=new JScrollPane(jTArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			txtAreaPanel.add(scp);
			panelForAllPanels.add(txtAreaPanel);

			server=new SimpleWebServer(host, port, wwwroot, quiet);
			PrintStream printStream = new PrintStream(new CustomOutputStream(jTArea));
			System.setOut(printStream);
			System.setErr(printStream);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		if(ae.getActionCommand().equals("Start"))
		{
			btnStart.setEnabled(false);
			btnStop.setEnabled(true);
			isStarted=true;
			ServerStatisticsPanel.startTime = System.currentTimeMillis();
			ServerRunner.startServer(server);
			ServerRunner.graphCreated=true;
			System.out.println("\n\n"+ServerRunner.GetAllMessages(3)+"\n\n");
			System.out.println("\n\n"+ServerRunner.GetAllMessages(8)+"\n\n");
		}
		else if(ae.getActionCommand().equals("Stop"))
		{
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			isStarted=false;
			ServerRunner.stopServer(server);
		}
	}

	public class CustomOutputStream extends OutputStream
	{
		private JTextArea textArea;

		public CustomOutputStream(JTextArea textArea)
		{
			this.textArea = textArea;
		}

		@Override
		public void write(int b) throws IOException
		{
			// redirects data to the text area
			textArea.append(String.valueOf((char)b));
			// scrolls the text area to the end of data
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

}
