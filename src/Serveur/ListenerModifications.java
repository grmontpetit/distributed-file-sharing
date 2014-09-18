package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class ListenerModifications implements Runnable{

	private Serveur serveur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		
		try{

			while (true){
				serverSocket = new ServerSocket(10130);
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
					case -3:
						f = in.readUTF();
						System.out.println("Modification du fichier: "+f);
						size = in.readInt();
						break;
					case -4:
						miseAJour = true;
						f = in.readUTF();
						size = in.readInt();
						break;
					default:
						if (miseAJour){
							b = new byte[size];
							in.readFully(b);
							modifierFichier(f,b,source);
							//envoyerMiseAJourServeurs(f,b,source);
							envoyerMiseAJourClients(f,b,source);
							done = true;
						}
						else{
							b = new byte[size];
							in.readFully(b);
							modifierFichier(f,b,source);
							envoyerMiseAJourServeurs(f,b,source);
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
					Socket socket = new Socket(ip,10130);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -4 pour une mise à jour
					out.writeInt(-4);
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
					Socket socket = new Socket(ip,10130);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -4 pour une mise à jour
					out.writeInt(-4);
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

	private void modifierFichier(String nom, byte[] b, InetAddress source){
		
		Fichier reference = null;
		
		for (Fichier f: serveur.getFichiers()){
			if (f.getNom().equals(nom)){
				reference = f;
			}
		}
	    //convert array of bytes into file
	    FileOutputStream fileOuputStream;
		try {
			reference.setOctets(b);
			fileOuputStream = new FileOutputStream(Constantes.REPERTOIRE_BASE+nom);
			fileOuputStream.write(b);
			System.out.println("Fichier "+nom+ " mis a jour");
			fileOuputStream.close();
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
