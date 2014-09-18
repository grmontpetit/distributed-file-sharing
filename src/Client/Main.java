package Client;

public class Main {

	public static void main(String[] args) {
		
		View vue = new View();
		Model modele = new Model();
		
		Controller controlleur = new Controller(vue, modele);
		
		vue.setController(controlleur);
		
	}
	
}
