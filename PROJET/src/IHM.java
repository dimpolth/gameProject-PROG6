import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class IHM extends JFrame implements ComponentListener {
	
	public static void main(String[] args) {
		
		Moteur m = new Moteur();		
		IHM i = new IHM();
		i.setVisible(true);
		
		i.com = new Communication(i,m,Communication.IHM);
		m.com = new Communication(i,m,Communication.MOTEUR);
		
	}
	
	public Communication com;
	Theme theme;
	
	JPanel coucheJeu;
	PopupBloquant popupB;
	PopupMenu popupM;
	PopupOptions popupO;
	PopupRegles popupR;
	TerrainGraphique tg;

	public IHM() {
		
		
		
		// Initialisation de la fenÃªtre
		super("Fanorona");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this);
		
		theme = new Theme( this );

		coucheJeu = new JPanel(new BorderLayout());
		coucheJeu.setBounds(0, 0, getSize().width, getSize().height);
		add(coucheJeu);
		
		// ZONE NORD
		JPanel voletNord = new JPanel(new BorderLayout());		
		coucheJeu.add(voletNord, BorderLayout.NORTH);
		
		// Boutons
		JPanel panneauMenu = new JPanel();
		voletNord.add(panneauMenu, BorderLayout.NORTH);
		
		Bouton boutonMenu = new Bouton("Menu");
		boutonMenu.addActionListener(new Ecouteur(Ecouteur.Bouton.MENU, this));
		panneauMenu.add(boutonMenu);
		Bouton boutonParam= new Bouton("Paramètres");
		boutonParam.addActionListener(new Ecouteur(Ecouteur.Bouton.PARAMETRES, this));
		panneauMenu.add(boutonParam);
		
		// Infos partie en cours
		
		voletNord.add( new BandeauInfos() );
		
		/*JPanel panneauScore = new JPanel( new GridBagLayout() );
		
		GridBagConstraints contraintes = new GridBagConstraints();		
	    contraintes.gridwidth = 1;	    
	    contraintes.fill = GridBagConstraints.BOTH;
	    contraintes.weightx = 1;
	    contraintes.weighty = 1;
	    //contraintes.anchor = GridBagConstraints.CENTER;
		
		voletNord.add(panneauScore);
		JLabel nomJoueur1 = new JLabel("Joueur 1",JLabel.CENTER);
		panneauScore.add(nomJoueur1,contraintes);
		JLabel scoreJoueur1 = new JLabel("10",JLabel.CENTER);
		panneauScore.add(scoreJoueur1,contraintes);
		JLabel tour = new JLabel("Au tour de Joueur 2",JLabel.CENTER);
		contraintes.weightx = 3;
		panneauScore.add(tour,contraintes);
		contraintes.weightx = 1;
		JLabel scoreJoueur2 = new JLabel("3",JLabel.CENTER);
		panneauScore.add(scoreJoueur2,contraintes);
		JLabel nomJoueur2 = new JLabel("Joueur 2",JLabel.CENTER);		
		panneauScore.add(nomJoueur2,contraintes);
		*/
		
		// ZONE CENTRE
		tg = new TerrainGraphique(this);
		coucheJeu.add(tg, BorderLayout.CENTER);
		
		// ZONE SUD
		JPanel voletSud = new JPanel();
		coucheJeu.add(voletSud, BorderLayout.SOUTH);
		Bouton boutonAnnuler = new Bouton("Annuler");
		boutonAnnuler.addActionListener(new Ecouteur(Ecouteur.Bouton.ANNULER, this));
		voletSud.add(boutonAnnuler);
		Bouton boutonRefaire = new Bouton("Refaire");
		boutonRefaire.addActionListener(new Ecouteur(Ecouteur.Bouton.REFAIRE, this));
		voletSud.add(boutonRefaire);

		JLayeredPane gestionCouche = getLayeredPane();
		popupB = new PopupBloquant();
		gestionCouche.add(popupB, new Integer(1));
		popupB.setVisible(false);
		popupM = new PopupMenu(this);
		gestionCouche.add(popupM, new Integer(2));
		popupM.setVisible(false);
		popupO = new PopupOptions(this);
		gestionCouche.add(popupO, new Integer(3));
		popupO.setVisible(false);
		popupR = new PopupRegles(this);
		gestionCouche.add(popupR, new Integer(3));	
		
		popupR.setVisible(false);
		
		theme.setTheme(Theme.Type.BOIS);

		setMinimumSize(new Dimension(640, 480));
		setSize(1000, 750);
		setVisible(true);
	}
	
	public void deplacer(Point o, Point a, ArrayList<Point> l) {
		tg.deplacer(o,a,l);
	}
	
	public void action(Ecouteur.Bouton id) {
		switch(id) {
		case REPRENDRE:
			popupB.setVisible(false);
			popupM.setVisible(false);
			break;
		case SAUVEGARDER:
			JFileChooser fcSauver = new JFileChooser();
			if(fcSauver.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				System.out.println("Action : sauvegarder");
			}		
			break;
		case CHARGER:
			JFileChooser fcCharger = new JFileChooser();
			if(fcCharger.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
				System.out.println("Action : charger");
			}
			break;
		case REGLES:
			popupM.setVisible(false);
			popupR.setVisible(true);
			break;
		case RECOMMENCER:
			break;
		case QUITTER:
			// Confirmation avant de quitter
			String choix[] = {"Oui","Non"};
			int retour = JOptionPane.showOptionDialog(this, "Voulez-vous sauvegarder la partie avant de quitter ?", "Attention", 1, 1, null, choix, choix[1]);
			if(retour == 1)
				System.exit(0);
			else
				action(Ecouteur.Bouton.SAUVEGARDER);
			break;
		case MENU:
			popupB.setVisible(true);
			popupM.setVisible(true);
			break;
		case PARAMETRES:
			popupB.setVisible(true);
			popupO.setVisible(true);
			break;
		case ANNULER:
			Echange e = new Echange();
			e.addAnnuler();
			com.envoyer(e);
			break;
		case REFAIRE:
			break;
		case OPTION_ANNULER:
			popupO.setVisible(false);
			popupB.setVisible(false);
			break;
		case OPTION_VALIDER:
			//TODO Modifications
			popupO.setVisible(false);
			popupB.setVisible(false);
			break;
		case REGLES_RETOUR:
			popupR.setVisible(false);
			popupM.setVisible(true);
			break;
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		coucheJeu.setBounds(0, 0, getWidth(), getHeight());
		popupB.setBounds(0, 0, getWidth(), getHeight());
		popupM.setBounds(getWidth() / 2 - 150, getHeight() / 2 - 250, 300, 500);
		popupO.setBounds(getWidth() / 2 - 300, getHeight() / 2 - 250, 600, 500);
		popupR.setBounds(getWidth() / 2 - 400, getHeight() / 2 - 250, 800, 500);
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	public void notifier(Echange e){
		
		if(e.getTerrain() != null){
			//tg.dessinerTerrain(e.getTerrain());
		}
		
		if(e.getIndication() != null){
			
		}
		
	}
}

@SuppressWarnings("serial")
class BandeauInfos extends JPanel{
	
	JLabel j1_identifiant, 
	j1_score,
	j2_identifiant,
	j2_score,
	texte;
	
	
	BandeauInfos(){
		super( new GridBagLayout() );	
		
		GridBagConstraints contraintes = new GridBagConstraints();		
	    contraintes.gridwidth = 1;	    
	    contraintes.fill = GridBagConstraints.BOTH;
	    contraintes.insets = new Insets(5,5,5,5);
		
		j1_identifiant = formater( new JLabel("Joueur 1") );
		contraintes.weightx = 4;
		add(j1_identifiant,contraintes);
		
		j1_score = formater ( new JLabel("10") );
		contraintes.weightx = 1;
		add(j1_score,contraintes);		
		  
		texte = formater( new JLabel("Au tour de Joueur 2") );
		contraintes.weightx = 10;
		add(texte,contraintes);	  
		
		j2_score = formater( new JLabel("3") );
		contraintes.weightx = 1;
		add(j2_score,contraintes);
		
		j2_identifiant = formater( new JLabel("Joueur 2") );
		contraintes.weightx = 4;
		add(j2_identifiant,contraintes);
	}
	
	JLabel formater(JLabel lab){
		lab.setBorder(BorderFactory.createLineBorder(Color.black));
		lab.setOpaque(true);
		lab.setBackground(Color.LIGHT_GRAY);
		lab.setForeground(Color.BLACK);
		lab.setHorizontalAlignment(JLabel.CENTER);
		lab.setFont( new Font(getToolTipText(), Font.BOLD, 18) );
		return lab;
	}
	
	
	
	
}

@SuppressWarnings("serial")
class PopupBloquant extends JPanel {
	public PopupBloquant() {
		super();
		setBackground(new Color(0, 0, 0, 128));
		setOpaque(false);
		addMouseListener(new MouseAdapter() {
		});
	}

	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}


@SuppressWarnings("serial")
class PopupMenu extends JPanel {
	public PopupMenu(IHM i) {
		super( new GridBagLayout() );
		//this.setBorder(BorderFactory.createLineBorder(Color.black));
		GridBagConstraints contraintes = new GridBagConstraints();		
	    contraintes.gridwidth = GridBagConstraints.REMAINDER;	
	    contraintes.fill = GridBagConstraints.BOTH;	  
	    contraintes.insets =  new Insets(2,2,2,2);
	    contraintes.ipady = 35;
	    contraintes.ipadx = 80;
	    
	    GridBagConstraints contraintesCategorie = (GridBagConstraints) contraintes.clone();
	    contraintesCategorie.insets = new Insets(2,2,25,2);
		
		Bouton boutonMenuReprendre = new Bouton("Reprendre la partie");
		boutonMenuReprendre.addActionListener(new Ecouteur(Ecouteur.Bouton.REPRENDRE, i));		
		add(boutonMenuReprendre,contraintesCategorie);
		
		Bouton boutonMenuSauvegarder = new Bouton("Sauvegarder cette partie");
		boutonMenuSauvegarder.addActionListener(new Ecouteur(Ecouteur.Bouton.SAUVEGARDER, i));
		add(boutonMenuSauvegarder,contraintes);
		Bouton boutonMenuCharger = new Bouton("Charger une partie");
		boutonMenuCharger.addActionListener(new Ecouteur(Ecouteur.Bouton.CHARGER, i));
		add(boutonMenuCharger,contraintes);
		Bouton boutonMenuRegles = new Bouton("Regles du jeu");
		boutonMenuRegles.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES, i));
		add(boutonMenuRegles,contraintesCategorie);
		
		Bouton buttonRecommencer = new Bouton("Nouvelle partie");
		buttonRecommencer.addActionListener(new Ecouteur(Ecouteur.Bouton.RECOMMENCER, i));
		add(buttonRecommencer,contraintes);
		Bouton boutonMenuQuitter = new Bouton("Quitter le jeu");
		boutonMenuQuitter.addActionListener(new Ecouteur(Ecouteur.Bouton.QUITTER, i));
		add(boutonMenuQuitter,contraintes);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}

