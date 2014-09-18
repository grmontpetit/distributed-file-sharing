package Client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ListenerModifications implements Runnable{

	private Controller controlleur;
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
							//envoyerMiseAJourClients(f,b,source);
							done = true;
						}
						else{
							b = new byte[size];
							in.readFully(b);
							modifierFichier(f,b,source);
							//envoyerMiseAJourServeurs(f,b,source);
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
	
	private void modifierFichier(String nom, byte[] b, InetAddress source){
		
		Fichier reference = null;
		
		for (Fichier f: controlleur.getFichiers()){
			if (f.getNom().equals(nom)){
				System.out.println("Fichier recu en parametre: "+nom);
				System.out.println("Fichier trouve en index: "+f.getNom());
				reference = f;
			}
		}
		
		File fichier = new File(Constantes.REPERTOIRE_BASE+nom);
		if (fichier.exists()){
			fichier.delete();
		}
		
	    //convert array of bytes into file
	    //FileOutputStream fileOuputStream;
		try {
			reference.setOctets(b);
			
			FileChannel fileChannel = new RandomAccessFile(fichier, "rw").getChannel();
			FileLock lock = fileChannel.lock();
			//ByteBuffer buffer = new ByteBuffer(b);
			fileChannel.write(ByteBuffer.wrap(b));
			System.out.println("Mise a jour du fichier en memoire "+b.length);
			//fileOuputStream = new FileOutputStream(Constantes.REPERTOIRE_BASE+nom);
			//fileOuputStream.write(b);
			System.out.println("Fichier "+nom+ " mis a jour");
			//fileOuputStream.close();
			lock.release();
			reference.setMd5(MD5Generator.generate(fichier));
			fileChannel.close();
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
