package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.ArrayList;


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
public class ListenerAjouts implements Runnable{

	private Serveur serveur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		
		try{

			while (true){
				serverSocket = new ServerSocket(10125);
				clientSocket = null;
				clientSocket = serverSocket.accept();
				InetAddress source = clientSocket.getInetAddress();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				
				byte[] b = null;
				String f = "";
				int size = in.readInt();
				
				boolean miseAJour = false;
				boolean done = false;
				
				while (!done){
					switch (size){
					case -1:
						f = in.readUTF();
						//System.out.println("Ajout de fichier: "+f);
						size = in.readInt();
						miseAJour = false;
						break;
					case -2:
						f = in.readUTF();
						//System.out.println("Mise à jour d'ajout: "+f);
						size = in.readInt();
						miseAJour = true;
						break;
					default:
						if (!miseAJour){
							b = new byte[size];
							in.readFully(b);
							System.out.println("Ajout de fichier: "+f);
							ajouterFichier(b,f);
							envoyerMiseAJourServeurs(f,b,source);
							envoyerMiseAJourClients(f,b,source);
							done = true;
						}
						else{
							b = new byte[size];
							in.readFully(b);
							System.out.println("Mise à jour d'ajout: "+f);
							ajouterFichier(b,f);
							//envoyerMiseAJourServeurs(f,b);
							envoyerMiseAJourClients(f,b,source);
							done = true;
						}
						break;
					}
				}
			
				clientSocket.close();
				serverSocket.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

	private void envoyerMiseAJourClients(String f, byte[] b, InetAddress s) {
		for (InetAddress ip: serveur.getClients()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(s)){
				try {
					System.out.println("Envoie d'une mise a jour au client "+ip);
					Socket socket = new Socket(ip,10125);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -2 pour une mise à jour
					out.writeInt(-2);
					out.flush();
					out.writeUTF(f);
					out.flush();
					out.writeInt(b.length);
					out.flush();				
					out.write(b);
					out.flush();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void envoyerMiseAJourServeurs(String f, byte[] b, InetAddress s) {
		for (InetAddress ip: serveur.getServeurs()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(s)){
				try {
					System.out.println("Envoie d'une mise a jour au serveur "+ip);
					Socket socket = new Socket(ip,10125);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -2 pour une mise à jour
					out.writeInt(-2);
					out.flush();
					out.writeUTF(f);
					out.flush();
					out.writeInt(b.length);
					out.flush();				
					out.write(b);
					out.flush();
					
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private void ajouterFichier(byte[] b, String f){
		
	    FileOutputStream fileOuputStream;
	    
		try {
			
			File file = new File(Constantes.REPERTOIRE_BASE+f);
			if (file.exists()){
				System.out.println("Le fichier existe deja dans le systeme.");
			}else{
				
				String extension = "";
				
				int i = f.lastIndexOf('.');
				if (i > 0) {
				    extension = f.substring(i+1);
				}
				
				fileOuputStream = new FileOutputStream(file);
			    fileOuputStream.write(b);
			    fileOuputStream.close();
			    serveur.ajouterFichier(new Fichier(f, "1" , b, extension ,file, MD5Generator.generate(file)));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public void setServeurReference(Serveur s){
		this.serveur = s;
	}
}
