package ihm;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.*;
import java.util.LinkedList;

import javax.swing.*;

import modele.*;
import reseau.*;


@SuppressWarnings("serial")
public class IHM extends JFrame implements ComponentListener {

	public Communication com;
	Theme theme;

	JPanel coucheJeu;
	PopupBloquant popupB;
	PopupMenu popupM;
	PopupOptions popupO;
	PopupRegles popupR;
	TerrainGraphique tg;
	BandeauInfos bandeauInfos;

	public IHM() {

		// Initialisation de la fenêtre
		super("Fanorona");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this);

		theme = new Theme(this);

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
		Bouton boutonParam = new Bouton("Paramètres");
		boutonParam.addActionListener(new Ecouteur(Ecouteur.Bouton.PARAMETRES,
				this));
		panneauMenu.add(boutonParam);
		
		tg = new TerrainGraphique(this);
		
		// Infos partie en cours
		bandeauInfos = new BandeauInfos(tg);
		voletNord.add(bandeauInfos);

	
		// ZONE CENTRE
		
		coucheJeu.add(tg, BorderLayout.CENTER);

		// ZONE SUD
		JPanel voletSud = new JPanel(new GridBagLayout());
		coucheJeu.add(voletSud, BorderLayout.SOUTH);

		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.insets = new Insets(2, 2, 5, 2);

		Bouton boutonValidation = new Bouton("Terminer mon tour");
		boutonValidation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent pEv){
				Echange e = new Echange();
				e.ajouter("finTour", true);
				com.envoyer(e);
			}
		});
		contraintes.gridwidth = GridBagConstraints.REMAINDER;
		voletSud.add(boutonValidation, contraintes);
		Bouton boutonAnnuler = new Bouton("Annuler");
		boutonAnnuler.addActionListener(new Ecouteur(Ecouteur.Bouton.ANNULER,
				this));
		contraintes.gridwidth = 1;
		voletSud.add(boutonAnnuler, contraintes);
		Bouton boutonRefaire = new Bouton("Refaire");
		boutonRefaire.addActionListener(new Ecouteur(Ecouteur.Bouton.REFAIRE,
				this));
		voletSud.add(boutonRefaire, contraintes);

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
	
	public Joueur[] getParamsJoueurs(){
		Joueur[] j = new Joueur[2];
		j[0] = new Joueur();
		j[1] = new Joueur();
		j[0].setNom(popupO.identifiantJoueur1.getText());		
		j[1].setNom(popupO.identifiantJoueur2.getText());
		return j;
		
	}

	public void lancer() {
		Echange e = new Echange();
		e.ajouter("nouvellePartie",true);
		e.ajouter("terrain", true);		
		e.ajouter("joueurs", getParamsJoueurs());
		com.envoyer(e);
	}

	public void action(Ecouteur.Bouton id) {

		switch (id) {
		case REPRENDRE:
			popupB.setVisible(false);
			popupM.setVisible(false);
			break;
		case SAUVEGARDER:
			JFileChooser fcSauver = new JFileChooser();
			if (fcSauver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				System.out.println("Action : sauvegarder");
			}
			break;
		case CHARGER:
			JFileChooser fcCharger = new JFileChooser();
			if (fcCharger.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				System.out.println("Action : charger");
			}
			break;
		case REGLES:
			popupM.setVisible(false);
			popupR.setVisible(true);
			break;
		case RECOMMENCER:
			lancer();
			break;
		case QUITTER:
			// Confirmation avant de quitter
			String choix[] = { "Oui", "Non" };
			int retour = JOptionPane.showOptionDialog(this,
					"Voulez-vous sauvegarder la partie avant de quitter ?",
					"Attention", 1, 1, null, choix, choix[1]);
			if (retour == 1)
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
			Echange e1 = new Echange();
			e1.ajouter("annuler", true);
			com.envoyer(e1);
			break;
		case REFAIRE:
			Echange e2 = new Echange();
			e2.ajouter("refaire", true);
			com.envoyer(e2);
			break;
		case OPTION_ANNULER:
			popupO.setVisible(false);
			popupB.setVisible(false);
			break;
		case OPTION_VALIDER:
			Echange e = new Echange();			
			e.ajouter("joueurs", getParamsJoueurs());
			com.envoyer(e);
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

	public void notifier(Echange e) {
		int tpsAnimation = 0;		
		
		Object dataValue;
		
		if ((dataValue = e.get("terrain")) != null) {
			
			tg.dessinerTerrain((Case[][]) dataValue);
		}
		if((dataValue = e.get("coup")) != null){			
			tg.lCoups.addLast( (CoupGraphique) dataValue );			
			CoupGraphique.afficherCoups(tg);
		}
		
		
		/* Gardez cet ordre */
		if((dataValue = e.get("pionDeselectionne")) != null){
			tg.deselectionner();
		}
		
		if((dataValue = e.get("pionSelectionne")) != null){
			tg.selectionner( (Point)dataValue );
		}
		/*if((dataValue = e.get("coups")) != null){
			LinkedList<CoupGraphique> cg = (LinkedList<CoupGraphique>)dataValue;
			java.util.Iterator<CoupGraphique> it = cg.iterator();
			while(it.hasNext()){
				tg.lCoups.addLast(it.next());
			}
			
			CoupGraphique.afficherCoups(tg);
		}*/
		
		/*
		if ((dataValue = e.get("deplacement")) != null) {
			Point[] pts = (Point[]) dataValue;
			tg.deplacer(pts[0], pts[1]);
			tpsAnimation += TerrainGraphique.ANIM_DEPL;
		}
		if ((dataValue = e.get("pionsManges")) != null) {
			new ExecuterDans(this, "pionsManges", dataValue, tpsAnimation);
		}
		if ((dataValue = e.get("choixPrise")) != null) {
			new ExecuterDans(this, "choixPrise", dataValue, tpsAnimation);
		}
		if ((dataValue = e.get("joueurs")) != null) {
			new ExecuterDans(this, "joueurs", dataValue, tpsAnimation);
		}
		
		*/
		
		if ((dataValue = e.get("bandeauSup")) != null) {
			bandeauInfos.setTexteSup( (String)dataValue   );
		}
		
		if ((dataValue = e.get("bandeauInf")) != null) {
			bandeauInfos.setTexteInf( (String)dataValue   );
		}
		
	}
}