@SuppressWarnings("serial")
class PopupOptions extends JPanel {
	public PopupOptions(IHM i) {
		super( new GridBagLayout() );
		
		GridBagConstraints contraintes = new GridBagConstraints();		
	    contraintes.gridwidth = GridBagConstraints.REMAINDER;	
	    contraintes.fill = GridBagConstraints.BOTH;	  
	    contraintes.insets =  new Insets(5,5,5,5);
	    contraintes.ipady = 15;
	    contraintes.ipadx = 15;
	    
	    GridBagConstraints contraintes_groupe = (GridBagConstraints) contraintes.clone();
	    contraintes_groupe.gridwidth = 1;
	    GridBagConstraints contraintes_groupe_fin = (GridBagConstraints) contraintes.clone();
	    contraintes_groupe_fin.gridwidth = GridBagConstraints.REMAINDER;
		
		JLabel selectJoueur1Etiq = new JLabel("1er joueur : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		add(selectJoueur1Etiq, contraintes);
		
		JTextField identifiantJoueur1 = new JTextField("Joueur 1");		
		contraintes.gridwidth = 1;
		add(identifiantJoueur1, contraintes);
		JComboBox<String> selectJoueur1 = new JComboBox<>(new String[] {"Humain", "Facile", "Normal", "Difficile"});	
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(selectJoueur1,contraintes);
		
		JLabel selectJoueur2Etiq = new JLabel("2ème joueur : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(selectJoueur2Etiq,contraintes);
		JTextField identifiantJoueur2 = new JTextField("Joueur 2");
		contraintes.gridwidth = 1;
		add(identifiantJoueur2, contraintes);
		JComboBox<String> selectJoueur2 = new JComboBox<>(new String[] {"Humain", "Facile", "Normal", "Difficile"});
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(selectJoueur2,contraintes);
		
		JLabel themeEtiq = new JLabel("Thème graphique : ");
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(themeEtiq,contraintes);
		JComboBox<String> theme = new JComboBox<>(new String[] {"Boisé"});
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(theme,contraintes);
		
		/*
		JLabel tourEtiq = new JLabel("Premier joueur alÃ©atoire : ");
		contraintes.gridwidth = 1;
		add(tourEtiq,contraintes);
		JCheckBox tour = new JCheckBox();
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(tour,contraintes);
		*/
		Bouton annuler = new Bouton("Annuler");
		annuler.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_ANNULER, i));
		contraintes.gridwidth = 1;
		add(annuler,contraintes);
		Bouton valider = new Bouton("Valider");
		valider.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_VALIDER, i));
		contraintes.gridwidth = GridBagConstraints.REMAINDER;;
		add(valider,contraintes);
		
		
		
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}

