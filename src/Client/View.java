package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class View extends JFrame implements Observer,ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -840305808728380514L;
	private JMenuBar menuBar;
	private JMenu menuFichier;
	private JMenuItem connexion;
	private JMenuItem quitter;
	private JButton ajouter;
	private JButton modifier;
	private JButton supprimer;
	private JButton montrerFichiers;
	private JPanel panneauPrincipal;
	private Controller controlleur;
	
	
	public View(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initMenus();
		initPanels();
		initButtons();
		initSize();
		addComponents();
		addActionListeners();
		setInitialConfiguration();
		this.setVisible(true);
		pack();
		repaint();
	}
	
	private void setInitialConfiguration() {
		ajouter.setEnabled(false);
		modifier.setEnabled(false);
		supprimer.setEnabled(false);
		montrerFichiers.setEnabled(false);
	}

	private void addActionListeners() {
		quitter.addActionListener(this);
		connexion.addActionListener(this);
		connexion.addActionListener(this);
		ajouter.addActionListener(this);
		modifier.addActionListener(this);
		supprimer.addActionListener(this);
		montrerFichiers.addActionListener(this);
		
	}

	private void initSize() {
		this.setSize(1024, 768);
		this.setResizable(false);
		
	}

	private void addComponents() {
		menuFichier.add(connexion);
		menuFichier.add(quitter);
		menuBar.add(menuFichier);
		
		panneauPrincipal.add(ajouter);
		panneauPrincipal.add(modifier);
		panneauPrincipal.add(supprimer);
		panneauPrincipal.add(montrerFichiers);
		modifier.setVisible(false);
		
		this.add(panneauPrincipal);
		this.setJMenuBar(menuBar);
		
	}

	private void initButtons() {
		ajouter = new JButton("Ajouter");
		modifier = new JButton("Modifier");
		supprimer = new JButton("Supprimer");
		montrerFichiers = new JButton("Montrer");
		
	}

	private void initPanels() {
		panneauPrincipal = new JPanel();
		
	}
	
	private void initMenus() {
		menuFichier = new JMenu("Fichier");
		connexion = new JMenuItem("Connexion");
		quitter = new JMenuItem("Quitter");
		menuBar = new JMenuBar();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	public void warning(String message){
		JOptionPane.showMessageDialog(this, message);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (arg0.getActionCommand() == "Quitter"){
			//System.out.println("Quitter");
			System.exit(0);
		}
		else if (arg0.getActionCommand() == "Connexion"){
			//System.out.println("Connexion");
			controlleur.etablirConnexionAuServeur();
		}
		else if (arg0.getSource() == ajouter){
			//System.out.println("ajouter");
		    JFileChooser chooser = new JFileChooser();
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile().getAbsoluteFile();
		    	controlleur.ajouterFichier(file);
		    }
		}
		else if (arg0.getSource() == modifier){
			//System.out.println("Modifier");
		}
		else if (arg0.getSource() == supprimer){
			//System.out.println("Supprimer");
		    JFileChooser chooser = new JFileChooser(Constantes.REPERTOIRE_BASE);
		    int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile().getAbsoluteFile();
		    	controlleur.supprimerFichier(file.getName());
		    }
		}
		else if(arg0.getSource() == montrerFichiers){
			controlleur.montrerFichiers();
		}
		
	}
	
	public void setController(Controller c){
		this.controlleur = c;
	}
	
	public void enableButtons(){
		ajouter.setEnabled(true);
		modifier.setEnabled(true);
		supprimer.setEnabled(true);
		montrerFichiers.setEnabled(true);
		connexion.setEnabled(false);
		this.pack();
		this.repaint();
	}

}
