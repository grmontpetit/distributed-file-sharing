package Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ListenerAjouts implements Runnable{

	private Controller controlleur;
	private Socket clientSocket;
	private ServerSocket serverSocket;
	
	@Override
	public void run() {
		try{

			while (true){
				serverSocket = new ServerSocket(10125);
				clientSocket = null;
				clientSocket = serverSocket.accept();
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
							//envoyerMiseAJourServeurs(f,b,source);
							//envoyerMiseAJourClients(f,b,source);
							done = true;
						}
						else{
							b = new byte[size];
							in.readFully(b);
							System.out.println("Mise à jour d'ajout: "+f);
							ajouterFichier(b,f);
							//envoyerMiseAJourServeurs(f,b);
							//envoyerMiseAJourClients(f,b,source);
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
			    controlleur.ajouterFichier(new Fichier(f, "1" , b, extension ,file, MD5Generator.generate(file)));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public void setControlleurReference(Controller c){
		this.controlleur = c;
	}

}