@SuppressWarnings("serial")
class PopupRegles extends JPanel {
	public PopupRegles(IHM i) {
		super( new GridBagLayout() );
		
		GridBagConstraints contraintes = new GridBagConstraints();	
		contraintes.gridx = 0;
		
	    contraintes.weightx = 2;
	    contraintes.weighty = 1;
	    contraintes.fill = GridBagConstraints.BOTH;
	    contraintes.anchor = GridBagConstraints.CENTER;	  	  
	    contraintes.insets =  new Insets(50,50,50,50);	   	    
		
	    JEditorPane regles = new JEditorPane();
	    regles.setContentType("text/html");
	    regles.setEditable(false);	    		
	    regles.setText("<html><h1>Les rÃ¨gles du Fanorona</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum felis accumsan quis. Suspendisse potenti. Morbi pharetra purus vitae blandit vehicula. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin elit dui, consequat tristique dictum ac, vulputate congue felis. Cras consequat augue nec suscipit maximus. Etiam fringilla erat lacinia sem tincidunt gravida. Vestibulum porttitor orci ut ante eleifend, tincidunt molestie elit ornare. Suspendisse placerat neque odio, a posuere quam congue non. Nulla diam orci, lobortis ut orci et, interdum malesuada arcu. Vestibulum porttitor vehicula urna, et ornare mi eleifend eget. In consequat congue eros eget volutpat. Proin quis rhoncus velit.</p></html>"); 
		add(regles,contraintes);
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.ipadx = 100;
		contraintes.ipady = 40;
		
		Bouton retour = new Bouton("Retour au menu");
		retour.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_RETOUR, i));
		add(retour,contraintes);
		
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}

class Ecouteur implements ActionListener {
	public enum Bouton {REPRENDRE, SAUVEGARDER, CHARGER, REGLES, RECOMMENCER, QUITTER, MENU, PARAMETRES, ANNULER, REFAIRE, OPTION_ANNULER, OPTION_VALIDER, REGLES_RETOUR}
	Bouton id;
	IHM i;
	public Ecouteur(Bouton id, IHM i) {
		this.id = id;
		this.i = i;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		i.action(id);
	}
}
