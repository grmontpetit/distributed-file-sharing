package Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ListenerSuppressions implements Runnable{

	private Controller controlleur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	@Override
	public void run() {
		
		try{
			while (true){
				serverSocket = new ServerSocket(10135);
				clientSocket = null;
				clientSocket = serverSocket.accept();
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
						//envoyerMiseAJourServeurs(f,source);
						//envoyerMiseAJourClients(f,source);
						done = true;
						break;
					case -5:
						f = in.readUTF();
						System.out.println("Suppression du fichier: "+f);
						//supprimerFichier(f);
						//envoyerMiseAJourServeurs(f,source);
						//envoyerMiseAJourClients(f,source);
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
		
		for (Fichier f: controlleur.getFichiers()){
			if (f.getNom().equals(temp.getName())){
				del = f;
			}
		}
		
		System.out.println("Suppression du fichier "+temp.getName()+" qui est indexe.");
		controlleur.getFichiers().remove(del);
		
		System.out.println("Suppression du fichier "+temp.getName()+" qui est sur le disque.");
		temp.delete();

	}
	
	public void setControlleurReference(Controller c){
		this.controlleur = c;
	}
}
