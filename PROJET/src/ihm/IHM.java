package ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import modele.Parametres;
import modele.Parametres.NiveauJoueur;
import reseau.Communication;
import reseau.Echange;

@SuppressWarnings("serial")
public class IHM extends JFrame implements ComponentListener {

	public Communication com;
	JFrame fenetreChargement;

	Theme theme;

	JPanel coucheJeu;
	PopupBloquant popupB;
	PopupMenu popupM;
	PopupOptions popupO;
	PopupRegles popupR;
	PopupReseau popupReseau;
	PopupVictoire popupV;
	TerrainGraphique tg;
	public BandeauInfos bandeauInfos;
	Chargement chargement, chargement2;

	Bouton boutonAnnuler;
	Bouton boutonRefaire;
	Bouton boutonValidation;

	

	public IHM() {

		// Initialisation de la fenêtre
		super("Fanorona");
		fenetreChargement(true);
		try {
			fenetreChargement.setIconImage(ImageIO.read(getClass().getResource("/images/icone.png")));
			setIconImage(ImageIO.read(getClass().getResource("/images/icone.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addComponentListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				action(Ecouteur.Bouton.QUITTER);
			}
		});

		theme = new Theme(this);

		coucheJeu = new JPanel(new BorderLayout());
		coucheJeu.setBounds(0, 0, getSize().width, getSize().height);
		add(coucheJeu);

		// ZONE NORD
		JPanel voletNord = new JPanel(new BorderLayout());
		coucheJeu.add(voletNord, BorderLayout.NORTH);
		voletNord.setOpaque(false);

		// Boutons
		JPanel panneauMenu = new JPanel();
		voletNord.add(panneauMenu, BorderLayout.NORTH);
		panneauMenu.setOpaque(false);

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
		voletSud.setOpaque(false);

		JPanel voletSudOuest = new JPanel();
		voletSudOuest.setOpaque(false);
		voletSud.add(voletSudOuest, BorderLayout.WEST);
		JPanel voletSudCentre = new JPanel();
		voletSudCentre.setOpaque(false);
		voletSud.add(voletSudCentre, BorderLayout.CENTER);
		JPanel voletSudEst = new JPanel();
		voletSudEst.setOpaque(false);
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

		theme.setTheme(Theme.Type.BOIS);

		setModeReseau(false);

		setMinimumSize(new Dimension(800, 600));
		setSize(Math.max(800, (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width * 0.75)), Math.max(600, (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().height * 0.75)));
		Toolkit screen = Toolkit.getDefaultToolkit();
		Dimension dFen = screen.getScreenSize();
		setLocation(dFen.width / 2 - getSize().width / 2, dFen.height / 2 - getSize().height / 2);

		setSize(new Dimension(800, 600));
		fenetreChargement(false);
		setVisible(true);

	}

	public Parametres getParametres() {

		Parametres params = new Parametres();
		params.j1_identifiant = popupO.identifiantJoueur1.getText();
		params.j2_identifiant = popupO.identifiantJoueur2.getText();
		params.j1_type = NiveauJoueur.getFromIndex(popupO.selectJoueur1.getSelectedIndex());
		params.j2_type = NiveauJoueur.getFromIndex(popupO.selectJoueur2.getSelectedIndex());

		return params;

	}

	public void nouvellePartie() {
		System.out.println("Nouvelle Partie");
		Echange e = new Echange();
		e.ajouter("nouvellePartie", true);
		e.ajouter("parametres", getParametres());
		e.ajouter("terrain", true);
		com.envoyer(e);
	}
	
	public void sauverPartie(){
		JFileChooser fcSauver = new JFileChooser();
		fcSauver.addChoosableFileFilter(new FileNameExtensionFilter(".fa", "fanorona"));
		File fileToBeSaved=null;
		if (fcSauver.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			if(!fcSauver.getSelectedFile().getAbsolutePath().endsWith(".fa")){
			    fileToBeSaved = new File(fcSauver.getSelectedFile() + ".fa");
			}
			Echange e = new Echange();
			e.ajouter("sauvegarder", fileToBeSaved);
			com.envoyer(e);
		}
	}
	
	public void chargerPartie(){
		JFileChooser fcCharger = new JFileChooser();
		fcCharger.addChoosableFileFilter(new FileNameExtensionFilter(".fa", "fanorona"));
		if (fcCharger.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			Echange e = new Echange();
			e.ajouter("charger", fcCharger.getSelectedFile());
			com.envoyer(e);
		}
	}
	
	public void quitter(boolean confirmation){
		if ((Communication.enReseau())) {
			String[] choix = { "Oui", "Non" };
			int retour;
			if(confirmation)
				retour = JOptionPane.showOptionDialog(this, "Vous êtes actuellement sur une partie en réseau. Voulez-vous vraiment quitter ?", "Attention", 1, 1, null, choix, choix[0]);
			else retour = 0;
			if (retour == 0) {
				com.envoyer("/QUIT");
				System.exit(0);
			}
		} else {
			String[] choix = { "Oui", "Non" };
			int retour;
			if(confirmation)
				retour = JOptionPane.showOptionDialog(this, "Voulez-vous sauvegarder la partie avant de quitter ?", "Attention", 1, 1, null, choix, choix[1]);
			else retour = 1;
			if (retour == 1) {
				System.exit(0);
			} else if (retour == 0) {
				action(Ecouteur.Bouton.SAUVEGARDER);
				System.exit(0);
			}
		}
	}

	public void action(Ecouteur.Bouton id) {

		switch (id) {
		case REPRENDRE:
			popupB.setVisible(false);
			popupM.setVisible(false);
			break;
		case SAUVEGARDER:
			sauverPartie();
			break;
		case CHARGER:
			chargerPartie();
			break;
		case MODE:
			if (Communication.enReseau()) {
				String choix[] = { "Confirmer", "Annuler" };
				int retour = JOptionPane.showOptionDialog(this, "Revenir au jeu local quittera la partie réseau.", "Attention", 1, JOptionPane.INFORMATION_MESSAGE, null, choix, choix[1]);

				if (retour == 0) {
					setModeReseau(false);
				}
				popupB.setVisible(false);
			} else {
				popupReseau.message.setText("");
				popupReseau.setVisible(true);
			}
			popupM.setVisible(false);

			break;
		case REGLES:
			popupM.setVisible(false);
			popupR.setVisible(true);
			break;
		case RECOMMENCER:
			nouvellePartie();
			popupM.setVisible(false);
			popupB.setVisible(false);
			break;
		case QUITTER:
			quitter(true);
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
			if (!EvenementGraphique.animationEnCours) {
				Echange e1 = new Echange();
				e1.ajouter("annuler", true);
				com.envoyer(e1);
			}
			break;
		case REFAIRE:
			if (!EvenementGraphique.animationEnCours) {
				Echange e2 = new Echange();
				e2.ajouter("refaire", true);
				com.envoyer(e2);
			}
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

			Parametres params = getParametres();
			if (popupO.theme.getSelectedItem() == "Standard")
				theme.setTheme(Theme.Type.STANDARD);
			else if (popupO.theme.getSelectedItem() == "Boisé")
				theme.setTheme(Theme.Type.BOIS);
			else if (popupO.theme.getSelectedItem() == "Marbre")
				theme.setTheme(Theme.Type.MARBRE);
			else if (popupO.theme.getSelectedItem() == "Sombre")
				theme.setTheme(Theme.Type.SOMBRE);
			else if (popupO.theme.getSelectedItem() == "Cochonou")
				theme.setTheme(Theme.Type.COCHON);

			if (!Communication.enReseau()) {
				Echange e = new Echange();
				e.ajouter("parametres", params);
				com.envoyer(e);
			}

			popupO.setVisible(false);
			popupB.setVisible(false);
			break;
		case REGLES_PLUS:
			try {
				Desktop.getDesktop().open(new File(getClass().getResource("/documents/regles.pdf").toURI()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
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
			String identifiant = popupReseau.identifiant.getText();
			;
			// Nouveau serveur
			if (popupReseau.etreHote.isSelected()) {
				errReseau = Communication.modeReseau("", identifiant);
				if (errReseau == null) {

					// 2 joueurs humain si on lance une partie réseau
					Parametres param = new Parametres();
					param.j1_type = Parametres.NiveauJoueur.HUMAIN;
					param.j2_type = Parametres.NiveauJoueur.HUMAIN;
					Echange ec = new Echange();
					ec.ajouter("nouvellePartie", true);
					ec.ajouter("parametres", param);
					com.envoyer(ec);
					JOptionPane.showMessageDialog(this, "Le serveur est ouvert sur le port : " + Communication.getPort() + "", "Port " + Communication.getPort() + "", 1);
				}

			} else {
				// ;//System.out.println("1");
				String hoteComplet = popupReseau.hote.getText();
				if (!hoteComplet.equals("")) {
					// ;//System.out.println("2");
					errReseau = Communication.modeReseau(hoteComplet, identifiant);
					// ;//System.out.println(errReseau);
				}
			}

			if (errReseau == null) {
				popupReseau.setVisible(false);
				popupB.setVisible(false);
				setModeReseau(true);
				popupReseau.message.setText(errReseau);

			} else {
				popupReseau.message.setText(errReseau);
			}

			break;
		}

	}

	public void envoyerIdentifiantReseau() {

	}

	public void fenetreChargement(boolean b) {
		if (b) {

			Toolkit screen = Toolkit.getDefaultToolkit();
			Dimension dFen = screen.getScreenSize();
			fenetreChargement = new JFrame();
			fenetreChargement.setLayout(new GridLayout(2, 1));
			JLabel texte = new JLabel("Chargement en cours...", SwingConstants.CENTER);
			texte.setFont(new Font("Arial", Font.BOLD, 25));
			fenetreChargement.add(texte);
			JPanel p = new JPanel();
			chargement2 = new Chargement();
			chargement2.afficher();
			p.add(chargement2);
			fenetreChargement.add(p);
			fenetreChargement.setUndecorated(true);
			// fenetreChargement.setSize(300,200);
			fenetreChargement.setSize(dFen.width / 5, dFen.height / 5);
			fenetreChargement.setResizable(false);
			fenetreChargement.setLocation(dFen.width / 2 - fenetreChargement.getSize().width / 2, dFen.height / 2 - fenetreChargement.getSize().height / 2);
			fenetreChargement.setVisible(true);

		} else {
			chargement2.cacher();
			fenetreChargement.setVisible(false);
		}

	}

	public void setModeReseau(boolean r) {
		if (!r) {
			popupM.boutonMenuReseau.setVisible(true);
			popupM.boutonMenuLocal.setVisible(false);
			popupM.boutonRecommencer.setEnabled(true);
		}
		else{	
			popupM.boutonMenuReseau.setVisible(false);
			popupM.boutonMenuLocal.setVisible(true);
			if(!Communication.estServeur())				
				popupM.boutonRecommencer.setEnabled(false);			
		}

		popupO.selectJoueur1Etiq.setVisible(!r);
		popupO.selectJoueur2Etiq.setVisible(!r);
		popupO.identifiantJoueur1.setVisible(!r);
		popupO.identifiantJoueur2.setVisible(!r);
		popupO.selectJoueur1.setVisible(!r);
		popupO.selectJoueur2.setVisible(!r);
		popupM.boutonMenuSauvegarder.setEnabled(!r);
		popupM.boutonMenuCharger.setEnabled(!r);

		
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
		popupM.setBounds(getWidth() / 2 - 175, getHeight() / 2 - 275, 350, 550);
		popupO.setBounds(getWidth() / 2 - 320, getHeight() / 2 - 200, 640, 400);
		popupR.setBounds(getWidth() / 2 - 400, getHeight() / 2 - 250, 800, 500);

		popupReseau.setBounds(getWidth() / 2 - 175, getHeight() / 2 - 275, 350, 550);
		popupV.setBounds(0, 0, getWidth(), getHeight());

	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	public void notifier(Echange e) {
	
		
		Object dataValue;
		
		if ((dataValue = e.get("terrain")) != null) {
			tg.dessinerTerrain((modele.Case[][]) dataValue); 
		}
		
		if ((dataValue = e.get("coup")) != null) {
			tg.lCoups.addLast((EvenementGraphique) dataValue);
			EvenementGraphique.afficherCoups(tg);
		}

		/* Gardez cet ordre */
		if ((dataValue = e.get("pionDeselectionne")) != null) {
			tg.deselectionner();
		}

		if ((dataValue = e.get("pionSelectionne")) != null) {
			tg.selectionner((Point) dataValue);

		}
		/*
		 * if((dataValue = e.get("coups")) != null){
		 * LinkedList<EvenementGraphique> cg =
		 * (LinkedList<EvenementGraphique>)dataValue;
		 * java.util.Iterator<EvenementGraphique> it = cg.iterator();
		 * while(it.hasNext()){ tg.lCoups.addLast(it.next()); }
		 * 
		 * EvenementGraphique.afficherCoups(tg); }
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
		if ((dataValue = e.get("annuler")) != null) {
			boutonAnnuler.setEnabled((boolean) dataValue);
		}
		if ((dataValue = e.get("refaire")) != null) {
			boutonRefaire.setEnabled((boolean) dataValue);
		}
		if ((dataValue = e.get("finTour")) != null) {
			boutonValidation.setEnabled((boolean) dataValue);
		}
		if ((dataValue = e.get("score")) != null) {
			int[] score = (int[]) dataValue;
			bandeauInfos.setScore(1, score[0]);
			bandeauInfos.setScore(2, score[1]);
		}
		if ((dataValue = e.get("parametres")) != null) {
			Parametres params = (Parametres) dataValue;
			System.out.println(params.j2_identifiant);
			if (params.j1_identifiant != null)
				bandeauInfos.setIdentifiant(1, params.j1_identifiant);
			if (params.j2_identifiant != null)
				bandeauInfos.setIdentifiant(2, params.j2_identifiant);
		}
		

	}
	
	public void notifier(String info){		
		
		if(info.equals("/INTER_SERVEUR") || info.equals("/ABANDON")){
			String message="";
			if(info.equals("/INTER_SERVEUR")){
				message = "Le seveur a mis fin à la partie en réseau en cours.";
			}
			else{
				message = "L'adversaire a mis fin à la partie en réseau.";
			}
			
			message +="\n Voulez-vous lancer une nouvelle partie en local ?";
			int reponse = JOptionPane.showConfirmDialog(this, message,
				      "Fin de partie réseau",
				      JOptionPane.YES_NO_OPTION);	
			setModeReseau(false);
			
			if (reponse == JOptionPane.YES_OPTION) nouvellePartie();
			else quitter(false);
			
		}
		
	}
}
