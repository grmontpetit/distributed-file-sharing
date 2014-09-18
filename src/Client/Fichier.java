package Client;
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
import java.io.File;

public class Fichier {

	private String nom;
	private String version;
	private byte[] octets;
	private String md5;
	private String type;
	//private File fichier;
	
	public Fichier(){}
	
	//public Fichier(File f){
	//	this.fichier = f;
	//	
	//}
	
	/*public Fichier(File f, byte[] b){
		this.fichier = f;
		this.octets = b;
		
	}*/
	
	public Fichier(String nom, String version, byte[] octets, String type,
			File fichier) {
		super();
		this.nom = nom;
		this.version = version;
		this.octets = octets;
		this.type = type;
		//this.fichier = fichier;
	}
	public Fichier(String nom, String version, byte[] octets, String type,
			File fichier, String md5) {
		super();
		this.nom = nom;
		this.version = version;
		this.octets = octets;
		this.type = type;
		//this.fichier = fichier;
		this.md5 = md5;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public byte[] getOctets() {
		return octets;
	}
	public void setOctets(byte[] octets) {
		this.octets = octets;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/*public File getFichier() {
		return fichier;
	}
	public void setFichier(File fichier) {
		this.fichier = fichier;
	}*/
	
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	
}
