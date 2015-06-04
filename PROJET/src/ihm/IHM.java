package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
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
	PopupReseau popupReseau;
	PopupVictoire popupV;
	TerrainGraphique tg;
	BandeauInfos bandeauInfos;
	Chargement chargement;
	
	Bouton boutonAnnuler;
	Bouton boutonRefaire;
	Bouton boutonValidation;
	
	boolean modeReseau = false;

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
		chargement = new Chargement();
		panneauMenu.add(chargement);
		Bouton boutonParam = new Bouton("Paramètres");
		boutonParam.addActionListener(new Ecouteur(Ecouteur.Bouton.PARAMETRES, this));
		panneauMenu.add(boutonParam);

		tg = new TerrainGraphique(this);

		// Infos partie en cours
		bandeauInfos = new BandeauInfos(tg);
		voletNord.add(bandeauInfos);

		// ZONE CENTRE

		coucheJeu.add(tg, BorderLayout.CENTER);

		// ZONE SUD
		JPanel voletSud = new JPanel(new BorderLayout());
		coucheJeu.add(voletSud, BorderLayout.SOUTH);

		
		JPanel voletSudOuest = new JPanel();
		voletSud.add(voletSudOuest, BorderLayout.WEST);
		JPanel voletSudCentre = new JPanel();
		voletSud.add(voletSudCentre, BorderLayout.CENTER);
		JPanel voletSudEst = new JPanel();
		voletSud.add(voletSudEst, BorderLayout.EAST);
		
		Bouton boutonAide = new Bouton("Aide");
		boutonAide.addActionListener(new Ecouteur(Ecouteur.Bouton.AIDE, this));
		voletSudOuest.add(boutonAide);
		
		boutonAnnuler = new Bouton("Annuler");
		boutonAnnuler.addActionListener(new Ecouteur(Ecouteur.Bouton.ANNULER, this));
		boutonAnnuler.setEnabled(false);
		voletSudCentre.add(boutonAnnuler);
		
		boutonRefaire = new Bouton("Refaire");
		boutonRefaire.addActionListener(new Ecouteur(Ecouteur.Bouton.REFAIRE, this));
		boutonRefaire.setEnabled(false);
		voletSudCentre.add(boutonRefaire);
		
		boutonValidation = new Bouton("Terminer");
		boutonValidation.addActionListener(new Ecouteur(Ecouteur.Bouton.TERMINER, this));
		boutonValidation.setEnabled(false);
		voletSudEst.add(boutonValidation);

		JLayeredPane gestionCouche = getLayeredPane();
		popupB = new PopupBloquant(new Color(0, 0, 0, 128));
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
		
		popupReseau = new PopupReseau(this);
		gestionCouche.add(popupReseau, new Integer(3));
		popupReseau.setVisible(false);
		
		popupV = new PopupVictoire();
		gestionCouche.add(popupV, new Integer(4));
		popupV.setVisible(false);

		theme.setTheme(Theme.Type.BOIS);
		
		setModeReseau(false);

		setMinimumSize(new Dimension(800, 600));
		setSize(Math.max(800,(int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width*0.75)), Math.max(600,(int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().height*0.75)));
		try {
			setIconImage(ImageIO.read(getClass().getResource("/images/icone.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setVisible(true);
		

	}

	public Joueur[] getParamsJoueurs() {
		Joueur[] j = new Joueur[2];
		j[0] = new Joueur();
		j[1] = new Joueur();
		j[0].setNom(popupO.identifiantJoueur1.getText());
		j[1].setNom(popupO.identifiantJoueur2.getText());
		return j;

	}

	public void lancer() {
		Echange e = new Echange();
		e.ajouter("nouvellePartie", true);
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
		case MODE:
			if(modeReseau){
				String choix[] = { "Confirmer", "Annuler" };
				int retour = JOptionPane.showOptionDialog(this, "Revenir au jeu local quittera la partie réseau.", "Attention", 1, 1, null, choix, choix[1]);
				if (retour == 1)
					setModeReseau(false);								
			}
			else{			
				popupReseau.erreur.setText("");
				popupReseau.setVisible(true);
			}
			popupM.setVisible(false);
			
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
			int retour = JOptionPane.showOptionDialog(this, "Voulez-vous sauvegarder la partie avant de quitter ?", "Attention", 1, 1, null, choix, choix[1]);
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
		case TERMINER:
			Echange e3 = new Echange();
			e3.ajouter("finTour", true);
			com.envoyer(e3);
			break;
		case AIDE:
			Echange e4 = new Echange();
			e4.ajouter("aide", true);
			com.envoyer(e4);
			break;
		case OPTION_ANNULER:
			popupO.setVisible(false);
			popupB.setVisible(false);
			break;

		case OPTION_VALIDER:
			Parametres params = new Parametres();
			params.j1_identifiant = popupO.identifiantJoueur1.getText();
			params.j2_identifiant = popupO.identifiantJoueur2.getText();
			params.j1_type = Parametres.NiveauJoueur.getFromIndex(popupO.selectJoueur1.getSelectedIndex());
			params.j2_type = Parametres.NiveauJoueur.getFromIndex(popupO.selectJoueur2.getSelectedIndex());

			Echange e = new Echange();
			e.ajouter("parametres", params);
			com.envoyer(e);
			
			popupO.setVisible(false);
			popupB.setVisible(false);
			break;
		case REGLES_RETOUR:
			popupR.setVisible(false);
			popupM.setVisible(true);
			break;
		
		
		case RESEAU_ANNULER:
			popupReseau.setVisible(false);
			popupM.setVisible(true);
			break;
		
		case RESEAU_VALIDER:
			
			String errReseau = null;
			// Nouveau serveur
			if(popupReseau.etreHote.isSelected()){
				errReseau = Communication.modeReseau("");				
			}
			else{
				System.out.println("1");
				String hoteComplet = popupReseau.hote.getText();
				if(!hoteComplet.equals("")){
					System.out.println("2");
					errReseau = Communication.modeReseau(hoteComplet);
					System.out.println(errReseau);
				}
			}
			
			if(errReseau == null){
				popupReseau.setVisible(false);
				popupB.setVisible(false);
				setModeReseau(true);
				popupReseau.erreur.setText(errReseau);
			}
			else{
				popupReseau.erreur.setText(errReseau);
			}
			
			
			break;
		}
		
	
	}
	
	public void setModeReseau(boolean r){
		if(!r){
			popupM.boutonMenuReseau.setVisible(true);
			popupM.boutonMenuLocal.setVisible(false);
		}
		else{
			popupM.boutonMenuReseau.setVisible(false);
			popupM.boutonMenuLocal.setVisible(true);
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
		popupM.setBounds(getWidth() / 2 - 150, getHeight() / 2 - 275, 300, 550);
		popupO.setBounds(getWidth() / 2 - 300, getHeight() / 2 - 250, 600, 500);
		popupR.setBounds(getWidth() / 2 - 400, getHeight() / 2 - 250, 800, 500);
		popupReseau.setBounds(getWidth() / 2 - 200, getHeight() / 2 - 175, 400, 350);
		popupV.setBounds(0, 0, getWidth(), getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	public void notifier(Echange e) {
		

		Object dataValue;
		if ((dataValue = e.get("terrain")) != null) {
			tg.dessinerTerrain((Case[][]) dataValue);
		}
		if ((dataValue = e.get("coup")) != null) {
			tg.lCoups.addLast((CoupGraphique) dataValue);
			CoupGraphique.afficherCoups(tg);
		}

		/* Gardez cet ordre */
		if ((dataValue = e.get("pionDeselectionne")) != null) {
			tg.deselectionner();
		}

		
		if((dataValue = e.get("pionSelectionne")) != null){			
			tg.selectionner( (Point)dataValue );

		}
		/*
		 * if((dataValue = e.get("coups")) != null){ LinkedList<CoupGraphique>
		 * cg = (LinkedList<CoupGraphique>)dataValue;
		 * java.util.Iterator<CoupGraphique> it = cg.iterator();
		 * while(it.hasNext()){ tg.lCoups.addLast(it.next()); }
		 * 
		 * CoupGraphique.afficherCoups(tg); }
		 */

		/*
		 * if ((dataValue = e.get("deplacement")) != null) { Point[] pts =
		 * (Point[]) dataValue; tg.deplacer(pts[0], pts[1]); tpsAnimation +=
		 * TerrainGraphique.ANIM_DEPL; } if ((dataValue = e.get("pionsManges"))
		 * != null) { new ExecuterDans(this, "pionsManges", dataValue,
		 * tpsAnimation); } if ((dataValue = e.get("choixPrise")) != null) { new
		 * ExecuterDans(this, "choixPrise", dataValue, tpsAnimation); } if
		 * ((dataValue = e.get("joueurs")) != null) { new ExecuterDans(this,
		 * "joueurs", dataValue, tpsAnimation); }
		 */

		if ((dataValue = e.get("bandeauSup")) != null) {
			bandeauInfos.setTexteSup((String) dataValue);
		}

		if ((dataValue = e.get("bandeauInf")) != null) {
			bandeauInfos.setTexteInf((String) dataValue);
		}
		if((dataValue = e.get("annuler")) != null) {
			boutonAnnuler.setEnabled((boolean)dataValue);
		}
		if((dataValue = e.get("refaire")) != null) {
			boutonRefaire.setEnabled((boolean)dataValue);
		}
		if((dataValue = e.get("score")) != null) {
			int[] score = (int[]) dataValue;
			bandeauInfos.setScore(1, score[0]);
			bandeauInfos.setScore(2, score[1]);
		}
		if((dataValue = e.get("parametres")) != null) {
			Parametres params = (Parametres)dataValue;
			bandeauInfos.setIdentifiant(1,params.j1_identifiant);
			bandeauInfos.setIdentifiant(2,params.j2_identifiant);			
		}
		if((dataValue = e.get("finTour")) != null) {
			boutonValidation.setEnabled((boolean)dataValue);
				
		}
		
		

	}
}
