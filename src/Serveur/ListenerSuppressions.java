package Serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
public class ListenerSuppressions implements Runnable{

	private Serveur serveur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		
		try{
			
			while (true){
				serverSocket = new ServerSocket(10135);
				clientSocket = null;
				clientSocket = serverSocket.accept();
				InetAddress source = clientSocket.getInetAddress();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				
				String f = "";
				int size = in.readInt();
				
				boolean done = false;
				
				while (!done){
					switch (size){
					case -4:
						f = in.readUTF();
						System.out.println("Suppression du fichier: "+f);
						supprimerFichier(f);
						envoyerMiseAJourServeurs(f,source);
						envoyerMiseAJourClients(f,source);
						done = true;
						break;
					case -5:
						f = in.readUTF();
						System.out.println("Suppression du fichier: "+f);
						supprimerFichier(f);
						//envoyerMiseAJourServeurs(f,source);
						envoyerMiseAJourClients(f,source);
						done = true;
						break;
					default:
						done = true;
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
	
	private void envoyerMiseAJourClients(String f, InetAddress s) {
		for (InetAddress ip: serveur.getClients()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(s)){
				try {
					System.out.println("Envoie d'une mise a jour de suppression au client "+ip);
					Socket socket = new Socket(ip,10135);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -4 pour une suppression
					out.writeInt(-4);
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

	private void envoyerMiseAJourServeurs(String f, InetAddress s) {
		for (InetAddress ip: serveur.getServeurs()){
			// On envoie pas de mise à jours à la source
			if (!ip.equals(s)){
				try {
					System.out.println("Envoie d'une mise a jour au serveur "+ip);
					Socket socket = new Socket(ip,10135);
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					
					// -5 pour une suppression - mise a jour
					out.writeInt(-5);
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
	
	private void supprimerFichier(String filename){
		System.out.println("Reception d'une demande de suppression de "+filename);
		File temp = null;
		Fichier del = null;
		
		for (final File fileEntry:Constantes.FOLDER.listFiles()){
			// Si le dossier a un sous repertoire
			if (fileEntry.isDirectory()) {
			// On ne traite pas ce cas pour l'application
			}
			else{
				if (fileEntry.getName().equals(filename)){
					temp = fileEntry;
				}
			}
		}
		
		for (Fichier f: serveur.getFichiers()){
			if (f.getNom().equals(temp.getName())){
				del = f;
			}
		}
		
		System.out.println("Suppression du fichier "+temp.getName()+" qui est indexe.");
		serveur.getFichiers().remove(del);
		
		System.out.println("Suppression du fichier "+temp.getName()+" qui est sur le disque.");
		temp.delete();

	}
	public void setServeurReference(Serveur s){
		this.serveur = s;
	}
}
