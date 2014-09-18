package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated
public class ListenerFileLock implements Runnable{

	private Socket clientSocket;
	private ServerSocket serverSocket;
	@SuppressWarnings("unused")
	private Controller controlleur;

	
	@Override
	public void run() {
		
		try{
			serverSocket = new ServerSocket(10140);
			while(true){
				
				clientSocket = null;
				clientSocket = serverSocket.accept();
				//InetAddress source = clientSocket.getInetAddress();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());

				String f = "";
				
				int size = in.readInt();
				
				boolean done = false;
				
				while (!done){
					switch (size){
					case -5://lockfile
						f = in.readUTF();
						System.out.println("Reception d'un lock sur le fichier "+f);
						//envoyerLockServeurs(f,source);
						//envoyerLockClients(f,source);
						//verouillerFichier(f);
						done = true;
						break;
					case -6://unlock file
						f = in.readUTF();
						System.out.println("Reception d'un unlock sur le fichier "+f);
						//envoyerUnlockServeurs(f,source);
						//envoyerUnlockClients(f,source);
						//deverouillerFichier(f);
						done = true;
						break;
					default:
						done = true;
						break;
					}
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
	
	
	public void setControlleurReference(Controller c){
		this.controlleur = c;
	}

}
