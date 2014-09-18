package Serveur;


import java.io.DataInputStream;
import java.io.IOException;
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
public class ListenerConnexions implements Runnable{

	private Serveur serveur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		
		try{
			serverSocket = new ServerSocket(10120);
			while(true){
				
				clientSocket = null;
				clientSocket = serverSocket.accept();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				
				byte messageType = in.readByte();
								
				boolean done = false;
				
				while (!done){
					  switch(messageType)
					  {
					  case 1: // Connexion
						  System.out.println("Ajout du client "+clientSocket.getInetAddress());
						  serveur.ajouterClient(clientSocket.getInetAddress());
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
	
	public void setServeurReference(Serveur s){
		this.serveur = s;
	}
}
