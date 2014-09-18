package Client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;

public class FileChangeChecker extends TimerTask{

	private Controller controlleur;
	
	public FileChangeChecker(Controller c){
		this.controlleur = c;
	}
	
	@Override
	public void run(){
			try{
				
				
				for (final File fileEntry:Constantes.FOLDER.listFiles()){
					// Si le dossier a un sous repertoire
					if (fileEntry.isDirectory()) {
						// On ne traite pas ce cas pour l'application
					}
					else{
						
						String md5 = MD5Generator.generate(fileEntry);
						
						for (Fichier f: controlleur.getFichiers()){
							// On compare le fichier sur le disque avec le fichier en memoire
							if (fileEntry.getName().equals(f.getNom().replace(Constantes.REPERTOIRE_BASE, ""))){
								
								if (!f.getMd5().equals(md5)){
									System.out.println("Le fichier a ete modifie: "+f.getNom());
									//envoyerFileLockServeur(fileEntry);
									envoyerMiseAJourServeur(fileEntry,f);
									mettreAJourCopieLocale(fileEntry,f);
									//envoyerFileUnlockServeur(fileEntry);
								}
							
							}
						
						}
					}
				}

				if (controlleur.getFichiers().size() < Constantes.FOLDER.listFiles().length){
					System.out.println("Un fichier a été ajouté");
					
					if (controlleur.getFichiers().size() == 0){
						for (final File fileEntry:Constantes.FOLDER.listFiles()){
							if (fileEntry.isDirectory()) {
								// On ne traite pas ce cas
							}
							else{
								controlleur.ajouterFichier(fileEntry);
							}
						}
					}
					else {
						for (final File fileEntry:Constantes.FOLDER.listFiles()){
							if (fileEntry.isDirectory()) {
								// On ne traite pas ce cas
							}
							else{
								for (Fichier f: controlleur.getFichiers()){
									// On compare le fichier sur le disque avec le fichier en memoire
									if (!fileEntry.getName().equals(f.getNom())){
										controlleur.ajouterFichier(fileEntry);
									}
								}
							}
						}
					}
				}
				else if(controlleur.getFichiers().size() > Constantes.FOLDER.listFiles().length){
					System.out.println("Suppression de fichier detectee");
					for (Fichier f: controlleur.getFichiers()){
						File temp = new File(Constantes.REPERTOIRE_BASE+f.getNom());
						if (Constantes.FOLDER.listFiles().length == 0){
							controlleur.supprimerFichier(f.getNom());
						}
						else if (!temp.exists()){
							System.out.println("Suppression de "+temp.getName());
							controlleur.supprimerFichier(f.getNom());
						}
					}
					
				}// end if else
			}
			catch(ConcurrentModificationException c){
				
			}
			catch(NullPointerException n){
				
			}
	}

	@Deprecated
	@SuppressWarnings("unused")
	private void envoyerFileUnlockServeur(File file) {
		
		try {
			Socket socket = new Socket(controlleur.getServer(),10140);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Envoie d'un unlock sur le fichier "+file.getName());
			out.writeInt(-6);
			out.flush();
			out.writeUTF(file.getName());
			out.flush();
			
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void mettreAJourCopieLocale(File fichierDisque, Fichier fichierIndex) {
		
		fichierIndex.setMd5(MD5Generator.generate(fichierDisque));
		fichierIndex.setNom(Constantes.REPERTOIRE_BASE+fichierDisque.getName());
		
		byte[] octets = null;
		try {
			octets = FileUtils.readFileToByteArray(fichierDisque);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		fichierIndex.setOctets(octets);
		int version = Integer.parseInt(fichierIndex.getVersion());
		version++;
		fichierIndex.setVersion(version+"");
	
	}

	private void envoyerMiseAJourServeur(File fichierDisque, Fichier fichierIndex) {
		System.out.println("Envoie d'une mise a jour de modification");
		
		byte[] fichier = null;		
		
		try {
			fichier = FileUtils.readFileToByteArray(fichierDisque);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			Socket socket = new Socket(controlleur.getServer(),10130);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Envoie d'une modification a "+socket.getInetAddress());
			out.writeInt(-3);
			out.flush();
			out.writeUTF(fichierDisque.getName());
			out.flush();
			
			out.writeInt(fichier.length);
			out.flush();
			
			out.write(fichier);
			out.flush();
			
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * La fonction n'est plus utilisee car
	 * le fichier est locke lors de l'ecriture
	 * par le client.
	 * @param fileEntry
	 */
	@Deprecated
	@SuppressWarnings("unused")
	private void envoyerFileLockServeur(File fileEntry) {
		
		try {
			Socket socket = new Socket(controlleur.getServer(),10140);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Envoie d'un lock sur le fichier "+fileEntry.getName());
			out.writeInt(-5);
			out.flush();
			out.writeUTF(fileEntry.getName());
			out.flush();
			
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setServeurReference(Controller c){
		this.controlleur = c;
	}
	
}
