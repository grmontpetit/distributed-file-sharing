package Client;
import org.apache.commons.io.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

	private View vue;
	private Model modele;
	
	public Controller(){}
	
	public Controller(View v, Model m){
		this.vue = v;
		this.modele = m;
		modele.addObserver(vue);
		verifierModifications();
		ecouterAjouts();
		ecouterSuppressions();
		ecouterModifications();
	}

	public void ecouterAjouts(){
		ListenerAjouts sync = new ListenerAjouts();
		sync.setControlleurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	public void ecouterSuppressions(){
		ListenerSuppressions sync = new ListenerSuppressions();
		sync.setControlleurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	public void ecouterModifications(){
		ListenerModifications sync = new ListenerModifications();
		sync.setControlleurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	public void verifierModifications(){
		Timer verification = new Timer();
		TimerTask t = new FileChangeChecker(this);
		verification.schedule(t, 3000,3000);
	}
	
	public void etablirConnexionAuServeur(){

		try {
			modele.setServeur(modele.getRandomServer());
			Socket socket = new Socket(modele.getServeur(),10120);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			out.writeByte(1);
			out.flush();
			
			out.close();
			socket.close();
			vue.enableButtons();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void ajouterFichier(Fichier f){
		modele.addFichier(f);
	}
	
	public void ajouterFichier(File f){
		String nom = f.getName();
		String version = "1";
		String md5 = MD5Generator.generate(f);
		
		byte[] octets = null;
		try {
			octets = FileUtils.readFileToByteArray(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String extension = "";
		
		int i = f.getName().lastIndexOf('.');
		if (i > 0) {
		    extension = f.getName().substring(i+1);
		}
		System.out.println("Ajout du fichier "+nom);
		
		boolean existe = false;
		
		for (Fichier fichier: modele.getFichiers()){
			if (fichier.getNom().equals(f.getName())){
				String message = "Le fichier existe deja dans le systeme!";
				System.out.println(message);
				vue.warning(message);
				existe = true;
			}		
		}
		if (!existe){
			File file = new File(Constantes.REPERTOIRE_BASE+f.getName());
			FileOutputStream fileOuputStream;
			try {
				fileOuputStream = new FileOutputStream(file);
			    fileOuputStream.write(octets);
			    fileOuputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// On copiie le fichier dans le repertoire sync
			modele.addFichier(new Fichier(nom,version,octets,extension,file,md5));
		}
		
		try {
			Socket socket = new Socket(modele.getServeur(),10125);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			

			out.writeInt(-1);
			out.flush();
			out.writeUTF(f.getName().toString());
			out.flush();
			System.out.println("Taille du fichier: "+octets.length);
			out.writeInt(octets.length);

			out.flush();
			out.write(octets);
			out.flush();
			
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void supprimerFichier(String filename){

		Fichier del = null;
		
		for (Fichier f: modele.getFichiers()){
			if (filename.equals(f.getNom())){
				del = f;
			}
		}
		
		try {
			Socket socket = new Socket(modele.getServeur(),10135);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			System.out.println("Envoie d'une demande de suppression pour "+filename);
			out.writeInt(-4);
			out.flush();
			out.writeUTF(filename);
			out.flush();
			
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File temp = new File(Constantes.REPERTOIRE_BASE+filename);
		if (temp.exists()){
			System.out.println("Suppression de "+filename +" qui est sur le disque");
			temp.delete();
		}
		
		System.out.println("Suppression du fichier "+del.getNom()+" qui est indexe.");
		modele.getFichiers().remove(del);
		
	}
	
	public void montrerFichiers(){
		
		for (Fichier f: modele.getFichiers()){
			System.out.println("#########################################");
			System.out.println("Nom du fichier: "+f.getNom().toString());
			System.out.println("Type de fichier: "+f.getType());
			System.out.println("Taille du fichier "+f.getOctets().length + " octets");
			System.out.println("#########################################");
		}
		
	}
	public ArrayList<Fichier> getFichiers(){
		return modele.getFichiers();
	}
	
	//public 
	//public InetAddress getRandomServer(){
	//	return modele.getRandomServer();
	//}
	
	public InetAddress getServer(){
		return modele.getServeur();
	}
}
