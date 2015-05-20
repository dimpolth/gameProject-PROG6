import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class IHM extends JFrame implements ComponentListener {
	// Lancement du programme
	public static void main(String[] args) {
		IHM m = new IHM();
		m.setVisible(true);
	}

	// Variables
	JPanel coucheJeu;
	PopupBloquant popupB;
	PopupMenu popupM;
	PopupOptions popupO;
	PopupRegles popupR;

	// Constructeur du moteur
	public IHM() {
		// Initialisation de la fenêtre
		super("Fanorona");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this);

		coucheJeu = new JPanel(new BorderLayout());
		coucheJeu.setBounds(0, 0, getSize().width, getSize().height);
		add(coucheJeu);
		JPanel voletNord = new JPanel(new BorderLayout());
		coucheJeu.add(voletNord, BorderLayout.NORTH);
		JPanel panneauMenu = new JPanel();
		voletNord.add(panneauMenu, BorderLayout.NORTH);
		JButton boutonMenu = new JButton("Menu");
		boutonMenu.addActionListener(new Ecouteur(Ecouteur.Bouton.MENU, this));
		panneauMenu.add(boutonMenu);
		JButton boutonParam= new JButton("Paramétrer");
		boutonParam.addActionListener(new Ecouteur(Ecouteur.Bouton.PARAMETRES, this));
		panneauMenu.add(boutonParam);
		JPanel panneauScore = new JPanel();
		voletNord.add(panneauScore);
		JLabel nomJoueur1 = new JLabel("Joueur 1");
		panneauScore.add(nomJoueur1);
		JLabel scoreJoueur1 = new JLabel("10");
		panneauScore.add(scoreJoueur1);
		JLabel tour = new JLabel("Au tour de Joueur 2");
		panneauScore.add(tour);
		JLabel scoreJoueur2 = new JLabel("3");
		panneauScore.add(scoreJoueur2);
		JLabel nomJoueur2 = new JLabel("Joueur 2");
		panneauScore.add(nomJoueur2);

		TerrainGraphique tg = new TerrainGraphique();
		coucheJeu.add(tg);

		JPanel voletSud = new JPanel();
		coucheJeu.add(voletSud, BorderLayout.SOUTH);
		JButton boutonAnnuler = new JButton("Annuler");
		boutonAnnuler.addActionListener(new Ecouteur(Ecouteur.Bouton.ANNULER, this));
		voletSud.add(boutonAnnuler);
		JButton boutonRefaire = new JButton("Refaire");
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

		setSize(1000, 750);
	}
	
	public void action(Ecouteur.Bouton id) {
		switch(id) {
		case REPRENDRE:
			popupB.setVisible(false);
			popupM.setVisible(false);
			break;
		case SAUVEGARDER:
			break;
		case CHARGER:
			break;
		case REGLES:
			popupM.setVisible(false);
			popupR.setVisible(true);
			break;
		case RECOMMENCER:
			break;
		case QUITTER:
			System.exit(0);
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

	// Gestion du redimensionnement de la fenêtre
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
		super();
		setLayout(new GridLayout(6, 1));
		JButton boutonMenuReprendre = new JButton("Reprendre");
		boutonMenuReprendre.addActionListener(new Ecouteur(Ecouteur.Bouton.REPRENDRE, i));
		add(boutonMenuReprendre);
		JButton boutonMenuSauvegarder = new JButton("Sauvegarder");
		boutonMenuSauvegarder.addActionListener(new Ecouteur(Ecouteur.Bouton.SAUVEGARDER, i));
		add(boutonMenuSauvegarder);
		JButton boutonMenuCharger = new JButton("Charger");
		boutonMenuCharger.addActionListener(new Ecouteur(Ecouteur.Bouton.CHARGER, i));
		add(boutonMenuCharger);
		JButton boutonMenuRegles = new JButton("Regles");
		boutonMenuRegles.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES, i));
		add(boutonMenuRegles);
		JButton buttonRecommencer = new JButton("Recommencer");
		buttonRecommencer.addActionListener(new Ecouteur(Ecouteur.Bouton.RECOMMENCER, i));
		add(buttonRecommencer);
		JButton boutonMenuQuitter = new JButton("Quitter");
		boutonMenuQuitter.addActionListener(new Ecouteur(Ecouteur.Bouton.QUITTER, i));
		add(boutonMenuQuitter);
	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
	}
}

@SuppressWarnings("serial")
class PopupOptions extends JPanel {
	public PopupOptions(IHM i) {
		super();
		setLayout(new GridLayout(5, 2));
		JLabel selectJoueur1Etiq = new JLabel("Joueur 1 : ");
		add(selectJoueur1Etiq);
		JComboBox<String> selectJoueur1 = new JComboBox<>(new String[] {"Humain", "Facile", "Normal", "Difficile"});
		add(selectJoueur1);
		JLabel selectJoueur2Etiq = new JLabel("Joueur 2 : ");
		add(selectJoueur2Etiq);
		JComboBox<String> selectJoueur2 = new JComboBox<>(new String[] {"Humain", "Facile", "Normal", "Difficile"});
		add(selectJoueur2);
		JLabel themeEtiq = new JLabel("Thème : ");
		add(themeEtiq);
		JComboBox<String> theme = new JComboBox<>(new String[] {"Bois"});
		add(theme);
		JLabel tourEtiq = new JLabel("Premier joueur aléatoire : ");
		add(tourEtiq);
		JCheckBox tour = new JCheckBox();
		add(tour);
		JButton annuler = new JButton("Annuler");
		annuler.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_ANNULER, i));
		add(annuler);
		JButton valider = new JButton("Valider");
		valider.addActionListener(new Ecouteur(Ecouteur.Bouton.OPTION_VALIDER, i));
		add(valider);
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
		super();
		setLayout(new GridLayout(2, 1));
		JLabel regles = new JLabel("<html><h1>Les règles du Fanorona</h1><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis rhoncus ut est nec posuere. In molestie est augue, sed fermentum felis accumsan quis. Suspendisse potenti. Morbi pharetra purus vitae blandit vehicula. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Proin elit dui, consequat tristique dictum ac, vulputate congue felis. Cras consequat augue nec suscipit maximus. Etiam fringilla erat lacinia sem tincidunt gravida. Vestibulum porttitor orci ut ante eleifend, tincidunt molestie elit ornare. Suspendisse placerat neque odio, a posuere quam congue non. Nulla diam orci, lobortis ut orci et, interdum malesuada arcu. Vestibulum porttitor vehicula urna, et ornare mi eleifend eget. In consequat congue eros eget volutpat. Proin quis rhoncus velit. </p></html>");
		add(regles);
		JButton retour = new JButton("Retour");
		retour.addActionListener(new Ecouteur(Ecouteur.Bouton.REGLES_RETOUR, i));
		add(retour);
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.red);
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