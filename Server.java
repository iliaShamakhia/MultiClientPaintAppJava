import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server{
	ArrayList<DataOutputStream> outs;
	int port=5555;
	
	public static void main(String[] args){
		new Server().go();
	}
	
	public void go(){
		outs=new ArrayList<DataOutputStream>();
		try {
			ServerSocket serv=new ServerSocket(port);
			while(true){
				Socket clientSock=serv.accept();
				DataOutputStream out=new DataOutputStream(clientSock.getOutputStream());
				outs.add(out);
				Thread th=new Thread(new ClientHandler(clientSock));
				th.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tellEveryone(int x,int y){
		Iterator<DataOutputStream> it=outs.iterator();
		while(it.hasNext()){
			try{
				DataOutputStream out=it.next();
				out.writeInt(x);
				out.writeInt(y);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	class ClientHandler implements Runnable{
		
		Socket sock;
		DataInputStream in;
		int clientX;
		int clientY;
		
		public ClientHandler(Socket clientSocket){
			try{
				sock=clientSocket;
				in=new DataInputStream(sock.getInputStream());
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		public void run() {
			while(true){
				try {
					clientX=in.readInt();
					clientY=in.readInt();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tellEveryone(clientX,clientY);
			}
		}
	}
	
}
