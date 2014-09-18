package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/********************************************************
Cours :             LOG735
Session :           ETE 2013
Groupe :            01
Projet :            Laboratoire #4 (projet de session)
Etudiant(e)(s) :    Gabriel Robitaille-Monpetit ROBG15078200  
        			Raby Chaabani CHAR01058801	
Professeur :        Mathieu Dubois
Date creee :        2013-07-11
Date dern. modif. : N/A

*********************************************************
Objet qui représente un fichier.
*********************************************************
*********************************************************
Historique des modifications
*********************************************************
2013-07-11 - Premiere Version
*********************************************************/
@Deprecated
public class ListenerFileLock implements Runnable{

	private Serveur serveur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		
		try{
			serverSocket = new ServerSocket(10140);
			while(true){
				
				clientSocket = null;
				clientSocket = serverSocket.accept();
				InetAddress source = clientSocket.getInetAddress();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());

				String f = "";
				
				int size = in.readInt();
				
				boolean done = false;
				
				while (!done){
					switch (size){
					case -5://lockfile
						f = in.readUTF();
						System.out.println("Reception d'un lock sur le fichier "+f);
						envoyerLockServeurs(f,source);
						envoyerLockClients(f,source);
						done = true;
						break;
					case -7://lockfile - miseajour
						f = in.readUTF();
						System.out.println("Reception d'un lock sur le fichier "+f);
						//envoyerLockServeurs(f,source);
						envoyerLockClients(f,source);
						done = true;
						break;
					case -6://unlock file
						f = in.readUTF();
						System.out.println("Reception d'un unlock sur le fichier "+f);
						envoyerUnlockServeurs(f,source);
						envoyerUnlockClients(f,source);
						done = true;
						break;
					case -8://unlock file - mise a jour
						f = in.readUTF();
						System.out.println("Reception d'un unlock sur le fichier "+f);
						//envoyerUnlockServeurs(f,source);
						envoyerUnlockClients(f,source);
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
	
	private void envoyerUnlockClients(String f, InetAddress source) {
		for (InetAddress ip: serveur.getClients()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(source)){
				try {
					System.out.println("Envoie d'un unlock au client "+ip);
					Socket socket = new Socket(ip,10140);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -6 pour un unlock
					out.writeInt(-6);
					out.flush();
					out.writeUTF(f);
					out.flush();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void envoyerUnlockServeurs(String f, InetAddress source) {
		for (InetAddress ip: serveur.getServeurs()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(source)){
				try {
					System.out.println("Envoie d'un unlock au serveur "+ip);
					Socket socket = new Socket(ip,10140);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -6 pour un unlock
					out.writeInt(-8);
					out.flush();
					out.writeUTF(f);
					out.flush();
					
					out.close();
					socket.close();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void envoyerLockServeurs(String f, InetAddress source) {
		for (InetAddress ip: serveur.getServeurs()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(source)){
				try {
					System.out.println("Envoie d'un lock au serveur "+ip);
					Socket socket = new Socket(ip,10140);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -5 pour un lock
					out.writeInt(-7);
					out.flush();
					out.writeUTF(f);
					out.flush();
					
					out.close();
					socket.close();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void envoyerLockClients(String f, InetAddress source) {
		for (InetAddress ip: serveur.getClients()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(source)){
				try {
					System.out.println("Envoie d'un lock au client "+ip);
					Socket socket = new Socket(ip,10140);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -5 pour un lock
					out.writeInt(-5);
					out.flush();
					out.writeUTF(f);
					out.flush();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public void setServeurReference(Serveur s){
		this.serveur = s;
	}
}
