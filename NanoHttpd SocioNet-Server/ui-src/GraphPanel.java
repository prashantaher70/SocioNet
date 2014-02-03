
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.util.Random;
@SuppressWarnings("serial")
public class GraphPanel extends JPanel 
{
	public Ellipse2D ellipses[];
	int noOfVertices;
	public GraphPanel() 
	{
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public void paintComponent(Graphics g) 
	{

        super.paintComponent(g);
        if(ServerOperationsPanel.isStarted)
        {
			noOfVertices=ServerRunner.getNoOfVertices();
			ellipses=new Ellipse2D[noOfVertices];
        	doDrawing(g);
			drawLinks(g);
        }
    }
	private void doDrawing(Graphics g) 
	{
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.blue);

        Dimension size = getSize();
        Insets insets = getInsets();

        int w = size.width - insets.left - insets.right-20;
        int h = size.height - insets.top - insets.bottom-20;

        Random r = new Random();
        @SuppressWarnings("unused")
		boolean allowed=false;
        for (int i = 0; i < noOfVertices; i++) 
        {
            	float x = Math.abs(r.nextInt()) % w;
            	float y = Math.abs(r.nextInt()) % h;
            	int j=0;
				x = Math.abs(r.nextInt()) % w;
				y = Math.abs(r.nextInt()) % h;
				while(j<i)
				{
					if(ellipses[j].intersects(x,y,10,10))
					{
						x = Math.abs(r.nextInt()) % w;
						y = Math.abs(r.nextInt()) % h;
						allowed=false;
						j=0;
					}
					else
					{
						allowed=true;
						j++;
					}
					
				}
				ellipses[i]=new Ellipse2D.Float(x,y,10,10);
            	g2d.setColor (Color.BLACK);
				g2d.drawString(String.valueOf(i+1),x,y);
				g2d.setColor(getColor(i));
            	g2d.fill (ellipses[i]);
            	g2d.draw(ellipses[i]);
        }
	}
	
	public Color getColor(int i)
	{
		Color color=Color.BLACK;
		switch(i%8)
		{
		case(0):
			color= Color.BLUE;
			break;
		case 1:
			color= Color.CYAN;
			break;
		case(2):
			color= Color.GRAY;
			break;
		case 3:
			color= Color.green;
			break;
		case(4):
			color= Color.MAGENTA;
			break;
		case 5:
			color= Color.ORANGE;
			break;
		case(6):
			color= Color.RED;
			break;
		case 7:
			color= Color.DARK_GRAY;
			break;
		
		}
		return color;
	}
	
	public void drawLinks(Graphics g)
	{
		try
		{
			for(int i=0;i<noOfVertices;i++)
			{
				g.setColor(getColor(i));
				int friendList[]=new int[40];
				friendList=ServerRunner.getEdges(i+1);
				for(int j=0;friendList[j]!=-1;j++)
				{
					if(friendList[j]>i+1)
					{
						g.drawLine(((int)ellipses[i].getCenterX()), (int)ellipses[i].getCenterY(), (int)ellipses[friendList[j]-1].getCenterX(), (int)ellipses[friendList[j]-1].getCenterY());
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
