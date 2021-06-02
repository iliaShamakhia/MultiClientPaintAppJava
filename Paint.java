import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Paint{
	JFrame frame;
	MyPanel myPanel;
	int x;
	int y;
	int xIn;
	int yIn;
	int port=5555;
	DataInputStream in;
	DataOutputStream out;
	Socket sock;

	public static void main(String[] args){
		new Paint().go();
	}
	
	public void go(){
		myPanel=new MyPanel();
		frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(myPanel);
		frame.addMouseMotionListener(myPanel);
		frame.setSize(300, 300);
		frame.setVisible(true);
		try {
			sock=new Socket("127.0.0.1",port);
			in=new DataInputStream(sock.getInputStream());
			out=new DataOutputStream(sock.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread t=new Thread(new IncomingReader());
		t.start();
	}
	class IncomingReader implements Runnable{

		public void run() {
			try{
				while(true){
					xIn=in.readInt();
					yIn=in.readInt();
					myPanel.repaint();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	class MyPanel extends JPanel implements MouseMotionListener{
		
		public void paintComponent(Graphics g){
			g.setColor(Color.red);
			if(x>0||y>0){
				g.fillOval(x, y, 20, 20);
			}
			if(xIn>0||yIn>0){
				g.fillOval(xIn, yIn, 20, 20);
			}
		}
		
		public void mouseDragged(MouseEvent e) {
			x=e.getX()-20;
			y=e.getY()-40;
			try {
				out.writeInt(x);
				out.writeInt(y);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			myPanel.repaint();
		}
		
		public void mouseMoved(MouseEvent arg0) {
			
		}
	}
}


