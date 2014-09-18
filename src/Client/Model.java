package Client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

public class Model extends Observable{

	private ArrayList<Fichier> fichiers;
	private ArrayList<InetAddress> serveurs;
	private InetAddress serveur;
	
	public Model(){
		
		fichiers = new ArrayList<Fichier>();
		serveurs = new ArrayList<InetAddress>();
		
		try {
			serveurs.add(InetAddress.getByName("192.168.1.50"));
			//serveurs.add(InetAddress.getByName("192.168.1.2"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addFichier(Fichier fichier){
		fichiers.add(fichier);
	}
	
	public void addServeur(InetAddress ip){
		serveurs.add(ip);
	}
	
	public ArrayList<InetAddress> getServeurs(){
		return this.serveurs;
	}
	
	public InetAddress getRandomServer(){
		
		Random r = new Random();
		int index = r.nextInt(serveurs.size());
		
		return serveurs.get(index);
		
	}
	public void setServeur(InetAddress ip){
		this.serveur = ip;
	}
	public InetAddress getServeur(){
		return this.serveur;
	}
	public ArrayList<Fichier> getFichiers(){
		return this.fichiers;
	}
}
