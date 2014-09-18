package Serveur;


import java.net.InetAddress;
/*
 * UnknownHost a utiliser lorsqu'il y aura
 * plusieurs serveurs dans la liste.
 */
import java.net.UnknownHostException;
import java.util.ArrayList;

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
Classe qui represente le serveur.
*********************************************************
*********************************************************
Historique des modifications
*********************************************************
2013-07-11 - Premiere Version
*********************************************************/
@SuppressWarnings("unused")
public class Serveur {

	private ArrayList<Fichier> fichiers;
	private InetAddress ip;
	private ArrayList<InetAddress> clients;
	private ArrayList<InetAddress> serveurs;
	
	public Serveur(){
		clients = new ArrayList<InetAddress>();
		serveurs = new ArrayList<InetAddress>();
		fichiers = new ArrayList<Fichier>();
		demarrerEcouteConnexions();
		demarrerEcouteAjouts();
		demarrerEcouteSuppressions();
		demarrerEcouteModifications();
		//try {
		//	serveurs.add(InetAddress.getByName("192.168.1.50"));
		//} catch (UnknownHostException e) {
		//	e.printStackTrace();
		//}
	}
	
	private void demarrerEcouteConnexions(){
		ListenerConnexions sync = new ListenerConnexions();
		sync.setServeurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	private void demarrerEcouteAjouts(){
		ListenerAjouts sync = new ListenerAjouts();
		sync.setServeurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	private void demarrerEcouteSuppressions(){
		ListenerSuppressions sync = new ListenerSuppressions();
		sync.setServeurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}
	
	private void demarrerEcouteModifications(){
		ListenerModifications sync = new ListenerModifications();
		sync.setServeurReference(this);
		Thread t = new Thread(sync);
		t.start();
	}

	public Serveur(ArrayList<Fichier> fichiers, InetAddress ip) {
		super();
		this.fichiers = fichiers;
		this.ip = ip;
	}
	
	public void ajouterClient(InetAddress ip){
		
		boolean existe = false;
		
		for (InetAddress a: clients){
			if (a.equals(ip)){
				existe = true;
			}
		}
		
		if (!existe){
			clients.add(ip);
		}
		else{
			System.out.println("Le client est deja dans la liste");
		}
		
	}
	
	public void retirerClient(InetAddress ip){
		
		for (InetAddress i: clients){
			if (ip.equals(i)){
				System.out.println("Deconnexion du client "+i.toString());
				clients.remove(i);
			}
		}
	}
	/**
	 * Lorsque le serveur recoit un fichier, il doit l'ajouter dans
	 * sa liste de fichiers et notifier tous les autres serveurs
	 * de l'ajout.
	 */
	public void ajouterFichier(Fichier fichier){
		
		System.out.println("Ajout du fichier en memoire "+fichier.getNom());
		fichiers.add(fichier);
				
	}
	/*
	 * La section suivante ne contient
	 * que des accesseurs et mutateurs.
	 */
	public ArrayList<Fichier> getFichiers() {
		return fichiers;
	}
	public void setFichiers(ArrayList<Fichier> fichiers) {
		this.fichiers = fichiers;
	}
	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public ArrayList<InetAddress> getServeurs(){
		return this.serveurs;
	}
	public void addServeur(InetAddress ip){
		serveurs.add(ip);
	}
	public ArrayList<InetAddress> getClients(){
		return this.clients;
	}
}
